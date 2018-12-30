package com.jonasdevrient.citypinboard.adapters

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.responses.ActionPostResponse
import com.jonasdevrient.citypinboard.responses.PostResponse
import com.jonasdevrient.citypinboard.services.GebruikerService
import com.jonasdevrient.citypinboard.utils.DateUtil
import get
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.post_item.view.*
import put
import retrofit2.HttpException

/**
 * Recycleview adapter used to the display the [posts]
 */
class PostsAdapter(val context: Context, private val posts: MutableList<PostResponse>) :
        RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.MyViewHolder {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        // create a new view
        val view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = posts[position]
        holder.setData(post, position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = posts.size


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentPost: PostResponse? = null
        private var currentPosition: Int = 0

        init {
            itemView.like_action_button.setOnClickListener {
                val username = sharedPreferences.get(context.resources.getString(R.string.sp_token_username), "unknownUser")

                if (isAlreadyLiked(this.currentPost!!)) {
                    unLikePost(username)
                } else {
                    likePost(username)
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun setData(post: PostResponse, position: Int) {
            itemView.title_post.text = post.title
            itemView.author_name.text = post.creator
            itemView.post_body.text = post.body
            itemView.amount_of_likes.text = post.likes.toString()
            itemView.date_post.text = DateUtil.toSimpleString(post.dateCreated!!)
            if (isAlreadyLiked(post)) {
                itemView.like_action_button.icon = context.getDrawable(R.drawable.ic_favorite_24dp)
            } else {
                itemView.like_action_button.icon = context.getDrawable(R.drawable.ic_favorite_border_24dp)
            }
            this.currentPost = post
            this.currentPosition = position
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun updateAmountOfLikes() {
            itemView.amount_of_likes.text = currentPost!!.likes.toString()
            if (isAlreadyLiked(currentPost!!)) {
                itemView.like_action_button.icon = context.getDrawable(R.drawable.ic_favorite_24dp)
            } else {
                itemView.like_action_button.icon = context.getDrawable(R.drawable.ic_favorite_border_24dp)
            }
        }

        private fun handleResponse(postResponse: List<PostResponse>) {
            val gson = Gson()
            val jsonLikedPosts = gson.toJson(postResponse)
            if (isAlreadyLiked(currentPost!!)) {
                currentPost!!.removeLike()
            } else {
                currentPost!!.addLike()
            }

            sharedPreferences.put(context.getString(R.string.sp_token_likedPosts), jsonLikedPosts)

            updateAmountOfLikes()
        }

        private fun handleResponseUnlike(postResponse: List<PostResponse>) {
            val mutableLikedList = postResponse.toMutableList()
            mutableLikedList.removeAll { p -> p._id == currentPost!!._id }
            print(mutableLikedList)
            val gson = Gson()
            val jsonLikedPosts = gson.toJson(mutableLikedList)
            if (isAlreadyLiked(currentPost!!)) {
                currentPost!!.removeLike()
            } else {
                currentPost!!.addLike()
            }

            sharedPreferences.put(context.getString(R.string.sp_token_likedPosts), jsonLikedPosts)

            updateAmountOfLikes()
        }

        private fun handleError(error: Throwable) {
            // Get error as HTTPException to get the exception code
            val httpError = error as HttpException
            Toast.makeText(context, context.resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }

        private fun isAlreadyLiked(post: PostResponse): Boolean {
            val gson = Gson()
            val likedPosts: List<PostResponse> = gson.fromJson(sharedPreferences.get(context.getString(R.string.sp_token_likedPosts), ""), Array<PostResponse>::class.java).toList()
            val bool = likedPosts.any { p -> p._id == post._id }
            return bool
        }

        private fun likePost(username: String) {
            val actionPostResponse = ActionPostResponse(username, currentPost!!._id)
            val call = GebruikerService.repository.likePost(actionPostResponse)
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError)
        }

        private fun unLikePost(username: String) {
            val actionPostResponse = ActionPostResponse(username, currentPost!!._id)
            val call = GebruikerService.repository.unLikePost(actionPostResponse)
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponseUnlike, this::handleError)
        }
    }
}