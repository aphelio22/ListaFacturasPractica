package com.example.listafacturaspractica.di

import android.content.Context
import com.example.listafacturaspractica.data.database.InvoiceDao
import com.example.listafacturaspractica.data.database.InvoiceDatabase
import com.example.listafacturaspractica.data.network.RetroService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Módulo Dagger Hilt que proporciona las dependencias necesarias para la inyección de dependencias en la aplicación.
 *
 * Este módulo incluye proveedores para la base de datos de facturas ([InvoiceDatabase]),
 * el DAO de facturas ([InvoiceDao]), el servicio Retrofit para la comunicación con la API ([RetroService]),
 * y la instancia de Retrofit utilizada para construir el servicio ([Retrofit]).
 *
 * @property BASE_URL La URL base de la API utilizada para construir la instancia de Retrofit.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getAppDatabase(@ApplicationContext context: Context): InvoiceDatabase {
        return InvoiceDatabase.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun getAppDao(invoiceDatabase: InvoiceDatabase): InvoiceDao {
        return invoiceDatabase.getAppDao()
    }

    val BASE_URL = "https://viewnextandroid.wiremockapi.cloud/"

    @Provides
    @Singleton
    fun getRetroServiceInterface(retrofit: Retrofit): RetroService {
        return retrofit.create(RetroService::class.java)
    }

    @Provides
    @Singleton
    fun getRetroInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}