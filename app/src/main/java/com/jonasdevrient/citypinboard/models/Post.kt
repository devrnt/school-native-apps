package com.jonasdevrient.citypinboard.models

import java.util.Date

class Post (
        var title: String,
        var body: String,
        var dateCreated: Date? = null,
        var likes: Int = 0,
        creator: String? = null
) {
    fun addLike() {
        likes++
    }

    fun removeLike() {
        likes--
    }
}