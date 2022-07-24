package com.lanier.roco.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lanier.roco.repo.viewmodel.SpiritViewModel

/**
 * Create by Eric
 * on 2022/7/23
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpiritScreen(navController: NavController, title: String){
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            var startSearch by remember {
                mutableStateOf(false)
            }
            var searchStr by remember {
                mutableStateOf("")
            }
            Column {
                SmallTopAppBar(
                    title = { Text(text = title) },
                    actions = {
                        IconButton(onClick = {
                            startSearch = !startSearch
                        }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
                        }
                    }
                )
                AnimatedVisibility(visible = startSearch) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchStr,
                            onValueChange = {
                                searchStr = it
                            },
                            label = {
                                Text(text = "搜索")
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    searchStr = ""
                                }) {
                                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "clear")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        SpiritListMain(navController, innerPadding)
    }
}

@Composable
fun SpiritListMain(navController: NavController, padding: PaddingValues){
    val vm: SpiritViewModel = viewModel()
    Column(modifier = Modifier.padding(padding)) {
        Text(text = "精灵列表")
    }
}
