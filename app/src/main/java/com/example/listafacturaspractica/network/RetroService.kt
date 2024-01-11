package com.example.listafacturaspractica.network

import retrofit2.Call
import com.example.listafacturaspractica.model.RepositoriesListResponse
import retrofit2.http.GET

interface RetroService {
    @GET("facturas")
    fun getDataFromApi(): Call<RepositoriesListResponse>
}