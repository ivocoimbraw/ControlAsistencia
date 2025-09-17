package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CAlumno
import com.example.controlasistencia.model.MAlumno

class VAlumno(private val activity: Activity) {

    private val controller = CAlumno(activity, this)
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

    init {
        controller.actualizarVista()
        setupListeners()
    }

    private fun setupListeners() {
        btnGuardar.setOnClickListener { guardarAlumno() }
        btnAgregar.setOnClickListener { controller.mostrarCrear() }
        btnEliminar.setOnClickListener { eliminarAlumno() }

        lvAlumnos.setOnItemClickListener { _, _, position, _ ->
            val alumnoTexto = adapter?.getItem(position) ?: ""
            val registro = alumnoTexto.split(" - ")[0]
            val alumno = controller.obtenerAlumno(registro)
            alumno?.let { controller.mostrarEditar(it) }
        }
    }

    fun mostrarAlumnos(alumnos: List<MAlumno>) {
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

    private fun guardarAlumno() {
        val registro = txtRegistro.text.toString()
        val apellidop = txtApellidoP.text.toString()
        val apellidom = txtApellidoM.text.toString()
        val nombre = txtNombre.text.toString()

        if (alumnoEditando != null) {
            controller.actualizarAlumno(alumnoEditando!!, registro, apellidop, apellidom, nombre)
        } else {
            controller.crearAlumno(registro, apellidop, apellidom, nombre)
        }
        limpiarFormulario()
    }

    private fun eliminarAlumno() {
        alumnoEditando?.let {
            controller.eliminarAlumno(it)
            limpiarFormulario()
        }
    }

    private fun limpiarFormulario() {
        txtRegistro.setText("")
        txtApellidoP.setText("")
        txtApellidoM.setText("")
        txtNombre.setText("")
        alumnoEditando = null
        btnEliminar.isEnabled = false
    }
}