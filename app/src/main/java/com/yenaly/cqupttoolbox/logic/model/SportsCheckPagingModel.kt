package com.yenaly.cqupttoolbox.logic.model

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/04/01 001 11:07
 * @Description : Description...
 */
sealed class SportsCheckPagingModel {
    data class InfoItem(val item: SportsCheckRecordModel.Rows) : SportsCheckPagingModel()
    data class SeparatorItem(val description: String) : SportsCheckPagingModel()
}

val SportsCheckPagingModel.InfoItem.date: String
    get() = this.item.startTime.split(' ')[0]