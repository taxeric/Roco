package com.lanier.roco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lanier.roco.manager.*
import com.lanier.roco.ui.screen.DetailScreen
import com.lanier.roco.ui.screen.GeneticScreen
import com.lanier.roco.ui.screen.MainScreen
import com.lanier.roco.ui.theme.RocoTheme
import com.lanier.roco.util.SpiritHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RocoTheme {
                SpiritHelper.defaultLocalJsonDataPath = externalCacheDir!!.absolutePath
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = ROUTE_MAIN_SCREEN){
                        composable(ROUTE_MAIN_SCREEN){
                            MainScreen(navController)
                        }
                        composable(ROUTE_GROUP_SCREEN,){
                            DetailScreen(navController)
                        }
                        composable(ROUTE_GENETIC_SCREEN){
                            GeneticScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RocoTheme {
        Greeting("Android")
    }
}