package com.example.listafacturaspractica.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase abstracta que crea la Base de Datos.
 */
@Database(entities = [Invoice::class], version = 1, exportSchema = false)
abstract class InvoiceDatabase: RoomDatabase() {
    abstract fun getAppDao(): InvoiceDao
    companion object{
        private var DB_INSTANCE: InvoiceDatabase? = null

        fun getAppDBInstance(context: Context): InvoiceDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(context.applicationContext, InvoiceDatabase::class.java, "invoice_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}