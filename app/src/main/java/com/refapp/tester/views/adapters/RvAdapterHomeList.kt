package com.refapp.tester.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.refapp.tester.models.ListMenuItem
import com.refapp.tester.R
import com.refapp.tester.models.MenuType

class RvAdapterHomeList(private val items: List<ListMenuItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var onItemClick: ((ListMenuItem) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.home_menu_item_cardview, parent, false)
                return HomeListViewHolder(v)
        }
        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = items[position]
                val h: HomeListViewHolder = holder as HomeListViewHolder
                h.title.text = item.headline
                h.description.text = item.subHeadline
                h.image.setImageResource(item.imageLocation)
        }

        inner class HomeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val image: ImageView = itemView.findViewById(R.id.hmicImageViewHero)
                val title: TextView = itemView.findViewById(R.id.hmicTextViewTitle)
                val description: TextView = itemView.findViewById(R.id.hmicTextViewDescription)
            init {
                    itemView.setOnClickListener {
                            onItemClick?.invoke(items[adapterPosition])
                    }
            }

        }
}