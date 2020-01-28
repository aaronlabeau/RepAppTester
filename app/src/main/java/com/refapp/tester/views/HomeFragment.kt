package com.refapp.tester.views


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eygsl.ctmob.speedup.R
import com.refapp.tester.models.MenuType
import com.refapp.tester.services.MenuFactory
import com.refapp.tester.views.adapters.RvAdapterHomeList

class HomeFragment : Fragment() {
    private var factory : MenuFactory? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        factory = MenuFactory(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        val activity = activity as Context
        val rv = view.findViewById<RecyclerView>(R.id.homeRecycleView)
        rv.layoutManager = LinearLayoutManager(activity)
        val f = factory as MenuFactory
        val adapter = RvAdapterHomeList(f.getMenuItems())

        //handle on click of a row/cell in the RecyclerView
        adapter.onItemClick = { item ->
            val controller = this.findNavController()
            when (item.menuType)     {
                MenuType.RouteDetails -> {
                    controller.navigate(R.id.action_homeFragment_to_RouteDetailsFragment)
                }
                MenuType.NetworkTools -> {
                    controller.navigate(R.id.action_homeFragment_to_NetworkToolsFragment)
                }
                else -> {
                   Log.d("HomeFragment", "Item selected not implemented")
                }
            }
        }

        rv.adapter = adapter

        return view
    }

}
