package com.example.cemeterywithserver.utils

import java.io.IOException

/*
    Exceptions for SafeApiRequest class

    We use these classes which extend IOException class to build exceptions with our error messages
    we get back from making network calls which return retrofit Response objects.

    class ApiException(t: T) : IOException(t : T)
 */
class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)