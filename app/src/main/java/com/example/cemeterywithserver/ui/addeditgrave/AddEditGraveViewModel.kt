package com.example.cemeterywithserver.ui.addeditgrave

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.repositories.CemeteryRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditGraveViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel() {

    private val _graveId = MutableLiveData<String>()
    val graveToEdit = _graveId.switchMap {
        repository.getGraveWithId(it)
    }

    fun setGraveId(graveId : String) {
        _graveId.value = graveId
    }

    fun addGrave(grave :Grave){
        GlobalScope.launch {
            repository.insertGrave(grave)
        }
    }
}