package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.view.VClaseViewSimple

class CClaseControllerSimple(
    private val context: Context,
    private val view: VClaseViewSimple
) {

    fun cargarClases() {
        val clases = MClase.listar(context)
        view.mostrarClases(clases)
    }

    fun mostrarCrear() {
        view.mostrarFormularioCrear()
    }

    fun mostrarEditar(clase: MClase) {
        view.mostrarFormularioEditar(clase)
    }

    fun crearClase(fecha: String, horaInicio: String, horaFin: String, estado: String, idGrupo: Int) {
        val clase = MClase(0, fecha, horaInicio, horaFin, estado, idGrupo)
        clase.insertar(context)
        actualizarVista()
    }

    fun actualizarClase(clase: MClase, fecha: String, horaInicio: String, horaFin: String, estado: String, idGrupo: Int) {
        clase.fecha = fecha
        clase.horaInicio = horaInicio
        clase.horaFin = horaFin
        clase.estado = estado
        clase.idGrupo = idGrupo
        clase.actualizar(context)
        actualizarVista()
    }

    fun eliminarClase(clase: MClase) {
        clase.eliminar(context)
        actualizarVista()
    }

    fun obtenerClase(id: Int): MClase? {
        return MClase.obtener(context, id)
    }

    fun actualizarVista() {
        cargarClases()
    }
}