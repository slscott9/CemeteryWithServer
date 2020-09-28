package com.example.cemeterywithserver.data.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_cemetery_table")
data class Cemetery(

    @PrimaryKey(autoGenerate = false)
    val cemeteryRowId: Int,

    val cemeteryName: String,

    val cemeteryLocation: String,

    val cemeteryState: String,

    val cemeteryCounty: String,

    val township: String,

    val range: String,

    val spot: String,

    val firstYear: String,

    val section: String

)




@Entity(tableName = "current_graves_table")
data class Grave(

    @PrimaryKey(autoGenerate = false)
    val graveRowId: Int,

    val cemeteryId: Int,

    val firstName: String,

    val lastName: String,

    val birthDate: String,

    val deathDate: String,

    val marriageYear: String,

    val comment: String,

    val graveNumber: String
)