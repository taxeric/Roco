package com.lanier.roco.ui.common

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lanier.roco.R

/**
 * Create by Eric
 * on 2022/7/9
 */
@Composable
fun TitleBar(backResource: Painter, backgroundColor: Color = Color.Black, titleColor: Color = Color.White, title: String, backEvent: () -> Unit = {}){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(45.dp)
        .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = backResource,
            contentDescription = "back", modifier = Modifier
                .clickable {
                    backEvent()
                }
                .weight(1f)
                .height(20.dp))
        Text(text = title, color = titleColor, modifier = Modifier.weight(9f))
    }
}
