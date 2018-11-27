package com.jonasdevrient.citypinboard.pinboards.posts

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomSheetDialogFragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.models.Post
import com.jonasdevrient.citypinboard.pinboards.PinboardDetailsActivity
import com.jonasdevrient.citypinboard.repositories.PinboardAPI
import com.jonasdevrient.citypinboard.responses.PostResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_post_fragment.*
import kotlinx.android.synthetic.main.add_post_fragment.view.*
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.HttpException

class AddPostFragment : BottomSheetDialogFragment() {
    private lateinit var pinboard: Pinboard
    private lateinit var sharedPreferences: SharedPreferences


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        pinboard = (activity as PinboardDetailsActivity).pinboard
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.add_post_fragment, container, false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        view.post_button.setOnClickListener {
            if (!isPostTitleValid(post_title_edit_text.text)) post_title_text_input.error = getString(R.string.error_post_field_empty) else post_title_text_input.error = null
            if (!isPostBodyValid(post_body_edit_text.text)) post_body_text_input.error = getString(R.string.error_post_field_empty) else post_body_text_input.error = null

            if (post_title_text_input.error == null && post_body_text_input.error == null) {
                val post = Post(
                        post_title_edit_text.text.toString(),
                        post_body_edit_text.text.toString(),
                        null,
                        0,
                        sharedPreferences.getString(getString(R.string.sp_token_username), "unknownUser")
                )

                val call = PinboardAPI.repository.addPostToPinboard(pinboard._id, post)
                call.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError)
            }


        }

        // Verwijder de error als er meer dan 0 karakters getypt worden
        view.post_title_edit_text.setOnKeyListener { _, _, _ ->
            if (isPostTitleValid(post_title_edit_text.text)) {
                post_title_text_input.error = null
            }
            false
        }

        // Clear the error once more than 0 karakters getypt worden
        view.post_body_edit_text.setOnKeyListener { _, _, _ ->
            if (isPostBodyValid(post_body_edit_text.text)) {
                post_body_text_input.error = null //Clear the error
            }
            false
        }

        return view
    }

    private fun isPostTitleValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }

    private fun isPostBodyValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }


    private fun handleResponse(post: PostResponse) {
        val newPost = post
        pinboard.posts!!.add(newPost)
        (activity as PinboardDetailsActivity).viewAdapter.notifyDataSetChanged()

        Toast.makeText(context, "Succesvol gepost", Toast.LENGTH_LONG).show()

        // reset add post form
        post_title_edit_text.text = null
        post_body_edit_text.text = null

        // close the bottomsheet
        this.dismiss()

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
