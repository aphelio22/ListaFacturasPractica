package com.example.listafacturaspractica.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.domain.model.Invoice

class InvoiceAdapter(private val invoiceList: List<Invoice>): RecyclerView.Adapter<InvoiceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InvoiceViewHolder(layoutInflater.inflate(R.layout.item_facturas, parent, false))
    }

    override fun getItemCount(): Int = invoiceList.size

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val item = invoiceList[position]
        holder.render(item)
    }
}