package com.example.listafacturaspractica.data.network.model

/**
 * Data Class que define la estructura del JSON.
 */
data class InvoiceRepositoriesListResponse(val numFacturas: Int, val facturas: List<InvoiceResponse>)