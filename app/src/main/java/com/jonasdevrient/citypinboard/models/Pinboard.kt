package com.jonasdevrient.citypinboard.models

class Pinboard(
        var id: Int,
        var city: String,
        var location: String,
        var posts: MutableList<String> = mutableListOf<String>()
) {
    val amountOfPosts get() = posts.count()


    fun amountOfPosts(): Int {
        return posts.count()
    }

    fun addPost(post: String) {
        posts.add(post)
    }
}

object Supplier {
    val pinboards = listOf<Pinboard>(
            Pinboard(1, "Oudenaarde", "1", mutableListOf("post 1", "post 2")),
            Pinboard(2, "Zingem", "2", mutableListOf()),
            Pinboard(3, "Namen", "3", mutableListOf("post 1")),
            Pinboard(4, "Gent", "4", mutableListOf()),
            Pinboard(2, "Zingem", "2", mutableListOf()),
            Pinboard(3, "Namen", "3", mutableListOf("post 1")),
            Pinboard(4, "Gent", "4", mutableListOf()),
            Pinboard(2, "Zingem", "2", mutableListOf()),
            Pinboard(3, "Namen", "3", mutableListOf("post 1")),
            Pinboard(4, "Gent", "4", mutableListOf()),
            Pinboard(2, "Zingem", "2", mutableListOf()),
            Pinboard(3, "Namen", "3", mutableListOf("post 1")),
            Pinboard(4, "Gent", "4", mutableListOf()),
            Pinboard(2, "Zingem", "2", mutableListOf()),
            Pinboard(3, "Namen", "3", mutableListOf("post 1")),
            Pinboard(4, "Gent", "4", mutableListOf()),
            Pinboard(2, "Zingem", "2", mutableListOf()),
            Pinboard(3, "Namen", "3", mutableListOf("post 1")),
            Pinboard(4, "Gent", "4", mutableListOf())
    )
}
