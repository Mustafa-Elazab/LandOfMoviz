package com.example.landofmoviz.data.remote.dto


import com.example.landofmoviz.data.remote.dto.PersonDTO
import com.google.gson.annotations.SerializedName

data class CreditsDTO(
    @SerializedName("cast")
    val cast: List<PersonDTO>,
    @SerializedName("crew")
    val crew: List<PersonDTO>,
    @SerializedName("guest_stars")
    val guestStars: List<PersonDTO>?
)