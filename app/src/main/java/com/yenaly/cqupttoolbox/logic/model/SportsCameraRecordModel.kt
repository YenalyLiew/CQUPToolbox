package com.yenaly.cqupttoolbox.logic.model

/**
 * The model of sports camera record.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/22 022 22:43
 * @Description : Description...
 */
data class SportsCameraRecordModel(
    val code: String,
    val total: Int,
    val rows: List<Rows>
) {
    data class Rows(
        val cameraChannelName: String,
        val placeName: String,
        val recordTime: String,
        val matchScore: Double,
        val imgUrl: String,
        val remark: String?
    )
}