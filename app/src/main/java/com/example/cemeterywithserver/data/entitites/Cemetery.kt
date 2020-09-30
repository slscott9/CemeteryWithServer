package com.example.cemeterywithserver.data.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

/*
    When using gson these fields must match the objects fields from the server in order to parse it correctly

    Can use annotations to make different names for the kotlin objects
 */
@Entity(tableName = "current_cemetery_table")
data class Cemetery(

    @PrimaryKey(autoGenerate = false)
    val cemeteryId: String = UUID.randomUUID().toString(),

    val name: String,

    val location: String,

    val state: String,

    val county: String,

    val township: String,

    val range: String,

    val spot: String,

    val firstYear: String,

    val section: String,

    @Expose(deserialize = false, serialize = false) //retrofit ingores this parameter when parsing
    var isSynced: Boolean = false



)

@Entity(tableName = "current_graves_table")
data class Grave(

    @PrimaryKey(autoGenerate = false)
    val graveId: String = UUID.randomUUID().toString(),

    val cemeteryId: Int,

    val firstName: String,

    val lastName: String,

    val birthDate: String,

    val deathDate: String,

    val marriageYear: String,

    val comment: String,

    val graveNumber: String,

    @Expose(deserialize = false, serialize = false)
    var isSynced: Boolean = false
)

