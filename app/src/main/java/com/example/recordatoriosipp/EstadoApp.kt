package com.example.recordatoriosipp

sealed class  EstadoApp {
    data object Cargando : EstadoApp()

    data object Vacio : EstadoApp()

    data class Exito(
        val recordatorios: List<Recordatorio>
    ) : EstadoApp()

    data class Error(
        val mensaje: String
    ) : EstadoApp()
}