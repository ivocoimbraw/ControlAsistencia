package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VGrupo
import com.example.controlasistencia.controller.CGrupo

class GrupoActivity : Activity() {
    
    private lateinit var view: VGrupo
    private lateinit var controller: CGrupo
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)

        view = VGrupo(this)
        controller = CGrupo(this, view)
        controller.inicializar()
    }
}