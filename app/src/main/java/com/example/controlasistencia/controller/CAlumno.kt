package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MAlumno
import com.example.controlasistencia.view.VAlumno

class CAlumno(
    private val context: Context,
    private val view: VAlumno
) {

    fun cargarAlumnos() {
        val alumnos = MAlumno.listar(context)
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
        alumno.insertar(context)
        actualizarVista()
    }

    fun actualizarAlumno(alumno: MAlumno, registro: String, apellidop: String, apellidom: String, nombre: String) {
        alumno.registro = registro
        alumno.apellidop = apellidop
        alumno.apellidom = apellidom
        alumno.nombre = nombre
        alumno.actualizar(context)
        actualizarVista()
    }

    fun eliminarAlumno(alumno: MAlumno) {
        alumno.eliminar(context)
        actualizarVista()
    }

    fun obtenerAlumno(registro: String): MAlumno? {
        return MAlumno.obtener(context, registro)
    }

    fun actualizarVista() {
        cargarAlumnos()
    }
}