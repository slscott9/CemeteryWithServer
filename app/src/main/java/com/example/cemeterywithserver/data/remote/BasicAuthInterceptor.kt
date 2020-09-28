package com.example.cemeterywithserver.data.remote

import com.example.cemeterywithserver.other.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {

    /*
        Can attach interceptors to each http request we make
        Each interceptor modify requests we make
     */

    val email: String? = null
    val password: String? = null


    /*
        If the http request is one of the IGNORE_AUTH_URLS then we dont authenticate that request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if(request.url.encodedPath in IGNORE_AUTH_URLS){
            return chain.proceed(request)
        }
        //if request was not one of the IGNORE we need to add auth header to request
        //Authorization means are you allowed to make that request
        //Authentication means making sure you are  actually you
        //If peter makes request he must be Authorized and Athenticated

        val authenticatedRequest = request.newBuilder()
            .header("Authorization", Credentials.basic(email ?: "", password ?: ""))
            .build()
        return chain.proceed(authenticatedRequest)
    }
}