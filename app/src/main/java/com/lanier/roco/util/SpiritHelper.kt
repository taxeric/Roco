package com.lanier.roco.util

import android.content.Context
import com.google.gson.Gson
import com.lanier.roco.entity.*
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

    /**
     * 原理
     *
     * 只有雄性能遗传
     *
     * A(公)+B(母)=B[X]
     * A(公)+C(母)=C[X]
     * B(公)+C(母)=C[Y1]
     * C(公)+B(母)=B[Y2]
     *
     * B(公X)+C(母)=C[X,Y1]
     * C(公X)+B(母)=B[X,Y2]
     */
    fun calculateSkills(fatherName: String, motherName: String, groupId: String){
        if (!spiritMap.containsKey(groupId)){
            "需要初始化 $groupId".log()
            return
        }
        //找到A公B母的指定精灵
        val allSkillsData = getSpiritGeneticSkillFromParentName(fatherName, motherName, groupId)
        if (allSkillsData == null){
            "未找到指定精灵".log()
            return
        }
        "第二代精灵 -> ${allSkillsData.father.spiritName}[公] + ${allSkillsData.mother.spiritName}[母] = ${allSkillsData.skills}".log()
        //找到除了A公B母的所有A公+C母精灵
        val allFatherSkillsData = getSpiritGeneticsDataFromFamilyNameExceptSelf(fatherName, motherName, groupId)
        //存放所有符合A公C母=[X]的所有精灵(即与A公B母技能只要有一个一样的精灵)
        val acSpiritResult = mutableListOf<SpiritData>()
        //遍历A公B母孵出蛋蛋的所有技能
        allSkillsData.skills.forEachIndexed AgBm@{ index, skill ->
            //遍历除了A公B母的所有A公+C母精灵
            allFatherSkillsData.forEach AgCm@{ spirit ->
                var occurent = 0
                //遍历除了A公B母的所有A公+C母精灵技能
                spirit.skills.forEach AgCmSkill@{ baseSkill ->
                    //如果不是空技能
                    if (baseSkill.skillName != "无") {
                        //如果A公B母当前遍历的技能名与A公C母精灵技能名一致,则不再遍历A公C母的其他技能
                        if (skill.skillName == baseSkill.skillName) {
                            occurent ++
                            "same skill of spirits -> ${spirit.father.spiritName} + ${spirit.mother.spiritName} = $skill".log()
                            val mSpirit = spirit.copy(skills = listOf(Skill(skillName = baseSkill.skillName)))
                            acSpiritResult.add(mSpirit)
                            return@AgCmSkill
                        }
                    }
                }
            }
        }
        //存放所有可携带多代遗传技能的精灵
        val resultSpiritData = mutableListOf<SpiritData>()
        if (acSpiritResult.isNotEmpty()){
            "共发现 ${acSpiritResult.size} 组精灵".log()
            //遍历A公C母,找到B公C母和C公B母的精灵组
            acSpiritResult.forEach { baseParent->
//                "parent skill -> ${baseParent.skills}".log()
                val bgcm = getSpiritGeneticSkillFromParentName(motherName, baseParent.mother.spiritName, groupId)
                val cgbm = getSpiritGeneticSkillFromParentName(baseParent.mother.spiritName, motherName, groupId)
                if (bgcm != null){
                    //彩翼虫 + 咔咔鸟 = 信号之光
//                    "父: ${bgcm.father.spiritName} + 母: ${bgcm.mother.spiritName} = ${bgcm.skills} ${bgcm.skills.size}".log()
                    val list = bgcm.skills.toMutableList()
                    list.add(baseParent.skills[0])
                    val resultSpirit = bgcm.copy(skills = list)
//                    "父: ${resultSpirit.father.spiritName} + 母: ${resultSpirit.mother.spiritName} = ${resultSpirit.skills} ${resultSpirit.skills.size}".log()
                    resultSpiritData.add(resultSpirit)
                }
                if (cgbm != null){
                    //咔咔鸟 + 彩翼虫 = 风之保护
//                    "父: ${cgbm.father.spiritName} + 母: ${cgbm.mother.spiritName} = ${cgbm.skills} ${cgbm.skills.size}".log()
                    val list = cgbm.skills.toMutableList()
                    list.add(baseParent.skills[0])
                    val resultSpirit = cgbm.copy(skills = list)
//                    "父: ${resultSpirit.father.spiritName} + 母: ${resultSpirit.mother.spiritName} = ${resultSpirit.skills} ${resultSpirit.skills.size}".log()
                    resultSpiritData.add(resultSpirit)
                }
            }
        } else {
            "未发现可多代遗传精灵".log()
        }
        //总结
        if (resultSpiritData.isNotEmpty()){
            resultSpiritData.forEach { spiritData ->
//                spiritData.skills.toMutableList().add(allSkillsData.skills[0])
                "第三代精灵 -> ${spiritData.father.spiritName}[公] + ${spiritData.mother.spiritName}[母] = ${spiritData.skills}".log()
            }
        }
    }

    fun getSpiritGeneticsDataFromFamilyNameExceptSelf(fatherName: String, motherName: String, groupId: String): List<SpiritData>{
        val baseSpiritData = getSpiritDataByGroupId(groupId)
        val result = mutableListOf<SpiritData>()
        baseSpiritData.forEach {
            if (it.father.spiritName == fatherName && it.mother.spiritName != motherName){
                result.add(it)
            }
        }
        return result
    }

    fun getSpiritGeneticSkillFromParentName(fatherName: String, motherName: String, groupId: String): SpiritData?{
        val baseSpiritData = getSpiritDataByGroupId(groupId)
        val result = mutableListOf<SpiritData>()
        baseSpiritData.forEach {
            if (it.father.spiritName == fatherName && it.mother.spiritName == motherName){
                result.add(it)
                return@forEach
            }
        }
        if (result.isEmpty()){
            return null
        }
        return result[0]
    }

    fun getSpiritGeneticSkillFromFatherName(name: String, groupId: String): List<SpiritData>{
        val baseSpiritData = getSpiritDataByGroupId(groupId)
        val result = mutableListOf<SpiritData>()
        baseSpiritData.forEach {
            if (it.father.spiritName == name){
                result.add(it)
            }
        }
        return result
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
                    stringBuilder.append("\"father\":{\"spiritName\":\"").append(tds[0].text()).append("\",\"male\":true},")
                    stringBuilder.append("\"mother\":{\"spiritName\":\"").append(tds[1].text()).append("\",\"male\":false},")
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