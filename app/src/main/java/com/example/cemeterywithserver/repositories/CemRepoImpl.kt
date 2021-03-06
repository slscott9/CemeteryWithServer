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

    /*
        withContext for suspend functions that need to return something

        Room and retrofit are main safe and handle dispatcher for you - do not need withContext
     */

    //Called from worker on Dispatcher.default which should be okay even with database inserts
    override suspend fun getunSynchedCemeteries(): List<Cemetery> {
         return cemeteryDao.getUnSynchedCemeteries()
    }

    override suspend fun getunSynchedGraves(): List<Grave> {
         return cemeteryDao.getUnSynchedGraves()

    }

    override suspend fun sendNewCemsToNetwork(cemList: List<Cemetery>) : Response<ServerResponse>{
        return cemeteryApi.updateCemList(AddCemRequest(cemList))
    }

    override suspend fun sendNewGravesToNetwork(graveList: List<Grave>) : Response<ServerResponse> {
         return cemeteryApi.updateGraveList(AddGravesRequest(graveList))
    }

    override suspend fun insertCemeteryList(cemList: List<Cemetery>)  {
        cemeteryDao.insertAllCemsFromNetwork(*cemList.toTypedArray())

    }

    override suspend fun insertGraveList(graveList: List<Grave>)  {
        cemeteryDao.insertAllGravesFromNetwork(*graveList.toTypedArray())
    }




//called from viewModelScope - coroutines running are canceled when loginViewModel is destroyed

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

    override suspend fun register(email: String, password: String): Resource<String> = withContext(Dispatchers.IO) {
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


//not called from anywhere - as of right now NetworkBound method takes care of refresh strategy
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


    //Flow allows reactive ui - wrap in resource so we can show states of list of cemeteries

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
                    insertCemeteryList(networkCemList)
                }
            },
            shouldFetch = {
                //default always fetch data if internet connection
                checkForInternetConnection(context)
            }
        )

    }


    override suspend fun insertCemetery(cemetery: Cemetery){
        cemeteryDao.insertCemetery(cemetery)

    }

    override suspend fun insertGrave(grave: Grave) {
        cemeteryDao.insertGrave(grave)

    }

    override suspend fun deleteGrave(graveRowId: Int) {
        cemeteryDao.deleteGrave(graveRowId)

    }

    //Return live data dispatcher are taken care of for you

    override fun getGraveWithId(graveRowId: String): LiveData<Grave> {
        return cemeteryDao.getGraveWithId(graveRowId)
    }

    override fun getCemeteryWithId(cemeteryId: String): LiveData<Cemetery> {
        return cemeteryDao.getCemeteryWithId(cemeteryId)
    }

    override fun getGravesWithCemeteryId(cemeteryId: String): LiveData<List<Grave>> {
        return cemeteryDao.getGravesWithCemeteryId(cemeteryId)
    }


}