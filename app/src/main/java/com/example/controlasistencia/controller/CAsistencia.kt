package com.example.controlasistencia.controller

import android.content.Context
import android.content.Intent
import android.widget.AdapterView
import com.example.controlasistencia.model.MAsistencia
import com.example.controlasistencia.model.MQrCode
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MAlumno
import com.example.controlasistencia.view.VAsistencia
import com.google.zxing.integration.android.IntentIntegrator

class CAsistencia(
    private val context: Context,
    private val view: VAsistencia
) {

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
    
    // Método para inicializar la vista y configurar listeners
    fun inicializar() {
        view.inicializarComponentes()
        cargarEstudiantes()
        configurarListeners()
        cargarAsistencias()
    }
    
    private fun cargarEstudiantes() {
        val estudiantes = MAlumno.listar(context)
        view.mostrarEstudiantes(estudiantes)
    }
    
    private fun configurarListeners() {
        // Configurar listener del spinner de estudiantes
        view.setOnEstudianteSeleccionado { estudiante ->
            view.habilitarBotones(estudiante != null)
        }
        
        // Configurar listener del botón escanear QR
        view.setOnEscanearQrClick {
            iniciarEscaneoQr()
        }
        
        // Configurar listener del botón ingreso manual
        view.setOnIngresarManualClick {
            view.mostrarDialogoQrManual { qrCode ->
                procesarAsistencia(qrCode)
            }
        }
    }
    
    private fun iniciarEscaneoQr() {
        view.iniciarEscaneoQr()
    }
    
    fun procesarResultadoEscaneo(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                view.mostrarResultado("Escaneo cancelado", false)
            } else {
                val qrCode = result.contents
                procesarAsistencia(qrCode)
            }
        }
    }
    
    private fun procesarAsistencia(qrCode: String) {
        val estudianteSeleccionado = view.obtenerEstudianteSeleccionado()
        if (estudianteSeleccionado != null) {
            val exito = marcarAsistencia(qrCode, estudianteSeleccionado.registro)
            if (exito) {
                view.mostrarResultado("¡Asistencia marcada exitosamente!", true)
                cargarAsistencias()
            } else {
                view.mostrarResultado("Error: Código QR inválido, clase no encontrada o asistencia ya marcada", false)
            }
        } else {
            view.mostrarResultado("Error: No se ha seleccionado un estudiante", false)
        }
    }
    
    private fun cargarAsistencias() {
        val asistencias = obtenerAsistencias()
        val estudiantes = MAlumno.listar(context)
        
        val asistenciaTextos = asistencias.map { asistencia ->
            val estudiante = estudiantes.find { it.registro == asistencia.idEstudiante }
            val nombreEstudiante = estudiante?.let { "${it.nombre} ${it.apellidop}" } ?: asistencia.idEstudiante
            "Clase ID: ${asistencia.idClase} - $nombreEstudiante - ${asistencia.fechaHoraRegistro}"
        }
        
        view.mostrarAsistencias(asistenciaTextos)
    }
    
    // Métodos para instanciar modelos
    fun crearInstanciaAsistencia(idClase: Int = 0, idEstudiante: String = "", fechaHoraRegistro: String = ""): MAsistencia {
        return MAsistencia(idClase, idEstudiante, fechaHoraRegistro)
    }
    
    fun crearInstanciaAsistenciaVacia(): MAsistencia {
        return MAsistencia(0, "", "")
    }
    
    fun crearInstanciaAlumno(registro: String = "", apellidop: String = "", apellidom: String = "", nombre: String = ""): MAlumno {
        return MAlumno(registro, apellidop, apellidom, nombre)
    }
    
    fun crearInstanciaQrCode(contenido: String = "", fechaGeneracion: String = "", idClase: Int = 0): MQrCode {
        return MQrCode()
    }
    
    fun crearInstanciaClase(fecha: String = "", horaInicio: String = "", horaFin: String = "", estado: String = "programada", idGrupo: Int = 0, idQrCode: Int = 0): MClase {
        return MClase(fecha = fecha, horaInicio = horaInicio, horaFin = horaFin, estado = estado, idGrupo = idGrupo, idQrCode = idQrCode)
    }
}