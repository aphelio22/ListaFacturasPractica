package com.example.listafacturaspractica.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.listafacturaspractica.databinding.PopUpBinding

class FragmentPopUp: DialogFragment() {
    private var _binding: PopUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = PopUpBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        // Configura el comportamiento de los elementos de diseño
        binding.botonCerrarPopUp.setOnClickListener {
            dismiss() // Cierra el pop-up al hacer clic en el botón "Cerrar"
        }

        val dialog = builder.create()
        val window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent) // Fondo transparente
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}