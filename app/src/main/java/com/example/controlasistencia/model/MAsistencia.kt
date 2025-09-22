package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.DBHelper
import java.text.SimpleDateFormat
import java.util.*

data class MAsistencia(
    var idClase: Int = 0,
    var idEstudiante: String = "",
    var fechaHoraRegistro: String = "",
    var qrVerificado: Boolean = false
) {
    // insertar(): boolean
    fun insertar(context: Context): Long {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("id_clase", idClase)
            put("id_estudiante", idEstudiante)
            put("fecha_hora_registro", fechaHoraRegistro)
            put("qr_verificado", if (qrVerificado) 1 else 0)
        }
        val newRowId = db.insert(DBHelper.TABLE_ASISTENCIA, null, values)
        db.close()
        return newRowId
    }

    // obtener(idClase: Int, idEstudiante: String): MAsistencia
    companion object {
        fun obtener(context: Context, idClase: Int, idEstudiante: String): MAsistencia? {
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.TABLE_ASISTENCIA,
                arrayOf("id_clase", "id_estudiante", "fecha_hora_registro", "qr_verificado"),
                "id_clase = ? AND id_estudiante = ?",
                arrayOf(idClase.toString(), idEstudiante),
                null, null, null
            )
            var asistencia: MAsistencia? = null
            if (cursor.moveToFirst()) {
                asistencia = MAsistencia(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_clase")),
                    cursor.getString(cursor.getColumnIndexOrThrow("id_estudiante")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora_registro")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("qr_verificado")) == 1
                )
            }
            cursor.close()
            db.close()
            return asistencia
        }

        // listar(): List<MAsistencia>
        fun listar(context: Context): List<MAsistencia> {
            val list = mutableListOf<MAsistencia>()
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(DBHelper.TABLE_ASISTENCIA, arrayOf("id_clase", "id_estudiante", "fecha_hora_registro", "qr_verificado"), null, null, null, null, "fecha_hora_registro DESC")
            if (cursor.moveToFirst()) {
                do {
                    val asistencia = MAsistencia(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_clase")),
                        cursor.getString(cursor.getColumnIndexOrThrow("id_estudiante")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora_registro")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("qr_verificado")) == 1
                    )
                    list.add(asistencia)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }

        // Marcar asistencia con QR verificado
        fun marcarAsistencia(context: Context, idClase: Int, idEstudiante: String): MAsistencia {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val fechaHoraActual = sdf.format(Date())
            
            val asistencia = MAsistencia(idClase, idEstudiante, fechaHoraActual, true)
            asistencia.insertar(context)
            return asistencia
        }
    }

    // actualizar(): boolean
    fun actualizar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("fecha_hora_registro", fechaHoraRegistro)
            put("qr_verificado", if (qrVerificado) 1 else 0)
        }
        val rows = db.update(DBHelper.TABLE_ASISTENCIA, values, "id_clase = ? AND id_estudiante = ?", arrayOf(idClase.toString(), idEstudiante))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val rows = db.delete(DBHelper.TABLE_ASISTENCIA, "id_clase = ? AND id_estudiante = ?", arrayOf(idClase.toString(), idEstudiante))
        db.close()
        return rows > 0
    }
}
