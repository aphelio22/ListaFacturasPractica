package com.example.listafacturaspractica.model

data class RepositoriesListResponse(val numFacturas: Int, val facturas: List<InvoiceResponse>)