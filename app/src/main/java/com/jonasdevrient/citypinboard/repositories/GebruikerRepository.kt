package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Gebruiker
import com.jonasdevrient.citypinboard.repositories.GebruikerAPI.repository
import com.jonasdevrient.citypinboard.responses.CheckGebruikersnaamResponse
import com.jonasdevrient.citypinboard.responses.RegistreerResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface with the necessary methods to call the endpoints of the backend
 */
interface GebruikerRepository {
    /**
     *  Logs a [gebruiker] in
     *  @return an @see Observable of type @see RegistreerResponse with a token
     */
    @POST("users/login")
    fun login(@Body gebruiker: Gebruiker): Observable<RegistreerResponse>

    /**
     *  Logs a [gebruiker] in
     *  @return an @see Observable of type @see RegistreerResponse with a token
     */
    @POST("users/register")
    fun registreer(@Body gebruiker: Gebruiker): Observable<RegistreerResponse>

    /**
     *  Checks if a username is already taken
     *  @return an @see Observable of type @see CheckGebruikersnaamResponse with result 'ok' or 'alreadyexists'
     */
    @POST("users/checkusername")
    fun checkGebruikersnaam(@Body gebruikersnaamResponse: CheckGebruikersnaamResponse): Observable<CheckGebruikersnaamResponse>

}

/**
 * Static class (see java) with a public attribute [repository]
 */
object GebruikerAPI {
    // online url
    private val API_BASE_URL = "https://citypinboard.herokuapp.com/api/"

    // local url
    // private val API_BASE_URL = "http://192.168.0.229:3000/api/"


    /**
     * [repository] used to make the required calls in the class @see GebruikerRepository
     */
    var repository = Retrofit
            .Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GebruikerRepository::class.java)
}