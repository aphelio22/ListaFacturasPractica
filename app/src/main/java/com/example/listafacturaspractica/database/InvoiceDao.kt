package com.example.listafacturaspractica.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.listafacturaspractica.model.Invoice

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoice_table")
    fun getAllInvoices(): LiveData<List<Invoice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoices(invoice: Invoice)

    @Query("DELETE FROM invoice_table")
    fun deleteAllRecords()
}