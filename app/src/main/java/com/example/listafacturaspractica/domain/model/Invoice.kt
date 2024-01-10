package com.example.listafacturaspractica.domain.model

import com.example.listafacturaspractica.data.database.entities.InvoiceEntity
import com.example.listafacturaspractica.data.model.InvoiceModel

data class Invoice(val descEstado: String, val importeOrdenacion: Float, val fecha: String)

fun InvoiceModel.toDomain() = Invoice(descEstado, importeOrdenacion, fecha)
fun InvoiceEntity.toDomain() = Invoice(descEstado, importeOrdenacion, fecha)
