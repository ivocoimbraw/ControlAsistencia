package com.example.controlasistencia.data.alumno

import android.util.Base64
import android.util.Log
import com.example.controlasistencia.model.MAlumno

/**
 * Decorador Concreto: Encripta los datos antes de escribirlos y desencripta al leerlos.
 * Este decorador añade una capa de seguridad al componente envuelto.
 */
class EncryptionDecorator(wrappee: AlumnoDataSource) : AlumnoDataSourceDecorator(wrappee) {
    private val tag = "EncryptionDecorator"
    
    override fun listar(): List<MAlumno> {
        // 1. Obtiene datos del objeto envuelto
        val data = super.listar()
        // 2. Desencripta los datos (en este caso simulado)
        Log.d(tag, "Desencriptando lista de ${data.size} alumnos")
        return data.map { decrypt(it) }
    }

    override fun obtener(registro: String): MAlumno? {
        // 1. Obtiene datos del objeto envuelto
        val data = super.obtener(registro)
        // 2. Desencripta si existe
        if (data != null) {
            Log.d(tag, "Desencriptando alumno: $registro")
            return decrypt(data)
        }
        return null
    }

    override fun insertar(alumno: MAlumno): Boolean {
        // 1. Encripta los datos antes de pasarlos al objeto envuelto
        Log.d(tag, "Encriptando alumno antes de insertar: ${alumno.registro}")
        val encrypted = encrypt(alumno)
        // 2. Pasa los datos encriptados al método insertar del objeto envuelto
        return super.insertar(encrypted)
    }

    override fun actualizar(alumno: MAlumno): Boolean {
        // 1. Encripta los datos antes de actualizar
        Log.d(tag, "Encriptando alumno antes de actualizar: ${alumno.registro}")
        val encrypted = encrypt(alumno)
        // 2. Pasa los datos encriptados al objeto envuelto
        return super.actualizar(encrypted)
    }

    override fun eliminar(alumno: MAlumno): Boolean {
        // Para eliminar no es necesario encriptar, solo delegamos
        return super.eliminar(alumno)
    }

    /**
     * Encripta los campos sensibles del alumno usando Base64 (simulación simple).
     * En producción usarías AES u otro algoritmo robusto.
     */
    private fun encrypt(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro, // El registro no se encripta (es la clave)
            apellidop = encryptString(alumno.apellidop),
            apellidom = encryptString(alumno.apellidom),
            nombre = encryptString(alumno.nombre)
        )
    }

    /**
     * Desencripta los campos del alumno.
     */
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
            value // Si no está encriptado, devuelve el original
        }
    }
}
