package com.lanier.roco.repo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lanier.roco.util.SpiritHelper
import com.lanier.roco.util.copyStreamToFile
import com.lanier.roco.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.File

/**
 * Create by Eric
 * on 2022/7/23
 *
 * 本类用于测试获取精灵及相关数据
 */
class NewsViewModel: ViewModel() {

    /**
     * 解析精灵数据
     * @param url 百科url
     */
    fun analyzeSpiritX(url: String){
        viewModelScope.launch {
            "load -> $url".log()
            try {
                //测试呼呼猪
                val sb = StringBuilder()
                sb.append("{")
                var name = ""
                var spiritId = -1
                withContext(Dispatchers.Default){
                    val document = Jsoup.connect(url)
                        .maxBodySize(1024*1024*5)
                        .get()
                    val baseInfo = document.select("div[class=basic-info J-basic-info cmn-clearfix]")
                    val baseLeft = baseInfo.select("dl[class=basicInfo-block basicInfo-left]")
                    val baseRight = baseInfo.select("dl[class=basicInfo-block basicInfo-right]")
                    val leftDataName = baseLeft.select("dt[class=basicInfo-item name]")
                    val leftDataValue = baseLeft.select("dd[class=basicInfo-item value]")
                    val rightDataName = baseRight.select("dt[class=basicInfo-item name]")
                    val rightDataValue = baseRight.select("dd[class=basicInfo-item value]")
                    val avatarDiv = document.select("div[class=summary-pic]")
                    val avatar = avatarDiv.select("a").attr("href")
                    sb.append("\"avatar\":").append("\"https://baike.baidu.com").append(avatar).append("\",")
                    if (leftDataName.size == leftDataValue.size){
                        leftDataName.forEachIndexed {index, data ->
                            var trimKey = data.text().replace(" ", "")
                            when (trimKey) {
                                "序号" -> {
                                    trimKey = "number"
                                    val number = leftDataValue[index].text().toInt()
                                    spiritId = number
                                    sb.append("\"$trimKey\":").append(number).append(",")
                                }
                                "主属性" -> {
                                    trimKey = "primary_attributes_id"
                                    sb.append("\"$trimKey\":").append(getIdByAttrName(leftDataValue[index].text())).append(",")
                                }
                                "副属性" -> {
                                    trimKey = "secondary_attributes_id"
                                    sb.append("\"$trimKey\":").append(getIdByAttrName(leftDataValue[index].text())).append(",")
                                }
                                "属性" -> {
                                    trimKey = "primary_attributes_id"
                                    sb.append("\"$trimKey\":").append(getIdByAttrName(leftDataValue[index].text())).append(",")
                                }
                                "组别" -> {
                                    trimKey = "group_id"
                                    sb.append("\"$trimKey\":").append(getIdByGroupName(leftDataValue[index].text())).append(",")
                                }
                                else -> {
                                    when (trimKey) {
                                        "中文名" -> {
                                            trimKey = "name"
                                            name = leftDataValue[index].text()
                                        }
                                    }
                                    sb.append("\"$trimKey\":").append("\"")
                                        .append(leftDataValue[index].text()).append("\"").append(",")
                                }
                            }
                        }
                    }
                    if (rightDataName.size == rightDataValue.size){
                        rightDataName.forEachIndexed {index, data ->
                            var trimKey = data.text().replace(" ", "")
                            when (trimKey){
                                "爱好" -> {
                                    trimKey = "hobby"
                                    sb.append("\"$trimKey\":").append("\"").append(rightDataValue[index].text()).append("\"").append(",")
                                }
                            }
                            if (trimKey == "身高"){
                                trimKey = "height"
                                val height = rightDataValue[index].text().replace("m", "").trim().replace(" ", "").toFloat()
                                sb.append("\"$trimKey\":").append(height).append(",")
                            }
                            if (trimKey == "体重"){
                                trimKey = "weight"
                                val weight = rightDataValue[index].text().lowercase().replace("kg", "").trim().replace(" ", "").toFloat()
                                sb.append("\"$trimKey\":").append(weight).append(",")
                            }
                            if (trimKey == "序号"){
                                trimKey = "number"
                                val number = rightDataValue[index].text().toInt()
                                spiritId = number
                                sb.append("\"$trimKey\":").append(number).append(",")
                            }
                        }
                    }
                    if (name.isNotEmpty()) {
                        val introduce = document.select("div[class=lemma-summary]").text()
                            .replace("$name，腾讯游戏《洛克王国》中的宠物。", "")
                            .replace("腾讯游戏《洛克王国》中的宠物。", "")
                            .trim()
                        sb.append("\"description\":").append("\"").append(introduce).append("\"").append(",")
                    }
                    val tables = document.select("table")
                    if (tables.size >= 2){
                        val zzzBody = tables[0].select("tbody")
                        val zzztrs = zzzBody.select("tr")
                        zzztrs.forEachIndexed {index, tr ->
                            val th = tr.select("td")
                            if (th.size == 7) {
                                val firstTh = th[0].text().trim().replace(" ", "")
                                if (firstTh == name) {
                                    sb
                                        .append("\"race_power\":").append(th[1].text()).append(",")
                                        .append("\"race_attack\":").append(th[2].text()).append(",")
                                        .append("\"race_defense\":").append(th[3].text()).append(",")
                                        .append("\"race_magic_attack\":").append(th[4].text()).append(",")
                                        .append("\"race_magic_defense\":").append(th[5].text()).append(",")
                                        .append("\"race_speed\":").append(th[6].text()).append(",")
                                    return@forEachIndexed
                                }
                            }
                        }
                        val skillsBody = tables[1].select("tbody")
                        val skillTrs = skillsBody.select("tr")
                        sb.append("\"skills\":[")
                        skillTrs.forEachIndexed {index, it ->
                            //只取技能名
                            if (index != 0){
                                val th = it.select("td")
                                sb.append("\"").append(th[0].text()).append("\"")
                            }
                            if (index != 0 && index != skillTrs.size - 1){
                                sb.append(",")
                            }
                        }
                        sb.append("]")
                    }
                }
                if (sb.last() == ','){
                    sb.delete(sb.length - 1, sb.length)
                }
                sb.append("}")
                if (name.isNotEmpty() && spiritId != -1) {
                    val file = File("${SpiritHelper.defaultLocalJsonDataPath}/", "$spiritId.json")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    file.writeText(sb.toString())
                    "下载 $name 数据完成".log()
                }
            } catch (e: Exception){
                "failed -> ${e.message}".log()
            }
        }
    }

