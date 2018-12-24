package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.models.Post
import com.jonasdevrient.citypinboard.responses.PostResponse
import io.reactivex.Observable
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