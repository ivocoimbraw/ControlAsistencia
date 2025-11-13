package com.example.controlasistencia.data.alumno

import android.content.Context
import com.example.controlasistencia.model.MAlumno

class AlumnoSqliteDataSource(private val context: Context) : AlumnoDataSource {
    override fun listar(): List<MAlumno> = MAlumno.listar(context)
    override fun obtener(registro: String): MAlumno? = MAlumno.obtener(context, registro)
    override fun insertar(alumno: MAlumno): Boolean = alumno.insertar(context)
    override fun actualizar(alumno: MAlumno): Boolean = alumno.actualizar(context)
    override fun eliminar(alumno: MAlumno): Boolean = alumno.eliminar(context)
}
