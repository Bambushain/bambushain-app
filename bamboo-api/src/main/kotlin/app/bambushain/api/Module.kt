package app.bambushain.api

import android.content.Context
import app.bambushain.api.apis.AuthenticationApi
import app.bambushain.api.apis.UserApi
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.api.infrastructure.ApiClient
import org.koin.dsl.module

val apiModule = module {
    factory {
        ApiClient(AuthenticationSettings.get(get<Context>())?.token)
    }
    factory {
        get<ApiClient>().createService(AuthenticationApi::class.java)
    }
    factory {
        get<ApiClient>().createService(UserApi::class.java)
    }
}