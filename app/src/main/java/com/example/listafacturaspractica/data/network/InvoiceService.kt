package com.example.listafacturaspractica.data.network

import com.example.listafacturaspractica.core.RetrofitHelper
import com.example.listafacturaspractica.data.model.InvoiceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InvoiceService @Inject constructor(private val api: InvoiceApiClient) {

    suspend fun getInvoices(): List<InvoiceModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getAllQuotes()
            response.body() ?: emptyList()
        }
    }
}