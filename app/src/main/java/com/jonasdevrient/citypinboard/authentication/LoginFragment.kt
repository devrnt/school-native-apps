package com.jonasdevrient.citypinboard.authentication

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import put
import retrofit2.HttpException


class LoginFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences

    // used to persist in the sharedPref
    private lateinit var username: String
    private lateinit var password: String

    lateinit var usernameInput: EditText
    lateinit var passwordInput: EditText

    lateinit var usernameInputLayout: TextInputLayout
    lateinit var passwordInputLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        usernameInput = view.username_edit_text
        passwordInput = view.password_edit_text

        usernameInputLayout = view.username_text_input
        passwordInputLayout = view.password_text_input

        readSharedPreferences()

        view.registreer_button.setOnClickListener {
            (activity as NavigationHost).navigateTo(RegistreerFragment(), false) // navigate to next fragment
        }

        view.next_button.setOnClickListener {
            when {
                !isUsernameValid(username_edit_text.text) -> usernameInputLayout.error = getString(R.string.error_username)
                else -> username_text_input.error = null
            }
            when {
                !isPasswordValid(password_edit_text.text) -> passwordInputLayout.error = getString(R.string.error_password)
                else -> password_text_input.error == null
            }


            if (username_text_input.error == null && password_text_input.error == null) {

                username = username_edit_text.text.toString()
                password = password_edit_text.text.toString()

                val call = GebruikerService.repository.login(Gebruiker(username_edit_text.text.toString(), password_edit_text.text.toString()))
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
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        return view
    }

    /**
     * Reads the username and password from the shared preferences from the previous
     * logged in user
     */
    private fun readSharedPreferences() {
        val gebruikersnaam = sharedPreferences.getString(getString(R.string.sp_token_username), "")
        val wachtwoord = sharedPreferences.getString(getString(R.string.sp_token_password), "")

        // set the input with the username and password that was red from the sharedPreferences
        usernameInput.setText(gebruikersnaam)
        passwordInput.setText(wachtwoord)
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
        sharedPreferences.put(getString(R.string.sp_token_username), username)
        sharedPreferences.put(getString(R.string.sp_token_password), password)

        // load the likedPosts
        fetchLikedPosts()
    }

    private fun handleError(error: Throwable) {
        // Get error as HTTPException to get the exception code
        print(error)
        val httpError = error as HttpException
        when {
            httpError.code() == 401 -> password_text_input.error = getString(R.string.error_wrong_user_or_password)
            else -> Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchLikedPosts() {
        val username = sharedPreferences.get(getString(R.string.sp_token_username), "unknownUser")
        val call = GebruikerService.repository.getLikedPosts(CheckGebruikersnaamResponse(username))
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleLikedPostsResponse, this::handleLikedPostsError)
    }

    private fun handleLikedPostsResponse(likedPosts: List<PostResponse>) {
        val gson = Gson()
        val jsonLikedPosts = gson.toJson(likedPosts)
        sharedPreferences.put(getString(R.string.sp_token_likedPosts), jsonLikedPosts)

        // navigate to next fragment
        (activity as NavigationHost).navigateTo(PinboardListFragment(), false)
    }

    private fun handleLikedPostsError(error: Throwable) {
        print(error)
    }


}
