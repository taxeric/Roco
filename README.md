# Roco
Compose实现的洛克王国精灵孵蛋技能遗传图鉴

## 截图
![screen](https://github.com/taxeric/Roco/blob/master/screen/x2.png)

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

## 数据
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
