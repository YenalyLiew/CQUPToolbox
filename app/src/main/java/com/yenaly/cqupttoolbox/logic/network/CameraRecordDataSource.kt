package com.yenaly.cqupttoolbox.logic.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.yenaly.cqupttoolbox.logic.model.SportsCameraRecordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.ceil

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/31 031 08:07
 * @Description : Description...
 */
class CameraRecordDataSource(
    private val yearTerm: String,
    private val getPages: (Int) -> Unit
) : PagingSource<Int, SportsCameraRecordModel.Rows>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SportsCameraRecordModel.Rows> {
        return try {
            val loadPage = params.key ?: 1
            val reqData = SportsNetwork.getSportsCameraRecord(yearTerm, loadPage)
            val stringResult = withContext(Dispatchers.IO) {
                runCatching {
                    reqData.body!!.string()
                }
            }
            val string = stringResult.getOrNull()
            if (stringResult.isSuccess) {
                val information = Gson().fromJson(string, SportsCameraRecordModel::class.java)
                if (information != null && information.code == "0") {
                    val keys = ceil(information.total / 10.0).toInt()
                    getPages(keys)
                    val prevKey = if (loadPage > 1) loadPage - 1 else null
                    val nextKey = if (loadPage < keys) loadPage + 1 else null
                    LoadResult.Page(information.rows, prevKey, nextKey)
                } else LoadResult.Error(RuntimeException("sport check info status is ${information.code}"))
            } else {
                LoadResult.Error(RuntimeException("sport check info status is error."))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SportsCameraRecordModel.Rows>): Int? {
        return null
    }
}