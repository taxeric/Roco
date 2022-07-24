package com.lanier.roco.repo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Create by Eric
 * on 2022/7/24
 */
class OtherViewModel: ViewModel() {

    var testStr by mutableStateOf("")
}