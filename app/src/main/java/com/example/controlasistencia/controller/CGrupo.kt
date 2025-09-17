package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.model.MMateria
import com.example.controlasistencia.view.VGrupo

class CGrupo(
    private val context: Context,
    private val view: VGrupo
) {
    
    fun cargarGrupos() {
        val grupos = MGrupo.listar(context)
        view.mostrarGrupos(grupos)
    }
    
    fun cargarMaterias() {
        val materias = MMateria.listar(context)
        view.mostrarMaterias(materias)
    }
    
    fun mostrarCrear() {
        cargarMaterias()
        view.mostrarFormularioCrear()
    }
    
    fun mostrarEditar(grupo: MGrupo) {
        cargarMaterias()
        view.mostrarFormularioEditar(grupo)
    }
    
    fun crearGrupo(nombre: String, idMateria: Int) {
        val grupo = MGrupo(nombre = nombre, id_materia = idMateria)
        grupo.insertar(context)
        actualizarVista()
    }
    
    fun actualizarGrupo(grupo: MGrupo, nuevoNombre: String, nuevoIdMateria: Int) {
        grupo.nombre = nuevoNombre
        grupo.id_materia = nuevoIdMateria
        grupo.actualizar(context)
        actualizarVista()
    }
    
    fun eliminarGrupo(grupo: MGrupo) {
        grupo.eliminar(context)
        actualizarVista()
    }
    
    fun obtenerMateria(idMateria: Int): String {
        return MGrupo.obtenerMateria(context, idMateria)
    }

    fun obtenerGrupo(idGrupo: Int): MGrupo? {
        return MGrupo.obtener(context, idGrupo)
    }
    
    fun actualizarVista() {
        cargarGrupos()
        cargarMaterias()
    }
}