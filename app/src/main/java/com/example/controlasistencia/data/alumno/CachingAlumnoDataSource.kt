package com.example.controlasistencia.data.alumno

import com.example.controlasistencia.model.MAlumno
import java.util.concurrent.ConcurrentHashMap

class CachingAlumnoDataSource(wrappee: AlumnoDataSource) : AlumnoDataSourceDecorator(wrappee) {
    @Volatile
    private var listCache: List<MAlumno>? = null
    private val itemCache = ConcurrentHashMap<String, MAlumno>()

    override fun listar(): List<MAlumno> {
        val cached = listCache
        if (cached != null) return cached
        val fresh = super.listar()
        listCache = fresh
        itemCache.clear()
        for (a in fresh) itemCache[a.registro] = a
        return fresh
    }

    override fun obtener(registro: String): MAlumno? {
        itemCache[registro]?.let { return it }
        val found = super.obtener(registro)
        if (found != null) {
            itemCache[registro] = found
        }
        return found
    }

    override fun insertar(alumno: MAlumno): Boolean {
        val ok = super.insertar(alumno)
        if (ok) invalidateAll()
        return ok
    }

    override fun actualizar(alumno: MAlumno): Boolean {
        val ok = super.actualizar(alumno)
        if (ok) invalidateAll()
        return ok
    }

    override fun eliminar(alumno: MAlumno): Boolean {
        val ok = super.eliminar(alumno)
        if (ok) invalidateAll()
        return ok
    }

    private fun invalidateAll() {
        listCache = null
        itemCache.clear()
    }
}
