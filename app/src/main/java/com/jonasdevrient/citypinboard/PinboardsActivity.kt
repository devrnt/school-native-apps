package com.jonasdevrient.citypinboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jonasdevrient.citypinboard.adapters.PinboardsAdapter
import com.jonasdevrient.citypinboard.models.Supplier
import kotlinx.android.synthetic.main.activity_pinboards.*

class PinboardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinboards)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleView.layoutManager = layoutManager

        val adapter = PinboardsAdapter(this, Supplier.pinboards)
        recycleView.adapter = adapter
    }
}
