# Ganshin Impact Pull Simulator 
**原神抽卡模拟器使用手册 ver0.0.1**  
***2020/2/17***  
**Powered by** ***Ag2s***
****

github: [Ganshin_Impact_Pull_Simulator](https://github.com/CRAFTSTARCN/Ganshin_Impact_Pull_Simulator)

## Release Note
| 版本号 | 发布时间 | 备注 |
|:-:|:-:|:-:|
|0.0.1|2021/2/17|完成框架和基础功能|
|0.0.2|2020/2/19|完成基本功能|

## 注意
请勿更改或者删除data内或者config内文件，否则会导致Fatal Error

## 运行环境
java 15.0.2 2021-01-19  
JRE：Java(TM) SE Runtime Environment (build 15.0.2+7-27)

### Note：
从Java11开始，Oracle并不提供JRE，而是引入Java Platform Module System，将该模块将完整的rt.jar和tools.jar文件分为75个不同的模块。故如果你的PC上Java版本较低或者无Java运行环境，请进入Oracle官网并下载最新的JDK安装版安装，或下载ZIP并配置环境。  
**下载地址：** [Java SE Development Kit 15 - Downloads](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html)

如果你想使用本机的较低版本进行运行，请前往Github下载源码并根据开发文档构建项目  
建议使用VSCODE进行构建

## 启动
***在Shell中启动：*** 打开powershell 进入Ganshin_Pull_Simulate.jar文件所在文件夹，输入
```
java --enable-preview -jar .\Ganshin_Pull_Simulate.jar
```
在命令行中启动  
**一键打开：** 双击launch.bat批处理文件

## 功能

进入后输入help可以查看帮助  
****
### 命令参数输入格式
参数以“--”为开头，输入参数名后使用=号设置值（help除外）  
如`pull --time=10`    
参数可以选择不输入，而是使用默认

## 列表
| 命令 | 参数(以;分隔 =号后为默认) | 描述 | 加入版本 | 最新版本
|:-:|:-:|:-:|:-:|:-:|
|help|任意个数字符串|查看帮助，参数为需要帮助的命令|0.0.1|0.0.2|
|pull|--time=1;|抽一发|0.0.1|0.0.1|
|showinfo|/|查看当前卡池信息|0.0.1|0.0.1|
|summary|/|查看总结|0.0.2|0.0.2|
|history|--max_line=65535;<br>--latest_date=0000-00-00_00:00:00;<br>--output_to=sysout;<br>--DESC=false;|查看历史信息，参数为：<br>最大条目;<br>最低日期（日期时间以_连接）;<br>输出到（sysout为打印到屏幕）;<br>是否按照新旧顺序降序|0.0.2|0.0.2|
|exit|/|退出|0.0.1|0.0.1|