package com.example.cemeterywithserver.data.remote.requests

import com.example.cemeterywithserver.data.entitites.Cemetery

data class AddCemRequest(
    val newCemList: List<Cemetery>
) {
}