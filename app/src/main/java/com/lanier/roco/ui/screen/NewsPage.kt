package com.lanier.roco.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lanier.roco.repo.NewsViewModel
import com.lanier.roco.util.log

/**
 * Create by Eric
 * on 2022/7/23
 */
@Composable
fun NewsScreen(navController: NavController){
    val context = LocalContext.current
    val vm: NewsViewModel = viewModel()
    "重组".log()
    Column {
        Text(text = "新闻大全")
        Button(onClick = {
            "解析".log()
            vm.analyzeSpiritX()
        }) {
            Text(text = "load")
        }
    }
}