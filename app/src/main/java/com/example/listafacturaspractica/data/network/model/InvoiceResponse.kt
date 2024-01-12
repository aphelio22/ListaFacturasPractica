package com.example.listafacturaspractica.data.network.model

/**
 * Data Class que contiene un Modelo de Facturas con los atributos que definen a cada una de ellas.
 */
data class InvoiceResponse(
    val descEstado: String,
    val fecha: String,
    val importeOrdenacion: Double
)