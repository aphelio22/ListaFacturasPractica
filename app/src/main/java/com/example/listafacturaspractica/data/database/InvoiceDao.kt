package com.example.listafacturaspractica.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Interfaz que establece los métodos que se usarán para manejar los datos de las distintas facturas en la Base de Datos
 * con Room.
 */
@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoice_table")
    fun getAllInvoices(): LiveData<List<Invoice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoices(invoice: Invoice)

    @Query("DELETE FROM invoice_table")
    fun deleteAllRecords()
}