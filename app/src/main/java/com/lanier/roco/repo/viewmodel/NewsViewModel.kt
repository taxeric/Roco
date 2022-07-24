package com.lanier.roco.repo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Create by Eric
 * on 2022/7/23
 */
class NewsViewModel: ViewModel() {

    var testStr by mutableStateOf("")
}