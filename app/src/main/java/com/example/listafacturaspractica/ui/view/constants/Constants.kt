package com.example.listafacturaspractica.ui.view.constants

class Constants {
    companion object {
        //Claves de los CheckBoxes.
        const val PAID_STRING: String = "PAID_STRING"
        const val CANCELED_STRING: String = "CANCELED_STRING"
        const val FIXED_PAYMENT_STRING: String = "FIXED_PAYMENT_STRING"
        const val PENDING_PAYMENT_STRING: String = "PENDING_PAYMENT_STRING"
        const val PAYMENT_PLAN_STRING: String = "PAYMENT_PLAN_STRING"

        /* Clave del Intent que se utiliza para enviar/recibir datos de los filtros
         * entre MainActivity y FilterActivity. */
        const val SEND_RECEIVE_FILTERS: String = "SEND_RECEIVE_FILTERS"
    }
}