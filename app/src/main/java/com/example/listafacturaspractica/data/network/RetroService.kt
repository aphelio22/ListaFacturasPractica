package com.example.listafacturaspractica.data.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockCircular
import co.infinum.retromock.meta.MockResponse
import co.infinum.retromock.meta.MockResponses
import retrofit2.Call
import com.example.listafacturaspractica.data.network.model.InvoiceRepositoriesListResponse
import retrofit2.http.GET

/**
 * Interfaz que define las operaciones para realizar llamadas a la API de facturas mediante Retrofit con método GET.
 */
    interface APIRetromockService : RetroService {
        @Mock
        @MockResponses(
            MockResponse(body = "mock.json"),
            MockResponse(body = "mock2.json"),
            MockResponse(body = "mock3.json")
        )
        @MockCircular
        @GET("/")
        override fun getDataFromApi(): Call<InvoiceRepositoriesListResponse>
    }

    interface APIRetrofitService : RetroService {
        @GET("facturas")
        override fun getDataFromApi(): Call<InvoiceRepositoriesListResponse>
    }


    interface RetroService {
        fun getDataFromApi(): Call<InvoiceRepositoriesListResponse>

    }