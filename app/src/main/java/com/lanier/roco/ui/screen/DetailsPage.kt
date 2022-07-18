package com.lanier.roco.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.lanier.roco.R
import com.lanier.roco.entity.SpiritData
import com.lanier.roco.manager.ROUTE_GENETIC_SCREEN
import com.lanier.roco.ui.common.TitleBar
import com.lanier.roco.util.SpiritHelper
import com.lanier.roco.util.log
import kotlinx.coroutines.delay

/**
 * Create by Eric
 * on 2022/7/9
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController){
    val groupName = SpiritHelper.getCurrentGroupData()?.groupName ?: "出错了"
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember {
        mutableStateOf(false)
    }
    var menuOpened by remember {
        mutableStateOf(false)
    }
    if (showSnackBar) {
        LaunchedEffect(key1 = "showSnackBar") {
            snackbarHostState.showSnackbar("待完善")
//            delay(2000L)
            showSnackBar = false
        }
    }
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
                actions = {
                    IconButton(onClick = {
                        menuOpened = !menuOpened
                    }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ){ innerPadding ->
        DetailScreenImpl(innerPadding)
    }
    if (menuOpened){
        DetailMenuItems {
            when (it) {
                0 -> navController.navigate(ROUTE_GENETIC_SCREEN)
                1 -> showSnackBar = true
            }
            menuOpened = false
        }
    }
}

@Composable
fun DetailScreenImpl(innerPadding: PaddingValues){
    val context = LocalContext.current
    val spiritList = remember {
        SpiritHelper.getSpiritDataByCurrentGroupId().toMutableStateList()
    }
    if (spiritList.isEmpty()) {
        LaunchedEffect(key1 = "init_data") {
            SpiritHelper.initSpiritDataByCurrentId(context) {
                if (it) {
                    spiritList.clear()
                    spiritList.addAll(SpiritHelper.getSpiritDataByCurrentGroupId())
                }
            }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), verticalAlignment = Alignment.CenterVertically) {
            val male = buildAnnotatedString {
                appendInlineContent("ic_male")
                append(" 父")
            }
            val female = buildAnnotatedString {
                appendInlineContent("ic_female")
                append(" 母")
            }
            val inlineContent = mapOf(
                "ic_male" to InlineTextContent(
                    placeholder = Placeholder(width = 2.em, height = 2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center),
                    children = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_male),
                            contentDescription = "male",
                        )
                    }),
                "ic_female" to InlineTextContent(
                    placeholder = Placeholder(width = 2.em, height = 2.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center),
                    children = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_female),
                            contentDescription = ""
                        )
                    }))
            Text(text = male, inlineContent = inlineContent, modifier = Modifier.weight(2.2f))
            Text(text = female, inlineContent = inlineContent, modifier = Modifier.weight(2.2f))
            Text(text = "skills", modifier = Modifier.weight(3f))
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp))
        DetailsLazyList(list = spiritList)
    }
}

@Composable
fun DetailsLazyList(list: List<SpiritData>){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        if (list.isEmpty()) {
            item {
                Text(text = "数据还未初始化或出错了", modifier = Modifier.fillMaxWidth())
            }
        } else {
            itemsIndexed(list) { index, data ->
                DetailItem(data = data)
            }
        }
    }
}

@Composable
fun DetailItem(data: SpiritData){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = data.father.spiritName, fontSize = 12.sp, modifier = Modifier.weight(2.2f))
        Text(text = data.mother.spiritName, fontSize = 12.sp, modifier = Modifier.weight(2.2f))
        Row(modifier = Modifier
            .weight(3f)
            .horizontalScroll(
                rememberScrollState()
            )){
            data.skills.forEachIndexed {index, it ->
                Text(text = it.skillName,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(if (index == 0) 0.dp else 5.dp, 2.dp, 0.dp, 2.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFFE8E8E8))
                        .padding(1.dp)
                )
            }
        }
    }
}

@Composable
fun DetailMenuItems(onDismiss: (Int) -> Unit){
    val scrollState = rememberScrollState()
    Dialog(
        onDismissRequest = { onDismiss(-1) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .verticalScroll(scrollState)) {
            Text(text = "调试", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = { onDismiss(0) }, modifier = Modifier.fillMaxWidth()){
                Text(text = "三代精灵")
            }
            TextButton(onClick = { onDismiss(1) }, modifier = Modifier.fillMaxWidth()){
                Text(text = "n代精灵")
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(onClick = { onDismiss(-1) }, modifier = Modifier
                .align(Alignment.End)
                .padding(5.dp)) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
