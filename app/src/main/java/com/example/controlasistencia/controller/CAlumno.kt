package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.data.alumno.AlumnoDataSource
import com.example.controlasistencia.model.MAlumno
import com.example.controlasistencia.view.VAlumno

class CAlumno(
    private val context: Context,
    private val view: VAlumno,
    private val dataSource: AlumnoDataSource
) {

    fun inicializar() {
        view.setOnGuardarClick { guardarAlumno() }
        view.setOnAgregarClick { mostrarCrear() }
        view.setOnEliminarClick { eliminarAlumno() }
        view.setOnAlumnoSeleccionado { alumno -> mostrarEditar(alumno) }
        actualizarVista()
    }

    fun cargarAlumnos() {
        val alumnos = dataSource.listar()
        view.mostrarAlumnos(alumnos)
    }

    fun mostrarCrear() {
        view.mostrarFormularioCrear()
    }

    fun mostrarEditar(alumno: MAlumno) {
        view.mostrarFormularioEditar(alumno)
    }

    fun crearAlumno(registro: String, apellidop: String, apellidom: String, nombre: String) {
        val alumno = MAlumno(registro, apellidop, apellidom, nombre)
        dataSource.insertar(alumno)
        actualizarVista()
    }

    fun actualizarAlumno(alumno: MAlumno, registro: String, apellidop: String, apellidom: String, nombre: String) {
        alumno.registro = registro
        alumno.apellidop = apellidop
        alumno.apellidom = apellidom
        alumno.nombre = nombre
        dataSource.actualizar(alumno)
        actualizarVista()
    }

    fun eliminarAlumno(alumno: MAlumno) {
        dataSource.eliminar(alumno)
        actualizarVista()
    }

    fun obtenerAlumno(registro: String): MAlumno? {
        return dataSource.obtener(registro)
    }

    fun actualizarVista() {
        cargarAlumnos()
    }
    
    // Métodos para instanciar modelos
    fun crearInstanciaAlumno(registro: String = "", apellidop: String = "", apellidom: String = "", nombre: String = ""): MAlumno {
        return MAlumno(registro, apellidop, apellidom, nombre)
    }
    
    fun crearInstanciaAlumnoVacia(): MAlumno {
        return MAlumno("", "", "", "")
    }
    
    private fun guardarAlumno() {
        val registro = view.getRegistro()
        val apellidop = view.getApellidoP()
        val apellidom = view.getApellidoM()
        val nombre = view.getNombre()
        val alumnoEditando = view.getAlumnoEditando()
        
        if (alumnoEditando != null) {
            actualizarAlumno(alumnoEditando, registro, apellidop, apellidom, nombre)
        } else {
            crearAlumno(registro, apellidop, apellidom, nombre)
        }
        view.limpiarFormulario()
    }
    
    private fun eliminarAlumno() {
        view.getAlumnoEditando()?.let {
            eliminarAlumno(it)
            view.limpiarFormulario()
        }
    }
}