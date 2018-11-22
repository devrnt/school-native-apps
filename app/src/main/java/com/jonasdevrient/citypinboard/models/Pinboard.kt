package com.jonasdevrient.citypinboard.models

import com.jonasdevrient.citypinboard.responses.PostResponse

data class Pinboard(
        var _id: String,
        var city: String,
        var location: Location,
        var posts: MutableList<PostResponse> = mutableListOf()
) {
    val amountOfPosts get() = posts.count()
}
