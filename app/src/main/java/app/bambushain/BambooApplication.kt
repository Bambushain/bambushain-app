package app.bambushain

import android.app.Application
import androidx.datastore.core.MultiProcessDataStoreFactory
import app.bambushain.api.apiModule
import app.bambushain.settings.AuthenticationSettingsSerializer
import app.bambushain.viewModels.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single {
        MultiProcessDataStoreFactory.create(
            serializer = AuthenticationSettingsSerializer(),
            produceFile = {
                File("${androidContext().cacheDir.path}/app.bambushain.authentication_settings")
            }
        )
    }
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
