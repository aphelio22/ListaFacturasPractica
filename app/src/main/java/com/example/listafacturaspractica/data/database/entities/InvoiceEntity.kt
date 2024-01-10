package com.example.listafacturaspractica.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.listafacturaspractica.domain.model.Invoice

@Entity(tableName = "invoice_table")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    val descEstado: String,
    val importeOrdenacion: Float,
    val fecha: String)

    fun Invoice.toDatabase(): InvoiceEntity = InvoiceEntity(descEstado = descEstado, importeOrdenacion = importeOrdenacion, fecha = fecha)



