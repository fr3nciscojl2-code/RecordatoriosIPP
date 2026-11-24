package com.example.recordatoriosipp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface IRecordatorioRepository {

    fun observarTodos(): Flow<List<Recordatorio>>

    suspend fun obtenerTodos(): List<Recordatorio>

    suspend fun obtenerPorId(id: String): Recordatorio?

    suspend fun agregarRecordatorio(recordatorio: Recordatorio)

    suspend fun actualizarRecordatorio(recordatorio: Recordatorio)

    suspend fun eliminarRecordatorio(id: String)

    suspend fun sincronizar()
}

class RecordatorioRepository(
    private val dao: RecordatorioDao,
    private val api: RecordatorioApiService =
        RecordatorioApiSimulada
) : IRecordatorioRepository {

    private val mutex = Mutex()

    override fun observarTodos(): Flow<List<Recordatorio>> {
        return dao.observarTodos().map { lista ->
            lista.map { it.toRecordatorio() }
        }
    }

    override suspend fun obtenerTodos(): List<Recordatorio> {
        return dao.obtenerTodos().map {
            it.toRecordatorio()
        }
    }

    override suspend fun obtenerPorId(id: String): Recordatorio? {
        return dao.obtenerPorId(id)?.toRecordatorio()
    }

    override suspend fun agregarRecordatorio(
        recordatorio: Recordatorio
    ) {
        mutex.withLock {
            dao.insertar(
                recordatorio.toEntity().copy(
                    actualizadoEn = System.currentTimeMillis(),
                    pendienteSincronizacion = true
                )
            )
        }
    }

    override suspend fun actualizarRecordatorio(
        recordatorio: Recordatorio
    ) {
        mutex.withLock {
            dao.actualizar(
                recordatorio.toEntity().copy(
                    actualizadoEn = System.currentTimeMillis(),
                    pendienteSincronizacion = true
                )
            )
        }
    }

    override suspend fun eliminarRecordatorio(id: String) {
        mutex.withLock {
            dao.eliminar(id)
        }
    }

    override suspend fun sincronizar() {
        mutex.withLock {
            val pendientes = dao.obtenerPendientes()

            for (local in pendientes) {
                val respuesta = api.guardarRecordatorio(
                    local.id,
                    local.toDto()
                )

                if (!respuesta.isSuccessful) {
                    throw IllegalStateException(
                        "Error al enviar recordatorio"
                    )
                }

                dao.marcarSincronizado(
                    local.id,
                    local.actualizadoEn
                )
            }

            val respuesta = api.obtenerRecordatorios()

            if (!respuesta.isSuccessful) {
                throw IllegalStateException(
                    "Error al descargar recordatorios"
                )
            }

            val remotos = respuesta.body().orEmpty()

            for (remoto in remotos) {
                val local =
                    dao.obtenerPorIdIncluyendoEliminados(
                        remoto.id
                    )

                if (local == null ||
                    (!local.pendienteSincronizacion &&
                            remoto.actualizadoEn > local.actualizadoEn)
                ) {
                    dao.insertar(remoto.toEntity())
                }
            }
        }
    }
}