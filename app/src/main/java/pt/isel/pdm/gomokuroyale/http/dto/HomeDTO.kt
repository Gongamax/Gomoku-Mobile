package pt.isel.pdm.gomokuroyale.http.dto

class GetHomeOutputModel : DTO {
    override fun getType(): Class<out DTO> {
        return this::class.java
    }
}