package com.example.controlasistencia.data.alumno

import com.example.controlasistencia.model.MAlumno

open class AlumnoDataSourceDecorator(private val wrappee: AlumnoDataSource) : AlumnoDataSource {
    override fun listar(): List<MAlumno> = wrappee.listar()
    override fun obtener(registro: String): MAlumno? = wrappee.obtener(registro)
    override fun insertar(alumno: MAlumno): Boolean = wrappee.insertar(alumno)
    override fun actualizar(alumno: MAlumno): Boolean = wrappee.actualizar(alumno)
    override fun eliminar(alumno: MAlumno): Boolean = wrappee.eliminar(alumno)
}
