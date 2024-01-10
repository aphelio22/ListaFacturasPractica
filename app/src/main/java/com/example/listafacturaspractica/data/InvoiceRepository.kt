package com.example.listafacturaspractica.data

import com.example.listafacturaspractica.data.database.dao.InvoiceDAO
import com.example.listafacturaspractica.data.database.entities.InvoiceEntity
import com.example.listafacturaspractica.data.model.InvoiceModel
import com.example.listafacturaspractica.data.network.InvoiceService
import com.example.listafacturaspractica.domain.model.Invoice
import com.example.listafacturaspractica.domain.model.toDomain

import javax.inject.Inject

class InvoiceRepository @Inject constructor(private val api: InvoiceService, private val invoiceDAO: InvoiceDAO) {
    /*
    suspend fun getAllInvoicesFromApi(): List<Invoice> {
        val response: List<InvoiceModel> = api.getInvoices()
        return response.map { it.toDomain() }
    }

    suspend fun getAllInvoicesFromDatabase(): List<Invoice> {
        val response: List<InvoiceEntity> = invoiceDAO.getAllInvoices()
        return response.map { it.toDomain() }
    }

    suspend fun insertInvoice(invoice: List<InvoiceEntity>) {
        invoiceDAO.insertAll(invoice)
    }

    suspend fun clearInvoice() {
        invoiceDAO.deleteAllInvoices()
    }
    */

}
