package com.jonasdevrient.citypinboard.staggeredgridlayout

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jonasdevrient.citypinboard.R

class StaggeredCityCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cityImage: ImageView = itemView.findViewById(R.id.city_image)
    var cityTitle: TextView = itemView.findViewById(R.id.city_title)
    var cityPrice: TextView = itemView.findViewById(R.id.city_price)

}