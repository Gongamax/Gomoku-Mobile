package pt.isel.pdm.gomokuroyale.http.dto

import com.google.gson.reflect.TypeToken

interface DTO {

    fun getType() : Class<out DTO> {
        return this::class.java
    }
}