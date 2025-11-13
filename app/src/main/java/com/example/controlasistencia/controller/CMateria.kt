package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.data.MateriaDAO
import com.example.controlasistencia.model.MMateria
import com.example.controlasistencia.view.VMateriaView

class CMateriaController(
    private val context: Context,
    private val view: VMateriaView
) {
    private val dao by lazy { MateriaDAO(context) }
    
    fun inicializar() {
        view.setOnGuardarClick { guardarMateria() }
        view.setOnAgregarClick { mostrarCrear() }
        view.setOnEliminarClick { eliminarMateria() }
        view.setOnMateriaSeleccionada { materia -> mostrarEditar(materia) }
        actualizarVista()
    }
    
    fun cargarMaterias() {
        val materias = dao.listar()
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
        dao.insertar(materia)
        actualizarVista()
    }
    
    fun actualizarMateria(materia: MMateria, nuevoNombre: String) {
        val actualizado = materia.copy(nombre = nuevoNombre)
        dao.actualizar(actualizado.id, actualizado)
        actualizarVista()
    }
    
    fun eliminarMateria(materia: MMateria) {
        dao.eliminar(materia.id)
        actualizarVista()
    }

    fun obtenerMateria(idMateria: Int): MMateria? {
        return dao.obtener(idMateria)
    }
    
    fun actualizarVista() {
        cargarMaterias()
    }
    
    // Métodos para instanciar modelos
    fun crearInstanciaMateria(nombre: String = ""): MMateria {
        return MMateria(nombre = nombre)
    }
    
    fun crearInstanciaMateriaVacia(): MMateria {
        return MMateria()
    }
    
    private fun guardarMateria() {
        val nombre = view.getNombre()
        val materiaEditando = view.getMateriaEditando()
        
        if (materiaEditando != null) {
            actualizarMateria(materiaEditando, nombre)
        } else {
            crearMateria(nombre)
        }
        view.limpiarFormulario()
    }
    
    private fun eliminarMateria() {
        view.getMateriaEditando()?.let {
            eliminarMateria(it)
            view.limpiarFormulario()
        }
    }
}