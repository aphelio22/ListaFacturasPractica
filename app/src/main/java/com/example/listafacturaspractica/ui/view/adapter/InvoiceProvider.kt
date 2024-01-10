package com.example.listafacturaspractica.ui.view.adapter

import com.example.listafacturaspractica.domain.model.Invoice

class InvoiceProvider {
    companion object{
        var listaInvoices = mutableListOf(
            Invoice("Pagada", 60.56f, "26 de mayo")
        )
    }
}