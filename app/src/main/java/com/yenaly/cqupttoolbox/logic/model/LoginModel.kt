package com.yenaly.cqupttoolbox.logic.model

/**
 * A data class for Jwzx login's livedata.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/16 016 16:25
 * @Description : Description...
 */
data class LoginModel(
    val username: String,
    val password: String,
    val captcha: String
)
