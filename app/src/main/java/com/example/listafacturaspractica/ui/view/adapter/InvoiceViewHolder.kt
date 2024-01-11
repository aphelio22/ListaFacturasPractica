package com.example.listafacturaspractica.ui.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listafacturaspractica.databinding.ItemFacturasBinding
import com.example.listafacturaspractica.model.Invoice

class InvoiceViewHolder(view: View): ViewHolder(view) {
    val binding = ItemFacturasBinding.bind(view)
    private lateinit var invoice: Invoice
    fun render(item: Invoice){
        binding.itemEstado.text = item.descEstado
        binding.itemImporte.text = item.importeOrdenacion.toString()
        binding.itemFecha.text = item.fecha
    }
}