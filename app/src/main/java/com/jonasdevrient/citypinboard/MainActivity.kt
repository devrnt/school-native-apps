package com.jonasdevrient.citypinboard

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLaunchApp.setOnClickListener {
            val intent = Intent(this, PinboardsActivity::class.java)
            startActivity(intent)
        }
    }
}
