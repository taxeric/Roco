package com.lanier.roco.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lanier.roco.entity.SpiritData
import com.lanier.roco.util.CalculateEntity
import com.lanier.roco.util.SpiritHelper
import com.lanier.roco.util.log

/**
 * Create by Eric
 * on 2022/7/17
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneticScreen(navController: NavController){
    val groupName = SpiritHelper.getCurrentGroupData()?.groupName ?: "出错了"
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            SmallTopAppBar(
                title = { Text(text = groupName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {}
            )
        }
    ){ innerPadding ->
        GeneticPageImpl(innerPadding = innerPadding)
    }
}

@Composable
fun GeneticPageImpl(innerPadding: PaddingValues){
    val context = LocalContext.current
    var fatherName by remember {
        mutableStateOf("")
    }
    var motherName by remember {
        mutableStateOf("")
    }
    var getData by remember {
        mutableStateOf(false)
    }
    val list = remember {
        mutableStateListOf<SpiritData>()
    }
    var errorMsg by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()) {
        OutlinedTextField(
            value = fatherName,
            onValueChange = {
                fatherName = it
            },
            label = {
                Text(text = "雄性精灵名字")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 2.dp)
        )
        OutlinedTextField(
            value = motherName,
            onValueChange = {
                motherName = it
            },
            label = {
                Text(text = "雌性精灵名字")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 2.dp)
        )
        Button(
            onClick = {
                getData = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 2.dp)
        ) {
            Text(text = "获取")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = errorMsg, color = Color.Red)
        DetailsLazyList(list = list)
    }
    if (getData) {
        if (fatherName.isEmpty() || motherName.isEmpty()) {
            Toast.makeText(context, "完善父母名字", Toast.LENGTH_SHORT).show()
        } else {
            val result = SpiritHelper.calculateSkills(fatherName, motherName, SpiritHelper.currentGroupId)
            if (result.isSuccess) {
                val baseData = result.getOrDefault(CalculateEntity("unknow error"))
                if (baseData.data.isEmpty()) {
                    errorMsg = baseData.errorMsg
                } else {
                    baseData.data.forEach {
                        "${it.father.spiritName} ${it.mother.spiritName} = ${it.skills}".log()
                    }
                    list.clear()
                    list.addAll(baseData.data)
                    errorMsg = ""
                }
            }
        }
        getData = false
    }
}
