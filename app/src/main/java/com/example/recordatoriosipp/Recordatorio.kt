package com.example.recordatoriosipp

data class Recordatorio(
    val id: String = java.util.UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: Prioridad,
    val completado: Boolean,
    val fechaLimite: String? = null
) {
    init {
        require(titulo.trim().length >= 4) {
            "El titulo debe tener al menos 4 caracteres"
        }

        require(descripcion.length <= 200) {
            "La descripcion no puede superar los 200 caracteres"
        }

        require(categoria.isNotBlank()) {
            "La categoria noo puede estar vacia"
        }
    }
}

