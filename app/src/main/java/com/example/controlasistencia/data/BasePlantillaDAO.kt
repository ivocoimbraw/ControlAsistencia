package com.example.controlasistencia.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.controlasistencia.DBHelper

abstract class BasePlantillaDAO<T : Any>(
    private val context: Context
) {
    protected abstract fun nombreTabla(): String
    protected abstract fun columnaId(): String
    protected abstract fun mapearCursor(cursor: Cursor): T
    protected abstract fun contentValues(entity: T, isUpdate: Boolean = false): ContentValues

    protected open fun validarParaInsert(entity: T) {}
    protected open fun validarParaUpdate(entity: T) {}

    // Métodos template finales -------------------------------------------------

    fun insertar(entity: T): Boolean {
        validarParaInsert(entity)
        val db = DBHelper(context).writableDatabase
        return try {
            db.insert(nombreTabla(), null, contentValues(entity, isUpdate = false)) > 0
        } catch (_: Exception) {
            false
        } finally {
            db.close()
        }
    }

    fun obtener(id: Int): T? {
        val db = DBHelper(context).readableDatabase
        val cursor = db.query(
            nombreTabla(),
            null,
            "${columnaId()}=?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) mapearCursor(it) else null
        }.also {
            db.close()
        }
    }

    fun listar(): List<T> {
        val db = DBHelper(context).readableDatabase
        val cursor = db.query(nombreTabla(), null, null, null, null, null, "id DESC")
        val out = mutableListOf<T>()
        cursor.use {
            while (it.moveToNext()) out.add(mapearCursor(it))
        }
        db.close()
        return out
    }

    fun eliminar(id: Int): Boolean {
        val db = DBHelper(context).writableDatabase
        return try {
            db.delete(nombreTabla(), "${columnaId()}=?", arrayOf(id.toString())) > 0
        } finally {
            db.close()
        }
    }

    fun actualizar(id: Int, entity: T): Boolean {
        validarParaUpdate(entity)
    val db = DBHelper(context).writableDatabase
        return try {
            db.update(nombreTabla(), contentValues(entity, isUpdate = true), "${columnaId()}=?", arrayOf(id.toString())) > 0
        } finally {
            db.close()
        }
    }
}
