package com.yenaly.cqupttoolbox.logic.model

import java.text.SimpleDateFormat
import java.util.*


/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/04/01 001 11:35
 * @Description : Description...
 */
sealed class SportsCameraPagingModel {
    data class InfoItem(val item: SportsCameraRecordModel.Rows) : SportsCameraPagingModel()
    data class SeparatorItem(val description: String) : SportsCameraPagingModel()
}

val SportsCameraPagingModel.InfoItem.date: String
    get() {
        val beforeDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val recordTime = beforeDateFormat.parse(item.recordTime)
        val afterDateFormat = SimpleDateFormat("yyyy-MM-dd EEEE", Locale.CHINA)
        return afterDateFormat.format(recordTime!!)
    }