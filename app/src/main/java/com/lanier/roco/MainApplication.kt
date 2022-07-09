package com.lanier.roco

import android.app.Application
import com.lanier.roco.manager.GroupManager
import com.lanier.roco.util.SpiritHelper

/**
 * Create by Eric
 * on 2022/7/7
 */
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        GroupManager.reset()
        SpiritHelper.resetGroupData(GroupManager.mGroupList)
    }
}