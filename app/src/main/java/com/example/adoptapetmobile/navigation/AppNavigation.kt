package com.example.adoptapetmobile.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adoptapetmobile.ui.screens.AgregarProducto
import com.example.adoptapetmobile.ui.screens.Home
import com.example.adoptapetmobile.ui.screens.LoginScreen
import com.example.adoptapetmobile.ui.screens.RegisterScreen
import com.example.adoptapetmobile.viewmodel.ProductViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    val productViewModel: ProductViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onGoLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            Home(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        composable("agregar") {
            AgregarProducto(
                navController = navController,
                viewModel = productViewModel
            )
        }
    }
}