package com.jonasdevrient.citypinboard

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jonasdevrient.citypinboard.models.Gebruiker
import com.jonasdevrient.citypinboard.repositories.GebruikerAPI
import com.jonasdevrient.citypinboard.responses.RegistreerResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import put
import retrofit2.HttpException


class LoginFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)




        view.registreer_button.setOnClickListener {
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            val registreerFragment = RegistreerFragment()

            fragmentTransaction.hide(this)
            fragmentTransaction.add(R.id.container, registreerFragment)
            fragmentTransaction.commit()
        }

        view.next_button.setOnClickListener {
            if (!isPasswordValid(password_edit_text.text)) password_text_input.error = getString(R.string.error_password) else password_text_input.error == null
            if (!isUsernameValid(username_edit_text.text)) username_text_input.error = getString(R.string.error_username) else username_text_input.error = null

            if (username_text_input.error == null && password_text_input.error == null) {

                val call = GebruikerAPI.repository.login(Gebruiker(username_edit_text.text.toString(), password_edit_text.text.toString()))
                call.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError)
            }
        }

        // Verwijder de error als er meer dan 4 karakters getypt worden
        view.username_edit_text.setOnKeyListener { _, _, _ ->
            if (isUsernameValid(username_edit_text.text)) {
                username_text_input.error = null
            }
            false
        }

        // Clear the error once more than 8 characters are typed
        view.password_edit_text.setOnKeyListener { _, _, _ ->
            if (isPasswordValid(password_edit_text.text)) {
                password_text_input.error = null //Clear the error
            }
            false
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    private fun isUsernameValid(text: Editable?): Boolean {
        return text != null && text.length >= 4
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }

    private fun handleResponse(registreerResponse: RegistreerResponse) {

        val token = registreerResponse.token

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.put(getString(R.string.sp_token_key), token)

        (activity as NavigationHost).navigateTo(PinboardListFragment(), false) // navigate to next fragment
    }

    private fun handleError(error: Throwable) {
        // Get error as HTTPException to get the exception code
        val httpError = error as HttpException
        when {
            httpError.code() == 401 -> password_text_input.error = getString(R.string.error_wrong_user_or_password)
            else -> Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }
}
