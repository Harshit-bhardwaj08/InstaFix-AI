// Updated AppNavigation.kt
package project.hackmol.hackmolinstafix.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.hackmol.hackmolinstafix.ui.screens.*
import project.hackmol.hackmolinstafix.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()
    var isUserLoggedIn by remember { mutableStateOf(authViewModel.isUserLoggedIn) }


    LaunchedEffect(key1 = isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) Screen.Home.route else Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    isUserLoggedIn = true
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.Signup.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(route = Screen.Signup.route) {
            SignupScreen(
                authViewModel = authViewModel,
                onSignUpSuccess = {
                    isUserLoggedIn = true
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                authViewModel = authViewModel,
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                authViewModel = authViewModel,
                onLogout = {
                    isUserLoggedIn = false
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                navController = navController,
            )
        }

        composable (route = Screen.AIDiagnosis.route){
            AIDiagnosisScreen(navController)
        }

        composable(route = Screen.EcoImpact.route) {
            EcoImpactScreen(navController)
        }

        composable(route = Screen.BookPro.route) {
            BookProScreen(navController)
        }

        composable(Screen.TrackRepair.route) {
            TrackRepairScreen(
                navController = navController,
                // pass in some test data or data from a ViewModel
                currentStep = 1,
                userLat = 12.9716,
                userLng = 77.5946,
                handymanLat = 12.9616,
                handymanLng = 77.5846
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                userName = authViewModel.currentUser?.displayName,
                userEmail = authViewModel.currentUser?.email,
                authViewModel = authViewModel,
                onLogoutClick = {
                    isUserLoggedIn = false
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object AIDiagnosis : Screen("ai")
    object TrackRepair : Screen("trackRepair")
    object EcoImpact : Screen("ecoImpact")
    object BookPro : Screen("bookPro")
    object Profile : Screen("profile")
}


// Example addition in InstaFixHomeScreen.kt for logout
/*
TopBar or Button to handle logout:

IconButton(onClick = {
    onLogoutClick()
}) {
    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
}
*/
