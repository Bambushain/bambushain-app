package app.bambushain.api.infrastructure

import app.bambushain.api.auth.ApiKeyAuth
import app.bambushain.api.infrastructure.Serializer.kotlinxSerializationJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient(
    private var baseUrl: String = defaultBasePath,
    private val okHttpClientBuilder: OkHttpClient.Builder? = null,
    private val callFactory: Call.Factory? = null,
    private val converterFactory: Converter.Factory? = null,
) {
    private val apiAuthorizations = mutableMapOf<String, Interceptor>()
    var logger: ((String) -> Unit)? = null

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(kotlinxSerializationJson.asConverterFactory("application/json".toMediaType()))
            .apply {
                if (converterFactory != null) {
                    addConverterFactory(converterFactory)
                }
            }
    }

    private val clientBuilder: OkHttpClient.Builder by lazy {
        okHttpClientBuilder ?: defaultClientBuilder
    }

    private val defaultClientBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor { message -> logger?.invoke(message) }
                .apply { level = HttpLoggingInterceptor.Level.BODY }
            )
    }

    init {
        normalizeBaseUrl()
    }

    constructor(
        apiKey: String?,
        baseUrl: String = defaultBasePath,
        okHttpClientBuilder: OkHttpClient.Builder? = null
    ) : this(baseUrl, okHttpClientBuilder) {
        val auth = ApiKeyAuth("header", "Authorization", "Panda $apiKey")
        addAuthorization("Panda", auth)
    }

    /**
     * Adds an authorization to be used by the client
     * @param authName Authentication name
     * @param authorization Authorization interceptor
     * @return ApiClient
     */
    fun addAuthorization(authName: String, authorization: Interceptor): ApiClient {
        if (apiAuthorizations.containsKey(authName)) {
            throw RuntimeException("auth name $authName already in api authorizations")
        }
        apiAuthorizations[authName] = authorization
        clientBuilder.addInterceptor(authorization)
        return this
    }

    fun setLogger(logger: (String) -> Unit): ApiClient {
        this.logger = logger
        return this
    }

    fun <S> createService(serviceClass: Class<S>): S {
        val usedCallFactory = this.callFactory ?: clientBuilder.build()
        return retrofitBuilder.callFactory(usedCallFactory).build().create(serviceClass)
    }

    private fun normalizeBaseUrl() {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
        }
    }

    private inline fun <T, reified U> Iterable<T>.runOnFirst(callback: U.() -> Unit) {
        for (element in this) {
            if (element is U) {
                callback.invoke(element)
                break
            }
        }
    }

    companion object {
        @JvmStatic
        protected val baseUrlKey = "app.bambushain.api.baseUrl"

        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(baseUrlKey, "https://pandas.bambushain.app")
        }

        var apiKey: String = ""
    }
}
