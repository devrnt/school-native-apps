package com.jonasdevrient.citypinboard.models

import java.util.*

class Post(
        val title: String,
        val body: String,
        val dateCreated: Date? = null,
        var likes: Int = 0,
        val creator: String? = null
)