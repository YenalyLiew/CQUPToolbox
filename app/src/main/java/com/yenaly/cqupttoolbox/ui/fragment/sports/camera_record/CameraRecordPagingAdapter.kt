package com.yenaly.cqupttoolbox.ui.fragment.sports.camera_record

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.logic.model.SportsCameraPagingModel
import com.yenaly.cqupttoolbox.ui.fragment.sports.SportsSeparatorViewHolder

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/31 031 08:28
 * @Description : Description...
 */
class CameraRecordPagingAdapter(
    private val fragment: Fragment
) : PagingDataAdapter<SportsCameraPagingModel, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SportsCameraPagingModel>() {
            override fun areItemsTheSame(
                oldItem: SportsCameraPagingModel,
                newItem: SportsCameraPagingModel
            ): Boolean {
                return (
                        oldItem is SportsCameraPagingModel.InfoItem &&
                                newItem is SportsCameraPagingModel.InfoItem &&
                                oldItem.item.recordTime == newItem.item.recordTime
                        ) || (
                        oldItem is SportsCameraPagingModel.SeparatorItem &&
                                newItem is SportsCameraPagingModel.SeparatorItem &&
                                oldItem.description == newItem.description
                        )
            }

            override fun areContentsTheSame(
                oldItem: SportsCameraPagingModel,
                newItem: SportsCameraPagingModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SportsCameraPagingModel.InfoItem -> R.layout.item_sports_camera_record
            is SportsCameraPagingModel.SeparatorItem -> R.layout.item_camera_separator_item
            null -> throw UnsupportedOperationException("Unknown view.")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_camera_separator_item -> {
                SportsSeparatorViewHolder.create(parent)
            }
            else -> {
                CameraRecordPagingViewHolder.create(parent, fragment) { position ->
                    val cameraRecordItem = getItem(position) as SportsCameraPagingModel.InfoItem
                    cameraRecordItem.item
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sportsCameraPagingModel = getItem(position)
        sportsCameraPagingModel.let {
            when (sportsCameraPagingModel) {
                is SportsCameraPagingModel.InfoItem -> {
                    (holder as CameraRecordPagingViewHolder).bind(
                        sportsCameraPagingModel.item,
                        fragment
                    )
                }
                is SportsCameraPagingModel.SeparatorItem -> {
                    (holder as SportsSeparatorViewHolder).bind(
                        sportsCameraPagingModel.description
                    )
                }
                null -> throw UnsupportedOperationException("Unknown view.")
            }
        }
    }
}