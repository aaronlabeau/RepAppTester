package com.refapp.tester.services

import android.content.Context
import com.refapp.tester.R
import com.refapp.tester.models.ListMenuItem
import com.refapp.tester.models.MenuType


class MenuFactory (val context: Context) {
    fun getMenuItems() : List<ListMenuItem>{
        val list = ArrayList<ListMenuItem>()

        list.add(ListMenuItem(
            context.resources.getString(R.string.azureTokenGeneratorTitle),
            context.resources.getString(R.string.azureTokenGeneratorDetails),
            0,
            true,
            MenuType.TokenGenerator,
            R.drawable.token_720_480)
        )

        list.add(ListMenuItem(
            context.resources.getString(R.string.azureTokenCacheTitle),
            context.resources.getString(R.string.azureTokenCacheDetails),
            1,
            true,
            MenuType.TokenCache,
            R.drawable.tokencache_720_480)
        )

        list.add(ListMenuItem(
            context.resources.getString(R.string.routedetailTitle),
            context.resources.getString(R.string.routedetailDetails),
            2,
            true,
            MenuType.RouteDetails,
            R.drawable.route_details_720)
        )

        list.add(ListMenuItem(
            context.resources.getString(R.string.networkToolsTitle),
            context.resources.getString(R.string.networkToolsDetails),
            3,
            true,
            MenuType.NetworkTools,
            R.drawable.network_tools_720_480)
        )

        list.add(ListMenuItem(
            context.resources.getString(R.string.endpointSelectorTitle),
            context.resources.getString(R.string.endpointSelectorDetails),
            6,
            true,
            MenuType.EndpointSelector,
            R.drawable.endpoint_selection_720_480)
        )
        return list.toList()
    }
}