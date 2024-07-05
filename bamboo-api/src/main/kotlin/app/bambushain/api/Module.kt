package app.bambushain.api

import app.bambushain.api.apis.AuthenticationApi
import app.bambushain.api.infrastructure.ApiClient
import org.koin.dsl.module

val apiModule = module {
    single {
        ApiClient()
    }
    single {
        get<ApiClient>().createService(AuthenticationApi::class.java)
    }
}