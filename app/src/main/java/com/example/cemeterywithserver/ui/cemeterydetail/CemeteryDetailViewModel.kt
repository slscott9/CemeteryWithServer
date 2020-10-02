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
    val cemeterySelected = _cemeteryId.switchMap {
        repository.getCemeteryWithId(it)
    }
    val graveList = _cemeteryId.switchMap {
        repository.getGravesWithCemeteryId(it)
    }

    /*
        if repository did not return live data use this to make it live data to observe

        	private val itemId = MutableLiveData<String>()
	        val result = itemId.switchMap {
		            liveData { emit(fetchItem(it))
		     }
}
     */



    fun setCemeteryId(cemeteryId: String ){
        _cemeteryId.value = cemeteryId
    }
}

