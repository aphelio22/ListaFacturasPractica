package com.example.listafacturaspractica.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.listafacturaspractica.data.database.InvoiceDao
import com.example.listafacturaspractica.data.database.Invoice
import com.example.listafacturaspractica.data.network.APIRetrofitService
import com.example.listafacturaspractica.data.network.APIRetromockService
import com.example.listafacturaspractica.data.network.RetroService
import com.example.listafacturaspractica.data.network.model.InvoiceRepositoriesListResponse
import com.example.listafacturaspractica.di.AppModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Clase que controla métodos propios de Room y Retrofit empleando Hilt.
 */
class InvoiceRepository @Inject constructor(
    private var retromockService: APIRetromockService,
    private var retrofitService: APIRetrofitService,
    private val invoiceDao: InvoiceDao
){
    private lateinit var service: RetroService
    private var data = "real"

    fun setData(newData: String) {
        data = newData
        decideService()
    }

    init {
        decideService()
    }

    private fun decideService() {
        if (data == "ficticio") {
            service = retromockService
        } else {
            service = retrofitService
        }
    }

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
     *
     * @param invoice Facturas que van a inyectarse.
     */
    fun insertInvoicesInRoom(invoice: Invoice) {
        invoiceDao.insertInvoices(invoice)
    }

    /**
     * Se hace la llamada a Retrofit y se insertan las facturas en la Base de Datos con Room
     * si la conexión se hace correctamente.
     */
    fun makeApiCall() {
        val call: Call<InvoiceRepositoriesListResponse> = service.getDataFromApi()
        call?.enqueue(object : Callback<InvoiceRepositoriesListResponse>{
            override fun onResponse(
                call: Call<InvoiceRepositoriesListResponse>,
                response: Response<InvoiceRepositoriesListResponse>
            ) {
               if (response.isSuccessful) {
                   invoiceDao.deleteAllInvoices()
                   response.body()?.facturas?.forEach{
                       insertInvoicesInRoom(Invoice(descEstado = it.descEstado, importeOrdenacion = it.importeOrdenacion, fecha = it.fecha))
                   }
               }
            }

            /**
             * Método que informa de posibles errores a la hora de establecer la conexión
             * con la Base de Datos.
             *
             * @param call La llamada (Call) que resultó en un error.
             * @param t El objeto Throwable que representa el error ocurrido durante la conexión.
             */
            override fun onFailure(call: Call<InvoiceRepositoriesListResponse>, t: Throwable) {
                Log.d("ERROR", "Ha ocurrido un error al establecer la conexión.")
            }
        })
    }
}