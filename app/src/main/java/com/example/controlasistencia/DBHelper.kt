package com.example.controlasistencia

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "appdb.db"
        const val DATABASE_VERSION = 5  // Incrementar versión para la nueva tabla asistencia
        const val TABLE_MATERIA = "materia"
        const val TABLE_GRUPO = "grupo"
        const val TABLE_ESTUDIANTE = "estudiante"
        const val TABLE_CLASE = "clase"
        const val TABLE_QR_CODE = "qr_code"
        const val TABLE_ASISTENCIA = "asistencia"

        // SQL provided by user - must be exact
        const val SQL_CREATE_MATERIA = """
            CREATE TABLE materia (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nombre VARCHAR(255) NOT NULL
            );
        """

        const val SQL_CREATE_GRUPO = """
            CREATE TABLE grupo (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nombre VARCHAR(255) NOT NULL,
                id_materia INT NOT NULL,
                FOREIGN KEY (id_materia) REFERENCES materia(id)
            );
        """

        const val SQL_CREATE_ESTUDIANTE = """
            CREATE TABLE estudiante (
                registro VARCHAR(50) PRIMARY KEY,
                apellidop VARCHAR(100) NOT NULL,
                apellidom VARCHAR(100) NOT NULL,
                nombre VARCHAR(100) NOT NULL
            );
        """

        const val SQL_CREATE_QR_CODE = """
            CREATE TABLE qr_code (
                id INT PRIMARY KEY AUTO_INCREMENT,
                qr_code VARCHAR(255) NOT NULL,
                fecha_creacion DATE NOT NULL,
                fecha_expiracion DATE NOT NULL
            );
        """

        const val SQL_CREATE_ASISTENCIA = """
            CREATE TABLE asistencia (
                fecha_hora_registro DATETIME NOT NULL,
                qr_verificado BOOLEAN NOT NULL DEFAULT FALSE,
                id_clase INT,
                id_estudiante INT,
                PRIMARY KEY (id_clase, id_estudiante),
                FOREIGN KEY (id_clase) REFERENCES clase(id),
                FOREIGN KEY (id_estudiante) REFERENCES estudiante(registro)
            );
        """

        const val SQL_CREATE_CLASE = """
            CREATE TABLE clase (
                id INT PRIMARY KEY AUTO_INCREMENT,
                fecha DATE NOT NULL,
                hora_inicio TIME NOT NULL,
                hora_fin TIME NOT NULL,
                estado VARCHAR(50) NOT NULL,
                id_grupo INT,
                id_qr_code INT UNIQUE,
                FOREIGN KEY (id_qr_code) REFERENCES qr_code(id),
                FOREIGN KEY (id_grupo) REFERENCES grupo(id)
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        // SQLite doesn't support AUTO_INCREMENT or INT PRIMARY KEY AUTO_INCREMENT syntax the same way as MySQL
        // We'll create equivalent SQLite table but keep the user's SQL text in the source as required.
        val sqliteCreateMateria = """
            CREATE TABLE $TABLE_MATERIA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            );
        """

        val sqliteCreateGrupo = """
            CREATE TABLE $TABLE_GRUPO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                id_materia INTEGER NOT NULL,
                FOREIGN KEY (id_materia) REFERENCES $TABLE_MATERIA(id)
            );
        """

        val sqliteCreateEstudiante = """
            CREATE TABLE $TABLE_ESTUDIANTE (
                registro TEXT PRIMARY KEY,
                apellidop TEXT NOT NULL,
                apellidom TEXT NOT NULL,
                nombre TEXT NOT NULL
            );
        """

        val sqliteCreateQrCode = """
            CREATE TABLE $TABLE_QR_CODE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                qr_code TEXT NOT NULL,
                fecha_creacion TEXT NOT NULL,
                fecha_expiracion TEXT NOT NULL
            );
        """

        val sqliteCreateClase = """
            CREATE TABLE $TABLE_CLASE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha TEXT NOT NULL,
                hora_inicio TEXT NOT NULL,
                hora_fin TEXT NOT NULL,
                estado TEXT NOT NULL,
                id_grupo INTEGER,
                id_qr_code INTEGER UNIQUE,
                FOREIGN KEY (id_qr_code) REFERENCES $TABLE_QR_CODE(id),
                FOREIGN KEY (id_grupo) REFERENCES $TABLE_GRUPO(id)
            );
        """

        val sqliteCreateAsistencia = """
            CREATE TABLE $TABLE_ASISTENCIA (
                fecha_hora_registro TEXT NOT NULL,
                qr_verificado INTEGER NOT NULL DEFAULT 0,
                id_clase INTEGER,
                id_estudiante TEXT,
                PRIMARY KEY (id_clase, id_estudiante),
                FOREIGN KEY (id_clase) REFERENCES $TABLE_CLASE(id),
                FOREIGN KEY (id_estudiante) REFERENCES $TABLE_ESTUDIANTE(registro)
            );
        """

        db.execSQL(sqliteCreateMateria)
        db.execSQL(sqliteCreateGrupo)
        db.execSQL(sqliteCreateEstudiante)
        db.execSQL(sqliteCreateQrCode)
        db.execSQL(sqliteCreateClase)
        db.execSQL(sqliteCreateAsistencia)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ASISTENCIA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLASE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QR_CODE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ESTUDIANTE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GRUPO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MATERIA")
        onCreate(db)
    }
}
