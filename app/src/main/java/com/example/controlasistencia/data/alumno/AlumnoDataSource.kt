package com.example.controlasistencia.data.alumno

import com.example.controlasistencia.model.MAlumno

interface AlumnoDataSource {
    fun listar(): List<MAlumno>
    fun obtener(registro: String): MAlumno?
    fun insertar(alumno: MAlumno): Boolean
    fun actualizar(alumno: MAlumno): Boolean
    fun eliminar(alumno: MAlumno): Boolean
}
