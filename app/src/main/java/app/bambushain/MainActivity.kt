package app.bambushain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.bambushain.composables.authentication.LoginScreen
import app.bambushain.ui.theme.BambushainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainComposable()
        }
    }
}

enum class Screens {
    LoginScreen,
}

@Composable
fun MainComposable() {
    val navController = rememberNavController()

    BambushainTheme {
        NavHost(
            navController = navController,
            startDestination = Screens.LoginScreen.name
        ) {
            composable(Screens.LoginScreen.name) {
                LoginScreen(navController = navController)
            }
        }
    }
}
