package com.jonasdevrient.citypinboard.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jonasdevrient.citypinboard.models.Pinboard
import io.reactivex.Single

@Dao
interface PinboardDao {

    @Query("SELECT * FROM pinboards")
    fun getAll(): Single<List<Pinboard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pinboards: List<Pinboard>)
}