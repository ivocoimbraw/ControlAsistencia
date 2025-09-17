package com.example.controlasistencia.view

import android.app.Activity
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.controller.CClaseControllerSimple
import com.example.controlasistencia.model.MClase

class VClaseViewSimple(private val activity: Activity) {

    private val controller = CClaseControllerSimple(activity, this)
    private val txtFecha: EditText = activity.findViewById(R.id.txtFechaClase)
    private val txtHoraInicio: EditText = activity.findViewById(R.id.txtHoraInicioClase)
    private val txtHoraFin: EditText = activity.findViewById(R.id.txtHoraFinClase)
    private val txtEstado: EditText = activity.findViewById(R.id.txtEstadoClase)
    private val txtIdGrupo: EditText = activity.findViewById(R.id.txtIdGrupoClase)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardarClase)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregarClase)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminarClase)
    private val lvClases: ListView = activity.findViewById(R.id.lvClases)

    private var claseEditando: MClase? = null
    private var adapter: ArrayAdapter<String>? = null

    init {
        controller.actualizarVista()
        setupListeners()
    }

    private fun setupListeners() {
        btnGuardar.setOnClickListener { guardarClase() }
        btnAgregar.setOnClickListener { controller.mostrarCrear() }
        btnEliminar.setOnClickListener { eliminarClase() }

        lvClases.setOnItemClickListener { _, _, position, _ ->
            val claseTexto = adapter?.getItem(position) ?: ""
            val id = claseTexto.split(" - ")[0].toInt()
            val clase = controller.obtenerClase(id)
            clase?.let { controller.mostrarEditar(it) }
        }
    }

    fun mostrarClases(clases: List<MClase>) {
        val items = clases.map { "${it.id} - ${it.fecha} ${it.horaInicio}-${it.horaFin} (${it.estado})" }
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lvClases.adapter = adapter
    }

    fun mostrarFormularioCrear() {
        txtFecha.setText("")
        txtHoraInicio.setText("")
        txtHoraFin.setText("")
        txtEstado.setText("")
        txtIdGrupo.setText("")
        claseEditando = null
        btnEliminar.isEnabled = false
    }

    fun mostrarFormularioEditar(clase: MClase) {
        txtFecha.setText(clase.fecha)
        txtHoraInicio.setText(clase.horaInicio)
        txtHoraFin.setText(clase.horaFin)
        txtEstado.setText(clase.estado)
        txtIdGrupo.setText(clase.idGrupo.toString())
        claseEditando = clase
        btnEliminar.isEnabled = true
    }

    private fun guardarClase() {
        val fecha = txtFecha.text.toString()
        val horaInicio = txtHoraInicio.text.toString()
        val horaFin = txtHoraFin.text.toString()
        val estado = txtEstado.text.toString()
        val idGrupo = txtIdGrupo.text.toString().toIntOrNull() ?: 0

        if (claseEditando != null) {
            controller.actualizarClase(claseEditando!!, fecha, horaInicio, horaFin, estado, idGrupo)
        } else {
            controller.crearClase(fecha, horaInicio, horaFin, estado, idGrupo)
        }
        limpiarFormulario()
    }

    private fun eliminarClase() {
        claseEditando?.let {
            controller.eliminarClase(it)
            limpiarFormulario()
        }
    }

    private fun limpiarFormulario() {
        txtFecha.setText("")
        txtHoraInicio.setText("")
        txtHoraFin.setText("")
        txtEstado.setText("")
        txtIdGrupo.setText("")
        claseEditando = null
        btnEliminar.isEnabled = false
    }
}