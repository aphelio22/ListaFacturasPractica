package com.example.listafacturaspractica.di

import android.content.Context
import co.infinum.retromock.Retromock
import com.example.listafacturaspractica.ResourceBodyFactory
import com.example.listafacturaspractica.data.database.InvoiceDao
import com.example.listafacturaspractica.data.database.InvoiceDatabase
import com.example.listafacturaspractica.data.network.APIRetrofitService
import com.example.listafacturaspractica.data.network.APIRetromockService
import com.example.listafacturaspractica.data.network.RetroService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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

    /**
     * Métdodo que obtiene un instancia de la Base de Datos con Room.
     *
     * @param context Contexto de la aplicación.
     * @return Una instancia de la clase 'InvoiceDatabase'.
     */
    @Provides
    @Singleton
    fun getAppDatabase(@ApplicationContext context: Context): InvoiceDatabase {
        return InvoiceDatabase.getAppDBInstance(context)
    }

    /**
     * Método que obtiene una una instancia de la clase 'InvoiceDAO' que contiene
     * métodos que se utilizan para manejar los datos de las facturas en la Base de Datos.
     *
     * @param invoiceDatabase Instancia de la Base de Datos desde la cual se va a coger
     * la instancia del DAO.
     * @return Una instancia de la clase 'InvoiceDAO'
     */
    @Provides
    @Singleton
    fun getAppDao(invoiceDatabase: InvoiceDatabase): InvoiceDao {
        return invoiceDatabase.getAppDao()
    }

    /**
     * URL de donde se obtienen los datos mediante Retrofit.
     */
    val BASE_URL = "https://viewnextandroid.wiremockapi.cloud/"


    @Provides
    @Singleton
    fun getRetrofit(retrofit: Retrofit): APIRetrofitService {
        return retrofit.create(APIRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun getRetromock(retromock: Retromock): APIRetromockService {
        return retromock.create(APIRetromockService::class.java)
    }

    /**
     * Método Dagger que proporciona una instancia única de Retrofit configurada con una URL base y un convertidor Gson.
     *
     * @return Instancia única de Retrofit configurada con la URL base y el convertidor Gson.
     */
    @Provides
    @Singleton
    fun buildRetroInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun buildRetromockInstance(retrofit: Retrofit): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory(ResourceBodyFactory())
            .build()

    }
}