package com.example.cemeterywithserver.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cemeterywithserver.other.Resource
import com.example.cemeterywithserver.repositories.CemeteryRepository
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val repository: CemeteryRepository
) : ViewModel(){

    private val _registerStatus = MutableLiveData<Resource<String>>()
    val registerStatus = _registerStatus


    fun register(email: String, password: String, repeatedPassword: String) {
        _registerStatus.postValue(Resource.loading(null)) //emit loading status when main thread is available

        if(email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()){
            _registerStatus.postValue(Resource.error("Please fill out all fields", null))
            return
        }
        if(password != repeatedPassword){
            _registerStatus.postValue(Resource.error("Passwords do not match", null))
        }

        /*
            validation is good? then post value of what the server returns
         */
        viewModelScope.launch {
            val result = repository.register(email, password)
            _registerStatus.postValue(result)
        }
    }

}