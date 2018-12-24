package com.jonasdevrient.citypinboard.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.jonasdevrient.citypinboard.responses.PostResponse

/**
 * Data class Pinboard
 * @property _id id of the pinboard
 * @property city city of the pinboard
 * @property location location of the city
 * @property posts the list of posts that accords to the pinboard
 */
@Entity(tableName = "pinboards")
data class Pinboard(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var _id: String,
        var city: String,
        @Ignore
        var location: Location,
        @Ignore
        var posts: MutableList<PostResponse>? = mutableListOf()
) {
    constructor() : this("", "", Location(1.0, 2.0), mutableListOf())

    val amountOfPosts get() = posts!!.count()
}
