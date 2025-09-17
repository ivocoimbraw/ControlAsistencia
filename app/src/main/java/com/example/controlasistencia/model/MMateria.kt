package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.DBHelper

data class MMateria(var id: Int = 0, var nombre: String = "") {
    // insertar(): boolean
    fun insertar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
        }
        val newRowId = db.insert(DBHelper.Companion.TABLE_MATERIA, null, values)
        db.close()
        return newRowId != -1L
    }

    // obtener(id: int): MMateria
    companion object {
        fun obtener(context: Context, id: Int): MMateria? {
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.Companion.TABLE_MATERIA,
                arrayOf("id", "nombre"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var materia: MMateria? = null
            if (cursor.moveToFirst()) {
                materia = MMateria(cursor.getInt(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("nombre")))
            }
            cursor.close()
            db.close()
            return materia
        }

        // listar(): List
        fun listar(context: Context): List<MMateria> {
            val list = mutableListOf<MMateria>()
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(DBHelper.Companion.TABLE_MATERIA, arrayOf("id", "nombre"), null, null, null, null, "id DESC")
            if (cursor.moveToFirst()) {
                do {
                    val m = MMateria(cursor.getInt(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("nombre")))
                    list.add(m)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }
    }

    // actualizar(): boolean
    fun actualizar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
        }
        val rows = db.update(DBHelper.Companion.TABLE_MATERIA, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val rows = db.delete(DBHelper.Companion.TABLE_MATERIA, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}