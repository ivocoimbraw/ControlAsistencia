package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.data.GrupoDAO
import com.example.controlasistencia.data.MateriaDAO
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.model.MMateria
import com.example.controlasistencia.view.VGrupo

class CGrupo(
    private val context: Context,
    private val view: VGrupo
) {
    private val grupoDAO by lazy { GrupoDAO(context) }
    private val materiaDAO by lazy { MateriaDAO(context) }
    
    fun inicializar() {
        view.setOnGuardarClick { guardarGrupo() }
        view.setOnAgregarClick { mostrarCrear() }
        view.setOnEliminarClick { eliminarGrupo() }
        view.setOnGrupoSeleccionado { grupo -> mostrarEditar(grupo) }
        view.setObtenerMateria { idMateria -> obtenerMateria(idMateria) }
        actualizarVista()
    }
    
    fun cargarGrupos() {
        val grupos = grupoDAO.listar()
        view.mostrarGrupos(grupos)
    }
    
    fun cargarMaterias() {
        val materias = materiaDAO.listar()
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
        grupoDAO.insertar(grupo)
        actualizarVista()
    }
    
    fun actualizarGrupo(grupo: MGrupo, nuevoNombre: String, nuevoIdMateria: Int) {
        val actualizado = grupo.copy(nombre = nuevoNombre, id_materia = nuevoIdMateria)
        grupoDAO.actualizar(actualizado.id, actualizado)
        actualizarVista()
    }
    
    fun eliminarGrupo(grupo: MGrupo) {
        grupoDAO.eliminar(grupo.id)
        actualizarVista()
    }
    
    fun obtenerMateria(idMateria: Int): String {
        return materiaDAO.obtener(idMateria)?.nombre ?: "Materia no encontrada"
    }

    fun obtenerGrupo(idGrupo: Int): MGrupo? {
        return grupoDAO.obtener(idGrupo)
    }
    
    fun actualizarVista() {
        cargarGrupos()
        cargarMaterias()
    }
    
    // Métodos para instanciar modelos
    fun crearInstanciaGrupo(nombre: String = "", idMateria: Int = 0): MGrupo {
        return MGrupo(nombre = nombre, id_materia = idMateria)
    }
    
    fun crearInstanciaGrupoVacia(): MGrupo {
        return MGrupo(0, "")
    }
    
    private fun guardarGrupo() {
        val nombre = view.getNombre()
        val materiaSeleccionada = view.getMateriaSeleccionada()
        val grupoEditando = view.getGrupoEditando()
        
        if (grupoEditando != null) {
            actualizarGrupo(grupoEditando, nombre, materiaSeleccionada.id)
        } else {
            crearGrupo(nombre, materiaSeleccionada.id)
        }
        view.limpiarFormulario()
    }
    
    private fun eliminarGrupo() {
        view.getGrupoEditando()?.let {
            eliminarGrupo(it)
            view.limpiarFormulario()
        }
    }
}