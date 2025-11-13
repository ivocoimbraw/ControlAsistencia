package com.example.controlasistencia.data.alumno

import android.util.Base64
import android.util.Log
import com.example.controlasistencia.model.MAlumno

class EncryptionDecorator(wrappee: AlumnoDataSource) : AlumnoDataSourceDecorator(wrappee) {
    private val tag = "EncryptionDecorator"
    
    override fun listar(): List<MAlumno> {
        val data = super.listar()

        Log.d(tag, "Desencriptando lista de ${data.size} alumnos")
        return data.map { decrypt(it) }
    }

    override fun obtener(registro: String): MAlumno? {
        val data = super.obtener(registro)

        if (data != null) {
            Log.d(tag, "Desencriptando alumno: $registro")
            return decrypt(data)
        }
        return null
    }

    override fun insertar(alumno: MAlumno): Boolean {
        Log.d(tag, "Encriptando alumno antes de insertar: ${alumno.registro}")
        val encrypted = encrypt(alumno)

        return super.insertar(encrypted)
    }

    override fun actualizar(alumno: MAlumno): Boolean {
        Log.d(tag, "Encriptando alumno antes de actualizar: ${alumno.registro}")
        val encrypted = encrypt(alumno)

        return super.actualizar(encrypted)
    }

    override fun eliminar(alumno: MAlumno): Boolean {
        return super.eliminar(alumno)
    }

    private fun encrypt(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro,
            apellidop = encryptString(alumno.apellidop),
            apellidom = encryptString(alumno.apellidom),
            nombre = encryptString(alumno.nombre)
        )
    }

    private fun decrypt(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro,
            apellidop = decryptString(alumno.apellidop),
            apellidom = decryptString(alumno.apellidom),
            nombre = decryptString(alumno.nombre)
        )
    }

    private fun encryptString(value: String): String {
        return Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
    }

    private fun decryptString(value: String): String {
        return try {
            String(Base64.decode(value, Base64.DEFAULT))
        } catch (e: Exception) {
            value
        }
    }
}
