package com.jonasdevrient.citypinboard.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jonasdevrient.citypinboard.R
import com.jonasdevrient.citypinboard.models.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostsAdapter(val context: Context?, private val posts: MutableList<Post>) :
        RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.MyViewHolder {
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
        var currentPost: Post? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, currentPost!!.likes.toString() + "Likes", Toast.LENGTH_SHORT).show()
            }
        }


        fun setData(post: Post, position: Int) {
            itemView.title_post.text = post.title
            itemView.author_name.text = post.creator
            itemView.post_body.text = post.body
            itemView.amount_of_likes.text = post.likes.toString()
            itemView.date_post.text = post.dateCreated.toString().subSequence(0, 10)
            this.currentPost = post
            this.currentPosition = position
        }

    }
}