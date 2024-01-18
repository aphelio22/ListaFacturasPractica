package com.example.listafacturaspractica.ui.view

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var invoiceAdapter: InvoiceAdapter
    private var filter: Filter? = null
    private var maxAmount: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("Facturas")

        invoiceAdapter = InvoiceAdapter() { invoice ->
            onItemSelected(invoice)
        }

        initViewModel()
        initMainViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var gson = Gson()
        return when (item.itemId) {
            R.id.invoiceMenuMain -> {
                val miIntent = Intent(this, FilterActivity::class.java)
                miIntent.putExtra("MAX_IMPORTE", maxAmount)
                if (filter != null) {
                    miIntent.putExtra("FILTRO_ENVIAR_RECIBIR_DATOS", gson.toJson(filter))
                }
                startActivity(miIntent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModel() {
        binding.rvFacturas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            invoiceAdapter = InvoiceAdapter() { invoice ->
                onItemSelected(invoice)
            }
            adapter = invoiceAdapter
        }
    }

    private fun initMainViewModel() {
        val viewModel = ViewModelProvider(this).get(InvoiceViewModel::class.java)
        viewModel.getAllRepositoryList().observe(this, Observer<List<Invoice>> {
            var filteredList = getFilteredListFromSharedPreferences()

            if (filteredList == null || filteredList.isEmpty()) {
                // Si la lista filtrada no está en SharedPreferences, cargar la lista original
                invoiceAdapter.setListInvoices(it)
            } else {
                // Si hay una lista filtrada almacenada, usarla
                invoiceAdapter.setListInvoices(filteredList)
            }

            invoiceAdapter.notifyDataSetChanged()

            if (it.isEmpty()) {
                viewModel.makeApiCall()
                Log.d("Datos", it.toString())
            }

            val filtroJson = intent.getStringExtra("FILTRO_ENVIAR_RECIBIR_DATOS")
            if (filtroJson != null) {
                filter = Gson().fromJson(filtroJson, Filter::class.java)
                var invoiceList = it

                filter?.let { nonNullFilter ->
                    invoiceList = verifyDateFilter(nonNullFilter.maxDate, nonNullFilter.minDate, invoiceList)
                    invoiceList = verifyBalanceBar(nonNullFilter.maxValueSlider, invoiceList)
                    invoiceList = verifyCheckBox(nonNullFilter.estate, invoiceList)
                    saveFilteredListToSharedPreferences(invoiceList)
                    invoiceAdapter.setListInvoices(invoiceList)
                    Log.d("FILTRO2", filter.toString())
                }

            } else {
                //Manejar el caso en que el valor sea null
                Log.e("MainActivity", "El valor de FILTRO_ENVIAR_RECIBIR_DATOS es null")
            }

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
            if (importeMaximo < facturaActual!!) importeMaximo = facturaActual
        }
        return importeMaximo
    }

    private fun verifyDateFilter(
        maxDate: String,
        minDate: String,
        filterList: List<Invoice>
    ): List<Invoice> {
        val filteredInvoices = ArrayList<Invoice>()
        if (minDate != "Dia/Mes/Año" && maxDate != "Dia/Mes/Año") {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            var minDateDate: Date? = null
            var maxDateDate: Date? = null

            try {
                minDateDate = simpleDateFormat.parse(minDate)
                maxDateDate = simpleDateFormat.parse(maxDate)
            } catch (e: ParseException) {
                Log.d("Error1: ", "comprobarFiltroFechas: ParseException")
            }
            for (invoice in filterList) {
                var invoiceDate = Date()
                try {
                    invoiceDate = simpleDateFormat.parse(invoice.fecha)
                } catch (e: ParseException) {
                    Log.d("Error2: ", "comprobarFiltroFechas: ParseException")
                }
                if (invoiceDate.after(minDateDate) && invoiceDate.before(maxDateDate)) {
                    filteredInvoices.add(invoice)
                }
            }
        }
        return filteredInvoices
    }

    private fun verifyCheckBox(
        estate: HashMap<String, Boolean>,
        invoiceList: List<Invoice>
    ): List<Invoice> {
        val checkBoxPaid = estate["PAGADAS_STRING"] ?: false
        val checkBoxCanceled = estate["ANULADAS_STRING"] ?: false
        val checkBoxFixedPayment = estate["CUOTA_FIJA_STRING"] ?: false
        val checkBoxPendingPayment = estate["PENDIENTES_PAGO_STRING"] ?: false
        val checkBoxPaymentPlan = estate["PLAN_PAGO_STRING"] ?: false

        val filteredInvoices = ArrayList<Invoice>()

        if (!checkBoxPaid && !checkBoxCanceled && !checkBoxFixedPayment && !checkBoxPendingPayment && !checkBoxPaymentPlan) {
            return invoiceList // Devolver la lista original sin aplicar ningún filtro.
        }

        for (invoice in invoiceList) {
            val invoiceState = invoice.descEstado
            val isPaid = invoiceState == "Pagada"
            val isCanceled = invoiceState == "Anuladas"
            val isFixedPayment = invoiceState == "cuotaFija"
            val isPendingPayment = invoiceState == "Pendiente de pago"
            val isPaymentPlan = invoiceState == "planPago"

            if ((isPaid && checkBoxPaid) || (isCanceled && checkBoxCanceled) || (isFixedPayment && checkBoxFixedPayment) || (isPendingPayment && checkBoxPendingPayment) || (isPaymentPlan && checkBoxPaymentPlan)) {
                filteredInvoices.add(invoice)
            }
        }
        return filteredInvoices
    }

    private fun verifyBalanceBar(
        maxValueSlider: Double,
        invoiceList: List<Invoice>
    ): List<Invoice> {
        val filteredInvoices = ArrayList<Invoice>()
        for (factura in invoiceList) {
            if (factura.importeOrdenacion!! < maxValueSlider) {
                filteredInvoices.add(factura)
            }
        }
        return filteredInvoices
    }

    private fun saveFilteredListToSharedPreferences(filteredList: List<Invoice>) {
        val gson = Gson()
        val filteredListJson = gson.toJson(filteredList)

        val prefs: SharedPreferences = getPreferences(MODE_PRIVATE)
        prefs.edit().putString("FILTERED_LIST", filteredListJson).apply()
    }

    private fun getFilteredListFromSharedPreferences(): List<Invoice>? {
        val prefs: SharedPreferences = getPreferences(MODE_PRIVATE)
        val filteredListJson: String? = prefs.getString("FILTERED_LIST", null)

        return if (filteredListJson != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Invoice>>() {}.type
            gson.fromJson(filteredListJson, type)
        } else {
            null


        }
    }
}