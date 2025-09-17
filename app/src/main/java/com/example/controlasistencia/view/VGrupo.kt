package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CGrupo
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.model.MMateria

class VGrupo(private val activity: Activity) {
    
    private val controller = CGrupo(activity, this)
    private val txtNombre: EditText = activity.findViewById(R.id.txtNombreGrupo)
    private val slcMateria: Spinner = activity.findViewById(R.id.slcMateria)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregar)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminar)
    private val lvGrupos: ListView = activity.findViewById(R.id.lvGrupos)
    
    private var grupoEditando: MGrupo? = null
    private var adapter: ArrayAdapter<String>? = null
    private var materias: List<MMateria> = emptyList()
    
    init {
        controller.actualizarVista()
        setupListeners()
    }
    
    private fun setupListeners() {
        btnGuardar.setOnClickListener { guardarGrupo() }
        btnAgregar.setOnClickListener { controller.mostrarCrear() }
        btnEliminar.setOnClickListener { eliminarGrupo() }
        
        lvGrupos.setOnItemClickListener { _, _, position, _ ->
            val grupoTexto = adapter?.getItem(position) ?: ""
            val id = grupoTexto.split(" - ")[0].toInt()
            val grupo = controller.obtenerGrupo( id)
            grupo?.let { controller.mostrarEditar(it) }
        }
    }
    
    fun mostrarGrupos(grupos: List<MGrupo>) {
        val items = grupos.map { "${it.id} - ${it.nombre} (${controller.obtenerMateria(it.id_materia)})" }
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lvGrupos.adapter = adapter
    }
    
    fun mostrarMaterias(materias: List<MMateria>) {
        this.materias = materias
        val items = materias.map { it.nombre }
        val spinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        slcMateria.adapter = spinnerAdapter
    }
    
    fun mostrarFormularioCrear() {
        txtNombre.setText("")
        slcMateria.setSelection(0)
        grupoEditando = null
        btnEliminar.isEnabled = false
    }
    
    fun mostrarFormularioEditar(grupo: MGrupo) {
        txtNombre.setText(grupo.nombre)
        val materiaIndex = materias.indexOfFirst { it.id == grupo.id_materia }
        if (materiaIndex >= 0) slcMateria.setSelection(materiaIndex)
        grupoEditando = grupo
        btnEliminar.isEnabled = true
    }
    
    private fun guardarGrupo() {
        val nombre = txtNombre.text.toString()
        val materiaSeleccionada = materias[slcMateria.selectedItemPosition]
        
        if (grupoEditando != null) {
            controller.actualizarGrupo(grupoEditando!!, nombre, materiaSeleccionada.id)
        } else {
            controller.crearGrupo(nombre, materiaSeleccionada.id)
        }
        limpiarFormulario()
    }
    
    private fun eliminarGrupo() {
        grupoEditando?.let {
            controller.eliminarGrupo(it)
            limpiarFormulario()
        }
    }
    
    private fun limpiarFormulario() {
        txtNombre.setText("")
        slcMateria.setSelection(0)
        grupoEditando = null
        btnEliminar.isEnabled = false
    }
}