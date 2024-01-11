package com.example.listafacturaspractica.di

import android.content.Context
import com.example.listafacturaspractica.database.InvoiceDao
import com.example.listafacturaspractica.database.InvoiceDatabase
import com.example.listafacturaspractica.network.RetroService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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