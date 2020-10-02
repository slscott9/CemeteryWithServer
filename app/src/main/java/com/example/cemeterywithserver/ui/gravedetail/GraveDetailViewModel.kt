package com.example.cemeterywithserver.ui.gravedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.repositories.CemeteryRepository

class GraveDetailViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel() {

    private val _graveId = MutableLiveData<String>()
    val graveSelected = _graveId.switchMap{
        repository.getGraveWithId(it)
    }

    fun setGraveId(graveId : String ){
        _graveId.value = graveId
    }
}