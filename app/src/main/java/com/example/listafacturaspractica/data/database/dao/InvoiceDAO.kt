package com.example.listafacturaspractica.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.listafacturaspractica.data.database.entities.InvoiceEntity

@Dao
interface InvoiceDAO {
    @Query("SELECT * FROM invoice_table")
    suspend fun getAllInvoices(): List<InvoiceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoices: List<InvoiceEntity>)

    @Query("DELETE FROM invoice_table")
    suspend fun deleteAllInvoices()
}