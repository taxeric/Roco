package com.lanier.roco.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lanier.roco.R
import com.lanier.roco.entity.SpiritData
import com.lanier.roco.ui.common.TitleBar
import com.lanier.roco.util.SpiritHelper
import com.lanier.roco.util.log

/**
 * Create by Eric
 * on 2022/7/9
 */
@Composable
fun DetailScreen(navController: NavController){
    val context = LocalContext.current
    val spiritList = remember {
        mutableStateListOf<SpiritData>()
    }
    var hasCheck by remember {
        mutableStateOf(false)
    }
    val parseData = SpiritHelper.getSpiritDataByCurrentGroupId().toMutableStateList()
    parseData.forEach {
        "${it.father} + ${it.mother} = ${it.skills}".log()
    }
    spiritList.clear()
    spiritList.addAll(parseData)
    if (!hasCheck && spiritList.isEmpty()) {
        hasCheck = true
        "has check".log()
        LaunchedEffect(key1 = "init_data") {
            SpiritHelper.initSpiritDataByCurrentId(context)
        }
    }
    val groupName = SpiritHelper.getCurrentGroupData()?.groupName ?: "出错了"
    Column(modifier = Modifier.fillMaxSize()) {
        TitleBar(backResource = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24), title = groupName){
            navController.popBackStack()
        }
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
                Text(text = "未添加或出错了", modifier = Modifier.fillMaxWidth())
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
