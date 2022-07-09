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
            add(EggGroupEntity("id_animal", "动物组"))
            add(EggGroupEntity("id_plant", "植物组"))
            add(EggGroupEntity("id_spirit", "精灵组"))
            add(EggGroupEntity("id_sky", "天空组", "test_1.html"))
            add(EggGroupEntity("id_immortal", "不死组"))
            add(EggGroupEntity("id_clever", "乖乖组"))
            add(EggGroupEntity("id_guard", "守护组"))
            add(EggGroupEntity("id_power", "力量组"))
        }
        "success -> ${_mGroupList.size}".log()
    }
}