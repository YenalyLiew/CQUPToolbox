package com.yenaly.cqupttoolbox.logic.model

import com.google.gson.annotations.SerializedName

/**
 * The model of the POST form of login from WeCQUPT.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/21 021 09:36
 * @Description : Description...
 */
data class WeLoginResponseModel(
    val message: String,
    val data: Data
) {
    data class Data(
        val name: String,

        @SerializedName("xh")
        val id: String,

        @SerializedName("sex")
        val gender: String,

        @SerializedName("yxm")
        val college: String
    )
}
