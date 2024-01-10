package com.example.listafacturaspractica.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.listafacturaspractica.data.model.InvoiceModel
import com.example.listafacturaspractica.domain.GetInvoicesUseCase
import com.example.listafacturaspractica.domain.GetRandomInvoiceUseCase
import com.example.listafacturaspractica.domain.model.Invoice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(

    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val getRandomInvoiceUseCase: GetRandomInvoiceUseCase
): ViewModel() {
/*
    val invoiceModel = MutableLiveData<Invoice>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getInvoicesUseCase()

            if(!result.isNullOrEmpty()){
                invoiceModel.postValue(result[0])
                isLoading.postValue(false)
            }
        }
    }

    fun randomQuote() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val quote = getRandomInvoiceUseCase()
            if (quote != null) {
                invoiceModel.postValue(quote)
            }
            isLoading.postValue(false)
        }
    }
*/
}