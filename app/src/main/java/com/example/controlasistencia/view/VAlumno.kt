package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.model.MAlumno

class VAlumno(private val activity: Activity) {

    private val txtRegistro: EditText = activity.findViewById(R.id.txtRegistroAlumno)
    private val txtApellidoP: EditText = activity.findViewById(R.id.txtApellidoPAlumno)
    private val txtApellidoM: EditText = activity.findViewById(R.id.txtApellidoMAlumno)
    private val txtNombre: EditText = activity.findViewById(R.id.txtNombreAlumno)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardarAlumno)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregarAlumno)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminarAlumno)
    private val lvAlumnos: ListView = activity.findViewById(R.id.lvAlumnos)

    private var alumnoEditando: MAlumno? = null
    private var adapter: ArrayAdapter<String>? = null
    private var alumnos: List<MAlumno> = emptyList()
    
    // Callbacks
    private var onGuardarClick: (() -> Unit)? = null
    private var onAgregarClick: (() -> Unit)? = null
    private var onEliminarClick: (() -> Unit)? = null
    private var onAlumnoSeleccionado: ((MAlumno) -> Unit)? = null

    init {
        setupListeners()
    }

    private fun setupListeners() {
        btnGuardar.setOnClickListener { onGuardarClick?.invoke() }
        btnAgregar.setOnClickListener { onAgregarClick?.invoke() }
        btnEliminar.setOnClickListener { onEliminarClick?.invoke() }

        lvAlumnos.setOnItemClickListener { _, _, position, _ ->
            val alumnoTexto = adapter?.getItem(position) ?: ""
            val registro = alumnoTexto.split(" - ")[0]
            val alumno = obtenerAlumnoPorRegistro(registro)
            alumno?.let { onAlumnoSeleccionado?.invoke(it) }
        }
    }
    
    // Métodos para configurar callbacks
    fun setOnGuardarClick(callback: () -> Unit) { onGuardarClick = callback }
    fun setOnAgregarClick(callback: () -> Unit) { onAgregarClick = callback }
    fun setOnEliminarClick(callback: () -> Unit) { onEliminarClick = callback }
    fun setOnAlumnoSeleccionado(callback: (MAlumno) -> Unit) { onAlumnoSeleccionado = callback }
    
    // Métodos para obtener datos del formulario
    fun getRegistro(): String = txtRegistro.text.toString()
    fun getApellidoP(): String = txtApellidoP.text.toString()
    fun getApellidoM(): String = txtApellidoM.text.toString()
    fun getNombre(): String = txtNombre.text.toString()
    fun getAlumnoEditando(): MAlumno? = alumnoEditando
    
    private fun obtenerAlumnoPorRegistro(registro: String): MAlumno? {
        return alumnos.find { it.registro == registro }
    }

    fun mostrarAlumnos(alumnos: List<MAlumno>) {
        this.alumnos = alumnos
        val items = alumnos.map { "${it.registro} - ${it.nombre} ${it.apellidop} ${it.apellidom}" }
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lvAlumnos.adapter = adapter
    }

    fun mostrarFormularioCrear() {
        txtRegistro.setText("")
        txtApellidoP.setText("")
        txtApellidoM.setText("")
        txtNombre.setText("")
        alumnoEditando = null
        btnEliminar.isEnabled = false
    }

    fun mostrarFormularioEditar(alumno: MAlumno) {
        txtRegistro.setText(alumno.registro)
        txtApellidoP.setText(alumno.apellidop)
        txtApellidoM.setText(alumno.apellidom)
        txtNombre.setText(alumno.nombre)
        alumnoEditando = alumno
        btnEliminar.isEnabled = true
    }

    fun limpiarFormulario() {
        txtRegistro.setText("")
        txtApellidoP.setText("")
        txtApellidoM.setText("")
        txtNombre.setText("")
        alumnoEditando = null
        btnEliminar.isEnabled = false
    }
}