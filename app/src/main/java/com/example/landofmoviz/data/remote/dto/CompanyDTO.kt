package com.example.landofmoviz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompanyDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)