package com.yenaly.cqupttoolbox.logic.model

/**
 * The model of the POST form of punching from WeCQUPT.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/21 021 18:15
 * @Description : Description...
 */
data class WePunchModel(
    val username: String,
    val password: String,
    val openId: String,
    val locationBig: String,
    val locationSmall: String,
    val latitude: Double,
    val longitude: Double,
    val currentLocation: String,
    val currentDetailedLocation: String
)
