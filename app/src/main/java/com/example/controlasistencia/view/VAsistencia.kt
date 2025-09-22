package com.example.controlasistencia.view

import android.app.Activity
import android.content.Intent
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CAsistencia
import com.example.controlasistencia.model.MAlumno
import com.google.zxing.integration.android.IntentIntegrator


class VAsistencia(private val activity: Activity) {

    private val controller = CAsistencia(activity)
    
    private lateinit var spEstudiantes: Spinner
    private lateinit var btnEscanearQr: Button
    private lateinit var btnIngresarManual: Button
    private lateinit var tvResultado: TextView
    private lateinit var lvAsistencias: ListView
    
    private var estudianteSeleccionado: MAlumno? = null
    private var estudiantes: List<MAlumno> = emptyList()

    fun init() {
        spEstudiantes = activity.findViewById(R.id.spEstudiantes)
        btnEscanearQr = activity.findViewById(R.id.btnEscanearQr)
        btnIngresarManual = activity.findViewById(R.id.btnIngresarManual)
        tvResultado = activity.findViewById(R.id.tvResultado)
        lvAsistencias = activity.findViewById(R.id.lvAsistencias)
        
        setupEstudiantes()
        setupListeners()
        cargarAsistencias()
    }
    
    private fun setupEstudiantes() {
        estudiantes = MAlumno.listar(activity)
        val estudianteNombres = estudiantes.map { "${it.nombre} ${it.apellidop} ${it.apellidom}" }
        
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, estudianteNombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstudiantes.adapter = adapter
        
        spEstudiantes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position >= 0 && position < estudiantes.size) {
                    estudianteSeleccionado = estudiantes[position]
                    btnEscanearQr.isEnabled = true
                    btnIngresarManual.isEnabled = true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                estudianteSeleccionado = null
                btnEscanearQr.isEnabled = false
                btnIngresarManual.isEnabled = false
            }
        }
    }
    
    private fun setupListeners() {
        btnEscanearQr.setOnClickListener {
            iniciarEscaneoQr()
        }
        
        btnIngresarManual.setOnClickListener {
            mostrarDialogoQrManual()
        }
    }
    
    private fun iniciarEscaneoQr() {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR de la clase")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }
    
    fun procesarResultadoQr(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                mostrarResultado("Escaneo cancelado", false)
            } else {
                val qrCode = result.contents
                procesarAsistencia(qrCode)
            }
        }
    }
    
    private fun procesarAsistencia(qrCode: String) {
        estudianteSeleccionado?.let { estudiante ->
            val exito = controller.marcarAsistencia(qrCode, estudiante.registro)
            if (exito) {
                mostrarResultado("¡Asistencia marcada exitosamente!", true)
                cargarAsistencias()
            } else {
                mostrarResultado("Error: Código QR inválido o clase no encontrada", false)
            }
        }
    }
    
    private fun mostrarResultado(mensaje: String, exito: Boolean) {
        tvResultado.text = mensaje
        tvResultado.setBackgroundColor(
            if (exito) android.graphics.Color.parseColor("#4CAF50")
            else android.graphics.Color.parseColor("#F44336")
        )
        tvResultado.setTextColor(android.graphics.Color.WHITE)
        tvResultado.visibility = android.view.View.VISIBLE
        
        // Ocultar el mensaje después de 3 segundos
        tvResultado.postDelayed({
            tvResultado.visibility = android.view.View.GONE
        }, 3000)
    }
    
    private fun cargarAsistencias() {
        val asistencias = controller.obtenerAsistencias()
        val asistenciaTextos = asistencias.map { asistencia ->
            val estudiante = estudiantes.find { it.registro == asistencia.idEstudiante }
            val nombreEstudiante = estudiante?.let { "${it.nombre} ${it.apellidop}" } ?: asistencia.idEstudiante
            "Clase ID: ${asistencia.idClase} - $nombreEstudiante - ${asistencia.fechaHoraRegistro}"
        }
        
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, asistenciaTextos)
        lvAsistencias.adapter = adapter
    }
    
    private fun mostrarDialogoQrManual() {
        val builder = android.app.AlertDialog.Builder(activity)
        val input = EditText(activity)
        input.hint = "Ej: CLASE_1_1234567890123"
        
        builder.setTitle("Ingreso Manual de QR")
        builder.setMessage("Ingresa el código QR manualmente:")
        builder.setView(input)
        
        builder.setPositiveButton("OK") { _, _ ->
            val qrCode = input.text.toString()
            if (qrCode.isNotEmpty()) {
                procesarAsistencia(qrCode)
            } else {
                mostrarResultado("Código QR vacío", false)
            }
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
            mostrarResultado("Escaneo cancelado", false)
        }
        
        builder.show()
    }
    
    // Métodos requeridos por el patrón (no se usan en este contexto)
    fun mostrarAlumnos(alumnos: List<MAlumno>) {}
    fun mostrarFormularioCrear() {}
    fun mostrarFormularioEditar(alumno: MAlumno) {}
}