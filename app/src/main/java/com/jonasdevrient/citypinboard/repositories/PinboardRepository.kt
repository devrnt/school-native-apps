package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.models.Post
import com.jonasdevrient.citypinboard.repositories.GebruikerAPI.repository
import com.jonasdevrient.citypinboard.repositories.PinboardAPI.repository
import com.jonasdevrient.citypinboard.responses.PostResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit interface with the necessary methods to call the endpoints of the backend
 */
interface PinboardRepository {
    /**
     *  Gets all the pinboards
     *  @return an @see Observable of a list @see Pinboard with a token
     */
    @GET("pinboards")
    fun getAll(): Observable<List<Pinboard>>

    @POST("pinboard/{id}/posts")
    fun addPostToPinboard(@Path("id") pinboardId: String, @Body post: Post): Observable<PostResponse>
}

/**
 * Static class (see java) with a public attribute [repository]
 */
object PinboardAPI {
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