package com.jonasdevrient.citypinboard.repositories

import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.persistence.PinboardDao
import io.reactivex.Observable
import io.reactivex.Observable.concatArray
import io.reactivex.schedulers.Schedulers

class MainRepository(val pinboardRepository: PinboardRepository, val pinboardDao: PinboardDao) {
    fun getAll(): Observable<List<Pinboard>> {
        return concatArray(
                getPinboardsFromDb(),
                getPinboardsFromApi()
        )
    }

    private fun getPinboardsFromDb(): Observable<List<Pinboard>>? {
        return pinboardDao
                .getAll()
                .toObservable()
                .doOnNext {
                    // getting the pinboards from database
                }
    }

    private fun getPinboardsFromApi(): Observable<List<Pinboard>>? {
        return PinboardAPI.repository.getAll().doOnNext {
            storePinboardsInDatabase(it)
        }
    }

    private fun storePinboardsInDatabase(pinboards: List<Pinboard>) {
        Observable.fromCallable { pinboardDao.insertAll(pinboards) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    // storing
                }
    }
}