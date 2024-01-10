package com.example.listafacturaspractica.data.model

import com.example.listafacturaspractica.domain.model.Invoice
import com.google.gson.annotations.SerializedName

data class InvoiceModel(
    @SerializedName("descEstado") val descEstado: String,
    @SerializedName("importeOrdenacion") val importeOrdenacion: Float,
    @SerializedName("fecha") val fecha: String
){
}