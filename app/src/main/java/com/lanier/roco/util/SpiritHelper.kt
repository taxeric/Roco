package com.lanier.roco.util

import android.content.Context
import com.google.gson.Gson
import com.lanier.roco.entity.BaseSpiritEntity
import com.lanier.roco.entity.EggGroupEntity
import com.lanier.roco.entity.SpiritData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

/**
 * Create by Eric
 * on 2022/7/9
 */
object SpiritHelper {

    private val gson = Gson()
    private val stringBuilder = StringBuilder()
    private val groupMap = LinkedHashMap<String, EggGroupEntity>()
    private val spiritMap = LinkedHashMap<String, List<SpiritData>>()
    var defaultLocalJsonDataPath = ""
    var currentGroupId = ""

    fun resetGroupData(list: List<EggGroupEntity>){
        list.forEach {
            groupMap[it.groupId] = it
        }
    }

    suspend fun initSpiritDataByCurrentId(context: Context, complete: (Boolean) -> Unit = {}) {
        try {
            if (groupMap.containsKey(currentGroupId)) {
                initSpiritDataByAssesName(context, groupMap[currentGroupId]!!, complete)
            } else {
                complete(false)
            }
        } catch (e: Exception){
            "failed -> $e".log()
            complete(false)
        }
    }

    suspend fun initSpiritDataByAssesName(context: Context, baseEntity: EggGroupEntity, complete: (Boolean) -> Unit = {}) {
        if (baseEntity.assetsName.isEmpty()){
            complete(false)
            return
        }
        val file = File("$defaultLocalJsonDataPath/", baseEntity.assetsName)
        withContext(Dispatchers.IO) {
            if (!file.exists()) {
                val ism = context.assets.open(baseEntity.assetsName)
                ism.copyStreamToFile(file)
            }
        }
        val document = Jsoup.parse(file, )
        withContext(Dispatchers.Default) {
            initData(document, baseEntity.groupId)
        }
        complete(true)
    }

    private fun initData(document: Document?, groupId: String) {
        document?.run {
            stringBuilder.delete(0, stringBuilder.length)
            stringBuilder.append("{\"groupId\":\"${groupId}\",\"data\":[")
            val trs = this.select("table").select("tr")
            trs.forEachIndexed {index, tr ->
                val tds = tr.select("td")
                if (tds.size == 3){
                    if (tds[2].text().contains("遗传") && tds[2].text().contains("技能")){
                        return@forEachIndexed
                    }
                    stringBuilder.append("{")
                    stringBuilder.append("\"father\":{\"spiritName\":\"").append(tds[0].text()).append("\"},")
                    stringBuilder.append("\"mother\":{\"spiritName\":\"").append(tds[1].text()).append("\"},")
                    stringBuilder.append("\"skills\":[")
                    val skills = tds[2].text().split("、")
                    skills.forEachIndexed { index1, skill ->
                        stringBuilder.append("{\"skillName\":\"").append(skill).append("\"}")
                        if (index1 != skills.size - 1){
                            stringBuilder.append(",")
                        }
                    }
                    stringBuilder.append("]}")
                }
                if (index != trs.size - 1){
                    stringBuilder.append(",")
                }
            }
            stringBuilder.append("]}")
            val base = gson.fromJson(stringBuilder.toString(), BaseSpiritEntity::class.java)
            spiritMap[groupId] = base.data
        }
    }

    fun getSpiritDataByGroupId(id: String): List<SpiritData>{
        if (spiritMap.containsKey(id)){
            return spiritMap[id]!!
        }
        return emptyList()
    }

    fun getSpiritDataByCurrentGroupId(): List<SpiritData>{
        return getSpiritDataByGroupId(currentGroupId)
    }

    fun getCurrentGroupData(): EggGroupEntity?{
        if (currentGroupId.isEmpty()){
            return null
        }
        return groupMap[currentGroupId]!!
    }

    fun reset(){
        stringBuilder.delete(0, stringBuilder.length)
    }
}