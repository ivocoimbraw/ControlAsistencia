package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MMateria
import com.example.controlasistencia.view.VMateriaView

class CMateriaController(
    private val context: Context,
    private val view: VMateriaView
) {
    
    fun cargarMaterias() {
        val materias = MMateria.listar(context)
        view.mostrarMaterias(materias)
    }
    
    fun mostrarCrear() {
        view.mostrarFormularioCrear()
    }
    
    fun mostrarEditar(materia: MMateria) {
        view.mostrarFormularioEditar(materia)
    }
    
    fun crearMateria(nombre: String) {
        val materia = MMateria(nombre = nombre)
        materia.insertar(context)
        actualizarVista()
    }
    
    fun actualizarMateria(materia: MMateria, nuevoNombre: String) {
        materia.nombre = nuevoNombre
        materia.actualizar(context)
        actualizarVista()
    }
    
    fun eliminarMateria(materia: MMateria) {
        materia.eliminar(context)
        actualizarVista()
    }

    fun obtenerMateria(idMateria: Int): MMateria? {
        return MMateria.obtener(context, idMateria)
    }
    
    fun actualizarVista() {
        cargarMaterias()
    }
}