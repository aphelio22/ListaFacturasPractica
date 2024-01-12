package com.example.listafacturaspractica.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityMainBinding
import com.example.listafacturaspractica.data.database.Invoice
import com.example.listafacturaspractica.ui.view.adapter.InvoiceAdapter
import com.example.listafacturaspractica.ui.viewmodel.InvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var invoiceAdapter: InvoiceAdapter

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
            }
        })
    }

    private fun onItemSelected(invoice: Invoice) {
        val fragmentManager = supportFragmentManager // Reemplaza con el FragmentManager adecuado
        val customPopupFragment = FragmentPopUp()
        customPopupFragment.show(fragmentManager, "FragmentPopUp")
    }
}