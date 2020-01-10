package com.refapp.tester.services

import android.content.Context
import androidx.annotation.StringRes
import com.refapp.tester.models.StringResult
import java.lang.StringBuilder

class NetworkToolsService(private var context: Context) {

    fun getNetworkPing(hostname: String): StringResult {
        val results = getNetworkCommandResults("ping -c 5 $hostname")
        val sb: StringBuilder = StringBuilder()
        val isError = results.isEmpty()
        for (s in results) {
            sb.append(s)
        }
        return when (isError) {
            true -> StringResult("", isError, "Error when trying to ping hostname $hostname")
            false -> StringResult(sb.toString(), isError, "")
        }
    }

    fun getNetworkTraceroute(search: String): StringResult {
        return StringResult("", true, "")
    }

    fun getNetworkRouteInfo(search: String): Array<String> {
        return getNetworkCommandResults("ip route get $search")
    }

    fun getNetworkRoutes(): Array<String> {
        return getNetworkCommandResults("ip route show")
    }

    fun getNetworkRouteDetails(): Array<String> {
        return getNetworkCommandResults("ip -o a")
    }

    private fun getNetworkCommandResults(command: String): Array<String> {
        val shellCommandService = ShellCommandService()
        val results = ArrayList<String>()
        val information = shellCommandService.getShellCommandResults(command)
        if (information.isNotEmpty()) {
            results.add(information)
        } else {
            results.add("No Information Found")
        }
        return results.toTypedArray()
    }
}