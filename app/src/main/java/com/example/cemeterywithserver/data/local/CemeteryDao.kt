package com.example.cemeterywithserver.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.data.entitites.Grave
import kotlinx.coroutines.flow.Flow

@Dao
interface CemeteryDao {

    @Query("select * from current_cemetery_table where isSynced is 0")
    suspend fun getUnSynchedCemeteries() : List<Cemetery>

    @Query("select * from current_graves_table where isSynced is 0")
    suspend fun getUnSynchedGraves() : List<Grave>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCemsFromNetwork(vararg cemetery: Cemetery)//tested

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGravesFromNetwork(vararg grave: Grave)

    @Insert
    suspend fun insertCemetery(cemetery: Cemetery) // tested

    @Query("select * from current_cemetery_table") //tested
    fun getAllCemeteries(): Flow<List<Cemetery>>

    @Query("select * from current_cemetery_table where cemeteryId= :cemeteryId") //tested
    fun getCemeteryWithId(cemeteryId: String): LiveData<Cemetery>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //tested
    suspend fun insertGrave(grave: Grave)

    @Query("delete from current_graves_table where graveId= :rowId") //tested
    suspend fun deleteGrave(rowId: Int)

    @Query("select * from current_graves_table where graveId=  :graveId") //tested
    fun getGraveWithId(graveId: String): LiveData<Grave>


    @Query("select * from current_graves_table where cemeteryId= :cemeteryId") //tested
    fun getGravesWithCemeteryId(cemeteryId: String) : LiveData<List<Grave>>

}