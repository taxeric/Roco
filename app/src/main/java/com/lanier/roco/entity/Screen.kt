package com.lanier.roco.entity

import com.lanier.roco.manager.ROUTE_SCREEN_MAIN_NEWS_LIST
import com.lanier.roco.manager.ROUTE_SCREEN_MAIN_OTHER_LIST
import com.lanier.roco.manager.ROUTE_SCREEN_MAIN_SPIRIT_LIST

/**
 * Create by Eric
 * on 2022/7/23
 */

sealed class Screen(val route: String, val title: String) {
    object SpiritList : Screen(ROUTE_SCREEN_MAIN_SPIRIT_LIST, "精灵")
    object NewsList : Screen(ROUTE_SCREEN_MAIN_NEWS_LIST, "新闻")
    object OtherList: Screen(ROUTE_SCREEN_MAIN_OTHER_LIST, "其他")
}
