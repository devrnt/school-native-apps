package com.jonasdevrient.citypinboard.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.jonasdevrient.citypinboard.models.Pinboard


/**
 * Abstract class used to create the Room database
 */
@Database(entities = [Pinboard::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    // code gets auto generated for this 'function'
    abstract fun pinboardDao(): PinboardDao

    companion object {
        private var instance: ApplicationDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ApplicationDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        ApplicationDatabase::class.java, "citypinboard_database")
                        .fallbackToDestructiveMigration()
                        .build()

            }
            return instance as ApplicationDatabase
        }
    }
}