    /**
     * 获取百度百科洛克王国宠物大全的链接
     * 此处获取后保存到 assets-test.html
     */
    suspend fun analyzeSpirit(context: Context, result: suspend (List<String>) -> Unit){
        viewModelScope.launch {
            try {
                val list = mutableListOf<String>()
                val file = File("${SpiritHelper.defaultLocalJsonDataPath}/", "test.html")
                if (!file.exists()){
                    val ism = context.assets.open("test.html")
                    ism.copyStreamToFile(file)
                }
                withContext(Dispatchers.Default){
/*                    val document = Jsoup.connect("https://baike.baidu.com/item/%E6%B4%9B%E5%85%8B%E7%8E%8B%E5%9B%BD%E5%AE%A0%E7%89%A9%E5%A4%A7%E5%85%A8/4962564")
                        .maxBodySize(1024*1024*5)
                        .get()*/
                    val document = Jsoup.parse(file)
                    val elements = document.select("div[class=para]")
                    val id = elements.text()
                    val links = elements.select("a")
                    "id -> $id".log()
                    links.forEach {
                        val link = it.attr("href")
                        "link -> https://baike.baidu.com$link".log()
                        list.add("https://baike.baidu.com$link")
                    }
                }
                "success -> ${list.size}".log()
                result(list)
            } catch (e: Exception){
                "failed -> ${e.message}".log()
                result(emptyList())
            }
        }
    }

