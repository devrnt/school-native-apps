package com.jonasdevrient.citypinboard.models

class Pinboard(
        var id: Int,
        var city: String,
        var location: String,
        var posts: MutableList<String> = mutableListOf<String>()
) {
    val amountOfPosts get() = posts.count();


    fun amountOfPosts(): Int {
        return posts.count()
    }

    fun addPost(post: String) {
        posts.add(post)
    }

}
