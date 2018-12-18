package com.jonasdevrient.citypinboard

import android.arch.lifecycle.Observer
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
import com.jonasdevrient.citypinboard.utils.ConnectionLiveData


class MainActivity : AppCompatActivity(), NavigationHost {

    private var snackbar: Snackbar? = null
    lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        loginFragment = LoginFragment()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container, loginFragment)
                    .commit()
        }

        checkInternetConnection()
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
            // user is still on login or register screen
            if (!supportFragmentManager.fragments[0]::class.isInstance(PinboardListFragment())) {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, GeenInternetFragment())
                        .commit()
            }

            val viewGroup = (this
                    .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

            val messageToUser = getString(R.string.you_are_offline)

            snackbar = Snackbar.make(viewGroup, messageToUser, Snackbar.LENGTH_LONG)
            snackbar?.duration = Snackbar.LENGTH_INDEFINITE
            snackbar?.show()

        } else {
            // online remove snackbar and start back to login screen
            snackbar?.dismiss()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, LoginFragment())
                    .commit()
        }

    }


}
