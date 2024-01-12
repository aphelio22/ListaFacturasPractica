package com.example.listafacturaspractica.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.listafacturaspractica.data.database.Invoice
import com.example.listafacturaspractica.data.repository.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(private val invoiceRepository: InvoiceRepository): ViewModel() {
    //Sigue siendo Room
    fun getAllRepositoryList(): LiveData<List<Invoice>> {
        return invoiceRepository.getAllInvoicesFromRoom()
    }

    fun makeApiCall() {
        invoiceRepository.makeApiCall()
    }
}