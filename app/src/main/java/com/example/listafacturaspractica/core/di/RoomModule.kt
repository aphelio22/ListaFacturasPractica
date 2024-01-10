package com.example.listafacturaspractica.core.di

import android.content.Context
import androidx.room.Room
import com.example.listafacturaspractica.data.database.InvoiceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    const val INVOICE_DATABASE_NAME = "invoice_database"
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): InvoiceDatabase {
        return InvoiceDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideInvoiceDAO(db: InvoiceDatabase) = db.getInvoiceDAO()
}