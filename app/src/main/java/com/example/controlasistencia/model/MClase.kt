package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.DBHelper

data class MClase(
    var id: Int = 0,
    var fecha: String = "",
    var horaInicio: String = "",
    var horaFin: String = "",
    var estado: String = "",
    var idGrupo: Int = 0,
    var idQrCode: Int? = null
) {
    // insertar(): boolean
    fun insertar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("fecha", fecha)
            put("hora_inicio", horaInicio)
            put("hora_fin", horaFin)
            put("estado", estado)
            put("id_grupo", idGrupo)
            idQrCode?.let { put("id_qr_code", it) }
        }
        val newRowId = db.insert(DBHelper.Companion.TABLE_CLASE, null, values)
        if (newRowId != -1L) {
            this.id = newRowId.toInt()
        }
        db.close()
        return newRowId != -1L
    }

    // obtener(id: Int): MClase
    companion object {
        fun obtener(context: Context, id: Int): MClase? {
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.Companion.TABLE_CLASE,
                arrayOf("id", "fecha", "hora_inicio", "hora_fin", "estado", "id_grupo", "id_qr_code"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var clase: MClase? = null
            if (cursor.moveToFirst()) {
                val idQrCodeIndex = cursor.getColumnIndex("id_qr_code")
                val idQrCodeValue = if (idQrCodeIndex >= 0 && !cursor.isNull(idQrCodeIndex)) {
                    cursor.getInt(idQrCodeIndex)
                } else null
                
                clase = MClase(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hora_fin")),
                    cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_grupo")),
                    idQrCodeValue
                )
            }
            cursor.close()
            db.close()
            return clase
        }

        // listar(): List<MClase>
        fun listar(context: Context): List<MClase> {
            val list = mutableListOf<MClase>()
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(DBHelper.Companion.TABLE_CLASE, arrayOf("id", "fecha", "hora_inicio", "hora_fin", "estado", "id_grupo", "id_qr_code"), null, null, null, null, "id DESC")
            if (cursor.moveToFirst()) {
                do {
                    val idQrCodeIndex = cursor.getColumnIndex("id_qr_code")
                    val idQrCodeValue = if (idQrCodeIndex >= 0 && !cursor.isNull(idQrCodeIndex)) {
                        cursor.getInt(idQrCodeIndex)
                    } else null
                    
                    val clase = MClase(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hora_fin")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_grupo")),
                        idQrCodeValue
                    )
                    list.add(clase)
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
            put("fecha", fecha)
            put("hora_inicio", horaInicio)
            put("hora_fin", horaFin)
            put("estado", estado)
            put("id_grupo", idGrupo)
            idQrCode?.let { put("id_qr_code", it) }
        }
        val rows = db.update(DBHelper.Companion.TABLE_CLASE, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val rows = db.delete(DBHelper.Companion.TABLE_CLASE, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}