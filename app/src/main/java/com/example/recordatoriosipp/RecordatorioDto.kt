package com.example.recordatoriosipp

data class RecordatorioDto(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String,
    val completado: Boolean,
    val fechaLimite: String?,
    val actualizadoEn: Long,
    val eliminado: Boolean
)

fun RecordatorioEntity.toDto(): RecordatorioDto {
    return RecordatorioDto(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        categoria = categoria,
        prioridad = prioridad,
        completado = completado,
        fechaLimite = fechaLimite,
        actualizadoEn = actualizadoEn,
        eliminado = eliminado
    )
}

fun RecordatorioDto.toEntity(): RecordatorioEntity {
    return RecordatorioEntity(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        categoria = categoria,
        prioridad = prioridad,
        completado = completado,
        fechaLimite = fechaLimite,
        actualizadoEn = actualizadoEn,
        pendienteSincronizacion = false,
        eliminado = eliminado
    )
}
