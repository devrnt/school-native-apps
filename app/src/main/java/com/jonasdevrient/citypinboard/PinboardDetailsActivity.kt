package com.jonasdevrient.citypinboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import com.jonasdevrient.citypinboard.adapters.PinboardsAdapter
import com.jonasdevrient.citypinboard.adapters.PostsAdapter
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.repositories.PinboardAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PinboardDetailsActivity() : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var pinboard: Pinboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinboard_details)

        val gson = Gson()
        val jsonPinboard = intent.getStringExtra(getString(R.string.key_pinboard))
        pinboard = gson.fromJson(jsonPinboard, Pinboard::class.java)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PostsAdapter(this, pinboard.posts)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_posts).apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }
    }

}
