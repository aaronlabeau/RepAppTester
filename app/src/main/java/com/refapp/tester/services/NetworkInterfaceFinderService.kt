package com.refapp.tester.services

import com.refapp.tester.models.NetInfo
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.collections.ArrayList

class NetworkInterfaceFinderService() {

    fun getNetworkConnections() : List<NetInfo> {
        val results = ArrayList<NetInfo>()
        var previousInterface: NetworkInterface? = null
        val ipStringBuilder = StringBuilder()

        for(i in NetworkInterface.getNetworkInterfaces().toList()) {
            if (!i.isLoopback) {
                if (previousInterface != null) {
                    if (i.displayName == previousInterface.displayName) {
                        val address = getIPAddress(i.inetAddresses)
                        if (address != null && address.isNotEmpty()){
                            ipStringBuilder.append(address)
                        }
                    } else {
                        //we found a new network dump the previous one into the list
                        if (ipStringBuilder.isEmpty()){
                            val address = getIPAddress(previousInterface.inetAddresses)
                            if (address != null && address.isNotEmpty()) {
                                ipStringBuilder.append(getIPAddress(previousInterface.inetAddresses))
                            }
                        }
                        if (ipStringBuilder.isNotEmpty()) {
                            val item = NetInfo(
                                previousInterface.name,
                                previousInterface.displayName,
                                ipStringBuilder.toString(),
                                previousInterface.isUp,
                                previousInterface.isPointToPoint,
                                previousInterface.isVirtual
                            )
                            results.add(item)
                            //clean up so we can track the next one
                            ipStringBuilder.clear()
                            previousInterface = i
                        }
                        else {
                            previousInterface = i
                        }
                    }
                } else {
                    previousInterface = i
                }
            }
        }
        //dump the last item
        if (previousInterface != null) {
            val address = getIPAddress(previousInterface.inetAddresses)
            if (address != null && address.isNotEmpty()) {
                ipStringBuilder.append(getIPAddress(previousInterface.inetAddresses))
            }
            var finalItem = NetInfo(
                previousInterface.name,
                previousInterface.displayName,
                ipStringBuilder.toString(),
                previousInterface.isUp,
                previousInterface.isPointToPoint,
                previousInterface.isVirtual
            )
            results.add(finalItem)
            ipStringBuilder.clear()
        }
        return results.toList()
    }

    fun getIPAddress(ipAddresses :Enumeration<InetAddress>) : String {
        val sb = StringBuilder()
        for(segment in ipAddresses)  {
            if (!segment.isLoopbackAddress && !segment.isMulticastAddress) {
               sb.append("${segment.hostAddress} \n")
            }
        }
        return sb.toString()
    }
}