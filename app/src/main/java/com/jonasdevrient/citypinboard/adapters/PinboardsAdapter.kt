package com.jonasdevrient.citypinboard.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jonasdevrient.citypinboard.PinboardDetailsActivity
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.models.Pinboard
import kotlinx.android.synthetic.main.pinboard_item.view.*

class PinboardsAdapter(val context: Context?, val pinboards: List<Pinboard>) : RecyclerView.Adapter<PinboardsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pinboard_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pinboards.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pinboard = pinboards[position]
        holder.setData(pinboard, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentPinboard: Pinboard? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, currentPinboard!!.city + "Clicked", Toast.LENGTH_SHORT).show()
                // open details page
                val intent = PinboardDetailsActivity(currentPinboard!!)
                intent
            }
        }


        fun setData(pinboard: Pinboard?, position: Int) {
            itemView.city_text.text = pinboard!!.city
            itemView.amount_of_posts.text = pinboard.amountOfPosts.toString()
            this.currentPinboard = pinboard
            this.currentPosition = position
        }

    }

}