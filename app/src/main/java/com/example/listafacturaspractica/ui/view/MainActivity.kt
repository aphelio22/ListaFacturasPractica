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

/**
 * MainActivity representa la pantalla principal de la aplicación,
 * mostrando una lista de facturas.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * Binding de la clase MainActivity.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Instancia del adaptador de las facturas.
     */
    private lateinit var invoiceAdapter: InvoiceAdapter

    /**
     * Instancia de la clase Filter.
     */
    private var filter: Filter? = null

    /**
     * Valor máximo del importe de las facturas que se le debe pasar
     * a la SeekBar de FilterActivity.
     */
    private var maxAmount: Double = 0.0

    private var minDate: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Establece el título de la Toolbar en 'Facturas'.
        supportActionBar?.setTitle("Facturas")

        //Permite establecer un 'onClickListener' en cada elemento de la lista de facturas.
        invoiceAdapter = InvoiceAdapter() { invoice ->
            onItemSelected(invoice)
        }
        initComponents()
    }

    /**
     * Inicializa los componentes relacionados con la comunicación entre la interfaz de usuario
     * y los datos de la aplicación, configurando el ViewModel principal y otros elementos necesarios.
     */
    private fun initComponents() {
        initViewModel()
        initMainViewModel()
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
            if (filteredList == null || filteredList.isEmpty()) { //Si la lista filtrada no está en SharedPreferences, carga la lista original.
                invoiceAdapter.setListInvoices(it)
            } else {  //Si hay una lista filtrada almacenada, se usa.
                invoiceAdapter.setListInvoices(filteredList)
            }
            invoiceAdapter.notifyDataSetChanged()

            if (it.isEmpty()) { //Si la lista original está vacía, se llama a la Api y se llena la lista.
                viewModel.makeApiCall()
                Log.d("Datos", it.toString())
            }

            /* Recibe la selección de datos que se ha hecho en los componentes de FilterActivity como un objeto
            *  y los utiliza para filtrar la lista de facturas.*/
            val filtroJson = intent.getStringExtra("FILTRO_ENVIAR_RECIBIR_DATOS")
            if (filtroJson != null) {
                filter = Gson().fromJson(filtroJson, Filter::class.java)

                var invoiceList = it

                //Verifica los datos según la fecha, el valor de la SeekBar y los CheckBoxes, y se aplican los filtros.
                filter?.let { nonNullFilter ->
                    invoiceList = verifyDateFilter(nonNullFilter.maxDate, nonNullFilter.minDate, invoiceList)
                    invoiceList = verifyBalanceBar(nonNullFilter.maxValueSlider, invoiceList)
                    invoiceList = verifyCheckBox(nonNullFilter.estate, invoiceList)

                    //Se gusarda la lista en SharedPreferences.
                    saveFilteredListToSharedPreferences(invoiceList)

                    //Se le pasa al adaptador la lista filtrada.
                    invoiceAdapter.setListInvoices(invoiceList)
                    Log.d("FILTRO2", filter.toString())
                }
            } else { //Se maneja el caso en el que 'filtroJson' sea null.
                Log.e("MainActivity", "El valor de FILTRO_ENVIAR_RECIBIR_DATOS es null")
            }

            //Se obtiene el máximo importe de las facturas para enviarse a FilterActivity.
            maxAmount = obtenerMayorImporte(it)
            Log.d("FILTROS!", filtroJson.toString())
        })
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

    private fun onItemSelected(invoice: Invoice) {
        val fragmentManager = supportFragmentManager // Reemplaza con el FragmentManager adecuado
        val customPopupFragment = FragmentPopUp("Esta función aún no ha sido implementada")
        customPopupFragment.show(fragmentManager, "FragmentPopUp")
    }

    private fun obtenerMayorImporte(invoicesList: List<Invoice>): Double {
        var maxAmount = 0.0

        for (invoice in invoicesList) {
            val actualInvoiceAmount = invoice.importeOrdenacion
                if (maxAmount < actualInvoiceAmount!!) {
                    maxAmount = actualInvoiceAmount
                }
            }
        return maxAmount
    }

    private fun verifyDateFilter(
        maxDate: String,
        minDate: String,
        filterList: List<Invoice>
    ): List<Invoice> {
        val filteredInvoices = ArrayList<Invoice>()
        if (minDate != "Dia/Mes/Año" && maxDate != "Dia/Mes/Año") {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            var minDateLocal: Date? = null
            var maxDateLocal: Date? = null

            try {
                minDateLocal = simpleDateFormat.parse(minDate)
                maxDateLocal = simpleDateFormat.parse(maxDate)
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
                if (invoiceDate.after(minDateLocal) && invoiceDate.before(maxDateLocal)) {
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