package pt.isel.pdm.gomokuroyale.http.dto

interface DTO {

    fun getType() : Class<out DTO> {
        return this::class.java
    }
}