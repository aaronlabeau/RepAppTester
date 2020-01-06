package com.refapp.tester.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.refapp.tester.R
import com.refapp.tester.models.NetworkInformation

class RvAdapterNetworkInfoList(private val item: NetworkInformation) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dnsCard = item.netInterfaces.size
    private var routeCard = item.netInterfaces.size  +  1
    private var networkDetailsCard = item.netInterfaces.size + 2

    override fun getItemViewType(position: Int): Int {
        if (position < item.netInterfaces.size)
            return NET_INTERFACE_CARD_TYPE
        return NET_INFO_DETAIL_CARD_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        when (viewType) {
            NET_INFO_DETAIL_CARD_TYPE -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_detail_info_cardview, parent, false)
                return NetworkDetailInfoViewHolder(v)
            }
            else -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_info_cardview, parent, false)
                return NetworkInterfaceViewHolder(v)
            }
        }
    }

    override fun getItemCount(): Int = item.netInterfaces.size + 3

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < item.netInterfaces.size) {
            setNetworkViewHolder(holder, position)
        } else {
            setNetworkDetailInfoViewHolder(holder, position)
        }
    }

    private fun setNetworkDetailInfoViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h: NetworkDetailInfoViewHolder = holder as NetworkDetailInfoViewHolder
        when (position) {
            dnsCard -> {
                h.header.text = "DNS Server Info"
                h.information.text = item.dnsServers
            }
            routeCard -> {
                h.header.text = "IP Route Table"
                h.information.text = item.routes
            }
            networkDetailsCard -> {
                h.header.text = "Network Interface Details"
                h.information.text = item.routeDetails
            }
        }
    }

    private fun setNetworkViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h: NetworkInterfaceViewHolder = holder as NetworkInterfaceViewHolder
        h.displayName.text = item.netInterfaces[position].displayName
        h.ipAddress.text = item.netInterfaces[position].ipAddress
        h.isUp.text = item.netInterfaces[position].isUp.toString()
        h.isPointToPoint.text = item.netInterfaces[position].isPointToPoint.toString()
        h.isVirtual.text = item.netInterfaces[position].isVirtual.toString()
    }

    class NetworkDetailInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.ndicvLabel)
        val information: TextView = itemView.findViewById(R.id.ndicvInformation)
    }

    class NetworkInterfaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayName: TextView = itemView.findViewById(R.id.nicvDisplayName)
        val ipAddress: TextView = itemView.findViewById(R.id.nicvIPAddress)
        val isUp: TextView = itemView.findViewById(R.id.nicvIsUp)
        val isPointToPoint: TextView = itemView.findViewById(R.id.nicvIsPointToPoint)
        val isVirtual: TextView = itemView.findViewById(R.id.nicvIsVirtual)
    }

    //NO MAGIC INT
    companion object {
        //decide which UI to load in for cards
        const val NET_INFO_DETAIL_CARD_TYPE = 0
        const val NET_INTERFACE_CARD_TYPE = 1
    }
}