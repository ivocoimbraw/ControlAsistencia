package com.example.controlasistencia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.controlasistencia.view.VAsistencia

class AsistenciaActivity : Activity() {

    private lateinit var view: VAsistencia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)
        
        view = VAsistencia(this)
        view.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        view.procesarResultadoQr(requestCode, resultCode, data)
    }
}
