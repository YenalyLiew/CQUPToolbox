package com.yenaly.cqupttoolbox.logic.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.yenaly.cqupttoolbox.logic.model.SportsCheckRecordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.ceil

/**
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/16 016 21:45
 * @Description : Description...
 */
class CheckRecordDataSource(
    private val yearTerm: String,
    private val getPages: (Int) -> Unit
) : PagingSource<Int, SportsCheckRecordModel.Rows>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SportsCheckRecordModel.Rows> {
        return try {
            val loadPage = params.key ?: 1
            val reqData = SportsNetwork.getSportsCheckRecord(yearTerm, loadPage)
            val stringResult = withContext(Dispatchers.IO) {
                runCatching {
                    reqData.body!!.string()
                }
            }
            val string = stringResult.getOrNull()
            if (stringResult.isSuccess) {
                val information = Gson().fromJson(string, SportsCheckRecordModel::class.java)
                if (information != null && information.code == "0") {
                    val keys = ceil(information.total / 20.0).toInt()
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

    override fun getRefreshKey(state: PagingState<Int, SportsCheckRecordModel.Rows>): Int? {
        return null
    }
}