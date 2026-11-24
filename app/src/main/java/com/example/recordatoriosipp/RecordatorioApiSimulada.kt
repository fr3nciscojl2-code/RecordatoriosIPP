package com.example.recordatoriosipp

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response
import java.io.IOException

object RecordatorioApiSimulada : RecordatorioApiService {

    private val simularError = false

    private val recordatorios =
        mutableMapOf<String, RecordatorioDto>()

    private val mutex = Mutex()

    override suspend fun obtenerRecordatorios():
            Response<List<RecordatorioDto>> {

        delay(500)

        if (simularError) {
            throw IOException("Error de conexión simulado")
        }

        return mutex.withLock {
            Response.success(recordatorios.values.toList())
        }
    }

    override suspend fun guardarRecordatorio(
        id: String,
        recordatorio: RecordatorioDto
    ): Response<RecordatorioDto> {

        delay(500)

        if (simularError) {
            throw IOException("Error de conexión simulado")
        }

        return mutex.withLock {
            val existente = recordatorios[id]

            val resultado = if (
                existente == null ||
                recordatorio.actualizadoEn >= existente.actualizadoEn
            ) {
                recordatorio.copy(id = id)
            } else {
                existente
            }

            recordatorios[id] = resultado

            Response.success(resultado)
        }
    }
}