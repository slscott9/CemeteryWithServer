package com.example.cemeterywithserver.ui.gravedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.repositories.CemeteryRepository

class GraveDetailViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel() {

    private val _graveId = MutableLiveData<String>()
    val graveSelected:LiveData<Grave> = Transformations.switchMap(_graveId){
        repository.getGraveWithId(it)
    }

    fun setGraveId(graveId : String ){
        _graveId.value = graveId
    }
}