package com.jonasdevrient.citypinboard.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jonasdevrient.citypinboard.models.Pinboard

@Database(entities = [Pinboard::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun pinboardDao(): PinboardDao
}
