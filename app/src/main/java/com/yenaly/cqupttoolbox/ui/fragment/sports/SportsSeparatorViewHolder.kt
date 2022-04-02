package com.yenaly.cqupttoolbox.ui.fragment.sports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/04/01 001 16:47
 * @Description : Description...
 */
class SportsSeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val description: TextView = view.findViewById(R.id.separator_description)

    fun bind(separatorText: String) {
        description.text = separatorText
    }

    companion object {
        fun create(parent: ViewGroup): SportsSeparatorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_camera_separator_item, parent, false)
            return SportsSeparatorViewHolder(view)
        }
    }
}