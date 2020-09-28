package com.example.cemeterywithserver.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.data.entitites.Grave
import com.example.cemeterywithserver.data.local.CemeteryDao
import com.example.cemeterywithserver.data.remote.CemeteryApi
import com.example.cemeterywithserver.data.remote.requests.AccountRequest
import com.example.cemeterywithserver.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CemRepoImpl @Inject constructor(
    private val cemeteryDao: CemeteryDao,
    private val cemeteryApi: CemeteryApi,
    private val context: Application
) : CemeteryRepository{

    override suspend fun register(email: String, password: String): Resource<String>  = withContext(Dispatchers.IO){
        try {
            val response = cemeteryApi.register(AccountRequest(email, password))
            if(response.isSuccessful){
                Resource.success(response.body()?.message)
            }else {
                Resource.error(response.message(), null)
            }
        }catch (e : Exception){
            Resource.error("Couldn't connect to service. Check network connection", null)
        }
    }



    override suspend fun getCemeteriesFromNetwork(): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val response = cemeteryApi.getAllCems()
            if(response.isSuccessful){
                cemeteryDao.insertAllCemsFromNetwork(response.body()!!)
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
                cemeteryDao.insertAllGravesFromNetwork(response.body()!!)
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

    override fun getAllCemeteries(): LiveData<List<Cemetery>> {
        return cemeteryDao.getAllCemeteries()
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