    /**
     * 获取4399洛克王国内的全部技能
     * 由于4399断代，很多技能遗失，故需要做完善处理
     */
    fun analyzeSkill(){
        try {
            viewModelScope.launch {
                val sb = StringBuilder()
                withContext(Dispatchers.Default) {
                    val document = Jsoup.connect("https://news.4399.com/luoke/jinengsearch/")
                        .maxBodySize(1024*1024*5)
                        .get()
                    val elements = document.select("div[class=m3-item]")
                    "size = ${elements.size}".log()
                    sb.append("{")
                        .append("\"totalItem\":").append(elements.size).append(",")
                        .append("\"data\":[")
                    if (elements.size != 0) {
                        elements.forEachIndexed {index, data ->
                            val tds = data.select("td")
                            val v0 = elements.size - index
                            val v1 = tds[0].text()//技能名
                            val v2str = tds[4].text()//属性
                            val v3 = tds[5].text()//简介(效果)
                            val v4str = tds[7].text()//类型
                            val v5 = tds[9].text()//威力
                            val v6 = tds[11].text()//pp
                            val v7 = tds[13].text().substring(0, 1)//是否遗传
                            val v8 = tds[15].text()//是否必中
                            val v9str = tds[17].text()//是否先手
                            val v2 = getIdBySkillName(v2str)
                            val v4 = when (v4str) {
                                "魔法" -> {
                                    1
                                }
                                "物理" -> {
                                    2
                                }
                                else -> {
                                    3
                                }
                            }
                            val v9 = if (v9str.contains("+")){
                                v9str.substring(v9str.length - 1).toInt()
                            } else {
                                0
                            }
                            val v10 = tds[19].text()//附加效果
                            sb.append("{")
                                .append("\"id\":").append(v0).append(",")
                                .append("\"name\":").append("\"").append(v1).append("\",")
                                .append("\"attributes_id\":").append(v2).append(",")
                                .append("\"description\":").append("\"").append(v3).append("\",")
                                .append("\"skill_type_id\":").append(v4).append(",")
                                .append("\"value\":").append(if (v5 == "-") 0 else v5.toInt()).append(",")
                                .append("\"amount\":").append(if (v6 == "-") 0 else v6.toInt()).append(",")
                                .append("\"is_genetic\":").append(if (v7 == "是") 1 else 0).append(",")
                                .append("\"speed\":").append(v9).append(",")
                                .append("\"is_be\":").append(if (v8 == "是") 1 else 0).append(",")
                                .append("\"additional_effects\":").append("\"").append(v10).append("\"")
                                .append("}")
                            if (index != elements.size - 1){
                                sb.append(",")
                            }
                        }
                        sb.append("]}")
                    }
                }
                withContext(Dispatchers.IO){
                    val file = File("${SpiritHelper.defaultLocalJsonDataPath}/", "skills.json")
                    if (!file.exists()){
                        file.createNewFile()
                    }
                    file.writeText(sb.toString().replace("查看详情", ""))
                    "success".log()
                }
            }
        } catch (e: Exception) {
            "failed -> ${e.message}".log()
        }
    }

    private fun getIdByGroupName(name: String) = when(name){
        "植物组" -> 2
        "精灵组" -> 3
        "天空组" -> 4
        "不死组" -> 5
        "乖乖组" -> 6
        "守护组" -> 7
        "力量组" -> 8
        "大地组" -> 9
        "动物组" -> 10
        else -> 1
    }

    private fun getIdBySkillName(name: String) = when(name) {
        "冰系" -> 1
        "恶魔系" -> 2
        "幽系" -> 3
        "土系" -> 4
        "水系" -> 5
        "石系" -> 6
        "普通系" -> 7
        "龙系" -> 8
        "火系" -> 9
        "毒系" -> 10
        "电系" -> 11
        "虫系" -> 12
        "草系" -> 13
        "武系" -> 14
        "机械系" -> 15
        "萌系" -> 16
        "翼系" -> 17
        "光系" -> 18
        "神草系" -> 19
        "神火系" -> 20
        "神水系" -> 21
        else -> -1
    }

    private fun getIdByAttrName(name: String) = when(name) {
        "冰" -> 1
        "恶魔" -> 2
        "幽灵" -> 3
        "土" -> 4
        "水" -> 5
        "石" -> 6
        "普通" -> 7
        "龙" -> 8
        "火" -> 9
        "毒" -> 10
        "电" -> 11
        "虫" -> 12
        "草" -> 13
        "武" -> 14
        "机械" -> 15
        "萌" -> 16
        "翼" -> 17
        "光" -> 18
        "神草" -> 19
        "神火" -> 20
        "神水" -> 21
        else -> -1
    }
}