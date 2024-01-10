package com.example.listafacturaspractica.data.network

import com.example.listafacturaspractica.data.model.InvoiceModel
import retrofit2.Response
import retrofit2.http.GET

interface InvoiceApiClient {
    @GET("facturas")
    suspend fun getAllQuotes(): Response<List<InvoiceModel>>
}