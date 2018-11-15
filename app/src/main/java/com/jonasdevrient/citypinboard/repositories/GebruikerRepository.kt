package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Gebruiker
import com.jonasdevrient.citypinboard.responses.CheckGebruikersnaamResponse
import com.jonasdevrient.citypinboard.responses.RegistreerResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GebruikerRepository {
    @POST("users/login")
    fun login(@Body gebruiker: Gebruiker): Observable<Boolean>

    @POST("users/register")
    fun registreer(@Body gebruiker: Gebruiker): Observable<RegistreerResponse>

    @POST("users/checkusername")
    fun checkGebruikersnaam(@Body gebruikersnaamResponse: CheckGebruikersnaamResponse): Observable<CheckGebruikersnaamResponse>

}

object GebruikerAPI {
    // online url
    private val API_BASE_URL = "https://citypinboard.herokuapp.com/api/"

    // local url
    // private val API_BASE_URL = "http://192.168.0.229:3000/api/"


    var repository = Retrofit
            .Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GebruikerRepository::class.java)
}