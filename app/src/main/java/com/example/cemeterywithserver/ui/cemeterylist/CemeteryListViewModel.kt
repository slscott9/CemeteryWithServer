package com.example.cemeterywithserver.ui.cemeterylist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.other.Event
import com.example.cemeterywithserver.other.Resource
import com.example.cemeterywithserver.repositories.CemeteryRepository
import timber.log.Timber

class CemeteryListViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel() {

    /*
        _forceupdate is changed emit what is in switchMap call getAllCems which handles refresh
        switch again and pass cem list to event class

        allCems can now be observed and states can be used to show what happended when gettting the cem list

     */

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _allCemeteries = _forceUpdate.switchMap {

        repository.getAllCemeteries().asLiveData(viewModelScope.coroutineContext)

    }.switchMap {

        MutableLiveData(Event(it)) //handles event once


    }
    val allCemteries: LiveData<Event<Resource<List<Cemetery>>>> = _allCemeteries

    fun syncCemeteryList() = _forceUpdate.postValue(true)


}