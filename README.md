# CQUPToolbox

![School](https://img.shields.io/badge/School-CQUPT-green.svg)
![GitHub stars](https://img.shields.io/github/stars/YenalyLiew/CQUPToolbox)
![GitHub forks](https://img.shields.io/github/forks/YenalyLiew/CQUPToolbox)

一个简单实用的CQUPT工具箱。

仅供学习 Android 开发使用，所有接口都为实验性接口，并不会对学校网站造成任何影响。

本软件主要采用Kotlin编写，主题为Material3与MaterialComponent混合体。

## 主要功能

### 1. 查询空教室（需要本地创建环境）
空教室查询，从而获得更准确、更详尽的空教室信息。
### 2. 智慧体育总览（需要本地创建环境）
相当于从今日校园上查询到的体育打卡综合信息，比今日校园上提供的更详细，可以看到奖励的详细打卡情况。
### 3. 智慧体育课外锻炼记录（需要本地创建环境）
打卡的详细记录。如果是跑步，还同时给你计算了跑步大概的速度，实际打卡圈数。
### 4. 智慧体育摄像头采集记录（需要本地创建环境）
今日校园里查询自己的采集记录只有简单的文字叙述，但在这里不仅有更精确的记录（包括是哪个摄像头），还有采集后自己的人脸照片（还有相似度）。仅能查询自己。
### 5. 一键健康打卡（需要本地创建环境）
如题。仅供学习接口如何使用，并不能真正的实现一键打卡。
### 6. 一键非审批出校申请（需要本地创建环境）
如题。仅供学习接口如何使用，并不能真正的实现一键打卡。

## 实现

依靠本地自建的网站而非学校官方网站爬取各种信息，方便学习爬虫与安卓开发的知识。

## 待实现

1. ~~Paging3运用到分页~~（已实现，并添加Paging列表分隔符）。
3. Flow运用在Repository层而不是LiveData。
4. 优化界面。

## 不足

1. LiveData用的不够完美，存在部分粘性事件。
2. 部分代码臃肿，可以适当优化。
