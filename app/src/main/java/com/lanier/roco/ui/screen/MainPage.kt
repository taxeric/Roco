package com.lanier.roco.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lanier.roco.entity.EggGroupEntity
import com.lanier.roco.R
import com.lanier.roco.manager.GroupManager
import com.lanier.roco.manager.ROUTE_GROUP_SCREEN
import com.lanier.roco.util.SpiritHelper

/**
 * Create by Eric
 * on 2022/7/7
 */
@Composable
fun MainScreen(navController: NavController){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(GroupManager.mGroupList) { index, data ->
            GroupView(groupEntity = data, index = index, navController)
        }
    }
}

@Composable
fun GroupView(groupEntity: EggGroupEntity, index: Int, navController: NavController){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable {
            SpiritHelper.currentGroupId = groupEntity.groupId
            navController.navigate(ROUTE_GROUP_SCREEN)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.ic_egg), contentDescription = "egg",
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight())
            Text(text = groupEntity.groupName, color = Color(0xFF000000),
                modifier = Modifier
                    .weight(4f)
                    .background(Color.Yellow))
            Text(text = "#${index + 1}", color = Color(0xFFC4C4C4),
                modifier = Modifier
                    .weight(1f))
        }
    }
}
