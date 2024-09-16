package app.bambushain

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.bambushain.api.auth.AuthenticationSettings
import app.bambushain.composables.authentication.LoginScreen
import app.bambushain.composables.user.UserList
import app.bambushain.ui.theme.BambushainTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MainComposable()
        }
    }
}

enum class Screens {
    LoginScreen,
    PandasScreen,
}

@Composable
fun MainComposable(context: Context = koinInject()) {
    val navController = rememberNavController()

    BambushainTheme(dynamicColor = false) {
        NavHost(
            navController = navController,
            startDestination = if (AuthenticationSettings.get(context)?.token != null) {
                Screens.PandasScreen.name
            } else {
                Screens.LoginScreen.name
            }
        ) {
            composable(Screens.LoginScreen.name) {
                LoginScreen(navController = navController)
            }
            composable(Screens.PandasScreen.name) {
                UserList(navController = navController)
            }
        }
    }
}
