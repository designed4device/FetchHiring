package dev.wellen.fetchhiring.model

import retrofit2.http.GET

interface FetchHiringApi {
    @GET("/hiring.json")
    suspend fun getListItems(): List<Item>


}

