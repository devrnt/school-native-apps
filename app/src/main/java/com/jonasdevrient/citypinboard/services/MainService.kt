package com.jonasdevrient.citypinboard.services

import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.persistence.PinboardDao
import com.jonasdevrient.citypinboard.repositories.PinboardAPI
import io.reactivex.Observable
import io.reactivex.Observable.concatArray
import io.reactivex.schedulers.Schedulers

/**
 * This service is used as an high abstraction layer to at the same time get the pinboards from api
 * and get the pinboards from the local Room database.
 * Requests made to the api are automatically stored in the local Room database.
 * @property pinboardDao used to get and store the fetched pinboards in the Room database.
 * @constructor Creates a mainService
 */
class MainService(val pinboardDao: PinboardDao) {
    /**
     * Gets all pinboards
     * @return an observable with the list of pinboards
     */
    fun getAll(): Observable<List<Pinboard>> {
        return concatArray(
                getPinboardsFromDb(),
                getPinboardsFromApi()
        )
    }

    /**
     * Gets all pinboards that are stored in the database
     * @return an observable with the list of pinboards that are stored in the database
     */
    private fun getPinboardsFromDb(): Observable<List<Pinboard>>? {
        return pinboardDao
                .getAll()
                .toObservable()
    }

    /**
     * Gets all pinboards from the rest api and stores the result in the database
     * @return an observable with the list of pinboards from the rest endpoint
     */
    private fun getPinboardsFromApi(): Observable<List<Pinboard>>? {
        return PinboardAPI.repository.getAll().doOnNext {
            storePinboardsInDatabase(it)
        }
    }

    /**
     * Stores the [pinboards] in the Room database
     */
    private fun storePinboardsInDatabase(pinboards: List<Pinboard>) {
        Observable.fromCallable { pinboardDao.insertAll(pinboards) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe()
    }
}