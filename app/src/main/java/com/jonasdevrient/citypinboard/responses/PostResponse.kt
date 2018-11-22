package com.jonasdevrient.citypinboard.responses

import java.util.*

data class PostResponse(
        val _id: String,
        val title: String,
        val body: String,
        val dateCreated: Date? = null,
        var likes: Int = 0,
        val creator: String? = null
) {
    fun addLike() {
        likes++
    }

    fun removeLike() {
        likes--
    }
}