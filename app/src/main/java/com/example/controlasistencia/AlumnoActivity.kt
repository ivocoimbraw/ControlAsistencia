package com.example.controlasistencia

import android.app.Activity
import android.os.Bundle
import com.example.controlasistencia.view.VAlumno
import com.example.controlasistencia.controller.CAlumno
import com.example.controlasistencia.data.alumno.AlumnoDataSource
import com.example.controlasistencia.data.alumno.AlumnoSqliteDataSource
import com.example.controlasistencia.data.alumno.CompressionDecorator
import com.example.controlasistencia.data.alumno.EncryptionDecorator

/**
 * Cliente: AlumnoActivity
 * 
 * El cliente puede envolver componentes en varias capas de decoradores,
 * siempre que trabajen con todos los objetos a través de la interfaz del componente.
 * 
 * Ejemplo del patrón Decorator siguiendo Refactoring Guru:
 * - AlumnoSqliteDataSource: Componente Concreto (FileDataSource)
 * - CompressionDecorator: Añade compresión de datos
 * - EncryptionDecorator: Añade encriptación de datos
 * 
 * La pila de decoradores es: Encryption > Compression > SQLite
 */
class AlumnoActivity : Activity() {

    private lateinit var view: VAlumno
    private lateinit var controller: CAlumno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        view = VAlumno(this)
        
        // Configuración del DataSource con decoradores (Opción 1 - Uso simple)
        // La variable source ahora contiene: Encryption > Compression > SQLite
        var source: AlumnoDataSource = AlumnoSqliteDataSource(this)
        source = CompressionDecorator(source)  // Envuelve con compresión
        source = EncryptionDecorator(source)   // Envuelve con encriptación
        
        // Ahora cuando insertemos/leamos datos:
        // 1. EncryptionDecorator encripta/desencripta
        // 2. CompressionDecorator comprime/descomprime
        // 3. AlumnoSqliteDataSource escribe/lee de la BD
        
        controller = CAlumno(this, view, source)
        controller.inicializar()
    }
}