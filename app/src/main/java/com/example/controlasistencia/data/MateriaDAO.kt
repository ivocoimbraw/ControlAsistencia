package com.example.controlasistencia.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.controlasistencia.DBHelper
import com.example.controlasistencia.model.MMateria

class MateriaDAO(context: Context) : BasePlantillaDAO<MMateria>(context) {
    override fun nombreTabla() = DBHelper.TABLE_MATERIA
    override fun columnaId() = "id"

    override fun mapearCursor(cursor: Cursor): MMateria {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
        return MMateria(id, nombre)
    }

    override fun contentValues(entity: MMateria, isUpdate: Boolean): ContentValues =
        ContentValues().apply {
            put("nombre", entity.nombre.trim())
        }

    override fun validarParaInsert(entity: MMateria) {
        require(entity.nombre.isNotBlank()) { "Nombre de materia vacío" }
    }

    override fun validarParaUpdate(entity: MMateria) {
        require(entity.nombre.isNotBlank()) { "Nombre de materia vacío" }
    }
}
