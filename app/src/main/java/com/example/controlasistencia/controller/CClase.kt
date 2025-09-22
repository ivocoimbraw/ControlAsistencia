package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.model.MQrCode
import com.example.controlasistencia.view.VClase

class CClase(
    private val context: Context,
    private val view: VClase
) {

    fun cargarClases() {
        val clases = MClase.listar(context)
        view.mostrarClases(clases)
    }

    fun cargarGrupos() {
        val grupos = MGrupo.listar(context)
        view.mostrarGrupos(grupos)
    }

    fun mostrarCrear() {
        cargarGrupos()
        view.mostrarFormularioCrear()
    }

    fun mostrarEditar(clase: MClase) {
        cargarGrupos()
        view.mostrarFormularioEditar(clase)
    }

    fun crearClase(fecha: String, horaInicio: String, horaFin: String, estado: String, idGrupo: Int) {
        val clase = MClase(0, fecha, horaInicio, horaFin, estado, idGrupo)
        
        // Insertar clase primero
        if (clase.insertar(context)) {
            // Generar QR automáticamente para la clase
            val qrCode = MQrCode.generarQrParaClase(context, clase.id, fecha)
            val qrId = qrCode.insertar(context)
            
            if (qrId > 0) {
                // Actualizar la clase con el ID del QR
                clase.idQrCode = qrId.toInt()
                clase.actualizar(context)
            }
        }
        
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

    fun obtenerGrupo(idGrupo: Int): String {
        return MGrupo.obtenerGrupo(context, idGrupo)
    }

    fun obtenerMateria(idMateria: Int): String {
        return MGrupo.obtenerMateria(context, idMateria)
    }

    fun obtenerQrCode(idQrCode: Int): MQrCode? {
        return MQrCode.obtener(context, idQrCode)
    }

    fun actualizarVista() {
        cargarClases()
        cargarGrupos()
    }
}