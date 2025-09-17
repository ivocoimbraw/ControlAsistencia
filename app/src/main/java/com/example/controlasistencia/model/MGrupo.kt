package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.DBHelper

data class MGrupo(var id: Int = 0, var nombre: String = "", var id_materia: Int = 0) {
    
    // insertar(): boolean
    fun insertar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_materia", id_materia)
        }
        val newRowId = db.insert(DBHelper.TABLE_GRUPO, null, values)
        db.close()
        return newRowId != -1L
    }

    // obtener(id: int): MGrupo
    companion object {
        fun obtener(context: Context, id: Int): MGrupo? {
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.TABLE_GRUPO,
                arrayOf("id", "nombre", "id_materia"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var grupo: MGrupo? = null
            if (cursor.moveToFirst()) {
                grupo = MGrupo(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_materia"))
                )
            }
            cursor.close()
            db.close()
            return grupo
        }

        // listar(): List
        fun listar(context: Context): List<MGrupo> {
            val list = mutableListOf<MGrupo>()
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.TABLE_GRUPO, 
                arrayOf("id", "nombre", "id_materia"), 
                null, null, null, null, "id DESC"
            )
            if (cursor.moveToFirst()) {
                do {
                    val g = MGrupo(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_materia"))
                    )
                    list.add(g)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }
        
        // obtenerMateria(): void - Obtiene el nombre de la materia asociada
        fun obtenerMateria(context: Context, idMateria: Int): String {
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.TABLE_MATERIA,
                arrayOf("nombre"),
                "id = ?",
                arrayOf(idMateria.toString()),
                null, null, null
            )
            var nombreMateria = "Materia no encontrada"
            if (cursor.moveToFirst()) {
                nombreMateria = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            }
            cursor.close()
            db.close()
            return nombreMateria
        }
    }

    // actualizar(): boolean
    fun actualizar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("id_materia", id_materia)
        }
        val rows = db.update(DBHelper.TABLE_GRUPO, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val rows = db.delete(DBHelper.TABLE_GRUPO, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}
