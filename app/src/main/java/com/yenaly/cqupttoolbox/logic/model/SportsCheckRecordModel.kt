package com.yenaly.cqupttoolbox.logic.model

import com.google.gson.annotations.SerializedName

/**
 * The model of sports check record.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/03 003 20:18
 * @Description : Description...
 */
data class SportsCheckRecordModel(
    val code: String,
    val total: Int,
    val rows: List<Rows>
) {
    data class Rows(
        val sportItem: String,
        val sportField: String,
        val mileage: String,
        val startTime: String,
        val endTime: String,
        val runStartTime: String?,
        val runEndTime: String?,
        val runLapCnt: Int?,

        @SerializedName("sportDura")
        val sportDuration: String,

        val weekly: Int,
        val isValid: String,
        val examIsValid: String,
        val extIsValid: String,
        val appealStatus: String?
    )
}
