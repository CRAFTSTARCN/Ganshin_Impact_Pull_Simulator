# Ganshin Impact Pull Simulator
**原神抽卡模拟器开发手册 0.0.1**  
***2021/2/18***  
**Powered by** ***Ag2s***  
**Developed by Visual Studio Code**
****

## 介绍
目前仅支持up池（   

## 获取
可以直接下载release，并按照Guide中的内容启动

## 构建

### JDK
使用JDK构建  
推荐版本：**Java 15.0.2**   
最低版本：Java 11  
*作者并未使用更低版本进行构建，兼容性有待测试*

### 工具
**使用VSCODE（推荐）：** 建议使用Viual Studio Code 安装 Java Extension Pack 然后打开本项目文件夹作为工作区，使用Java Projects扩展的“导出到Jar”进行构建。此扩展导出到Jar文件时可将第三方jar库同时打包进入jar文件中，方便分享与再次发布  

直接使用命令行构建：进入项目文件夹，输入：
****
```
javac -cp ".;.\lib\*" -sourcepath  ".\src\" -encoding UTF-8  src\App.java  -d .\bin      
```
此时，运行也需指定`classpath`，命令如下：
```
java -cp ".;.\lib\*;.\bin\" --enable-preview App
```
此时，你可以将bin、lib文件夹以及data和config文件夹打包，并在该目录下运行程序，你甚至可以编写bat脚本以方便运行。
