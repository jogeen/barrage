##  局域答辩网弹幕系统3.0

### 概要
该系统适用于上课，答辩等局域网公屏的场景。


### 系统架构

![1577771973727](https://github.com/jogeen/barrage/blob/master/images/架构.png)

### 工程结构

-barrage
--barrage-anchor   #弹幕展示端(主播端)
--barrage-core      #核心包
--barrage-web     #web端，普通用户发弹幕，和管理员系统管理
--barrage.sql      #初始sql文件

### 功能模块

#### 共屏主播

- 启动 barrage-anchor，默认占用端口号5233

#### 管理员

- 链接主机：可以链接和切换不同的共屏的主播端。
- 用户充值：可以给用户充值金币数
- 题目发布：可以在线发布题目，指定题目奖励金币值。
#### 普通用户


- 文字弹幕：发送文本文字
- 礼物弹幕：发送礼物弹幕，需要消耗对应金币数
- 在线答题：在线答题，可以获取金币

### 快速入门

1. 共屏主播端启动 barrage-anchor模块
2. 管理员端，准备数据库和redis
3. 导入barrage.sql
4. 管理员启动barrage-web
5. 管理员访问localhost:8080/admin.html
6. ![1577771913040](https://github.com/jogeen/barrage/blob/master/images/admin.png)

7.用后访问  管理员IP:8080/login.html登录

![1577772265796](https://github.com/jogeen/barrage/blob/master/images/login.png)
8.登录成功之后的主页。
![](https://github.com/jogeen/barrage/blob/master/images/index.png)

9.发送弹幕

![1577772372945](https://github.com/jogeen/barrage/blob/master/images/barrage.png)
