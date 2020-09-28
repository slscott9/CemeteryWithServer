package com.example.cemeterywithserver.other

open class Event<out T> (private val content: T) {

    //this boolean allows us to only display snackbars once and not multiple times on rotation
    //private set means we are allowed to set this value from within this class but not from outside
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() = if(hasBeenHandled){
        null
    }else {
        hasBeenHandled = true
        content
    }

    //returns content no matter what
    fun peekContent() = content
}