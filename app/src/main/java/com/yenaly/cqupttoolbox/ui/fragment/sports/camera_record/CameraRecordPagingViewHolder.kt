package com.yenaly.cqupttoolbox.ui.fragment.sports.camera_record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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
 * @Time : 2022/04/01 001 17:12
 * @Description : Description...
 */
class CameraRecordPagingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val faceRealtime: ImageView = view.findViewById(R.id.face_realtime)
    private val faceSimilarity: TextView = view.findViewById(R.id.face_similarity)
    private val placeName: TextView = view.findViewById(R.id.place_name)
    private val cameraChannelName: TextView = view.findViewById(R.id.camera_channel_name)
    private val recordTime: TextView = view.findViewById(R.id.record_time)
    private val remark: TextView = view.findViewById(R.id.remark)

    fun bind(cameraRecord: SportsCameraRecordModel.Rows?, fragment: Fragment) {
        val faceSimilarityText = "${cameraRecord?.matchScore}%"
        faceSimilarity.text = faceSimilarityText
        cameraChannelName.text = cameraRecord?.cameraChannelName
        placeName.text = cameraRecord?.placeName
        recordTime.text = cameraRecord?.recordTime
        if (cameraRecord?.remark != null) {
            val remarkText = "(${cameraRecord.remark})"
            remark.text = remarkText
            remark.visibility = View.VISIBLE
        } else remark.visibility = View.GONE
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
        Glide.with(fragment).load(addCookieImageUrl).into(faceRealtime)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            fragment: Fragment,
            getCameraRecord: (position: Int) -> SportsCameraRecordModel.Rows
        ): CameraRecordPagingViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_sports_camera_record, parent, false)
            val holder = CameraRecordPagingViewHolder(view)
            val imgEntryView =
                LayoutInflater.from(parent.context).inflate(R.layout.dialog_photo, parent, false)
            val faceRealtimeBig: ImageView = imgEntryView.findViewById(R.id.face_realtime_big)
            val dialog = AlertDialog.Builder(fragment.requireContext()).create()
            imgEntryView.setOnClickListener { dialog.cancel() }
            holder.faceRealtime.setOnClickListener {
                val position = holder.bindingAdapterPosition
                val cameraRecord = getCameraRecord(position)
                val imageUrl = SportsNetwork.SMART_SPORTS_SCHEME +
                        SportsNetwork.SMART_SPORTS_BASE +
                        cameraRecord.imgUrl
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
    }
}