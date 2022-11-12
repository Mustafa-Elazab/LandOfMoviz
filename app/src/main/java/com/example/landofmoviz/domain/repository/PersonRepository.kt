package com.example.landofmoviz.domain.repository


import com.example.landofmoviz.domain.model.PersonDetail
import com.example.landofmoviz.domain.model.PersonList
import com.example.landofmoviz.utils.Resource

interface PersonRepository {
    suspend fun getPersonSearchResults(query: String, page: Int): Resource<PersonList>
    suspend fun getPersonDetails(personId: Int): Resource<PersonDetail>
}