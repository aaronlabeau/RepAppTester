package com.refapp.tester.services

import android.content.Context

class NetworkRouteFinderService(var context: Context) {
    fun getNetworkRoutes(): Array<String> {
        return getNetworkCommandResults("ip route show")
    }

    fun getNetworkRouteDetails() : Array<String> {
        return getNetworkCommandResults("ip addr show")
    }
    private fun getNetworkCommandResults(command: String) : Array<String>{
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