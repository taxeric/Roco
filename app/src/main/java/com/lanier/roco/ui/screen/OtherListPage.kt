package com.lanier.roco.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lanier.roco.repo.viewmodel.OtherViewModel

/**
 * Create by Eric
 * on 2022/7/23
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherScreen(navController: NavController, title: String){
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            SmallTopAppBar(
                title = { Text(text = title) },
            )
        }
    ){ innerPadding ->
        OthersMain(navController = navController, padding = innerPadding)
    }
}

@Composable
fun OthersMain(navController: NavController, padding: PaddingValues){
    val vm: OtherViewModel = viewModel()
    Column(modifier = Modifier.padding(padding)) {
        Text(text = "其他")
    }
}
