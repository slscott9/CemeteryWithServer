package com.example.cemeterywithserver.other

object Constants {


    val IGNORE_AUTH_URLS = listOf<String>("/login", "/register")

    const val  DATABASE_NAME = "current_cemetery_database"

    /*
        dont use local host why?

        Even though server is running on local host, since we lauched our app on a device local host refers to the android deviece not the pc
        solution is use our windows ipv4 address
     */
    const val BASE_URL = "http://192.168.1.3:8080"

    const val ENCRYPTED_SHARED_PREF_NAME = "encr_shared_pref"
}