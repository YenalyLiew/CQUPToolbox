package com.yenaly.cqupttoolbox.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/04/02 002 17:46
 * @Description : Description...
 */
data class OutSchoolInfoModel(
    val status: String,
    val message: String,
    val data: Data
) {
    data class Data(

        @SerializedName("modules_list")
        val modulesList: List<ModulesList>
    ) {
        data class ModulesList(
            val id: String,
            val name: String
        )
    }
}