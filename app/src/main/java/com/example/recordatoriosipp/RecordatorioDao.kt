package com.example.recordatoriosipp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordatorioDao {

    @Query("SELECT * FROM recordatorios WHERE eliminado = 0")
    fun observarTodos(): Flow<List<RecordatorioEntity>>

    @Query("SELECT * FROM recordatorios WHERE eliminado = 0")
    suspend fun obtenerTodos(): List<RecordatorioEntity>

    @Query("SELECT * FROM recordatorios WHERE id = :id AND eliminado = 0 LIMIT 1")
    suspend fun obtenerPorId(id: String): RecordatorioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(recordatorio: RecordatorioEntity)

    @Update
    suspend fun actualizar(recordatorio: RecordatorioEntity)

    @Query("""
        UPDATE recordatorios
        SET eliminado = 1,
            pendienteSincronizacion = 1,
            actualizadoEn = :fecha
        WHERE id = :id
    """)
    suspend fun eliminar(
        id: String,
        fecha: Long = System.currentTimeMillis()
    )

    @Query("""
        SELECT * FROM recordatorios
        WHERE eliminado = 0 AND categoria = :categoria
    """)
    fun filtrarPorCategoria(
        categoria: String
    ): Flow<List<RecordatorioEntity>>

    @Query("""
        SELECT * FROM recordatorios
        WHERE eliminado = 0 AND completado = :completado
    """)
    fun filtrarPorEstado(
        completado: Boolean
    ): Flow<List<RecordatorioEntity>>

    @Query("""
        SELECT * FROM recordatorios
        WHERE eliminado = 0 AND prioridad = :prioridad
    """)
    fun filtrarPorPrioridad(
        prioridad: String
    ): Flow<List<RecordatorioEntity>>

    @Query("""
        SELECT * FROM recordatorios
        WHERE pendienteSincronizacion = 1
    """)
    suspend fun obtenerPendientes(): List<RecordatorioEntity>

    @Query("""
        UPDATE recordatorios
        SET pendienteSincronizacion = 0
        WHERE id = :id AND actualizadoEn = :version
    """)
    suspend fun marcarSincronizado(id: String, version: Long)

    @Query("SELECT * FROM recordatorios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorIdIncluyendoEliminados(
        id: String
    ): RecordatorioEntity?
}
