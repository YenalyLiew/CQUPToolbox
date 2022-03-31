package com.yenaly.cqupttoolbox.ui.fragment.sports.check_record

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.logic.model.SportsCheckRecordModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/30 030 19:33
 * @Description : Description...
 */
class CheckRecordPagingAdapter(
    private val fragment: Fragment
) : PagingDataAdapter<SportsCheckRecordModel.Rows, CheckRecordPagingAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SportsCheckRecordModel.Rows>() {
            override fun areItemsTheSame(
                oldItem: SportsCheckRecordModel.Rows,
                newItem: SportsCheckRecordModel.Rows
            ): Boolean {
                return oldItem.startTime == newItem.startTime
            }

            override fun areContentsTheSame(
                oldItem: SportsCheckRecordModel.Rows,
                newItem: SportsCheckRecordModel.Rows
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private val itemMap = mapOf("001" to "跑步", "002" to "其他")
    private val validMap = mapOf("Y" to true, "N" to false, "1" to true, "3" to false)
    private val appealStatusMap = mapOf(2 to true, 3 to false, null to true)

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.sports_check_date)
        val start: TextView = view.findViewById(R.id.sports_check_start)
        val end: TextView = view.findViewById(R.id.sports_check_end)
        val week: TextView = view.findViewById(R.id.sports_check_week)
        val totalTime: TextView = view.findViewById(R.id.sports_check_total_time)
        val mile: TextView = view.findViewById(R.id.sports_check_mile)
        val averageSpeed: TextView = view.findViewById(R.id.sports_check_average_speed)
        val item: TextView = view.findViewById(R.id.sports_check_item)
        val place: TextView = view.findViewById(R.id.sports_check_place)
        val valid: TextView = view.findViewById(R.id.sports_check_valid)
        val examOrExtra: TextView = view.findViewById(R.id.exam_or_extra)
        val lap: TextView = view.findViewById(R.id.sports_check_lap)
        val colorTint: TextView = view.findViewById(R.id.color_tint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sports_check_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkRecord = getItem(position)
        holder.date.text =
            checkRecord?.startTime?.substringBefore(' ')
        holder.start.text = fragment.getString(
            R.string.start_time,
            checkRecord?.startTime?.substringAfter(' ')
        )
        holder.end.text = fragment.getString(
            R.string.end_time,
            checkRecord?.endTime?.substringAfter(' ')
        )
        holder.week.text =
            fragment.getString(R.string.which_week, checkRecord?.weekly)
        holder.totalTime.text = fragment.getString(
            R.string.total_time,
            checkRecord?.sportDuration
        )
        holder.item.text = fragment.getString(
            R.string.sports_item,
            itemMap[checkRecord?.sportItem]
        )
        holder.place.text = checkRecord?.sportField
        if (checkRecord?.sportItem == "001") {
            holder.mile.text =
                fragment.getString(
                    R.string.total_mile,
                    checkRecord.mileage
                )
            holder.lap.text =
                fragment.getString(R.string.total_lap, checkRecord.runLapCnt)
            val runStartTime = simpleDateFormat.parse(checkRecord.runStartTime!!)
            val runEndTime = simpleDateFormat.parse(checkRecord.runEndTime!!)
            val runTimeInterval = (runEndTime!!.time - runStartTime!!.time) / 1000.0 / 60.0
            val averageSpeed =
                runTimeInterval / checkRecord.runLapCnt!!
            holder.averageSpeed.text =
                fragment.getString(R.string.average_speed, averageSpeed)
            holder.lap.visibility = View.VISIBLE
            holder.mile.visibility = View.VISIBLE
            holder.averageSpeed.visibility = View.VISIBLE
        } else {
            holder.lap.visibility = View.GONE
            holder.mile.visibility = View.GONE
            holder.averageSpeed.visibility = View.GONE
        }
        if (validMap[checkRecord?.isValid] == true) {
            holder.valid.text =
                fragment.getString(R.string.sports_is_valid)
            if (validMap[checkRecord?.examIsValid] == true) {
                holder.examOrExtra.text =
                    fragment.getString(R.string.exam_is_valid)
                holder.colorTint.setBackgroundColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.forest_green
                    )
                )
            } else {
                holder.examOrExtra.text =
                    fragment.getString(R.string.extra_is_valid)
                holder.colorTint.setBackgroundColor(
                    ContextCompat.getColor(
                        fragment.requireContext(),
                        R.color.blue
                    )
                )
            }
            holder.valid.setTextColor(Color.BLUE)
            holder.valid.paint.isFakeBoldText = false
            holder.examOrExtra.setTextColor(Color.BLUE)
            holder.examOrExtra.paint.isFakeBoldText = true
            holder.examOrExtra.visibility = View.VISIBLE
        } else {
            holder.valid.text =
                fragment.getString(R.string.sports_is_invalid)
            holder.colorTint.setBackgroundColor(
                ContextCompat.getColor(
                    fragment.requireContext(),
                    R.color.red
                )
            )
            holder.valid.setTextColor(Color.RED)
            holder.valid.paint.isFakeBoldText = true
            holder.examOrExtra.visibility = View.GONE
        }
    }
}