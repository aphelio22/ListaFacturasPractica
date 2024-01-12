package com.example.listafacturaspractica.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityFilterBinding
import com.example.listafacturaspractica.databinding.ActivityMainBinding

/**
 * Clase que representa la Activity de Filtros.
 */
class FilterActivity : AppCompatActivity() {

    /**
     * Binding para la vista de la Activity.
     */
    private lateinit var binding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configura el título de la ToolBar.
        supportActionBar?.setTitle("Filtrar Facturas")
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