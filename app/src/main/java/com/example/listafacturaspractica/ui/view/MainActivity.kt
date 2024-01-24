package com.example.listafacturaspractica.ui.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityMainBinding
import com.example.listafacturaspractica.data.database.Invoice
import com.example.listafacturaspractica.ui.view.adapter.InvoiceAdapter
import com.example.listafacturaspractica.ui.view.constants.Constants
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
    private val viewModel: InvoiceViewModel by viewModels()

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Establece el título de la Toolbar en 'Facturas'.
        supportActionBar?.setTitle("Facturas")

        initComponents()
    }

    /**
     * Inicializa los componentes relacionados con la comunicación entre la interfaz de usuario
     * y los datos de la aplicación, configurando el ViewModel principal y otros elementos necesarios.
     */
    private fun initComponents() {
        initView()
        initViewModel()
    }

    /**
     * Configura RecyclerView y onIntemSelected.
     */
    private fun initView() {
        binding.rvInvoices.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            invoiceAdapter = InvoiceAdapter() { invoice ->
                onItemSelected(invoice)
            }
            adapter = invoiceAdapter
        }
    }

    /**
     * Inicializa el ViewModel y maneja los cambios en la lista de facturas entre
     * los datos y la vista.
     */
    private fun initViewModel() {
        //val viewModel = ViewModelProvider(this).get(InvoiceViewModel::class.java)
        viewModel.getAllRepositoryList().observe(this, Observer<List<Invoice>> {

            var filteredList = getFilteredListFromSharedPreferences()
            if (filteredList == null || filteredList.isEmpty()) { //Si la lista filtrada no está en SharedPreferences, carga la lista original.
                invoiceAdapter.setListInvoices(it)
            } else {  //Si hay una lista filtrada almacenada, se usa.
                invoiceAdapter.setListInvoices(filteredList)
            }
            invoiceAdapter.notifyDataSetChanged()

            callInvoices(it)
            applyFilters(it)

            //Se obtiene el máximo importe de las facturas para enviarse a FilterActivity.
            maxAmount = getMaxAmount(it)
        })
    }

    /**
     * Si la Base de Datos con Room está vacía, llama al método 'makeApiCall()' del ViewModel
     * el cual se encarga de llamar a la Api mediante Retrofit y llenar la Base de Datos.
     *
     * @param invoiceList Lista de facturas.
     */
    private fun callInvoices(invoiceList: List<Invoice>) {
        if (invoiceList.isEmpty()) { //Si la lista original está vacía, se llama a la Api y se llena la lista.
            viewModel.makeApiCall()
            Log.d("Datos", invoiceList.toString())
        }
    }

    /**
     * Recibe la selección de datos que se ha hecho en los componentes de FilterActivity como un objeto
     * y los utiliza para filtrar la lista de facturas.
     *
     * @param invoiceList Lista de facturas.
     * */
    private fun applyFilters(invoiceList: List<Invoice>) {
        val filtroJson = intent.getStringExtra(Constants.SEND_RECEIVE_FILTERS)
        if (filtroJson != null) {
            filter = Gson().fromJson(filtroJson, Filter::class.java)

            var invoiceList = invoiceList

            //Verifica los datos según la fecha, el valor de la SeekBar y los CheckBoxes, y se aplican los filtros.
            filter?.let { nonNullFilter ->
                invoiceList = verifyDateFilter(nonNullFilter.maxDate, nonNullFilter.minDate, invoiceList)
                invoiceList = verifyBalanceBar(nonNullFilter.maxValueSlider, invoiceList)
                invoiceList = verifyCheckBox(nonNullFilter.estate, invoiceList)

                //Se gusarda la lista en SharedPreferences.
                saveFilteredListToSharedPreferences(invoiceList)

                //Si la lista está vacía se hace visible 'tvEmptyList'.
                if (invoiceList.isEmpty()) {
                    binding.tvEmptyList.visibility = View.VISIBLE
                }

                //Se le pasa al adaptador la lista filtrada.
                invoiceAdapter.setListInvoices(invoiceList)
            }
        } else { //Se maneja el caso en el que 'filtroJson' sea null.
            Log.e("MainActivity", "El valor de ${Constants.SEND_RECEIVE_FILTERS} es null")
        }
    }

    //Métodos propios de la declaración del menú.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    //Métodos propios de la declaración del menú.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var gson = Gson()
        return when (item.itemId) {

            //Al pulsar en el menú se envían el máximo importe y los filros de vuelta a FilterActivity.
            R.id.invoiceMenuMain -> {
                val miIntent = Intent(this, FilterActivity::class.java)
                miIntent.putExtra(Constants.MAX_AMOUNT, maxAmount)
                if (filter != null) {
                    miIntent.putExtra(Constants.SEND_RECEIVE_FILTERS, gson.toJson(filter))
                }
                startActivity(miIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Método por el cual se carga el PopUp cuando se pulsa sobre un elemento de la lista.
    private fun onItemSelected(invoice: Invoice) {
        val fragmentManager = supportFragmentManager
        val customPopupFragment = FragmentPopUp(getString(R.string.notImplementedYet))
        customPopupFragment.show(fragmentManager, "FragmentPopUp")
    }

    /**
     * Obtiene el mayor importe de toda la lista de facturas.
     *
     * @param invoiceList Lista de facturas.
     * @return El mayor importe.
     */
    private fun getMaxAmount(invoiceList: List<Invoice>): Double {
        var maxAmount = 0.0

        for (invoice in invoiceList) {
            val actualInvoiceAmount = invoice.importeOrdenacion
                if (maxAmount < actualInvoiceAmount!!) {
                    maxAmount = actualInvoiceAmount
                }
            }
        return maxAmount
    }

    /**
     * Filtra la lista de facturas por fecha según los datos pasados desde FilterActivity.
     *
     * @param maxDate Fecha máxima del intervalo.
     * @param minDate Fecha mínima del intervalo.
     * @param invoiceList Lista de facturas a filtrar.
     * @return La lista filtrada por fecha.
     */
    private fun verifyDateFilter(
        maxDate: String,
        minDate: String,
        invoiceList: List<Invoice>
    ): List<Invoice> {
        val filteredInvoices = ArrayList<Invoice>()
        if (minDate != getString(R.string.dayMonthYear) && maxDate != getString(R.string.dayMonthYear)) {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            //Variables locales del método en las que se almacenan la fecha mínima y la fecha máxima parseadas.
            var minDateLocal: Date? = null
            var maxDateLocal: Date? = null

            try {
                //Parseo de las fechas.
                minDateLocal = simpleDateFormat.parse(minDate)
                maxDateLocal = simpleDateFormat.parse(maxDate)
            } catch (e: ParseException) {
                Log.d("Error1: ", "comprobarFiltroFechas: ParseException")
            }
            for (invoice in invoiceList) {
                var invoiceDate = Date()
                try {
                    invoiceDate = simpleDateFormat.parse(invoice.fecha)
                } catch (e: ParseException) {
                    Log.d("Error2: ", "comprobarFiltroFechas: ParseException")
                }

                //Se verifica si la fecha de la factura está dentro del intervalo especificado.
                if (invoiceDate.after(minDateLocal) && invoiceDate.before(maxDateLocal)) {
                    filteredInvoices.add(invoice)
                }
            }
        }
        return filteredInvoices
    }

    /**
     * Filtra la lista de facturas por estado según los datos pasados desde FilterActivity.
     *
     * @param state HashMap con el estado de los CheckBoxes.
     * @param invoiceList Lista de facturas a filtrar.
     * @return La lista filtrada por fecha.
     */
    private fun verifyCheckBox(
        state: HashMap<String, Boolean>,
        invoiceList: List<Invoice>
    ): List<Invoice> {

        //Se obtienen los estados de las CheckBoxes.
        val checkBoxPaid = state[Constants.PAID_STRING] ?: false
        val checkBoxCanceled = state[Constants.CANCELED_STRING] ?: false
        val checkBoxFixedPayment = state[Constants.FIXED_PAYMENT_STRING] ?: false
        val checkBoxPendingPayment = state[Constants.PENDING_PAYMENT_STRING] ?: false
        val checkBoxPaymentPlan = state[Constants.PAYMENT_PLAN_STRING] ?: false

        //Lista que contendrá las facturas filtradas por estado.
        val filteredInvoices = ArrayList<Invoice>()

        if (!checkBoxPaid && !checkBoxCanceled && !checkBoxFixedPayment && !checkBoxPendingPayment && !checkBoxPaymentPlan) {
            return invoiceList //Devuelve la lista original sin aplicar ningún filtro si no se selecciona ningún estado.
        }

        //Verificación de los estados de las facturas y los CheckBoxes seleccionados.
        for (invoice in invoiceList) {
            val invoiceState = invoice.descEstado
            val isPaid = invoiceState == "Pagada"
            val isCanceled = invoiceState == "Anuladas"
            val isFixedPayment = invoiceState == "cuotaFija"
            val isPendingPayment = invoiceState == "Pendiente de pago"
            val isPaymentPlan = invoiceState == "planPago"

            //Se añade la factura a la lista filtrada si cumple con alguno de los estados seleccionados.
            if ((isPaid && checkBoxPaid) || (isCanceled && checkBoxCanceled) || (isFixedPayment && checkBoxFixedPayment) || (isPendingPayment && checkBoxPendingPayment) || (isPaymentPlan && checkBoxPaymentPlan)) {
                filteredInvoices.add(invoice)
            }
        }
        return filteredInvoices
    }

    /**
     * Filtra la lista de facturas por importe según los datos pasados desde FilterActivity.
     *
     * @param maxValueSlider Importe de la SeekBar.
     * @param invoiceList Lista de facturas a filtrar.
     * @return La lista filtrada por fecha.
     */
    private fun verifyBalanceBar(
        maxValueSlider: Double,
        invoiceList: List<Invoice>
    ): List<Invoice> {
        //Lista que contendrá las facturas filtradas por el valor del SeekBar.
        val filteredInvoices = ArrayList<Invoice>()
        for (factura in invoiceList) {
            //Se añade la factura a la lista filtrada si su importe es menor que el valor seleccionado.
            if (factura.importeOrdenacion!! < maxValueSlider) {
                filteredInvoices.add(factura)
            }
        }
        return filteredInvoices
    }

    /**
     * Guarda la lista filrada en SharedPreferences.
     *
     * @param invoiceList Lista de facturas.
     */
    private fun saveFilteredListToSharedPreferences(invoiceList: List<Invoice>) {
        val gson = Gson()
        val filteredListJson = gson.toJson(invoiceList)

        val prefs: SharedPreferences = getPreferences(MODE_PRIVATE)
        prefs.edit().putString("FILTERED_LIST", filteredListJson).apply()
    }

    /**
     * Carga la lista filrada en SharedPreferences.
     *
     * @param invoiceList Lista de facturas.
     */
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