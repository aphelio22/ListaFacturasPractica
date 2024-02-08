package com.example.listafacturaspractica.ui.view.adapter

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.R.color.pendingPaymentInvoice
import com.example.listafacturaspractica.databinding.ItemFacturasBinding
import com.example.listafacturaspractica.data.database.Invoice
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ViewHolder para la RecyclerView que representa un elemento de la lista de facturas.
 *
 * @param view La vista que se va a asociar al ViewHolder.
 */
class InvoiceViewHolder(view: View): ViewHolder(view) {
    val binding = ItemFacturasBinding.bind(view)

    //Método propio del ViewHolder del RecyclerView.
    fun render(item: Invoice, onClickListener: (Invoice) -> Unit) {
        binding.itemEstado.text = item.descEstado
        binding.itemImporte.text = item.importeOrdenacion.toString()
        binding.itemFecha.text = item.fecha?.let { formatDate(it) }.toString()
        itemView.setOnClickListener {
            onClickListener(item)
        }

        /*
        * Si el texto de estado de las facturas es 'Pendiente de pago' se coloreará de rojo,
        * si no de verde.
        */
        if (binding.itemEstado.text == "Pendiente de pago"){
            binding.itemEstado.visibility = View.VISIBLE
            binding.itemEstado.setTextColor(Color.RED)
        } else if (binding.itemEstado.text == "Pagada") {
            binding.itemEstado.text = ""
        } else {
            binding.itemEstado.setTextColor(Color.GRAY)
        }


    }

    fun formatDate(date: String): String {
        try {
            val insert = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val format = insert.parse(date)
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))

            return format?.let { outputFormat.format(it) } ?: date


        } catch (e: ParseException) {
            e.printStackTrace()
            return date
        }
    }
}