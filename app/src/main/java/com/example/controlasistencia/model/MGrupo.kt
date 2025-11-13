package com.example.controlasistencia.model

/**
 * Data class pura para Grupo. Persistencia movida a GrupoDAO.
 */
data class MGrupo(
    val id: Int = 0,
    val nombre: String = "",
    val id_materia: Int = 0
)
