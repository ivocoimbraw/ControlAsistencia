package com.example.controlasistencia.model

/**
 * Data class pura para Materia. La lógica de persistencia vive ahora en MateriaDAO.
 */
data class MMateria(
    val id: Int = 0,
    val nombre: String = ""
)