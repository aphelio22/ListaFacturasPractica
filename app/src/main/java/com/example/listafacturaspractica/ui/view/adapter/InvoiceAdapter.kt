package com.example.listafacturaspractica.ui.view.adapter

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.data.database.Invoice

/**
 * Adaptador para la RecyclerView que muestra una lista de facturas.
 *
 * @property onClickListener Función de click para manejar eventos de clic en elementos de la lista.
 */
class InvoiceAdapter(private val onClickListener: (Invoice) -> Unit): RecyclerView.Adapter<InvoiceViewHolder>() {


    /**
     * Lista de facturas que se mostrará en la RecyclerView.
     */
    private var listInvoices: List<Invoice>? = null

    /**
     * Establece la lista de facturas que se mostrará en la RecyclerView.
     *
     * @param listInvoices Lista de facturas.
     */
    fun setListInvoices(listInvoices: List<Invoice>?) {
        this.listInvoices = listInvoices
    }

    //Métodos propios del Adapter de RecyclerView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InvoiceViewHolder(layoutInflater.inflate(R.layout.item_facturas, parent, false))
    }

    override fun getItemCount(): Int {
        if (listInvoices == null) return 0
        return listInvoices?.size!!
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.render(listInvoices?.get(position)!!, onClickListener)
    }
}