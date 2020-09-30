package com.example.cemeterywithserver.ui.cemeterydetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.repositories.CemeteryRepository

class CemeteryDetailViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _cemeterySelected = repository.getCemeteryWithId(savedStateHandle.get<String>(CEMETERY_ID)!!)
    val cemeterySelected : LiveData<Cemetery> = _cemeterySelected



    companion object {
        const val CEMETERY_ID = "CEMETERY_ID"
    }
}

