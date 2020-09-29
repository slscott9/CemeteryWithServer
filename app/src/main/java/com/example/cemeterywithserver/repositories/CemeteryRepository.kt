package com.example.cemeterywithserver.repositories

import androidx.lifecycle.LiveData
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.data.remote.CemeteryApi
import com.example.cemeterywithserver.data.remote.responses.ServerResponse
import com.example.cemeterywithserver.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

interface CemeteryRepository {

    suspend fun getunSynchedCemeteries() : List<Cemetery>

    suspend fun getunSynchedGraves() : List<Grave>

    suspend fun sendNewCemsToNetwork(cemList: List<Cemetery>) : Response<ServerResponse>

    suspend fun sendNewGravesToNetwork(graveList: List<Grave>) :Response<ServerResponse>

    suspend fun insertCemeteryList(cemList: List<Cemetery>)

    suspend fun insertGraveList(graveList: List<Grave>)

    suspend fun login(email: String, password: String) : Resource<String>

    suspend fun register(email: String, password : String) : Resource<String>

    suspend fun getCemeteriesFromNetwork() : Resource<String>

    suspend fun getGravesFromNetwork() : Resource<String>

    suspend fun getNewCemeteriesForNetwork(): List<Cemetery>

    fun getAllCemeteries(): Flow<Resource<List<Cemetery>>>

    suspend fun insertCemetery(cemetery: Cemetery)

    suspend fun insertGrave(grave: Grave)

    suspend fun deleteGrave(graveRowId: Int)

    fun getGraveWithRowId(graveRowId: Int): LiveData<Grave>

    fun getCemeteryWithRowId(cemeteryRowId: Int): LiveData<Cemetery>

    fun getGravesWithCemeteryId(cemeteryId: Int): LiveData<List<Grave>>


}