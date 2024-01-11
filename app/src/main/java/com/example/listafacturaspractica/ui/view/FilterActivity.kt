package com.example.listafacturaspractica.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityFilterBinding
import com.example.listafacturaspractica.databinding.ActivityMainBinding

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle("Filtrar Facturas")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.invoiceMenuFilter -> {
                val miIntent = Intent(this, MainActivity::class.java)
                startActivity(miIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}