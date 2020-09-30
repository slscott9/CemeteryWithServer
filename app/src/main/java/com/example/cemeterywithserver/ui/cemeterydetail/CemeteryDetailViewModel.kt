package com.example.cemeterywithserver.ui.cemeterydetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.repositories.CemeteryRepository

class CemeteryDetailViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository,
) : ViewModel() {


    private val _cemeteryId = MutableLiveData<String>()
    val cemeterySelected = Transformations.switchMap(_cemeteryId) {
        repository.getCemeteryWithId(it)
    }



    fun setCemeteryId(cemeteryId: String ){
        _cemeteryId.value = cemeteryId
    }
}

