package app.bambushain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.bambushain.composables.authentication.LoginScreen
import app.bambushain.composables.user.UserList
import app.bambushain.ui.theme.BambushainTheme

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
fun MainComposable() {
    val navController = rememberNavController()

    BambushainTheme(dynamicColor = false) {
        NavHost(
            navController = navController,
            startDestination = Screens.PandasScreen.name
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
