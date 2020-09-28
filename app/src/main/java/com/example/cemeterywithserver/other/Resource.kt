package com.example.cemeterywithserver.other

/*
    Can use this class for every project that uses an api
 */

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    // 3 functions that return the corresponding Status
    companion object {
        fun <T> success(data: T?) : Resource<T> {
            return Resource(Status.SUCCESS, data, null) //no message because it was a success
        }

        fun <T> error(message: String, data: T?) : Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? ) : Resource<T> {
            return Resource(Status.LOADING, data, null )
        }
    }


}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}