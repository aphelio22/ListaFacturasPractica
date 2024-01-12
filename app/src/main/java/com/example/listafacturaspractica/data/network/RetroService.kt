package com.example.listafacturaspractica.data.network

import retrofit2.Call
import com.example.listafacturaspractica.data.network.model.InvoiceRepositoriesListResponse
import retrofit2.http.GET

/**
 * Interfaz que define las operaciones para realizar llamadas a la API de facturas mediante Retrofit con método GET.
 */
interface RetroService {
    @GET("facturas")
    fun getDataFromApi(): Call<InvoiceRepositoriesListResponse>
}