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
    /**
     * Método para obtener todas las facturas de las Base de Datos con Room.
     */
    @Query("SELECT * FROM invoice_table")
    fun getAllInvoices(): LiveData<List<Invoice>>

    /**
     * Método para insertar las facturas en la Base de Datos con Room.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoices(invoice: Invoice)

    /**
     * Método para eliminar todas las facturas de la Base de Datos con Room.
     */
    @Query("DELETE FROM invoice_table")
    fun deleteAllInvoices()
}