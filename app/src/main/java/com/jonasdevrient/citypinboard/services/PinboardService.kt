package com.jonasdevrient.citypinboard.services

import com.jonasdevrient.citypinboard.repositories.PinboardRepository
import com.jonasdevrient.citypinboard.services.PinboardService.repository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Static class with a public attribute [repository] to use the [repository]
 * with the right configurations, adapters and configurations.
 */
object PinboardService {
    // online url
    private val API_BASE_URL = "https://citypinboard.herokuapp.com/api/"

    // local url
    // private val API_BASE_URL = "http://192.168.0.229:3000/api/"

    /**
     * [repository] used to make the required calls in the class @see PinboardRepository
     */
    var repository = Retrofit
            .Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PinboardRepository::class.java)
}