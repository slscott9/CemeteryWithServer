package com.example.cemeterywithserver.data.remote

import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.data.remote.requests.AccountRequest
import com.example.cemeterywithserver.data.remote.requests.AddCemRequest
import com.example.cemeterywithserver.data.remote.requests.AddGravesRequest
import com.example.cemeterywithserver.data.remote.responses.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.annotation.PostConstruct

interface CemeteryApi {

    @POST("/register")
    suspend fun register(@Body registerRequest: AccountRequest): Response<ServerResponse>

    @POST("/login")
    suspend fun login(@Body registerRequest: AccountRequest): Response<ServerResponse>

    //authenticate methods
    @POST("/updateCemeteryList")
    suspend fun updateCemList(@Body addNewCemRequest: AddCemRequest) : Response<ServerResponse>

    @POST("/updateGraveList")
    suspend fun updateGraveList(@Body addNewGraveRequest: AddGravesRequest) : Response<ServerResponse>

    @GET("/getAllCems")
    suspend fun getAllCems() : Response<List<Cemetery>>

    @GET("/getAllGraves")
    suspend fun getAllGraves() : Response<List<Grave>>
}