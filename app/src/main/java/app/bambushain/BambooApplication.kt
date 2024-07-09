package app.bambushain

import android.app.Application
import app.bambushain.api.apiModule
import app.bambushain.viewModels.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val appModule = module {
}

class BambooApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BambooApplication)
            modules(apiModule, appModule, viewModelModule)
        }
    }
}
