package com.refapp.tester.models

data class NetInfo(var name:String,
                   var displayName:String,
                   var ipAddress: String,
                   var isUp: Boolean,
                   var isPointToPoint: Boolean,
                   var isVirtual: Boolean)
