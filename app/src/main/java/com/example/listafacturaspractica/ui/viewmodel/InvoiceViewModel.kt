package com.example.listafacturaspractica.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.listafacturaspractica.model.Invoice
import com.example.listafacturaspractica.network.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(private val invoiceRepository: InvoiceRepository): ViewModel() {
    //Sigue siendo Room
    fun getAllRepositoryList(): LiveData<List<Invoice>> {
        return invoiceRepository.getAllRecords()
    }

    fun makeApiCall() {
        invoiceRepository.makeApiCall()
    }
}