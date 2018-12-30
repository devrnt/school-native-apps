package com.jonasdevrient.citypinboard.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.jonasdevrient.citypinboard.models.Pinboard
import com.jonasdevrient.citypinboard.persistence.ApplicationDatabase
import com.jonasdevrient.citypinboard.services.MainService
import io.reactivex.Observable

class PinboardViewModel(application: Application) : AndroidViewModel(application) {
    private val applicationDatabase = ApplicationDatabase.getInstance(application)

    val pinboardDao = applicationDatabase.pinboardDao()

    private val mainService = MainService(pinboardDao)

    fun getAllPinboards(): Observable<List<Pinboard>> {
        return mainService.getAll()
    }

}