package com.lanier.roco.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lanier.roco.repo.viewmodel.NewsViewModel
import com.lanier.roco.util.log

/**
 * Create by Eric
 * on 2022/7/23
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController, title: String){
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            SmallTopAppBar(
                title = { Text(text = title) },
            )
        }
    ) { innerPadding ->
        NewsMain(navController = navController, padding = innerPadding)
    }
}

@Composable
fun NewsMain(navController: NavController, padding: PaddingValues){
    Column(modifier = Modifier.padding(padding)) {
        Text(text = "新闻列表")
    }
}
