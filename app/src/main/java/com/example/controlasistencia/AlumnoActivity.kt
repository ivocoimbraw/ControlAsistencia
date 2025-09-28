package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VAlumno
import com.example.controlasistencia.controller.CAlumno

class AlumnoActivity : Activity() {

    private lateinit var view: VAlumno
    private lateinit var controller: CAlumno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        view = VAlumno(this)
        controller = CAlumno(this, view)
        controller.inicializar()
    }
}