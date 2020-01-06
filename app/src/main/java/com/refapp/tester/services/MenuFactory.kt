package com.refapp.tester.services

import android.content.res.Resources
import com.refapp.tester.R
import com.refapp.tester.models.ListMenuItem
import com.refapp.tester.models.MenuType


class MenuFactory {
    fun getMenuItems() : List<ListMenuItem>{
        val list = ArrayList<ListMenuItem>()
        val system = Resources.getSystem()

        list.add(ListMenuItem(
            system.getString(R.string.azureTokenGeneratorTitle),
            system.getString(R.string.azureTokenGeneratorDetails),
            0,
            true,
            MenuType.TokenGenerator,
            R.drawable.token_720_480)
        )

        list.add(ListMenuItem(
            system.getString(R.string.azureTokenCacheTitle),
            system.getString(R.string.azureTokenCacheDetails),
            1,
            true,
            MenuType.TokenCache,
            R.drawable.tokencache_720_480)
        )

        list.add(ListMenuItem(
            system.getString(R.string.routedetailTitle),
            system.getString(R.string.routedetailDetails),
            2,
            true,
            MenuType.RouteDetails,
            R.drawable.route_details_720)
        )

        list.add(ListMenuItem(
            system.getString(R.string.networkToolsTitle),
            system.getString(R.string.networkToolsDetails),
            3,
            true,
            MenuType.NetworkTools,
            R.drawable.network_tools_720_480)
        )

        list.add(ListMenuItem(
            system.getString(R.string.endpointSelectorTitle),
            system.getString(R.string.endpointSelectorDetails),
            6,
            true,
            MenuType.EndpointSelector,
            R.drawable.endpoint_selection_720_480)
        )
        return list.toList()
    }
}