package com.refapp.tester.models

data class NetworkInformation(var dnsServers: String,
                              var routes: String,
                              var routeDetails: String,
                              var netInterfaces: List<NetInfo>) {
}