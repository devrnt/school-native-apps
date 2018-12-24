package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Gebruiker
import com.jonasdevrient.citypinboard.responses.ActionPostResponse
import com.jonasdevrient.citypinboard.responses.CheckGebruikersnaamResponse
import com.jonasdevrient.citypinboard.responses.PostResponse
import com.jonasdevrient.citypinboard.responses.RegistreerResponse
import io.reactivex.Observable
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

    /**
     *  Gets the liked posts of the given user
     *  @return an @see Observable of type @see List of type @see PostResponse with the liked posts
     */
    @POST("users/likedPosts")
    fun getLikedPosts(@Body checkGeruikersnaamResponse: CheckGebruikersnaamResponse): Observable<List<PostResponse>>

    /**
     *  Likes a post for the given user
     *  @return an @see Observable of type @see List of type @see PostResponse with the updated liked posts
     */
    @POST("users/likePost")
    fun likePost(@Body actionPostResponse: ActionPostResponse): Observable<List<PostResponse>>

    /**
     *  Unlikes a post for the given user
     *  @return an @see Observable of type @see List of type @see PostResponse with the updated liked posts
     */
    @POST("users/unLikePost")
    fun unLikePost(@Body actionPostResponse: ActionPostResponse): Observable<List<PostResponse>>

}
