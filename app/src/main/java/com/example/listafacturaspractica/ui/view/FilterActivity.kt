package com.example.listafacturaspractica.ui.view

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.SeekBar
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityFilterBinding
import com.example.listafacturaspractica.ui.view.constants.Constants
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Clase que representa la Activity de Filtros.
 */
class FilterActivity : AppCompatActivity() {

    /**
     * Binding para la vista de la Activity.
     */
    private lateinit var binding: ActivityFilterBinding
    private var filter: Filter? = null
    private var maxAmount: Int = 0
    private lateinit var paid: CheckBox
    private lateinit var canceled: CheckBox
    private lateinit var fixedPayment: CheckBox
    private lateinit var pendingPayment: CheckBox
    private lateinit var paymentPlan: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()

        //Configura el título de la ToolBar.
        supportActionBar?.setTitle("Filtrar Facturas")
    }

    private fun initComponents() {
        initCalendar()
        initSeekBar()
        initCheckBoxes()
        applySavedFilters()
        // Intenta cargar los filtros iniciales si existen
        val filtroJson = intent.getStringExtra(Constants.SEND_RECEIVE_FILTERS)
        if (filtroJson != null) {
            filter = Gson().fromJson(filtroJson, Filter::class.java)
            filter?.let { nonNullFilter ->
                loadFilters(nonNullFilter)
            }
        }
        Log.d("FiltroJSON", filtroJson.toString())

        binding.aplicar.setOnClickListener {
            updateAndSaveFilters()
            val gson = Gson()
            val maxValueSlider = binding.valorSeekBar.text.toString().toDouble()
            val state = hashMapOf(
                Constants.PAID_STRING to paid.isChecked,
                Constants.CANCELED_STRING to canceled.isChecked,
                Constants.FIXED_PAYMENT_STRING to fixedPayment.isChecked,
                Constants.PENDING_PAYMENT_STRING to pendingPayment.isChecked,
                Constants.PAYMENT_PLAN_STRING to paymentPlan.isChecked
            )

            val minDate = binding.fechaDesde.text.toString()
            val maxDate = binding.fechaHasta.text.toString()
            Log.d("CHECK", state.toString())
            Log.d("MAX", maxValueSlider.toString())
            Log.d("MINDATE", minDate)
            Log.d("MAXDATE", maxDate)
            val filter: com.example.listafacturaspractica.ui.view.Filter = Filter(maxDate, minDate, maxValueSlider, state)
            if (!minDate.equals("Dia/Mes/Año") && !maxDate.equals("Dia/Mes/Año")) {
                val miIntent = Intent(this, MainActivity::class.java)
                miIntent.putExtra(Constants.SEND_RECEIVE_FILTERS, gson.toJson(filter))
                startActivity(miIntent)
            } else {
                noDatePopUp()
            }
        }

        binding.eliminar.setOnClickListener {
            resetFilters()
        }
    }

    private fun noDatePopUp() {
        val fragmentManager = supportFragmentManager // Reemplaza con el FragmentManager adecuado
        val customPopupFragment =
            FragmentPopUp("Debe elegir entre qué fechas quiere filtrar las facturas.")
        customPopupFragment.show(fragmentManager, "FragmentPopUp")
    }



    private fun initCalendar() {
        //Declaración de los botones fechaDesde / fechaHasta.
        //Declaración de fechaDesde.
        binding.fechaDesde.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year1, monthOfYear, dayOfMonth ->
                    binding.fechaDesde.text = "$dayOfMonth/${monthOfYear + 1}/$year1"
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        //Declaración de fechaHasta.
        binding.fechaHasta.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year1, monthOfYear, dayOfMonth ->
                    binding.fechaHasta.text = "$dayOfMonth/${monthOfYear + 1}/$year1"
                },
                year,
                month,
                day
            )
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val minDateLocal = binding.fechaDesde.text.toString()
            val minDate: Date
            try {
                minDate = simpleDateFormat.parse(minDateLocal)
                datePickerDialog.datePicker.minDate = minDate.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            datePickerDialog.show()
        }
    }

    private fun initSeekBar() {
        //Declaración SeekBar.
        maxAmount = intent.getDoubleExtra("MAX_IMPORTE", 0.0).toInt() + 1
        binding.seekBar.max = maxAmount
        binding.tvMaxSeekbar.text = "${maxAmount}€"
        binding.tvMinSeekbar.text = "0€"
        binding.valorSeekBar.text = "${maxAmount}"
        binding.seekBar.progress = maxAmount


        //Configuración del Listener para el SeekBar.
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.valorSeekBar.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Sin función
                Log.d("onStartTrackingTouch()", "onStartTrackingTouch: el método ha fallado.")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Sin función
                Log.d("onStopTrackingTouch()", "onStopTrackingTouch: el método ha fallado.")
            }
        })
    }

    private fun initCheckBoxes() {
        paid = binding.cbPagadas
        canceled = binding.cbAnuladas
        fixedPayment = binding.cbCuotaFija
        pendingPayment = binding.cbPendientesPago
        paymentPlan = binding.cbPlanPago
    }

    //Método propio para la creación de un menú.
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_filter_activity, menu)
            return true
        }

        //Método propio para establecer el comportamiento del menú al pulsar sobre él.
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.invoiceMenuFilter -> {
                    //Intent que se usa para pasan de FilterActivity a MainActivity.
                    val miIntent = Intent(this, MainActivity::class.java)
                    startActivity(miIntent)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

    private fun applySavedFilters() {
        val prefs = getPreferences(MODE_PRIVATE)
        val filterJson = prefs.getString(Constants.FILTER_STATE, null)

        if (filterJson != null) {
            val gson = Gson()
            filter = gson.fromJson(filterJson, Filter::class.java)
            filter?.let { nonNullFilter ->
                loadFilters(nonNullFilter)
            }
        }
    }

    private fun saveFilterState(filter: Filter) {
        val prefs = getPreferences(MODE_PRIVATE)
        val gson = Gson()
        val filterJson = gson.toJson(filter)

        prefs.edit().putString(Constants.FILTER_STATE, filterJson).apply()
    }

    private fun loadFilters(filter: Filter) {
        binding.fechaDesde.text = filter.minDate
        binding.fechaHasta.text = filter.maxDate
        binding.seekBar.progress = filter.maxValueSlider.toInt()
        binding.cbPagadas.isChecked = filter.estate[Constants.PAID_STRING] ?: false
        binding.cbAnuladas.isChecked = filter.estate[Constants.CANCELED_STRING] ?: false
        binding.cbCuotaFija.isChecked = filter.estate[Constants.FIXED_PAYMENT_STRING] ?: false
        binding.cbPendientesPago.isChecked = filter.estate[Constants.PENDING_PAYMENT_STRING] ?: false
        binding.cbPlanPago.isChecked = filter.estate[Constants.PAYMENT_PLAN_STRING] ?: false
    }

    private fun updateAndSaveFilters() {
        // Actualiza los filtros
        val maxValueSlider = binding.valorSeekBar.text.toString().toDouble()
        val state = hashMapOf(
            Constants.PAID_STRING to paid.isChecked,
            Constants.CANCELED_STRING to canceled.isChecked,
            Constants.FIXED_PAYMENT_STRING to fixedPayment.isChecked,
            Constants.PENDING_PAYMENT_STRING to pendingPayment.isChecked,
            Constants.PAYMENT_PLAN_STRING to paymentPlan.isChecked
        )
        val minDate = binding.fechaDesde.text.toString()
        val maxDate = binding.fechaHasta.text.toString()
        filter = Filter(maxDate, minDate, maxValueSlider, state)

        // Guarda el estado de los filtros en las preferencias compartidas
        saveFilterState(filter!!)
    }

    private fun resetFilters() {
        maxAmount = intent.getDoubleExtra("MAX_IMPORTE", 0.0).toInt() + 1
        binding.fechaDesde.text = getString(R.string.botonDesde)
        binding.fechaHasta.text = getString(R.string.botonHasta)
        binding.seekBar.progress = maxAmount
        binding.cbPagadas.isChecked = false
        binding.cbAnuladas.isChecked = false
        binding.cbCuotaFija.isChecked = false
        binding.cbPendientesPago.isChecked = false
        binding.cbPlanPago.isChecked = false

    }

}