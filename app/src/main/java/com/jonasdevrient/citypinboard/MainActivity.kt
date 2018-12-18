package com.jonasdevrient.citypinboard

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.jonasdevrient.citypinboard.authentication.GeenInternetFragment
import com.jonasdevrient.citypinboard.authentication.LoginFragment
import com.jonasdevrient.citypinboard.pinboards.PinboardListFragment
import com.jonasdevrient.citypinboard.utils.ConnectivityReceiver


class MainActivity : AppCompatActivity(), NavigationHost, ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var connectivityReceiver: ConnectivityReceiver

    private var snackbar: Snackbar? = null
    lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        setContentView(R.layout.activity_main)

        loginFragment = LoginFragment()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container, loginFragment)
                    .commit()
        }
    }


    /**
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible.
     */
    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnackBar(isConnected)
    }

    override fun onDestroy() {
        unregisterReceiver(connectivityReceiver)
        super.onDestroy()
    }

    private fun showSnackBar(isConnected: Boolean) {
        if (!isConnected) {
            for (fragment in supportFragmentManager.fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }

            if (!supportFragmentManager.fragments[0]::class.isInstance(PinboardListFragment())) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, GeenInternetFragment()).commit()

                val viewGroup = (this
                        .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

                val messageToUser = "Je bent offline"

                snackbar = Snackbar.make(viewGroup, messageToUser, Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
                snackbar?.duration = Snackbar.LENGTH_INDEFINITE
                snackbar?.show()

            }

        } else {
            snackbar?.dismiss()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, LoginFragment())
                    .commit()
        }

    }


}
