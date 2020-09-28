package com.example.cemeterywithserver.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cemeterywithserver.other.Resource
import com.example.cemeterywithserver.repositories.CemeteryRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<String>>()
    val registerStatus: LiveData<Resource<String>> = _registerStatus


}