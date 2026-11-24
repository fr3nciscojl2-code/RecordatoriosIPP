package com.example.recordatoriosipp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recordatorios")
data class RecordatorioEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String,
    val completado: Boolean,
    val fechaLimite: String? = null,
    val actualizadoEn: Long = System.currentTimeMillis(),
    val pendienteSincronizacion: Boolean = true,
    val eliminado: Boolean = false
)

fun RecordatorioEntity.toRecordatorio(): Recordatorio {
    return Recordatorio(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        categoria = categoria,
        prioridad = Prioridad.valueOf(prioridad),
        completado = completado,
        fechaLimite = fechaLimite
    )
}

fun Recordatorio.toEntity(): RecordatorioEntity {
    return RecordatorioEntity(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        categoria = categoria,
        prioridad = prioridad.name,
        completado = completado,
        fechaLimite = fechaLimite
    )
}
