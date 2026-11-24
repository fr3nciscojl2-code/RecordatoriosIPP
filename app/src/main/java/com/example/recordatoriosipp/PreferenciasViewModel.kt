package com.example.recordatoriosipp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferenciasViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio =
        PreferenciasRepository(application)

    val preferencias = repositorio.preferencias.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PreferenciasUsuario()
    )

    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            repositorio.cambiarModoOscuro(activado)
        }
    }

    fun cambiarMostrarCompletados(mostrar: Boolean) {
        viewModelScope.launch {
            repositorio.cambiarMostrarCompletados(mostrar)
        }
    }

    fun cambiarOrden(orden: OrdenRecordatorios) {
        viewModelScope.launch {
            repositorio.cambiarOrden(orden)
        }
    }
}
