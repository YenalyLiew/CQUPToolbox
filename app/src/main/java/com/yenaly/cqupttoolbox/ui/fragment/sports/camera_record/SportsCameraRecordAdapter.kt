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
 * An adapter for sports camera record recycler view.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/22 022 22:13
 * @Description : Description...
 */
class SportsCameraRecordAdapter(
    private val fragment: Fragment,
    private val sportsRecordList: List<SportsCameraRecordModel.Rows>
) : RecyclerView.Adapter<SportsCameraRecordAdapter.ViewHolder>() {

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
            val imageUrl = SportsNetwork.SMART_SPORTS_SCHEME +
                    SportsNetwork.SMART_SPORTS_BASE +
                    sportsRecordList[position].imgUrl
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
        val faceSimilarityText = "${sportsRecordList[position].matchScore}%"
        holder.faceSimilarity.text = faceSimilarityText
        holder.cameraChannelName.text = sportsRecordList[position].cameraChannelName
        holder.placeName.text = sportsRecordList[position].placeName
        holder.recordTime.text = sportsRecordList[position].recordTime
        if (sportsRecordList[position].remark != null) {
            val remarkText = "(${sportsRecordList[position].remark})"
            holder.remark.text = remarkText
            holder.remark.visibility = View.VISIBLE
        } else holder.remark.visibility = View.GONE
        val imageUrl = SportsNetwork.SMART_SPORTS_SCHEME +
                SportsNetwork.SMART_SPORTS_BASE +
                sportsRecordList[position].imgUrl
        val addCookieImageUrl = GlideUrl(
            imageUrl,
            LazyHeaders.Builder().addHeader(
                "Cookie",
                Cookies.smartSportsCookiesList[0].toString().substringBefore(';')
            ).build()
        )
        Glide.with(fragment).load(addCookieImageUrl).into(holder.faceRealtime)
    }

    override fun getItemCount(): Int {
        return sportsRecordList.size
    }
}