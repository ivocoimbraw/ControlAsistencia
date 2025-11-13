package com.example.controlasistencia.data.alumno

import android.util.Log
import com.example.controlasistencia.model.MAlumno
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

class CompressionDecorator(wrappee: AlumnoDataSource) : AlumnoDataSourceDecorator(wrappee) {
    private val tag = "CompressionDecorator"
    
    override fun listar(): List<MAlumno> {
        val data = super.listar()
        Log.d(tag, "Descomprimiendo lista de ${data.size} alumnos")
        return data.map { decompress(it) }
    }

    override fun obtener(registro: String): MAlumno? {
        val data = super.obtener(registro)
        if (data != null) {
            Log.d(tag, "Descomprimiendo alumno: $registro")
            return decompress(data)
        }
        return null
    }

    override fun insertar(alumno: MAlumno): Boolean {

        Log.d(tag, "Comprimiendo alumno antes de insertar: ${alumno.registro}")
        val compressed = compress(alumno)

        return super.insertar(compressed)
    }

    override fun actualizar(alumno: MAlumno): Boolean {
        Log.d(tag, "Comprimiendo alumno antes de actualizar: ${alumno.registro}")
        val compressed = compress(alumno)

        return super.actualizar(compressed)
    }

    override fun eliminar(alumno: MAlumno): Boolean {
        return super.eliminar(alumno)
    }

    private fun compress(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro,
            apellidop = compressString(alumno.apellidop),
            apellidom = compressString(alumno.apellidom),
            nombre = compressString(alumno.nombre)
        )
    }

    private fun decompress(alumno: MAlumno): MAlumno {
        return MAlumno(
            registro = alumno.registro,
            apellidop = decompressString(alumno.apellidop),
            apellidom = decompressString(alumno.apellidom),
            nombre = decompressString(alumno.nombre)
        )
    }

    private fun compressString(value: String): String {
        if (value.startsWith("[COMPRESSED]")) return value
        return "[COMPRESSED]$value"
    }

    private fun decompressString(value: String): String {
        return if (value.startsWith("[COMPRESSED]")) {
            value.removePrefix("[COMPRESSED]")
        } else {
            value
        }
    }
}
