package com.example.listafacturaspractica.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.listafacturaspractica.data.database.dao.InvoiceDAO
import com.example.listafacturaspractica.data.database.entities.InvoiceEntity

@Database(entities = [InvoiceEntity::class], version = 1)
abstract class InvoiceDatabase: RoomDatabase() {
    companion object{
        @Volatile
        private var _INSTANCE: InvoiceDatabase? = null

        fun getInstance(context: Context): InvoiceDatabase {
            return _INSTANCE ?: synchronized(this) {
                _INSTANCE ?: buildDatabase(context).also { db -> _INSTANCE = db }
            }
        }

        private fun buildDatabase(context: Context): InvoiceDatabase {
            return Room.databaseBuilder(
                context.applicationContext, // context
                InvoiceDatabase::class.java, // db
                "invoice_database" // db name
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun getInvoiceDAO(): InvoiceDAO
}