package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MQrCode

class CQrCode(private val context: Context) {

    fun obtenerQrPorId(id: Int): MQrCode? {
        return MQrCode.obtener(context, id)
    }

    fun insertarQr(qrCode: MQrCode): Long {
        return qrCode.insertar(context)
    }
}