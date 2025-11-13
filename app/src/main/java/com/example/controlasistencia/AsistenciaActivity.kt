package com.example.controlasistencia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.controlasistencia.view.VAsistencia
import com.example.controlasistencia.controller.CAsistencia

class AsistenciaActivity : Activity() {

    private lateinit var view: VAsistencia
    private lateinit var controller: CAsistencia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)
        
        view = VAsistencia(this)
        controller = CAsistencia(this, view)
        controller.inicializar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controller.procesarResultadoEscaneo(requestCode, resultCode, data)
    }
}
