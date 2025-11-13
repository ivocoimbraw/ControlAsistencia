package com.example.controlasistencia.data.alumno

import android.util.Log
import com.example.controlasistencia.model.MAlumno

class LoggingAlumnoDataSource(wrappee: AlumnoDataSource) : AlumnoDataSourceDecorator(wrappee) {
    private val tag = "LoggingAlumnoDS"

    override fun listar(): List<MAlumno> {
        val start = System.nanoTime()
        try {
            val result = super.listar()
            Log.d(tag, "listar -> ${result.size}")
            return result
        } catch (t: Throwable) {
            Log.e(tag, "listar error", t)
            throw t
        } finally {
            val durMs = (System.nanoTime() - start) / 1_000_000
            Log.d(tag, "listar duration=${durMs}ms")
        }
    }

    override fun obtener(registro: String): MAlumno? {
        val start = System.nanoTime()
        try {
            val result = super.obtener(registro)
            Log.d(tag, "obtener($registro) -> ${result != null}")
            return result
        } catch (t: Throwable) {
            Log.e(tag, "obtener($registro) error", t)
            throw t
        } finally {
            val durMs = (System.nanoTime() - start) / 1_000_000
            Log.d(tag, "obtener($registro) duration=${durMs}ms")
        }
    }

    override fun insertar(alumno: MAlumno): Boolean {
        val start = System.nanoTime()
        try {
            val ok = super.insertar(alumno)
            Log.d(tag, "insertar(${alumno.registro}) -> $ok")
            return ok
        } catch (t: Throwable) {
            Log.e(tag, "insertar(${alumno.registro}) error", t)
            throw t
        } finally {
            val durMs = (System.nanoTime() - start) / 1_000_000
            Log.d(tag, "insertar(${alumno.registro}) duration=${durMs}ms")
        }
    }

    override fun actualizar(alumno: MAlumno): Boolean {
        val start = System.nanoTime()
        try {
            val ok = super.actualizar(alumno)
            Log.d(tag, "actualizar(${alumno.registro}) -> $ok")
            return ok
        } catch (t: Throwable) {
            Log.e(tag, "actualizar(${alumno.registro}) error", t)
            throw t
        } finally {
            val durMs = (System.nanoTime() - start) / 1_000_000
            Log.d(tag, "actualizar(${alumno.registro}) duration=${durMs}ms")
        }
    }

    override fun eliminar(alumno: MAlumno): Boolean {
        val start = System.nanoTime()
        try {
            val ok = super.eliminar(alumno)
            Log.d(tag, "eliminar(${alumno.registro}) -> $ok")
            return ok
        } catch (t: Throwable) {
            Log.e(tag, "eliminar(${alumno.registro}) error", t)
            throw t
        } finally {
            val durMs = (System.nanoTime() - start) / 1_000_000
            Log.d(tag, "eliminar(${alumno.registro}) duration=${durMs}ms")
        }
    }
}
