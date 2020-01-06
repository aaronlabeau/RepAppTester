package com.refapp.tester.models

data class ListMenuItem(
    val headline: String,
    val subHeadline: String,
    val orderBy: Int,
    val isEnabled: Boolean,
    val menuType: MenuType,
    val imageLocation: Int
)

enum class MenuType {
    EndpointSelector,
    TokenGenerator,
    TokenCache,
    InTuneDetails,
    InTuneFileEncryption,
    RouteDetails,
    NetworkTools
}

