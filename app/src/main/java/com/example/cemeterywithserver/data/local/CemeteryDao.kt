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
    fun getCemeteryWithId(cemeteryId: Int): LiveData<Cemetery>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //tested
    suspend fun insertGrave(grave: Grave)

    @Query("delete from current_graves_table where graveId= :rowId") //tested
    suspend fun deleteGrave(rowId: Int)

    @Query("select * from current_graves_table where graveId=  :rowId") //tested
    fun getGraveWithRowid(rowId: Int): LiveData<Grave>


    @Query("select * from current_graves_table where cemeteryId= :cemeteryId") //tested
    fun getGravesWithCemeteryId(cemeteryId: Int) : LiveData<List<Grave>>


    @Query("select max(cemeteryId) from current_cemetery_table") //tested
    suspend fun getMaxCemeteryRowNum(): Int?

    @Query("select max(graveId) from current_graves_table") //tested
    suspend fun getMaxGraveRowNum(): Int?

    @Query("select * from current_cemetery_table")
    suspend fun getAllCemsForNetwork() : List<Cemetery>
}