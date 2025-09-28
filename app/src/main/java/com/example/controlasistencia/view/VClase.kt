package com.example.controlasistencia.view

import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import android.widget.*
import com.example.controlasistencia.R
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MGrupo
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class VClase(private val activity: Activity) {
    private val datePicker: DatePicker = activity.findViewById(R.id.datePicker)
    private val timePickerInicio: TimePicker = activity.findViewById(R.id.timePickerInicio)
    private val timePickerFin: TimePicker = activity.findViewById(R.id.timePickerFin)
    private val slcEstado: Spinner = activity.findViewById(R.id.slcEstado)
    private val slcGrupo: Spinner = activity.findViewById(R.id.slcGrupo)
    private val btnGuardar: Button = activity.findViewById(R.id.btnGuardarClase)
    private val btnAgregar: Button = activity.findViewById(R.id.btnAgregarClase)
    private val btnEliminar: Button = activity.findViewById(R.id.btnEliminarClase)
    private val lvClases: ListView = activity.findViewById(R.id.lvClases)
    private val imgQrCode: ImageView = activity.findViewById(R.id.imgQrCode)
    private val btnDescargarQr: Button = activity.findViewById(R.id.btnDescargarQr)

    private var claseEditando: MClase? = null
    private var adapter: ArrayAdapter<String>? = null
    private var grupos: List<MGrupo> = emptyList()
    private val estadosClase = arrayOf("PROGRAMADA", "CANCELADA")
    
    // Callbacks para el controlador
    private var onGuardarClick: (() -> Unit)? = null
    private var onAgregarClick: (() -> Unit)? = null
    private var onEliminarClick: (() -> Unit)? = null
    private var onDescargarQrClick: (() -> Unit)? = null
    private var onClaseSeleccionada: ((Int) -> Unit)? = null

    fun inicializarComponentes() {
        setupEstados()
        
        // Inicialmente botón eliminar y descarga QR deshabilitados
        btnEliminar.isEnabled = false
        btnDescargarQr.isEnabled = false
        imgQrCode.setImageResource(android.R.color.transparent)
    }
    
    private fun setupEstados() {
        val estadoAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, estadosClase)
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        slcEstado.adapter = estadoAdapter
        slcEstado.setSelection(0) // "PROGRAMADA" por defecto
    }
    
    // Métodos para configurar callbacks desde el controlador
    fun setOnGuardarClick(callback: () -> Unit) {
        this.onGuardarClick = callback
        btnGuardar.setOnClickListener { callback() }
    }
    
    fun setOnAgregarClick(callback: () -> Unit) {
        this.onAgregarClick = callback
        btnAgregar.setOnClickListener { callback() }
    }
    
    fun setOnEliminarClick(callback: () -> Unit) {
        this.onEliminarClick = callback
        btnEliminar.setOnClickListener { callback() }
    }
    
    fun setOnDescargarQrClick(callback: () -> Unit) {
        this.onDescargarQrClick = callback
        btnDescargarQr.setOnClickListener { callback() }
    }
    
    fun setOnClaseSeleccionada(callback: (Int) -> Unit) {
        this.onClaseSeleccionada = callback
        lvClases.setOnItemClickListener { _, _, position, _ ->
            callback(position)
        }
    }

    fun mostrarClases(clases: List<MClase>, gruposDisponibles: List<MGrupo> = emptyList()) {
        val gruposParaBuscar = if (gruposDisponibles.isNotEmpty()) gruposDisponibles else grupos
        val items = clases.map { 
            val grupo = gruposParaBuscar.find { grupo -> grupo.id == it.idGrupo }
            val materiaGrupo = grupo?.let { "${it.id_materia} - ${it.nombre}" } ?: "Grupo ${it.idGrupo}"
            "${it.id} - ${it.fecha} ${it.horaInicio}-${it.horaFin} (${materiaGrupo}) [${it.estado}]"
        }
        adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, items)
        lvClases.adapter = adapter
    }

    fun mostrarGrupos(grupos: List<MGrupo>) {
        this.grupos = grupos
        val items = grupos.map { "${it.id_materia} - ${it.nombre}" }
        val spinnerAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        slcGrupo.adapter = spinnerAdapter
    }

    fun mostrarFormularioCrear() {
        // Establecer fecha actual
        val calendar = Calendar.getInstance()
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        
        // Establecer hora actual para inicio
        timePickerInicio.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePickerInicio.minute = calendar.get(Calendar.MINUTE)
        
        // Establecer hora actual + 1 hora para fin
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        timePickerFin.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePickerFin.minute = calendar.get(Calendar.MINUTE)
        
        slcEstado.setSelection(0) // "PROGRAMADA" por defecto
        if (grupos.isNotEmpty()) slcGrupo.setSelection(0)
        claseEditando = null
        btnEliminar.isEnabled = false
        imgQrCode.setImageResource(android.R.color.transparent)
        btnDescargarQr.isEnabled = false
    }

    fun mostrarFormularioEditar(clase: MClase) {
        // Parsear fecha
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(clase.fecha)
            val calendar = Calendar.getInstance()
            calendar.time = date ?: Date()
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        } catch (e: Exception) {
            // Si hay error, usar fecha actual
            val calendar = Calendar.getInstance()
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
        
        // Parsear hora inicio
        try {
            val timeParts = clase.horaInicio.split(":")
            timePickerInicio.hour = timeParts[0].toInt()
            timePickerInicio.minute = timeParts[1].toInt()
        } catch (e: Exception) {
            timePickerInicio.hour = 8
            timePickerInicio.minute = 0
        }
        
        // Parsear hora fin
        try {
            val timeParts = clase.horaFin.split(":")
            timePickerFin.hour = timeParts[0].toInt()
            timePickerFin.minute = timeParts[1].toInt()
        } catch (e: Exception) {
            timePickerFin.hour = 9
            timePickerFin.minute = 0
        }
        
        val estadoIndex = estadosClase.indexOf(clase.estado)
        if (estadoIndex >= 0) slcEstado.setSelection(estadoIndex) else slcEstado.setSelection(0)
        
        val grupoIndex = grupos.indexOfFirst { it.id == clase.idGrupo }
        if (grupoIndex >= 0) slcGrupo.setSelection(grupoIndex)
        claseEditando = clase
        btnEliminar.isEnabled = true
    }

    // Clase para encapsular los datos del formulario
    data class DatosFormulario(
        val fecha: String,
        val horaInicio: String,
        val horaFin: String,
        val estado: String,
        val grupoSeleccionado: Int
    )
    
    // Métodos para obtener información del estado actual
    fun obtenerDatosFormulario(): DatosFormulario {
        val fecha = String.format("%04d-%02d-%02d", datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
        val horaInicio = String.format("%02d:%02d", timePickerInicio.hour, timePickerInicio.minute)
        val horaFin = String.format("%02d:%02d", timePickerFin.hour, timePickerFin.minute)
        val estado = estadosClase[slcEstado.selectedItemPosition]
        val grupoSeleccionado = slcGrupo.selectedItemPosition
        
        return DatosFormulario(fecha, horaInicio, horaFin, estado, grupoSeleccionado)
    }
    
    fun obtenerClaseEditando(): MClase? {
        return claseEditando
    }
    
    fun obtenerGrupos(): List<MGrupo> {
        return grupos
    }

    fun limpiarFormulario() {
        // Restablecer a fecha y hora actual
        val calendar = Calendar.getInstance()
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        timePickerInicio.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePickerInicio.minute = calendar.get(Calendar.MINUTE)
        
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        timePickerFin.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePickerFin.minute = calendar.get(Calendar.MINUTE)
        
        slcEstado.setSelection(0) // "PROGRAMADA" por defecto
        if (grupos.isNotEmpty()) slcGrupo.setSelection(0)
        claseEditando = null
        btnEliminar.isEnabled = false
        imgQrCode.setImageResource(android.R.color.transparent)
        btnDescargarQr.isEnabled = false
    }
    
    fun mostrarQr(qrCode: String, habilitarDescarga: Boolean) {
        if (qrCode.isNotEmpty() && habilitarDescarga) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(qrCode, BarcodeFormat.QR_CODE, 200, 200)
                imgQrCode.setImageBitmap(bitmap)
                btnDescargarQr.isEnabled = true
            } catch (e: Exception) {
                e.printStackTrace()
                imgQrCode.setImageResource(android.R.color.transparent)
                btnDescargarQr.isEnabled = false
            }
        } else {
            imgQrCode.setImageResource(android.R.color.transparent)
            btnDescargarQr.isEnabled = false
        }
    }
    
    fun descargarQrArchivo(qrCode: String, claseId: Int) {
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(qrCode, BarcodeFormat.QR_CODE, 400, 400)
            
            val fileName = "QR_Clase_${claseId}.png"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            
            Toast.makeText(activity, "QR descargado en Descargas/$fileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Error al descargar QR", Toast.LENGTH_SHORT).show()
        }
    }
}