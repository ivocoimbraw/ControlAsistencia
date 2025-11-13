package com.example.controlasistencia.data.alumno

import android.util.Log
import com.example.controlasistencia.model.MAlumno
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

/**
 * Decorador Concreto: Comprime los datos antes de escribirlos y descomprime al leerlos.
 * Este decorador optimiza el almacenamiento reduciendo el tamaño de los datos.
 */
class CompressionDecorator(wrappee: AlumnoDataSource) : AlumnoDataSourceDecorator(wrappee) {
    private val tag = "CompressionDecorator"
    
    override fun listar(): List<MAlumno> {
        // 1. Obtiene datos del objeto envuelto
        val data = super.listar()
        // 2. Descomprime los datos (en este caso simulado)
        Log.d(tag, "Descomprimiendo lista de ${data.size} alumnos")
        return data.map { decompress(it) }
    }

    override fun obtener(registro: String): MAlumno? {
        // 1. Obtiene datos del objeto envuelto
        val data = super.obtener(registro)
        // 2. Descomprime si existe
        if (data != null) {
            Log.d(tag, "Descomprimiendo alumno: $registro")
            return decompress(data)
        }
        return null
    }

    override fun insertar(alumno: MAlumno): Boolean {
        // 1. Comprime los datos antes de pasarlos al objeto envuelto
        Log.d(tag, "Comprimiendo alumno antes de insertar: ${alumno.registro}")
        val compressed = compress(alumno)
        // 2. Pasa los datos comprimidos al método insertar del objeto envuelto
        return super.insertar(compressed)
    }

    override fun actualizar(alumno: MAlumno): Boolean {
        // 1. Comprime los datos antes de actualizar
        Log.d(tag, "Comprimiendo alumno antes de actualizar: ${alumno.registro}")
        val compressed = compress(alumno)
        // 2. Pasa los datos comprimidos al objeto envuelto
        return super.actualizar(compressed)
    }

    override fun eliminar(alumno: MAlumno): Boolean {
        // Para eliminar no es necesario comprimir, solo delegamos
        return super.eliminar(alumno)
    }

    /**
     * Comprime los campos del alumno (simulación simple con prefijo).
     * En producción usarías GZIP o Deflate para comprimir texto real.
     */
    private fun compress(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro, // El registro no se comprime (es la clave)
            apellidop = compressString(alumno.apellidop),
            apellidom = compressString(alumno.apellidom),
            nombre = compressString(alumno.nombre)
        )
    }

    /**
     * Descomprime los campos del alumno.
     */
    private fun decompress(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro,
            apellidop = decompressString(alumno.apellidop),
            apellidom = decompressString(alumno.apellidom),
            nombre = decompressString(alumno.nombre)
        )
    }

    /**
     * Simulación de compresión: agrega prefijo [COMPRESSED].
     * En producción, usarías algoritmos de compresión reales.
     */
    private fun compressString(value: String): String {
        if (value.startsWith("[COMPRESSED]")) return value
        return "[COMPRESSED]$value"
    }

    /**
     * Simulación de descompresión: remueve prefijo [COMPRESSED].
     */
    private fun decompressString(value: String): String {
        return if (value.startsWith("[COMPRESSED]")) {
            value.removePrefix("[COMPRESSED]")
        } else {
            value // Si no está comprimido, devuelve el original
        }
    }
}
