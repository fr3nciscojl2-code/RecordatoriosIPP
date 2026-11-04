package com.example.recordatoriosipp

data class Recordatorio(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: Prioridad,
    val completado: Boolean,
    val fechaLimite: String? = null
) {
    init {
        require(titulo.isNotBlank()) {
            "El titulo no puede estar vacio"
        }

        require(descripcion.isNotBlank()) {
            "La descripcion no puede estar vacia"
        }

        require(categoria.isNotBlank()) {
            "La categoria noo puede estar vacia"
        }
    }
}

