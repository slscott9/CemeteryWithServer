package com.example.cemeterywithserver.ui.addeditcemetery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.cemeterywithserver.repositories.CemeteryRepository

class AddEditCemViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel() {

    /*
        savedStateHandle has key for edit and cemetery chosen
        get key in savedStateHandle? if key is edit query for Cemetery set fields in binding

        IMPORTANT - use global scope if the view model will be destroyed before we can insert!!!

        things launched inside of viewModelScope only execute while the view model is active
        since viewModelScope is attached and aware of the view models lifecycle
     */
}