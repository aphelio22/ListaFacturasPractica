package com.example.listafacturaspractica.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listafacturaspractica.R
import com.example.listafacturaspractica.databinding.ActivityMainBinding
import com.example.listafacturaspractica.ui.view.adapter.InvoiceAdapter
import com.example.listafacturaspractica.ui.view.adapter.InvoiceProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: InvoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFacturas.layoutManager = LinearLayoutManager(this)
        binding.rvFacturas.adapter =
            InvoiceAdapter(InvoiceProvider.listaInvoices)
    }
}