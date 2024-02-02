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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
     * Binding para la vista de la activity.
     */
    private lateinit var binding: ActivityFilterBinding

    /**
     * Instancia de la clase Filter.
     */
    private var filter: Filter? = null

    /**
     * Importe máximo de las facturas.
     */
    private var maxAmount: Int = 0

    /**
     * CheckBox de facturas pagadas.
     */
    private lateinit var paid: CheckBox

    /**
     * CheckBox de facturas canceladas.
     */
    private lateinit var canceled: CheckBox

    /**
     * CheckBox de facturas con cuota fija.
     */
    private lateinit var fixedPayment: CheckBox

    /**
     * CheckBox de facturas pendientes de pago.
     */
    private lateinit var pendingPayment: CheckBox

    /**
     * CheckBox de facturas con plan de pago.
     */
    private lateinit var paymentPlan: CheckBox

    private lateinit var intentLaunch: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configura el título de la ToolBar en 'Filtrar Facturas'.
        supportActionBar?.setTitle("Filtrar Facturas")

        initComponents()
        applySavedFilters()
        intentLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val maxImporte = result.data?.extras?.getDouble(Constants.MAX_AMOUNT) ?: 0.0
                    val filtroJson = result.data?.extras?.getString(Constants.SEND_RECEIVE_FILTERS)
                    if (filtroJson != null) {
                        val gson = Gson()
                        val objFiltro = gson.fromJson(filtroJson, Filter::class.java)
                        // Hacer lo que sea necesario con objFiltro y maxImporte
                    }
                }
            }
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     */
    private fun initComponents() {
        initCalendar()
        initSeekBar()
        initCheckBoxes()
        initApplyFiltersButton()
        initResetFilterButton()
    }

    /**
     * Inicializa el calendario con los botones de fecha mínima y fecha máxima.
     */
    private fun initCalendar() {
        initMinDateButton()
        initMaxDateButton()
    }

    /**
     * Inicializa el botón de fecha mínima.
     */
    private fun initMinDateButton() {
        binding.minDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year1, monthOfYear, dayOfMonth ->
                    binding.minDate.text = "$dayOfMonth/${monthOfYear + 1}/$year1"
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    /**
     * Inicializa el botón de fecha máxima.
     */
    private fun initMaxDateButton() {
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
            val minDateLocal = binding.minDate.text.toString()
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

    /**
     * Inicializa la SeekBar.
     */
    private fun initSeekBar() {
        //Declaración SeekBar.
        maxAmount = intent.getDoubleExtra(Constants.MAX_AMOUNT, 0.0).toInt() + 1
        binding.seekBar.max = maxAmount
        binding.tvMaxSeekbar.text = "${maxAmount}"
        binding.tvMinSeekbar.text = "0"
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

    /**
     * Inicializa los CheckBoxes.
     */
    private fun initCheckBoxes() {
        paid = binding.cbPagadas
        canceled = binding.cbAnuladas
        fixedPayment = binding.cbCuotaFija
        pendingPayment = binding.cbPendientesPago
        paymentPlan = binding.cbPlanPago
    }

    /**
     * Iniciliza el botón de resetear filtros.
     */
    private fun initResetFilterButton() {
        binding.eliminar.setOnClickListener {
            resetFilters()
        }
    }

    /**
     * Coge los filtros guardados que se cargan desde 'loadFilters()' y los aplica a los
     * filtros que recibe desde MainActivity.
     */
    private fun applySavedFilters() {
        val prefs = getPreferences(MODE_PRIVATE)
        val filterJson = prefs.getString(Constants.FILTER_STATE, null)

        if (filterJson != null) {
            val gson = Gson()
            filter = gson.fromJson(filterJson, Filter::class.java)
            Log.d("FILTROS", filter.toString())
            filter?.let { nonNullFilter ->
                loadFilters(nonNullFilter)
            }
        }
    }

    /**
     * Guarda el estado de los filtros.
     */
    private fun saveFilterState(filter: Filter) {
        val prefs = getPreferences(MODE_PRIVATE)
        val gson = Gson()
        val filterJson = gson.toJson(filter)

        prefs.edit().putString(Constants.FILTER_STATE, filterJson).apply()
    }

    /**
     * Carga los filtros guardados.
     */
    private fun loadFilters(filter: Filter) {
        binding.minDate.text = filter.minDate
        binding.fechaHasta.text = filter.maxDate
        binding.seekBar.progress = filter.maxValueSlider.toInt()
        binding.cbPagadas.isChecked = filter.estate[Constants.PAID_STRING] ?: false
        binding.cbAnuladas.isChecked = filter.estate[Constants.CANCELED_STRING] ?: false
        binding.cbCuotaFija.isChecked = filter.estate[Constants.FIXED_PAYMENT_STRING] ?: false
        binding.cbPendientesPago.isChecked = filter.estate[Constants.PENDING_PAYMENT_STRING] ?: false
        binding.cbPlanPago.isChecked = filter.estate[Constants.PAYMENT_PLAN_STRING] ?: false
    }

    /**
     * Actualiza y guarda el estado de los filtros usando el método 'saveFilterState()'.
     */
    private fun updateAndSaveFilters() {
        //Actualiza los filtros.
        val maxValueSlider = binding.valorSeekBar.text.toString().toDouble()
        val state = hashMapOf(
            Constants.PAID_STRING to paid.isChecked,
            Constants.CANCELED_STRING to canceled.isChecked,
            Constants.FIXED_PAYMENT_STRING to fixedPayment.isChecked,
            Constants.PENDING_PAYMENT_STRING to pendingPayment.isChecked,
            Constants.PAYMENT_PLAN_STRING to paymentPlan.isChecked
        )
        val minDate = binding.minDate.text.toString()
        val maxDate = binding.fechaHasta.text.toString()
        filter = Filter(maxDate, minDate, maxValueSlider, state)

        //Guarda el estado de los filtros en las preferencias compartidas.
        saveFilterState(filter!!)
    }

    /**
     * Inicializa el botón para aplicar los filtros.
     */
    private fun initApplyFiltersButton() {
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

            val minDate = binding.minDate.text.toString()
            val maxDate = binding.fechaHasta.text.toString()



            if (!minDate.equals("Dia/Mes/Año") && !maxDate.equals("Dia/Mes/Año")) {
                val filter: Filter = Filter(maxDate, minDate, maxValueSlider, state)
                val miIntent = Intent(this, MainActivity::class.java)
                miIntent.putExtra(Constants.SEND_RECEIVE_FILTERS, gson.toJson(filter))
                //setResult(RESULT_OK, miIntent)
                intentLaunch.launch(miIntent)
                finish()
            } else { //Si alguna de las dos fechas, o las dos, no equivale a "Dia/Mes/Año" se realiza el intent, sino salta el PopUp.
                noDatePopUp()
            }
        }
    }

    /**
     * Llama al PopUp pasándole un mensaje cuando no ha sido seleccionada alguna de las dos del intervalo o ninguna.
     */
    private fun noDatePopUp() {
        val fragmentManager = supportFragmentManager // Reemplaza con el FragmentManager adecuado
        val customPopupFragment =
            FragmentPopUp("Debe elegir entre qué fechas quiere filtrar las facturas.")
        customPopupFragment.show(fragmentManager, "FragmentPopUp")
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

    /**
     * Método usado para resetear los filtros.
     */
    private fun resetFilters() {
        maxAmount = intent.getDoubleExtra(Constants.MAX_AMOUNT, 0.0).toInt() + 1
        binding.minDate.text = getString(R.string.dayMonthYear)
        binding.fechaHasta.text = getString(R.string.dayMonthYear)
        binding.seekBar.progress = maxAmount
        binding.cbPagadas.isChecked = false
        binding.cbAnuladas.isChecked = false
        binding.cbCuotaFija.isChecked = false
        binding.cbPendientesPago.isChecked = false
        binding.cbPlanPago.isChecked = false

    }
}