# 2022/10/09
新项目[RocoGuide]已开源，点击[前往](https://github.com/taxeric/RocoGuide)**
# 2022/07/25
**本项目不再维护，新项目[RocoGuide]暂时闭源。  
本项目目前提供:
- [百度百科精灵大全数据获取](https://github.com/taxeric/Roco/blob/master/app/src/main/java/com/lanier/roco/util/JsoupUtil.kt)
- [4399技能大全数据获取](https://github.com/taxeric/Roco/blob/master/app/src/main/java/com/lanier/roco/util/JsoupUtil.kt)
- [遗传信息获取](https://github.com/taxeric/Roco/blob/master/app/src/main/java/com/lanier/roco/util/SpiritHelper.kt)

对于某些技能，百科里额外添加了`（技能石）`，项目里没有处理，您可以根据喜好来排除这些字符。此外，由于4399洛克王国断代约3年，技能及精灵缺失严重，**您或许需要手动录入/更改数据**。

如果您**只需要遗传信息**，只需
1. 将[MainActivity.kt](https://github.com/taxeric/Roco/blob/master/app/src/main/java/com/lanier/roco/MainActivity.kt) 下的`Scaffold`方法体注释
2. 将`NavHost`取消注释

# Roco
Compose实现的洛克王国~~孵蛋技能遗传图鉴~~指南

## 截图
![screen](https://github.com/taxeric/Roco/blob/master/screen/x2.png)

## 需求变更
### 2022/07/21
为了更好学习Compose，遂将遗传图鉴变更为指南

## TODO
- 精灵大全
- 技能大全
- 环境大全
- 天气大全
- 伤害计算
- 新闻攻略

以下为原版README
---

## 原理
### 孵蛋
生下的宠物是由母宠物决定的，遗传技能是由公宠物决定的，母宠物是不负责遗传技能的。

### 技能遗传
```kotlin
* 只有雄性能遗传
*
* A(公X)+B(母)=B[X]
* A(公X)+C(母)=C[X]
* B(公Y1)+C(母)=C[Y1]
* C(公Y2)+B(母)=B[Y2]
*
* B(公X,Y1)+C(母)=C[X,Y1]
* C(公X,Y2)+B(母)=B[X,Y2]
```

## 遗传数据
- [动物组](http://news.4399.com/luoke/miji/201107-04-102712.html)
- [精灵组](http://news.4399.com/luoke/miji/201107-04-102712_2.html)
- [天空组](http://news.4399.com/luoke/miji/201107-04-102712_3.html)
- [不死组](http://news.4399.com/luoke/miji/201107-04-102712_4.html)
- [植物组](http://news.4399.com/luoke/miji/201107-04-102712_5.html)
- [乖乖组](http://news.4399.com/luoke/miji/201107-04-102712_6.html)
- [守护组](http://news.4399.com/luoke/miji/201107-04-102712_7.html)
- [力量组](http://news.4399.com/luoke/miji/201107-04-102712_8.html)
- [大地组](http://news.4399.com/gonglue/luoke/miji/348158.html)

## 库
- [Jsoup](https://github.com/jhy/jsoup)
- [Gson](https://github.com/google/gson)
