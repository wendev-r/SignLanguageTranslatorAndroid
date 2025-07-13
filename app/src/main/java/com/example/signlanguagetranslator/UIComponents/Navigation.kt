package com.example.signlanguagetranslator.UIComponents

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable


@Composable
fun Navigation(
    onBluetoothStateChanged:()->Unit
){

    val navController = rememberNavController()

    NavHost(navController=navController, startDestination = Screen.StartScreen.route) {
        composable(Screen.StartScreen.route) {
            StartScreen(navController = navController )
        }
        composable(Screen.SignLanguageIdentificationScreen.route) {
            SignLanguageIdentificationScreen(onBluetoothStateChanged)
//                SignLanguageIdentificationScreen()

        }
    }
}

sealed class Screen(val route:String){
    object StartScreen:Screen("start_screen")
    object SignLanguageIdentificationScreen:Screen("sl_id_screen")
}