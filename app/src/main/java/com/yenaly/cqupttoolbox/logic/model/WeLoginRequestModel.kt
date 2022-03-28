package com.yenaly.cqupttoolbox.logic.model

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/25 025 21:17
 * @Description : Description...
 */
data class WeLoginRequestModel(
    val username: String,
    val password: String,
    val openId: String
)