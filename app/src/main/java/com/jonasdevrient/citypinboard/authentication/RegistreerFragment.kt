package com.jonasdevrient.citypinboard.authentication

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.jonasdevrient.citypinboard.NavigationHost
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.models.Gebruiker
import com.jonasdevrient.citypinboard.pinboards.PinboardListFragment
import com.jonasdevrient.citypinboard.responses.CheckGebruikersnaamResponse
import com.jonasdevrient.citypinboard.responses.PostResponse
import com.jonasdevrient.citypinboard.responses.RegistreerResponse
import com.jonasdevrient.citypinboard.services.GebruikerService
import get
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.registreer_fragment.*
import kotlinx.android.synthetic.main.registreer_fragment.view.*
import put


class RegistreerFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gebruiker: Gebruiker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.registreer_fragment, container, false)

        view.back_button.setOnClickListener {
            (activity as NavigationHost).navigateTo(LoginFragment(), false) // navigate to next fragment
        }


        // Verwijder de error als er meer dan 4 karakters getypt worden
        view.username_edit_text_reg.setOnKeyListener { _, _, _ ->
            if (isUsernameValid(username_edit_text_reg.text)) {
                username_text_input_reg.error = null
            }
            false
        }

        // Verwijder de error als er meer dan 8 karakters getypt worden
        view.password_edit_text_reg.setOnKeyListener { _, _, _ ->
            if (isPasswordValid(password_edit_text_reg.text)) {
                password_text_input_reg.error = null
            }
            false
        }

        // Verwijder de error als er meer dan 8 karakters getypt worden
        view.confirm_password_edit_text_reg.setOnKeyListener { _, _, _ ->
            if (isConfirmPasswordValid(confirm_password_edit_text_reg.text, password_edit_text_reg.text)) {
                confirm_password_text_input_reg.error = null
            }
            false
        }

        view.confirm_registreer_button.setOnClickListener {
            if (!isUsernameValid(username_edit_text_reg.text)) username_text_input_reg.error = getString(R.string.error_username) else username_text_input_reg.error = null
            if (!isPasswordValid(password_edit_text_reg.text)) password_text_input_reg.error = getString(R.string.error_password) else password_text_input_reg.error = null
            if (!isConfirmPasswordValid(confirm_password_edit_text_reg.text, password_edit_text_reg.text)) confirm_password_text_input_reg.error = getString(R.string.error_confirm_password) else confirm_password_text_input_reg.error = null

            if (username_text_input_reg.error == null && password_text_input_reg.error == null && confirm_password_text_input_reg.error == null) {
                attemptRegistreer(username_edit_text_reg.text!!, password_edit_text_reg.text!!)
            }
        }

        return view
    }

    private fun attemptRegistreer(gebruikersNaam: Editable, wachtwoord: Editable) {
        gebruiker = Gebruiker(gebruikersNaam.toString(), wachtwoord.toString())
        val callGebruikersnaam = GebruikerService.repository.checkGebruikersnaam(CheckGebruikersnaamResponse(gebruiker.username))

        callGebruikersnaam.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleGebruikersnaamResponse, this::handleGebruikersnaamError)

        /*val call = GebruikerService.repository.registreer(gebruiker)
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private fun isUsernameValid(text: Editable?): Boolean {
        return text != null && text.length >= 4
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }

    private fun isConfirmPasswordValid(text: Editable?, textPass: Editable?): Boolean {
        return text?.trim().toString().equals(textPass?.trim().toString())
    }

    private fun handleGebruikersnaamResponse(gebruikersnaamResponse: CheckGebruikersnaamResponse) {
        when {
            gebruikersnaamResponse.username == "ok" -> {
                val call = GebruikerService.repository.registreer(gebruiker)
                call.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError)
            }
            gebruikersnaamResponse.username == "alreadyexists" -> username_text_input_reg.error = getString(R.string.error_useralreadyexists)
        }
    }

    private fun handleGebruikersnaamError(error: Throwable) {
        print(error)
    }

    private fun handleResponse(registreerResponse: RegistreerResponse) {

        val token = registreerResponse.token

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.put(getString(R.string.sp_token_key), token)
        sharedPreferences.put(getString(R.string.sp_token_username), gebruiker.username)

        fetchLikedPosts()

        (activity as NavigationHost).navigateTo(PinboardListFragment(), false) // navigate to next fragment

    }

    private fun handleError(error: Throwable) {
        print(error)
    }


    private fun fetchLikedPosts() {
        val username = sharedPreferences.get(getString(R.string.sp_token_username), "unknownUser")
        val call = GebruikerService.repository.getLikedPosts(CheckGebruikersnaamResponse(username))
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleLikedPostsResponse, this::handleError)
    }

    private fun handleLikedPostsResponse(likedPosts: List<PostResponse>) {
        val gson = Gson()
        val jsonLikedPosts = gson.toJson(likedPosts)
        sharedPreferences.put(getString(R.string.sp_token_likedPosts), jsonLikedPosts)
    }
}
