package com.yenaly.cqupttoolbox.logic.model

/**
 * The model of the week time from WeCQUPT.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/28 028 20:20
 * @Description : Description...
 */
data class WeekTimeModel(
    val message: String,
    val data: Data
) {
    data class Data(
        val term: String,
        val week: String,
        val day: String
    )
}
