package com.lanier.roco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lanier.roco.entity.Screen
import com.lanier.roco.manager.*
import com.lanier.roco.ui.screen.*
import com.lanier.roco.ui.theme.RocoTheme
import com.lanier.roco.util.SpiritHelper

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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
                    Scaffold(
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination
                            val items = listOf(
                                Screen.NewsList,
                                Screen.SpiritList,
                                Screen.OtherList
                            )
                            BottomAppBar {
                                items.forEach { item ->
                                    BottomNavigationItem(
                                        icon = {},
                                        label = { Text(item.title) },
                                        selected = currentRoute?.hierarchy?.any { it.route == item.route } == true,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(navController,
                            startDestination = Screen.NewsList.route,
                            Modifier.padding(innerPadding)) {
                            composable(Screen.NewsList.route) { NewsScreen(navController) }
                            composable(Screen.SpiritList.route) { SpiritScreen(navController) }
                            composable(Screen.OtherList.route) { OtherScreen(navController) }
                        }
                    }
/*                    NavHost(navController = navController, startDestination = ROUTE_MAIN_SCREEN){
                        composable(ROUTE_MAIN_SCREEN){
                            MainScreen(navController)
                        }
                        composable(ROUTE_GROUP_SCREEN,){
                            DetailScreen(navController)
                        }
                        composable(ROUTE_GENETIC_SCREEN){
                            GeneticScreen(navController)
                        }
                    }*/
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