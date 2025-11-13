package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.model.MAlumno
import com.google.zxing.integration.android.IntentIntegrator

class VAsistencia(private val activity: Activity) {
    
    private lateinit var spEstudiantes: Spinner
    private lateinit var btnEscanearQr: Button
    private lateinit var btnIngresarManual: Button
    private lateinit var tvResultado: TextView
    private lateinit var lvAsistencias: ListView
    
    private var estudianteSeleccionado: MAlumno? = null
    private var estudiantes: List<MAlumno> = emptyList()
    
    // Callbacks para el controlador
    private var onEstudianteSeleccionado: ((MAlumno?) -> Unit)? = null
    private var onEscanearQrClick: (() -> Unit)? = null
    private var onIngresarManualClick: (() -> Unit)? = null

    fun inicializarComponentes() {
        spEstudiantes = activity.findViewById(R.id.spEstudiantes)
        btnEscanearQr = activity.findViewById(R.id.btnEscanearQr)
        btnIngresarManual = activity.findViewById(R.id.btnIngresarManual)
        tvResultado = activity.findViewById(R.id.tvResultado)
        lvAsistencias = activity.findViewById(R.id.lvAsistencias)
        
        // Inicialmente botones deshabilitados
        btnEscanearQr.isEnabled = false
        btnIngresarManual.isEnabled = false
    }
    
    fun mostrarEstudiantes(estudiantes: List<MAlumno>) {
        this.estudiantes = estudiantes
        val estudianteNombres = estudiantes.map { "${it.nombre} ${it.apellidop} ${it.apellidom}" }
        
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, estudianteNombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstudiantes.adapter = adapter
        
        spEstudiantes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position >= 0 && position < estudiantes.size) {
                    estudianteSeleccionado = estudiantes[position]
                } else {
                    estudianteSeleccionado = null
                }
                onEstudianteSeleccionado?.invoke(estudianteSeleccionado)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                estudianteSeleccionado = null
                onEstudianteSeleccionado?.invoke(null)
            }
        }
    }
    // Métodos para configurar callbacks desde el controlador
    fun setOnEstudianteSeleccionado(callback: (MAlumno?) -> Unit) {
        this.onEstudianteSeleccionado = callback
    }
    
    fun setOnEscanearQrClick(callback: () -> Unit) {
        this.onEscanearQrClick = callback
        btnEscanearQr.setOnClickListener { callback() }
    }
    
    fun setOnIngresarManualClick(callback: () -> Unit) {
        this.onIngresarManualClick = callback
        btnIngresarManual.setOnClickListener { callback() }
    }
    
    fun iniciarEscaneoQr() {
        val integrator = IntentIntegrator(activity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR de la clase")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }
    
    // Métodos para obtener información del estado actual
    fun obtenerEstudianteSeleccionado(): MAlumno? {
        return estudianteSeleccionado
    }
    
    fun habilitarBotones(habilitar: Boolean) {
        btnEscanearQr.isEnabled = habilitar
        btnIngresarManual.isEnabled = habilitar
    }
    
    fun mostrarResultado(mensaje: String, exito: Boolean) {
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
    
    fun mostrarAsistencias(asistenciaTextos: List<String>) {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, asistenciaTextos)
        lvAsistencias.adapter = adapter
    }
    
    fun mostrarDialogoQrManual(onQrIngresado: (String) -> Unit) {
        val builder = android.app.AlertDialog.Builder(activity)
        val input = EditText(activity)
        input.hint = "Ej: CLASE_1_1234567890123"
        
        builder.setTitle("Ingreso Manual de QR")
        builder.setMessage("Ingresa el código QR manualmente:")
        builder.setView(input)
        
        builder.setPositiveButton("OK") { _, _ ->
            val qrCode = input.text.toString()
            if (qrCode.isNotEmpty()) {
                onQrIngresado(qrCode)
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