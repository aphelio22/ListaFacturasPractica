package com.example.listafacturaspractica.domain

import com.example.listafacturaspractica.data.InvoiceRepository
import com.example.listafacturaspractica.data.model.InvoiceModel
import com.example.listafacturaspractica.domain.model.Invoice
import javax.inject.Inject

class GetRandomInvoiceUseCase @Inject constructor(private val repository: InvoiceRepository) {
    /*
    suspend operator fun invoke(): Invoice?{
        val invoices = repository.getAllInvoicesFromDatabase()
        if (!invoices.isNullOrEmpty()) {
            val randomNumber = (invoices.indices).random()
            return invoices[randomNumber]
        }
        return null
    }
    */

}