package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MAsistencia
import com.example.controlasistencia.model.MQrCode
import com.example.controlasistencia.model.MClase

class CAsistencia(private val context: Context) {

    fun marcarAsistencia(qrCode: String, registroEstudiante: String): Boolean {
        // Buscar el QR en la base de datos
        val qrCodeObj = buscarQrPorCodigo(qrCode) ?: return false
        
        // Buscar la clase asociada a este QR
        val clase = buscarClasePorQrId(qrCodeObj.id) ?: return false
        
        // Verificar si ya existe una asistencia para este estudiante y clase
        val asistenciaExistente = MAsistencia.obtener(context, clase.id, registroEstudiante)
        if (asistenciaExistente != null) {
            return false // Ya marcó asistencia
        }
        
        // Marcar asistencia
        MAsistencia.marcarAsistencia(context, clase.id, registroEstudiante)
        return true
    }
    
    fun obtenerAsistencias(): List<MAsistencia> {
        return MAsistencia.listar(context)
    }
    
    private fun buscarQrPorCodigo(qrCode: String): MQrCode? {
        // Obtener todos los QR y buscar por código
        val qrCodes = obtenerTodosLosQr()
        return qrCodes.find { it.qrCode == qrCode }
    }
    
    private fun buscarClasePorQrId(qrId: Int): MClase? {
        // Obtener todas las clases y buscar por id_qr_code
        val clases = obtenerTodasLasClases()
        return clases.find { it.idQrCode == qrId }
    }
    
    private fun obtenerTodosLosQr(): List<MQrCode> {
        return MQrCode.listar(context)
    }
    
    private fun obtenerTodasLasClases(): List<MClase> {
        return MClase.listar(context)
    }
}