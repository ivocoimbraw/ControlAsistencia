package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VAlumno
import com.example.controlasistencia.controller.CAlumno
import com.example.controlasistencia.data.alumno.AlumnoDataSource
import com.example.controlasistencia.data.alumno.AlumnoSqliteDataSource
import com.example.controlasistencia.data.alumno.CompressionDecorator
import com.example.controlasistencia.data.alumno.EncryptionDecorator

class AlumnoActivity : Activity() {

    private lateinit var view: VAlumno
    private lateinit var controller: CAlumno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        view = VAlumno(this)
        
        var source: AlumnoDataSource = AlumnoSqliteDataSource(this)
        source = CompressionDecorator(source)
        source = EncryptionDecorator(source)
        
        controller = CAlumno(this, view, source)
        controller.inicializar()
    }
}