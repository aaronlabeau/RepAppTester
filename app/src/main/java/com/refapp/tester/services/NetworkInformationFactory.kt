package com.refapp.tester.services

import android.content.Context
import com.refapp.tester.models.NetworkInformation

class NetworkInformationFactory(var context: Context) {

    fun getNetworkInformation() : NetworkInformation {
        val networkInterfaces = NetworkInterfaceFinderService()
        val dnsConfig =  NetworkDNSConfigFinderService(context)
        val routeConfig = NetworkRouteFinderService(context)

        val sbDNSServers = StringBuilder()
        for (s in dnsConfig.getServers())
            sbDNSServers.append("$s\n")

        val sbRoutes = StringBuilder()
        for (s in routeConfig.getNetworkRoutes())
            sbRoutes.append("$s\n")

        val sbRouteDetails = StringBuilder()
        for (s in routeConfig.getNetworkRouteDetails())
            sbRouteDetails.append("$s\n")

        return NetworkInformation(
            sbDNSServers.toString(),
            sbRoutes.toString(),
            sbRouteDetails.toString(),
            networkInterfaces.getNetworkConnections())
    }
}