package com.lanier.roco.manager

import com.lanier.roco.entity.EggGroupEntity
import com.lanier.roco.util.log

/**
 * Create by Eric
 * on 2022/7/7
 */
object GroupManager {

    private val _mGroupList = mutableListOf<EggGroupEntity>()
    val mGroupList get() = _mGroupList

    fun reset(){
        _mGroupList.run {
            clear()
            add(EggGroupEntity("id_animal", "动物组", "group_animal.html"))
            add(EggGroupEntity("id_plant", "植物组"))
            add(EggGroupEntity("id_spirit", "精灵组", "group_spirit.html"))
            add(EggGroupEntity("id_sky", "天空组", "group_sky.html"))
            add(EggGroupEntity("id_immortal", "不死组", "group_immortal.html"))
            add(EggGroupEntity("id_clever", "乖乖组", "group_clever.html"))
            add(EggGroupEntity("id_guard", "守护组"))
            add(EggGroupEntity("id_power", "力量组", "group_power.html"))
            add(EggGroupEntity("id_ground", "大地组", "group_ground.html"))
        }
        "success -> ${_mGroupList.size}".log()
    }
}