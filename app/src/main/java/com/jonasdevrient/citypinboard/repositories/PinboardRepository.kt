package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Pinboard
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PinboardRepository {
    @GET("pinboards")
    fun getAll(): Observable<List<Pinboard>>
}

object PinboardAPI {
    // online url
    private val API_BASE_URL = "http://citypinboard.herokuapp.com/api/"

    // local url
    // private val API_BASE_URL = "http://192.168.0.229:3000/api/"


    var repository = Retrofit
            .Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PinboardRepository::class.java)
}