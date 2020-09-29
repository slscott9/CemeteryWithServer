package com.example.cemeterywithserver.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.data.local.CemeteryDao
import com.example.cemeterywithserver.data.remote.CemeteryApi
import com.example.cemeterywithserver.data.remote.requests.AccountRequest
import com.example.cemeterywithserver.data.remote.requests.AddCemRequest
import com.example.cemeterywithserver.data.remote.requests.AddGravesRequest
import com.example.cemeterywithserver.data.remote.responses.ServerResponse
import com.example.cemeterywithserver.other.Resource
import com.example.cemeterywithserver.other.checkForInternetConnection
import com.example.cemeterywithserver.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class CemRepoImpl @Inject constructor(
    private val cemeteryDao: CemeteryDao,
    private val cemeteryApi: CemeteryApi,
    private val context: Application
) : CemeteryRepository{

    override suspend fun getunSynchedCemeteries(): List<Cemetery> {
        return cemeteryDao.getUnSynchedCemeteries()
    }

    override suspend fun getunSynchedGraves(): List<Grave> {
        return cemeteryDao.getUnSynchedGraves()

    }

    override suspend fun sendNewCemsToNetwork(cemList: List<Cemetery>) : Response<ServerResponse>{

        return cemeteryApi.updateCemList(AddCemRequest(cemList))
    }

    override suspend fun sendNewGravesToNetwork(graveList: List<Grave>) : Response<ServerResponse>{
        return cemeteryApi.updateGraveList(AddGravesRequest(graveList))
    }

    override suspend fun insertCemeteryList(cemList: List<Cemetery>) {
        cemeteryDao.insertAllCemsFromNetwork(*cemList.toTypedArray())

    }

    override suspend fun insertGraveList(graveList: List<Grave>) {
        cemeteryDao.insertAllGravesFromNetwork(*graveList.toTypedArray())
    }

    override suspend fun login(email: String, password: String): Resource<String> = withContext(Dispatchers.IO){
        try {
            val response = cemeteryApi.login(AccountRequest(email, password))
            if(response.isSuccessful && response.body()!!.successful){
                Resource.success((response.body()?.message))
            }else{
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        }catch (e : Exception){
            Resource.error("Couldn't connect to server. Check your internet connection" , null)
        }
    }

    override suspend fun register(email: String, password: String): Resource<String>  = withContext(Dispatchers.IO){
        try {
            val response = cemeteryApi.register(AccountRequest(email, password))
            if(response.isSuccessful && response.body()!!.successful){

                Resource.success(response.body()?.message)
            }else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        }catch (e : Exception){
            Resource.error("Couldn't connect to service. Check network connection", null)
        }
    }



    override suspend fun getCemeteriesFromNetwork(): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val response = cemeteryApi.getAllCems()
            if(response.isSuccessful){
                Timber.i(response.body().toString())
                insertCemeteryList(response.body()!!)
                Resource.success("Successfully retrieved cemeteries from network")
            }else{
                Resource.error("Failed to connect to service. Check network connection", null)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Resource.error("Unknown error ", null)
        }

    }

    override suspend fun getGravesFromNetwork(): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val response = cemeteryApi.getAllGraves()
            if(response.isSuccessful){
                insertGraveList(response.body()!!)
                Resource.success("Successfully retrieved cemeteries from network")
            }else{
                Resource.error("Failed to connect to service. Check network connection", null)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Resource.error("Unknown error ", null)
        }

    }

    override suspend fun getNewCemeteriesForNetwork(): List<Cemetery> = withContext(Dispatchers.IO){
         cemeteryDao.getAllCemsForNetwork()

    }

    //wrap in resource so we can show states of list of cemeteries
    override fun getAllCemeteries(): Flow<Resource<List<Cemetery>>>{
        Timber.i("in get all cemeteries  ")

        return networkBoundResource(
            query = {
                cemeteryDao.getAllCemeteries()
            },
            fetch = {
                cemeteryApi.getAllCems()
            },
            saveFetchResult = {
                it.body()?.let {networkCemList ->
                    Timber.i("in save fethc result")
                    insertCemeteryList(networkCemList)
                }
            },
            shouldFetch = {
                //default always fetch data if internet connection
                checkForInternetConnection(context)
            }
        )

    }

    override suspend fun insertCemetery(cemetery: Cemetery) = withContext(Dispatchers.IO){
        cemeteryDao.insertCemetery(cemetery)

    }

    override suspend fun insertGrave(grave: Grave) = withContext(Dispatchers.IO){
        cemeteryDao.insertGrave(grave)

    }

    override suspend fun deleteGrave(graveRowId: Int) {
        cemeteryDao.deleteGrave(graveRowId)

    }

    override fun getGraveWithRowId(graveRowId: Int): LiveData<Grave> {
        return cemeteryDao.getGraveWithRowid(graveRowId)

    }

    override fun getCemeteryWithRowId(cemeteryRowId: Int): LiveData<Cemetery> {
        return cemeteryDao.getCemeteryWithId(cemeteryRowId)

    }

    override fun getGravesWithCemeteryId(cemeteryId: Int): LiveData<List<Grave>> {
        return cemeteryDao.getGravesWithCemeteryId(cemeteryId)

    }


}