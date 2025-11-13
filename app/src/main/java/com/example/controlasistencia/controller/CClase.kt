package com.example.controlasistencia.controller

import android.content.Context
import com.example.controlasistencia.model.MClase
import com.example.controlasistencia.model.MGrupo
import com.example.controlasistencia.model.MMateria
import com.example.controlasistencia.model.MQrCode
import com.example.controlasistencia.view.VClase

class CClase(
    private val context: Context,
    private val view: VClase
) {

    fun cargarClases() {
        val clases = MClase.listar(context)
        view.mostrarClases(clases)
    }

    fun cargarGrupos() {
        val grupos = MGrupo.listar(context)
        val materias = MMateria.listar(context)
        view.mostrarGrupos(grupos, materias)
    }

    fun mostrarCrear() {
        cargarGrupos()
        view.mostrarFormularioCrear()
    }

    fun mostrarEditar(clase: MClase) {
        cargarGrupos()
        view.mostrarFormularioEditar(clase)
    }

    fun crearClase(fecha: String, horaInicio: String, horaFin: String, estado: String, idGrupo: Int) {
        val clase = MClase(0, fecha, horaInicio, horaFin, estado, idGrupo)
        
        // Insertar clase primero
        if (clase.insertar(context)) {
            // Generar QR automáticamente para la clase
            val qrCode = MQrCode.generarQrParaClase(context, clase.id, fecha)
            val qrId = qrCode.insertar(context)
            
            if (qrId > 0) {
                // Actualizar la clase con el ID del QR
                clase.idQrCode = qrId.toInt()
                clase.actualizar(context)
            }
        }
        
        actualizarVista()
    }

    fun actualizarClase(clase: MClase, fecha: String, horaInicio: String, horaFin: String, estado: String, idGrupo: Int) {
        clase.fecha = fecha
        clase.horaInicio = horaInicio
        clase.horaFin = horaFin
        clase.estado = estado
        clase.idGrupo = idGrupo
        clase.actualizar(context)
        actualizarVista()
    }

    fun eliminarClase(clase: MClase) {
        clase.eliminar(context)
        actualizarVista()
    }

    fun obtenerClase(id: Int): MClase? {
        return MClase.obtener(context, id)
    }

    fun obtenerGrupo(idGrupo: Int): String {
        return MGrupo.obtenerGrupo(context, idGrupo)
    }

    fun obtenerMateria(idMateria: Int): String {
        val materia = MMateria.obtener(context, idMateria)
        return materia?.nombre ?: "Materia no encontrada"
    }

    fun obtenerQrCode(idQrCode: Int): MQrCode? {
        return MQrCode.obtener(context, idQrCode)
    }

    fun actualizarVista() {
        cargarGrupos()
        cargarClases()
    }
    
    // Métodos para instanciar modelos
    fun crearInstanciaClase(fecha: String = "", horaInicio: String = "", horaFin: String = "", estado: String = "programada", idGrupo: Int = 0, idQrCode: Int = 0): MClase {
        return MClase(fecha = fecha, horaInicio = horaInicio, horaFin = horaFin, estado = estado, idGrupo = idGrupo, idQrCode = idQrCode)
    }
    
    fun crearInstanciaClaseVacia(): MClase {
        return MClase()
    }
    
    fun crearInstanciaQrCode(contenido: String = "", fechaGeneracion: String = "", idClase: Int = 0): MQrCode {
        return MQrCode()
    }
    
    // Método para inicializar la vista y configurar listeners
    fun inicializar() {
        view.inicializarComponentes()
        configurarListeners()
        actualizarVista()
    }
    
    private fun configurarListeners() {
        // Configurar listeners de botones
        view.setOnGuardarClick { guardarClase() }
        view.setOnAgregarClick { mostrarCrear() }
        view.setOnEliminarClick { eliminarClaseActual() }
        view.setOnDescargarQrClick { descargarQr() }
        
        // Configurar listener del ListView
        view.setOnClaseSeleccionada { position ->
            manejarSeleccionClase(position)
        }
    }
    
    private fun guardarClase() {
        val datosFormulario = view.obtenerDatosFormulario()
        val grupos = view.obtenerGrupos()
        
        if (grupos.isNotEmpty() && datosFormulario.grupoSeleccionado >= 0 && datosFormulario.grupoSeleccionado < grupos.size) {
            val grupoSeleccionado = grupos[datosFormulario.grupoSeleccionado]
            val claseEditando = view.obtenerClaseEditando()

            if (claseEditando != null) {
                actualizarClase(
                    claseEditando, 
                    datosFormulario.fecha, 
                    datosFormulario.horaInicio, 
                    datosFormulario.horaFin, 
                    datosFormulario.estado, 
                    grupoSeleccionado.id
                )
            } else {
                crearClase(
                    datosFormulario.fecha, 
                    datosFormulario.horaInicio, 
                    datosFormulario.horaFin, 
                    datosFormulario.estado, 
                    grupoSeleccionado.id
                )
            }
            view.limpiarFormulario()
        }
    }
    
    private fun eliminarClaseActual() {
        val claseEditando = view.obtenerClaseEditando()
        claseEditando?.let {
            eliminarClase(it)
            view.limpiarFormulario()
        }
    }
    
    private fun descargarQr() {
        val claseEditando = view.obtenerClaseEditando()
        if (claseEditando?.idQrCode != null) {
            val qr = obtenerQrCode(claseEditando.idQrCode!!)
            if (qr != null) {
                view.descargarQrArchivo(qr.qrCode, claseEditando.id)
            }
        }
    }
    
    private fun manejarSeleccionClase(position: Int) {
        val clases = MClase.listar(context)
        if (position >= 0 && position < clases.size) {
            val claseSeleccionada = clases[position]
            mostrarEditar(claseSeleccionada)
            mostrarQrDeClase(claseSeleccionada)
        }
    }
    
    private fun mostrarQrDeClase(clase: MClase) {
        if (clase.idQrCode != null) {
            val qr = obtenerQrCode(clase.idQrCode!!)
            if (qr != null) {
                view.mostrarQr(qr.qrCode, true)
            } else {
                view.mostrarQr("", false)
            }
        } else {
            view.mostrarQr("", false)
        }
    }
}