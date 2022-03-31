package com.yenaly.cqupttoolbox.ui.fragment.sports.camera_record

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.logic.model.SportsCameraRecordModel
import com.yenaly.cqupttoolbox.logic.network.Cookies
import com.yenaly.cqupttoolbox.logic.network.SportsNetwork

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/31 031 08:28
 * @Description : Description...
 */
class CameraRecordPagingAdapter(
    private val fragment: Fragment
) : PagingDataAdapter<SportsCameraRecordModel.Rows, CameraRecordPagingAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SportsCameraRecordModel.Rows>() {
            override fun areItemsTheSame(
                oldItem: SportsCameraRecordModel.Rows,
                newItem: SportsCameraRecordModel.Rows
            ): Boolean {
                return oldItem.recordTime == newItem.recordTime
            }

            override fun areContentsTheSame(
                oldItem: SportsCameraRecordModel.Rows,
                newItem: SportsCameraRecordModel.Rows
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var imgEntryView: View
    private lateinit var faceRealtimeBig: ImageView
    private lateinit var dialog: AlertDialog

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val faceRealtime: ImageView = view.findViewById(R.id.face_realtime)
        val faceSimilarity: TextView = view.findViewById(R.id.face_similarity)
        val placeName: TextView = view.findViewById(R.id.place_name)
        val cameraChannelName: TextView = view.findViewById(R.id.camera_channel_name)
        val recordTime: TextView = view.findViewById(R.id.record_time)
        val remark: TextView = view.findViewById(R.id.remark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sports_camera_record, parent, false)
        val holder = ViewHolder(view)
        imgEntryView =
            LayoutInflater.from(parent.context).inflate(R.layout.dialog_photo, parent, false)
        faceRealtimeBig = imgEntryView.findViewById(R.id.face_realtime_big)
        dialog = AlertDialog.Builder(fragment.requireContext()).create()
        imgEntryView.setOnClickListener { dialog.cancel() }
        holder.faceRealtime.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val cameraRecord = getItem(position)
            val imageUrl = SportsNetwork.SMART_SPORTS_SCHEME +
                    SportsNetwork.SMART_SPORTS_BASE +
                    cameraRecord!!.imgUrl
            val addCookieImageUrl = GlideUrl(
                imageUrl,
                LazyHeaders.Builder().addHeader(
                    "Cookie",
                    Cookies.smartSportsCookiesList[0].toString().substringBefore(';')
                ).build()
            )
            Glide.with(fragment).load(addCookieImageUrl).into(faceRealtimeBig)
            dialog.setView(imgEntryView)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cameraRecord = getItem(position)
        val faceSimilarityText = "${cameraRecord?.matchScore}%"
        holder.faceSimilarity.text = faceSimilarityText
        holder.cameraChannelName.text = cameraRecord?.cameraChannelName
        holder.placeName.text = cameraRecord?.placeName
        holder.recordTime.text = cameraRecord?.recordTime
        if (cameraRecord?.remark != null) {
            val remarkText = "(${cameraRecord.remark})"
            holder.remark.text = remarkText
            holder.remark.visibility = View.VISIBLE
        } else holder.remark.visibility = View.GONE
        val imageUrl = SportsNetwork.SMART_SPORTS_SCHEME +
                SportsNetwork.SMART_SPORTS_BASE +
                cameraRecord!!.imgUrl
        val addCookieImageUrl = GlideUrl(
            imageUrl,
            LazyHeaders.Builder().addHeader(
                "Cookie",
                Cookies.smartSportsCookiesList[0].toString().substringBefore(';')
            ).build()
        )
        Glide.with(fragment).load(addCookieImageUrl).into(holder.faceRealtime)
    }
}