package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.model.MMateria

class VGrupo(private val activity: Activity) {

    private val txtNombre: EditText = activity.findViewById(R.id.txtNombreGrupo)
    private val slcMateria: Spinner = activity.findViewById(R.id.slcMateria)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregar)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminar)
    private val lvGrupos: ListView = activity.findViewById(R.id.lvGrupos)
    
    private var grupoEditando: MGrupo? = null
    private var adapter: ArrayAdapter<String>? = null
    private var materias: List<MMateria> = emptyList()
    
    // Callbacks
    private var onGuardarClick: (() -> Unit)? = null
    private var onAgregarClick: (() -> Unit)? = null
    private var onEliminarClick: (() -> Unit)? = null
    private var onGrupoSeleccionado: ((MGrupo) -> Unit)? = null
    private var obtenerMateria: ((Int) -> String)? = null
    
    init {
        setupListeners()
    }
    
    private fun setupListeners() {
        btnGuardar.setOnClickListener { onGuardarClick?.invoke() }
        btnAgregar.setOnClickListener { onAgregarClick?.invoke() }
        btnEliminar.setOnClickListener { onEliminarClick?.invoke() }
        
        lvGrupos.setOnItemClickListener { _, _, position, _ ->
            val grupoTexto = adapter?.getItem(position) ?: ""
            val id = grupoTexto.split(" - ")[0].toInt()
            // Necesitamos obtener el grupo de alguna manera
            val grupo = obtenerGrupoPorId(id)
            grupo?.let { onGrupoSeleccionado?.invoke(it) }
        }
    }
    
    // Métodos para configurar callbacks
    fun setOnGuardarClick(callback: () -> Unit) { onGuardarClick = callback }
    fun setOnAgregarClick(callback: () -> Unit) { onAgregarClick = callback }
    fun setOnEliminarClick(callback: () -> Unit) { onEliminarClick = callback }
    fun setOnGrupoSeleccionado(callback: (MGrupo) -> Unit) { onGrupoSeleccionado = callback }
    fun setObtenerMateria(callback: (Int) -> String) { obtenerMateria = callback }
    
    // Métodos para obtener datos del formulario
    fun getNombre(): String = txtNombre.text.toString()
    fun getMateriaSeleccionada(): MMateria = materias[slcMateria.selectedItemPosition]
    fun getGrupoEditando(): MGrupo? = grupoEditando
    
    // Lista para almacenar los grupos y poder obtenerlos por ID
    private var grupos: List<MGrupo> = emptyList()
    
    private fun obtenerGrupoPorId(id: Int): MGrupo? {
        return grupos.find { it.id == id }
    }
    
    fun mostrarGrupos(grupos: List<MGrupo>) {
        this.grupos = grupos
        val items = grupos.map { 
            val materia = obtenerMateria?.invoke(it.id_materia) ?: ""
            "${it.id} - ${it.nombre} ($materia)" 
        }
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
    
    fun limpiarFormulario() {
        txtNombre.setText("")
        slcMateria.setSelection(0)
        grupoEditando = null
        btnEliminar.isEnabled = false
    }

}