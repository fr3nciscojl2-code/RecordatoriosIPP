package com.example.recordatoriosipp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecordatoriosViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio: IRecordatorioRepository =
        RecordatorioRepository(
            RecordatoriosDatabase
                .obtenerInstancia(application)
                .recordatorioDao()
        )

    private val _estado =
        MutableStateFlow<EstadoApp>(EstadoApp.Cargando)

    val estado: StateFlow<EstadoApp> = _estado

    private val _sincronizando = MutableStateFlow(false)

    val sincronizando: StateFlow<Boolean> = _sincronizando

    private val _mensajeSincronizacion =
        MutableStateFlow<String?>(null)

    val mensajeSincronizacion: StateFlow<String?> =
        _mensajeSincronizacion

    private var cargaJob: Job? = null

    init {
        cargarRecordatorios()
    }

    fun cargarRecordatorios() {
        cargaJob?.cancel()

        cargaJob = viewModelScope.launch {
            _estado.value = EstadoApp.Cargando

            try {
                repositorio.observarTodos().collect { lista ->
                    _estado.value = if (lista.isEmpty()) {
                        EstadoApp.Vacio
                    } else {
                        EstadoApp.Exito(lista)
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = EstadoApp.Error(
                    "No se pudieron cargar los recordatorios"
                )
            }
        }
    }

    fun agregarRecordatorio(
        titulo: String,
        descripcion: String,
        categoria: String,
        prioridad: String
    ) {
        viewModelScope.launch {
            try {
                val nuevo = Recordatorio(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    prioridad = Prioridad.valueOf(prioridad),
                    completado = false
                )

                repositorio.agregarRecordatorio(nuevo)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = EstadoApp.Error(
                    "No se pudo guardar el recordatorio"
                )
            }
        }
    }

    fun obtenerRecordatorioPorId(id: String): Recordatorio? {
        val actual = _estado.value

        return if (actual is EstadoApp.Exito) {
            actual.recordatorios.find { it.id == id }
        } else {
            null
        }
    }

    fun actualizarRecordatorio(recordatorio: Recordatorio) {
        viewModelScope.launch {
            try {
                repositorio.actualizarRecordatorio(recordatorio)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = EstadoApp.Error(
                    "No se pudo actualizar el recordatorio"
                )
            }
        }
    }

    fun eliminarRecordatorio(id: String) {
        viewModelScope.launch {
            try {
                repositorio.eliminarRecordatorio(id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _estado.value = EstadoApp.Error(
                    "No se pudo eliminar el recordatorio"
                )
            }
        }
    }

    fun sincronizarRecordatorios() {
        if (_sincronizando.value) return

        _sincronizando.value = true
        _mensajeSincronizacion.value = null

        viewModelScope.launch {
            try {
                repositorio.sincronizar()

                _mensajeSincronizacion.value =
                    "Sincronización completada correctamente"

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensajeSincronizacion.value =
                    "No se pudo completar la sincronización. Intenta nuevamente."
            } finally {
                _sincronizando.value = false
            }
        }
    }

    fun limpiarMensajeSincronizacion() {
        _mensajeSincronizacion.value = null
    }
}