package com.example.listafacturaspractica.domain

import com.example.listafacturaspractica.data.InvoiceRepository
import com.example.listafacturaspractica.data.database.entities.toDatabase
import com.example.listafacturaspractica.domain.model.Invoice
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(private val repository: InvoiceRepository) {
/*
    suspend operator fun invoke(): List<Invoice> {
        val invoices = repository.getAllInvoicesFromApi()

       return if (invoices.isNotEmpty()) {
           repository.clearInvoice()
            repository.insertInvoice(invoices.map { it.toDatabase() })
           invoices
        } else {
            repository.getAllInvoicesFromDatabase()
        }
    }
    */

}
