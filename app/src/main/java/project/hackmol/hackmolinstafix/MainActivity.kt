package project.hackmol.hackmolinstafix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import project.hackmol.hackmolinstafix.navigation.AppNavigation
import project.hackmol.hackmolinstafix.ui.theme.HackmolInstafixTheme
import project.hackmol.hackmolinstafix.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackmolInstafixTheme {
                MainApp()
            }
        }
    }
}


@Composable
fun MainApp() {
    val navController: NavHostController = rememberNavController()


    AppNavigation(navController = navController)
}
