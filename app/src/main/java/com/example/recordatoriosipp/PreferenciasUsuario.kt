package com.example.recordatoriosipp

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.preferenciasDataStore by preferencesDataStore(
    name = "preferencias_usuario"
)

enum class OrdenRecordatorios {
    FECHA,
    PRIORIDAD
}

data class PreferenciasUsuario(
    val modoOscuro: Boolean = false,
    val mostrarCompletados: Boolean = true,
    val orden: OrdenRecordatorios = OrdenRecordatorios.FECHA
)

class PreferenciasRepository(context: Context) {

    private val dataStore =
        context.applicationContext.preferenciasDataStore

    private object Claves {
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val MOSTRAR_COMPLETADOS = booleanPreferencesKey("mostrar_completados")
        val ORDEN = stringPreferencesKey("orden_recordatorios")
    }

    val preferencias: Flow<PreferenciasUsuario> =
        dataStore.data
            .catch { error ->
                if (error is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw error
                }
            }
            .map { valores ->
                PreferenciasUsuario(
                    modoOscuro = valores[Claves.MODO_OSCURO] ?: false,
                    mostrarCompletados = valores[Claves.MOSTRAR_COMPLETADOS] ?: true,
                    orden = OrdenRecordatorios.entries.firstOrNull {
                        it.name == valores[Claves.ORDEN]
                    } ?: OrdenRecordatorios.FECHA
                )
            }

    suspend fun cambiarModoOscuro(activado: Boolean) {
        dataStore.edit {
            it[Claves.MODO_OSCURO] = activado
        }
    }

    suspend fun cambiarMostrarCompletados(mostrar: Boolean) {
        dataStore.edit {
            it[Claves.MOSTRAR_COMPLETADOS] = mostrar
        }
    }

    suspend fun cambiarOrden(orden: OrdenRecordatorios) {
        dataStore.edit {
            it[Claves.ORDEN] = orden.name
        }
    }
}
