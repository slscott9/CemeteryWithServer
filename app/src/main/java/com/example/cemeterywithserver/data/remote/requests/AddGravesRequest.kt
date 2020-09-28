package com.example.cemeterywithserver.data.remote.requests

import com.example.cemeterywithserver.data.entitites.Grave

data class AddGravesRequest(
    val newGraveList : List<Grave>
) {
}