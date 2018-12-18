package com.jonasdevrient.citypinboard.pinboards

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.adapters.PostsAdapter
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.pinboards.posts.AddPostFragment
import com.jonasdevrient.citypinboard.utils.ConnectionLiveData
import kotlinx.android.synthetic.main.activity_pinboard_details.*


class PinboardDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var pinboard: Pinboard
    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pinboard_details)

        checkInternetConnection()

        val gson = Gson()
        val jsonPinboard = intent.getStringExtra(getString(R.string.key_pinboard))
        pinboard = gson.fromJson(jsonPinboard, Pinboard::class.java)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PostsAdapter(this, pinboard.posts!!)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_posts).apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }


        // show b
        floatingActionButton.setOnClickListener {
            val bottomSheet = AddPostFragment()
            bottomSheet.show(supportFragmentManager, "bottomSheet")
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val parentIntent = NavUtils.getParentActivityIntent(this)
                parentIntent!!.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(parentIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkInternetConnection() {
        val connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this, Observer { isConnected ->
            if (isConnected != null) {
                showSnackBar(isConnected)
            }
        })
    }

    private fun showSnackBar(isConnected: Boolean) {
        // no internet
        if (!isConnected) {
            floatingActionButton.hide()

            val viewGroup = (this
                    .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

            val messageToUser = getString(R.string.you_are_offline)

            snackbar = Snackbar.make(viewGroup, messageToUser, Snackbar.LENGTH_LONG)
            snackbar?.duration = Snackbar.LENGTH_INDEFINITE
            snackbar?.show()

        } else {
            floatingActionButton.show()
            // online remove snackbar and start back to login screen
            snackbar?.dismiss()
        }

    }
}
