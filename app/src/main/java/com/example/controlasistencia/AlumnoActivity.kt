package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VAlumno
import com.example.controlasistencia.controller.CAlumno
import com.example.controlasistencia.data.alumno.AlumnoSqliteDataSource
import com.example.controlasistencia.data.alumno.CachingAlumnoDataSource
import com.example.controlasistencia.data.alumno.LoggingAlumnoDataSource

class AlumnoActivity : Activity() {

    private lateinit var view: VAlumno
    private lateinit var controller: CAlumno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        view = VAlumno(this)
        val base = AlumnoSqliteDataSource(this)
        val cached = CachingAlumnoDataSource(base)
        val decorated = LoggingAlumnoDataSource(cached)
        controller = CAlumno(this, view, decorated)
        controller.inicializar()
    }
}