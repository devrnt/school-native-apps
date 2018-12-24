package com.jonasdevrient.citypinboard.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.models.StadFoto
import com.jonasdevrient.citypinboard.pinboards.PinboardDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pinboard_item.view.*

/**
 * Recycleview adapter used to the display the [pinboards]
 */
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
                val gson = Gson()
                val jsonPinboard = gson.toJson(currentPinboard)

                val intent = Intent(context, PinboardDetailsActivity::class.java)
                intent.putExtra(context!!.resources.getString(R.string.key_pinboard), jsonPinboard)
                context.startActivity(intent)
            }
        }


        fun setData(pinboard: Pinboard?, position: Int) {
            itemView.city_text.text = pinboard!!.city
            itemView.amount_of_posts.text = context!!.getString(R.string.pinboard_card_amount_of_posts, pinboard.posts!!.count())
            Picasso.get().load(StadFoto.image(pinboard.city.toLowerCase().trim())).fit().centerCrop().placeholder(R.drawable.ic_favorite_24dp).error(R.drawable.ic_icon).into(itemView.city_image)

            this.currentPinboard = pinboard
            this.currentPosition = position
        }

    }

}