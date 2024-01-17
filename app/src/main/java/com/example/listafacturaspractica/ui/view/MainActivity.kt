package com.example.listafacturaspractica.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityMainBinding
import com.example.listafacturaspractica.data.database.Invoice
import com.example.listafacturaspractica.ui.view.adapter.InvoiceAdapter
import com.example.listafacturaspractica.ui.viewmodel.InvoiceViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var invoiceAdapter: InvoiceAdapter
    private var maxAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("Facturas")

        invoiceAdapter = InvoiceAdapter(){
            invoice ->  onItemSelected(invoice)
        }

        initViewModel()
        initMainViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.invoiceMenuMain -> {
               val miIntent = Intent(this, FilterActivity::class.java)
                miIntent.putExtra("MAX_IMPORTE", maxAmount)
                startActivity(miIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModel() {
        binding.rvFacturas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            invoiceAdapter = InvoiceAdapter(){
                    invoice ->  onItemSelected(invoice)
            }
            adapter = invoiceAdapter
        }
    }

    private fun initMainViewModel() {
        val viewModel = ViewModelProvider(this).get(InvoiceViewModel::class.java)
        viewModel.getAllRepositoryList().observe(this, Observer<List<Invoice>>{
            invoiceAdapter.setListInvoices(it)
            invoiceAdapter.notifyDataSetChanged()


            if (it.isEmpty()) {
                viewModel.makeApiCall()
                Log.d("Datos", it.toString() )
            } else {


                /*
                // Realizar las operaciones de filtrado aquí

                // Filtrar por fecha
                val filteredByDate = comprobarFiltroFechas(fechaMax, fechaMin, invoices)

                // Filtrar por barra de importe
                val filteredByImporte = comprobarBarraImporte(maxValuesSlider, filteredByDate)

                // Filtrar por CheckBox
                val filteredByCheckBox = comprobarChekBox(estado, filteredByImporte)

                // Actualizar el adaptador con la lista filtrada
                invoiceAdapter.setListInvoices(filteredByCheckBox)
                invoiceAdapter.notifyDataSetChanged()
                */
            }
            val filtroJson = intent.getStringExtra("FILTRO_ENVIAR_RECIBIR_DATOS")
            if (filtroJson != null) {
                val filter = Gson().fromJson(filtroJson, Filter::class.java)
                Log.d("FILTRO2", filter.toString())

            } else {
                // Manejar el caso en que el valor sea null
                Log.e("MainActivity", "El valor de FILTRO_ENVIAR_RECIBIR_DATOS es null")
            }
            val invoiceList = it
            maxAmount = obtenerMayorImporte(it)
            Log.d("FILTROS!", filtroJson.toString())


        })
    }

    private fun onItemSelected(invoice: Invoice) {
        val fragmentManager = supportFragmentManager // Reemplaza con el FragmentManager adecuado
        val customPopupFragment = FragmentPopUp()
        customPopupFragment.show(fragmentManager, "FragmentPopUp")
    }

    private fun obtenerMayorImporte(invoices: List<Invoice>): Double {
        var importeMaximo = 0.0
        for (factura in invoices) {
            val facturaActual = factura.importeOrdenacion
            if(importeMaximo < facturaActual!!) importeMaximo = facturaActual
        }
        return  importeMaximo
    }
}