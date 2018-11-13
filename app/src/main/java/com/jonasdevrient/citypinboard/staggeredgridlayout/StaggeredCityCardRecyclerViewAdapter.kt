package com.jonasdevrient.citypinboard.staggeredgridlayout


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.network.CityEntry
import com.squareup.picasso.Picasso

/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1
 * item in the second column, and so on.
 */
class StaggeredCityCardRecyclerViewAdapter(private val citiesList: List<CityEntry>?) : RecyclerView.Adapter<StaggeredCityCardViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return position % 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggeredCityCardViewHolder {
        var layoutId = R.layout.staggered_city_card_first
        if (viewType == 1) {
            layoutId = R.layout.staggered_city_card_second
        } else if (viewType == 2) {
            layoutId = R.layout.staggered_city_card_third
        }

        val layoutView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return StaggeredCityCardViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: StaggeredCityCardViewHolder, position: Int) {
        if (citiesList != null && position < citiesList.size) {
            val city = citiesList[position]
            holder.cityTitle.text = city.title
            holder.cityPrice.text = city.price
            Picasso.get().load(city.url).into(holder.cityImage)
        }
    }

    override fun getItemCount(): Int {
        return citiesList?.size ?: 0
    }
}
