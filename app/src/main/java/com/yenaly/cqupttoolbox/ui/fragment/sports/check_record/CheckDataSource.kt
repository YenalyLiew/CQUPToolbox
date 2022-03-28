package com.yenaly.cqupttoolbox.ui.fragment.sports.check_record

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yenaly.cqupttoolbox.logic.Repository
import com.yenaly.cqupttoolbox.logic.model.SportsCheckRecordModel
import kotlin.math.ceil

/**
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/16 016 21:45
 * @Description : Description...
 */
class CheckDataSource(private val yearTerm: String) :
    PagingSource<Int, SportsCheckRecordModel.Rows>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SportsCheckRecordModel.Rows> {
        return try {
            val currentPage = params.key ?: 1
            val reqData = Repository().getSportsCheckRecord(yearTerm, currentPage)
            val nextPage =
                if (currentPage < ceil(reqData.value?.getOrNull()?.total!! / 20.0).toInt()) currentPage + 1 else null
            if (reqData.value?.getOrNull() != null) {
                LoadResult.Page(reqData.value?.getOrNull()?.rows!!, null, nextPage)
            } else LoadResult.Error(Throwable())
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SportsCheckRecordModel.Rows>): Int? {
        return 0
    }
}