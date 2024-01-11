package com.example.listafacturaspractica.network

import androidx.lifecycle.LiveData
import com.example.listafacturaspractica.database.InvoiceDao
import com.example.listafacturaspractica.model.Invoice
import com.example.listafacturaspractica.model.RepositoriesListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class InvoiceRepository @Inject constructor(
    private val retroService: RetroService,
    private val invoiceDao: InvoiceDao){

    //Cambiar nombre getAllInvoicesFromRoom
    fun getAllRecords(): LiveData<List<Invoice>> {
        return invoiceDao.getAllInvoices()
    }

    fun insertRecord(invoice: Invoice) {
        invoiceDao.insertInvoices(invoice)
    }

    //Retofit inserta
    fun makeApiCall() {
        val call: Call<RepositoriesListResponse> = retroService.getDataFromApi()
        call?.enqueue(object : Callback<RepositoriesListResponse>{
            override fun onResponse(
                call: Call<RepositoriesListResponse>,
                response: Response<RepositoriesListResponse>
            ) {
               if (response.isSuccessful) {
                   invoiceDao.deleteAllRecords()
                   response.body()?.facturas?.forEach{
                       insertRecord(Invoice(descEstado = it.descEstado, importeOrdenacion = it.importeOrdenacion, fecha = it.fecha))
                   }
               }
            }

            override fun onFailure(call: Call<RepositoriesListResponse>, t: Throwable) {
                //Do nothing
            }

        })
    }
}