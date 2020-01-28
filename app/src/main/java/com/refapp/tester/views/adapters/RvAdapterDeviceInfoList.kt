package com.refapp.tester.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eygsl.ctmob.speedup.R
import com.refapp.tester.models.DeviceInfo
import com.refapp.tester.services.DeviceInfoFactory

class RvAdapterDeviceInfoList(private val items: List<DeviceInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_info_cardview, parent, false)
        return DeviceInfoListViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val h : DeviceInfoListViewHolder = holder as DeviceInfoListViewHolder
        h.name.text = item.title
        h.description.text = item.description
        h.value.text = item.settingValue
    }

    inner class DeviceInfoListViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.dicvLabelName)
        val description : TextView = itemView.findViewById(R.id.dicvDescription)
        val value: TextView = itemView.findViewById(R.id.dicvLabelValue)
    }
}