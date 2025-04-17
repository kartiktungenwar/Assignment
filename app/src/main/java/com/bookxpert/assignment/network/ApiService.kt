package com.bookxpert.assignment.network


import com.bookxpert.assignment.util.Constants.URL_API
import com.mobicule.tatasky_bb.service.model.response.User
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Poonamchand Sahu 11 Nov 2022
 */

interface ApiService {

    @GET(URL_API)
    suspend fun getLoanBalances(): Response<List<User>>
}