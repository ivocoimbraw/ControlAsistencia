package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VClase
import com.example.controlasistencia.controller.CClase

class ClaseActivity : Activity() {

    private lateinit var view: VClase
    private lateinit var controller: CClase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clase)

        view = VClase(this)
        controller = CClase(this, view)
        controller.inicializar()
    }
}