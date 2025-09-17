package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CMateriaController
import com.example.controlasistencia.model.MMateria

class VMateriaView(private val activity: Activity) {
    
    private val controller = CMateriaController(activity, this)
    private val txtNombre: EditText = activity.findViewById(R.id.txtNombreMateria)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregar)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminar)
    private val lvMaterias: ListView = activity.findViewById(R.id.lvMaterias)
    
    private var materiaEditando: MMateria? = null
    private var adapter: ArrayAdapter<String>? = null
    
    init {
        controller.actualizarVista()
        setupListeners()
    }
    
    private fun setupListeners() {
        btnGuardar.setOnClickListener { guardarMateria() }
        btnAgregar.setOnClickListener { controller.mostrarCrear() }
        btnEliminar.setOnClickListener { eliminarMateria() }
        
        lvMaterias.setOnItemClickListener { _, _, position, _ ->
            val materiaTexto = adapter?.getItem(position) ?: ""
            val id = materiaTexto.split(" - ")[0].toInt()
            val materia = controller.obtenerMateria( id)
            materia?.let { controller.mostrarEditar(it) }
        }
    }
    
    fun mostrarMaterias(materias: List<MMateria>) {
        val items = materias.map { "${it.id} - ${it.nombre}" }
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lvMaterias.adapter = adapter
    }
    
    fun mostrarFormularioCrear() {
        txtNombre.setText("")
        materiaEditando = null
        btnEliminar.isEnabled = false
    }
    
    fun mostrarFormularioEditar(materia: MMateria) {
        txtNombre.setText(materia.nombre)
        materiaEditando = materia
        btnEliminar.isEnabled = true
    }
    
    private fun guardarMateria() {
        val nombre = txtNombre.text.toString()
        if (materiaEditando != null) {
            controller.actualizarMateria(materiaEditando!!, nombre)
        } else {
            controller.crearMateria(nombre)
        }
        limpiarFormulario()
    }
    
    private fun eliminarMateria() {
        materiaEditando?.let {
            controller.eliminarMateria(it)
            limpiarFormulario()
        }
    }
    
    private fun limpiarFormulario() {
        txtNombre.setText("")
        materiaEditando = null
        btnEliminar.isEnabled = false
    }
}