package com.lanier.roco.entity

/**
 * Author: 芒硝
 * Email : 1248389474@qq.com
 * Date  : 2022/7/8 10:02
 * Desc  :
 *
 * 遗传原理：生下的宠物是由母宠物决定的，遗传技能是由公宠物决定的，母宠物是不负责遗传技能的
 * 天空组
 * 哈古利乌+咔咔鸟 → 守护咔咔鸟+雨蝶 → 守护风保雨蝶+彩翼虫；
 *
 * 精灵组
 * 火神+雪精灵 → 火之仞雪精灵+多哥 → 火之仞冰晶多哥+喵喵；
 *
 * 乖乖组
 * 花楹子+灵狐 → 毒雾灵狐+梅花鹿 → 毒雾、火之仞梅花鹿+地鼠；
 */

/**
 * 蛋蛋组别
 */
data class EggGroupEntity(
    /**
     * 蛋组id
     */
    val groupId: String = "",

    /**
     * 蛋组名称
     */
    val groupName: String = "",

    /**
     * 资源
     */
    val assetsName: String = "",

    /**
     * 蛋组icon
     */
    val groupIcon: String = "",
)

data class Spirit(
    val spiritName: String = "",
    val male: Boolean = true
)

data class BaseSpiritEntity(
    val `data`: List<SpiritData> = listOf(),
    val groupId: String = ""
)

data class SpiritData(
    val father: Spirit = Spirit(male = true),
    val mother: Spirit = Spirit(male = false),
    val skills: List<Skill> = listOf()
)

data class Skill(
    val skillName: String = ""
)
