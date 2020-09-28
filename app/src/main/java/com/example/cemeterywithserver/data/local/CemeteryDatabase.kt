package com.example.cemeterywithserver.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.example.cemeterywithserver.data.entitites.Grave

@Database(entities = [Cemetery::class, Grave::class], version = 1)
abstract class CemeteryDatabase : RoomDatabase() {

    abstract fun cemeteryDao() : CemeteryDao
}