package com.example.listafacturaspractica.ui.view.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ItemFacturasBinding
import com.example.listafacturaspractica.data.database.Invoice

class InvoiceViewHolder(view: View): ViewHolder(view) {
    val binding = ItemFacturasBinding.bind(view)
    private lateinit var invoice: Invoice
    fun render(item: Invoice, onClickListener: (Invoice) -> Unit){
        binding.itemEstado.text = item.descEstado
        binding.itemImporte.text = item.importeOrdenacion.toString()
        binding.itemFecha.text = item.fecha
        itemView.setOnClickListener{
            onClickListener(item)
        }

        if (binding.itemEstado.text.equals("Pendiente de pago")) {
            val notPaiInvoice = ContextCompat.getColor(itemView.context, R.color.pendiente)
            binding.itemEstado.setTextColor(notPaiInvoice)
        } else{
            val paidInvoice = ContextCompat.getColor(itemView.context, R.color.factura_pagada)
            binding.itemEstado.setTextColor(paidInvoice)
        }
    }
}