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
import com.google.gson.Gson
import java.util.Calendar

/**
 * Clase que representa la Activity de Filtros.
 */
class FilterActivity : AppCompatActivity() {

    /**
     * Binding para la vista de la Activity.
     */
    private lateinit var binding: ActivityFilterBinding
    private lateinit var filtrar: com.example.listafacturaspractica.ui.view.Filter
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

        binding.aplicar.setOnClickListener {
            val gson = Gson()
            val maxValueSlider = binding.valorSeekBar.text.toString().toDouble()
            val state = hashMapOf(
                "PAGADAS_STRING" to paid.isChecked,
                "ANULADAS_STRING" to canceled.isChecked,
                "CUOTA_FIJA_STRING" to fixedPayment.isChecked,
                "PENDIENTE_PAGO_STRING" to pendingPayment.isChecked,
                "PLAN_PAGO_STRING" to paymentPlan.isChecked
            )


            val minDate = binding.fechaDesde.text.toString()
            val maxDate = binding.fechaHasta.text.toString()
            Log.d("CHECK", state.toString())
            Log.d("MAX", maxValueSlider.toString())
            Log.d("MINDATE", minDate.toString())
            Log.d("MAXDATE", maxDate.toString())
            val filter: com.example.listafacturaspractica.ui.view.Filter = Filter(maxDate, minDate, maxValueSlider, state)
            val miIntent = Intent(this, MainActivity::class.java)
            miIntent.putExtra("FILTRO_ENVIAR_RECIBIR_DATOS", gson.toJson(filter))
            startActivity(miIntent)
        }


    }

    private fun initComponents() {
        initCalendar()
        initSeekBar()
        initCheckBoxes()
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
            datePickerDialog.show()
        }
    }

    private fun initSeekBar() {
        //Declaración SeekBar.
        var maxAmount = intent.getDoubleExtra("MAX_IMPORTE", 0.0).toInt() + 1
        binding.seekBar.max = maxAmount
        binding.tvMaxSeekbar.text = "${maxAmount}€"
        binding.tvMinSeekbar.text = "0€"
        binding.valorSeekBar.text = "${maxAmount}€"
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
}