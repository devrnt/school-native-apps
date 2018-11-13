package com.jonasdevrient.citypinboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*


class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        view.next_button.setOnClickListener{
            if(!isPasswordValid(password_edit_text.text)){
                password_text_input.error = getString(R.string.error_password)
            } else {
                password_text_input.error = null // Clear the error
                (activity as NavigationHost).navigateTo(PinboardListFragment(), false) // navigate to next fragment
            }
        }

        // Clear the error once more than 8 characters are typed
        view.password_edit_text.setOnKeyListener {_, _, _ ->
            if (isPasswordValid(password_edit_text.text)) {
                password_text_input.error = null //Clear the error
            }
            false
        }
        return view
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }

}
