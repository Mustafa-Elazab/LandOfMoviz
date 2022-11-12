package com.example.landofmoviz.data.repository

import com.example.landofmoviz.data.mapper.toPersonDetail
import com.example.landofmoviz.data.mapper.toPersonList
import com.example.landofmoviz.data.remote.api.PersonApi
import com.example.landofmoviz.domain.model.PersonDetail
import com.example.landofmoviz.domain.model.PersonList
import com.example.landofmoviz.domain.repository.PersonRepository
import com.example.landofmoviz.utils.Resource
import com.example.landofmoviz.utils.SafeApiCall
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val api: PersonApi,
    private val safeApiCall: SafeApiCall
) : PersonRepository {
    override suspend fun getPersonSearchResults(query: String, page: Int): Resource<PersonList> =
        safeApiCall.execute {
            api.getPersonSearchResults(query, page).toPersonList()
        }

    override suspend fun getPersonDetails(personId: Int): Resource<PersonDetail> =
        safeApiCall.execute {
            api.getPersonDetails(personId).toPersonDetail()
        }
}