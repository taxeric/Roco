package com.lanier.roco.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lanier.roco.repo.viewmodel.OtherViewModel

/**
 * Create by Eric
 * on 2022/7/23
 */
@Composable
fun OtherScreen(navController: NavController){
    val vm: OtherViewModel = viewModel()
    Column {
        OutlinedTextField(value = vm.testStr, onValueChange = {
            vm.testStr = it
        })
    }
}
