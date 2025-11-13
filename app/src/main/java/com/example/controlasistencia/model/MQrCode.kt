package com.example.controlasistencia.model

import android.content.ContentValues
import android.content.Context
import com.example.controlasistencia.DBHelper
import java.text.SimpleDateFormat
import java.util.*

data class MQrCode(
    var id: Int = 0,
    var qrCode: String = "",
    var fechaCreacion: String = "",
    var fechaExpiracion: String = ""
) {
    // insertar(): boolean
    fun insertar(context: Context): Long {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("qr_code", qrCode)
            put("fecha_creacion", fechaCreacion)
            put("fecha_expiracion", fechaExpiracion)
        }
        val newRowId = db.insert(DBHelper.Companion.TABLE_QR_CODE, null, values)
        db.close()
        return newRowId
    }

    // obtener(id: Int): MQrCode
    companion object {
        fun obtener(context: Context, id: Int): MQrCode? {
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(
                DBHelper.Companion.TABLE_QR_CODE,
                arrayOf("id", "qr_code", "fecha_creacion", "fecha_expiracion"),
                "id = ?",
                arrayOf(id.toString()),
                null, null, null
            )
            var qrCode: MQrCode? = null
            if (cursor.moveToFirst()) {
                qrCode = MQrCode(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("qr_code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_expiracion"))
                )
            }
            cursor.close()
            db.close()
            return qrCode
        }

        // listar(): List<MQrCode>
        fun listar(context: Context): List<MQrCode> {
            val list = mutableListOf<MQrCode>()
            val dbh = DBHelper(context)
            val db = dbh.readableDatabase
            val cursor = db.query(DBHelper.Companion.TABLE_QR_CODE, arrayOf("id", "qr_code", "fecha_creacion", "fecha_expiracion"), null, null, null, null, "id DESC")
            if (cursor.moveToFirst()) {
                do {
                    val qrCode = MQrCode(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("qr_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_creacion")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_expiracion"))
                    )
                    list.add(qrCode)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }

        // Generar QR code para una clase
        fun generarQrParaClase(context: Context, claseId: Int, fecha: String): MQrCode {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            
            // Fecha de creación (hoy)
            val fechaCreacion = sdf.format(calendar.time)
            
            // Fecha de expiración (fecha de la clase + 1 día)
            try {
                calendar.time = sdf.parse(fecha) ?: Date()
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            } catch (e: Exception) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            val fechaExpiracion = sdf.format(calendar.time)
            
            // Generar código QR único
            val timestamp = System.currentTimeMillis()
            val qrCodeString = "CLASE_${claseId}_${timestamp}"
            
            return MQrCode(0, qrCodeString, fechaCreacion, fechaExpiracion)
        }
    }

    // actualizar(): boolean
    fun actualizar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val values = ContentValues().apply {
            put("qr_code", qrCode)
            put("fecha_creacion", fechaCreacion)
            put("fecha_expiracion", fechaExpiracion)
        }
        val rows = db.update(DBHelper.Companion.TABLE_QR_CODE, values, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }

    // eliminar(): boolean
    fun eliminar(context: Context): Boolean {
        val dbh = DBHelper(context)
        val db = dbh.writableDatabase
        val rows = db.delete(DBHelper.Companion.TABLE_QR_CODE, "id = ?", arrayOf(id.toString()))
        db.close()
        return rows > 0
    }
}