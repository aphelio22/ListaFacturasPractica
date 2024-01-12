package com.example.listafacturaspractica.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase que define una entidad(modelo) que será inyectada en la Base de Datos con Room.
 */
@Entity(tableName = "invoice_table")
class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descEstado: String?,
    val importeOrdenacion: Double?,
    val fecha: String?) {

}