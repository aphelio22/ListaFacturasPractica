package com.example.listafacturaspractica.data.repository

import androidx.lifecycle.LiveData
import com.example.listafacturaspractica.data.database.InvoiceDao
import com.example.listafacturaspractica.data.database.Invoice
import com.example.listafacturaspractica.data.network.RetroService
import com.example.listafacturaspractica.data.network.model.InvoiceRepositoriesListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Clase que controla métodos propios de Room y Retrofit.
 */
class InvoiceRepository @Inject constructor(
    private val retroService: RetroService,
    private val invoiceDao: InvoiceDao
){

    /**
     * Recupera todas las facturas almacenadas en la Base de Datos con Room.
     *
     * @return Las facturas de la Base de Datos utilizando el método propio del DAO.
     */
    fun getAllInvoicesFromRoom(): LiveData<List<Invoice>> {
        return invoiceDao.getAllInvoices()
    }

    /**
     * Inserta facturas en la Base de Datos con Room utilizando el método propio definido en el DAO.
     */
    fun insertInvoicesInRoom(invoice: Invoice) {
        invoiceDao.insertInvoices(invoice)
    }

    /**
     * Se hace la llamada a Retrofit y se insertan las facturas en la Base de Datos con Room
     * si la conexión se hace correctamente.
     */
    fun makeApiCall() {
        val call: Call<InvoiceRepositoriesListResponse> = retroService.getDataFromApi()
        call?.enqueue(object : Callback<InvoiceRepositoriesListResponse>{
            override fun onResponse(
                call: Call<InvoiceRepositoriesListResponse>,
                response: Response<InvoiceRepositoriesListResponse>
            ) {
               if (response.isSuccessful) {
                   invoiceDao.deleteAllRecords()
                   response.body()?.facturas?.forEach{
                       insertInvoicesInRoom(Invoice(descEstado = it.descEstado, importeOrdenacion = it.importeOrdenacion, fecha = it.fecha))
                   }
               }
            }

            override fun onFailure(call: Call<InvoiceRepositoriesListResponse>, t: Throwable) {
                //Do nothing
            }

        })
    }
}