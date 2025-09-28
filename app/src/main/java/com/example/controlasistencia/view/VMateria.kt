package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.model.MMateria

class VMateriaView(private val activity: Activity) {
    
    private val txtNombre: EditText = activity.findViewById(R.id.txtNombreMateria)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregar)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminar)
    private val lvMaterias: ListView = activity.findViewById(R.id.lvMaterias)
    
    private var materiaEditando: MMateria? = null
    private var adapter: ArrayAdapter<String>? = null
    private var materias: List<MMateria> = emptyList()
    
    // Callbacks
    private var onGuardarClick: (() -> Unit)? = null
    private var onAgregarClick: (() -> Unit)? = null
    private var onEliminarClick: (() -> Unit)? = null
    private var onMateriaSeleccionada: ((MMateria) -> Unit)? = null
    
    init {
        setupListeners()
    }
    
    private fun setupListeners() {
        btnGuardar.setOnClickListener { onGuardarClick?.invoke() }
        btnAgregar.setOnClickListener { onAgregarClick?.invoke() }
        btnEliminar.setOnClickListener { onEliminarClick?.invoke() }
        
        lvMaterias.setOnItemClickListener { _, _, position, _ ->
            val materiaTexto = adapter?.getItem(position) ?: ""
            val id = materiaTexto.split(" - ")[0].toInt()
            val materia = obtenerMateriaPorId(id)
            materia?.let { onMateriaSeleccionada?.invoke(it) }
        }
    }
    
    // Métodos para configurar callbacks
    fun setOnGuardarClick(callback: () -> Unit) { onGuardarClick = callback }
    fun setOnAgregarClick(callback: () -> Unit) { onAgregarClick = callback }
    fun setOnEliminarClick(callback: () -> Unit) { onEliminarClick = callback }
    fun setOnMateriaSeleccionada(callback: (MMateria) -> Unit) { onMateriaSeleccionada = callback }
    
    // Métodos para obtener datos del formulario
    fun getNombre(): String = txtNombre.text.toString()
    fun getMateriaEditando(): MMateria? = materiaEditando
    
    private fun obtenerMateriaPorId(id: Int): MMateria? {
        return materias.find { it.id == id }
    }
    
    fun mostrarMaterias(materias: List<MMateria>) {
        this.materias = materias
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
    
    fun limpiarFormulario() {
        txtNombre.setText("")
        materiaEditando = null
        btnEliminar.isEnabled = false
    }
}