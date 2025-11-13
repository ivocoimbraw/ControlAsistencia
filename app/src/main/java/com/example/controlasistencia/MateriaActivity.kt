package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VMateriaView
import com.example.controlasistencia.controller.CMateriaController

class MateriaActivity : Activity() {
    
    private lateinit var view: VMateriaView
    private lateinit var controller: CMateriaController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materia)
        
        view = VMateriaView(this)
        controller = CMateriaController(this, view)
        controller.inicializar()
    }
}