package com.jonasdevrient.citypinboard.models

data class Pinboard(
        var _id: String,
        var city: String,
        var location: Location,
        var posts: MutableList<Post> = mutableListOf()
) {
    val amountOfPosts get() = posts.count()
}
