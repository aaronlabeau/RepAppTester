package com.refapp.tester.services

import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import com.refapp.tester.models.NetworkInformation
import com.refapp.tester.models.StringResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkInformationFactory(var context: Context) {
    private val ipRegEx: String =
        "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$"
    private val regEx: Regex = Regex(ipRegEx)

    private val dnsConfig = NetworkDNSService(context)
    private val routeConfig = NetworkRouteFinderService(context)

    fun getNslookupResult(hostname: String): StringResult {
        return dnsConfig.getNslookupResult(hostname)
    }

    fun getNetworkRouteInfo(hostname: String): StringResult {
        var strResult: StringResult
        val sb: StringBuilder = StringBuilder()
        var search: String = hostname
        val isMatch = regEx.matches(hostname)
        var isError = false
        var errorMessage = ""
        if (!isMatch) {
            val searchResult = getNslookupResult(hostname)
            if (searchResult.isError) {
                errorMessage = searchResult.debugMessage
                search = ""
                isError = true
            } else {
                search = searchResult.result
                sb.append("Resolved $hostname to $search \n\n")
            }
        }
        strResult = if (!isError) {
            val results = routeConfig.getNetworkRouteInfo(search)
            sb.append("Results for accessing resource $search are below:\n\n")
            for (s in results) {
                sb.append(s)
            }
            StringResult(sb.toString(), isError, "")
        } else {
            StringResult(
                search,
                isError,
                errorMessage)
        }
        return strResult
    }

    fun getNetworkInformation(): NetworkInformation {
        val networkInterfaces = NetworkInterfaceFinderService()

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
            networkInterfaces.getNetworkConnections()
        )
    }
}