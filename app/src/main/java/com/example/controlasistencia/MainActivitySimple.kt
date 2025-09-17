package com.example.controlasistencia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.controlasistencia.MateriaActivity

class MainActivitySimple : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_simple)
        
        val btnMaterias: Button = findViewById(R.id.btnMaterias)
        val btnGrupos: Button = findViewById(R.id.btnGrupos)
        val btnAlumnos: Button = findViewById(R.id.btnAlumnos)
        
        btnMaterias.setOnClickListener {
            startActivity(Intent(this, MateriaActivity::class.java))
        }
        
        btnGrupos.setOnClickListener {
            startActivity(Intent(this, GrupoActivity::class.java))
        }

        btnAlumnos.setOnClickListener {
            startActivity(Intent(this, AlumnoActivity::class.java))
        }
    }
}