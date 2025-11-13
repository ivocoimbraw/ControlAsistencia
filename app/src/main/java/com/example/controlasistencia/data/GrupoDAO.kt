package com.example.controlasistencia.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.controlasistencia.DBHelper
import com.example.controlasistencia.model.MGrupo

class GrupoDAO(context: Context) : BasePlantillaDAO<MGrupo>(context) {
    override fun nombreTabla() = DBHelper.TABLE_GRUPO
    override fun columnaId() = "id"

    override fun mapearCursor(cursor: Cursor): MGrupo {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        val idMateria = cursor.getInt(cursor.getColumnIndexOrThrow("id_materia"))
        return MGrupo(id, nombre, idMateria)
    }

    override fun contentValues(entity: MGrupo, isUpdate: Boolean): ContentValues =
        ContentValues().apply {
            put("nombre", entity.nombre.trim())
            put("id_materia", entity.id_materia)
        }

    override fun validarParaInsert(entity: MGrupo) {
        require(entity.nombre.isNotBlank()) { "Nombre de grupo vacío" }
    }

    override fun validarParaUpdate(entity: MGrupo) {
        require(entity.nombre.isNotBlank()) { "Nombre de grupo vacío" }
    }
}
