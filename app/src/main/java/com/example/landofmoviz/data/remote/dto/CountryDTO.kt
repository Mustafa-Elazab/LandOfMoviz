package com.example.landofmoviz.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CountryDTO(
    @SerializedName("name")
    val name: String
)