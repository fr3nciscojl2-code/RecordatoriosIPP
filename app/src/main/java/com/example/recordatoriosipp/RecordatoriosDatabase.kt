package com.example.recordatoriosipp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RecordatorioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecordatoriosDatabase : RoomDatabase() {

    abstract fun recordatorioDao(): RecordatorioDao

    companion object {

        @Volatile
        private var INSTANCE: RecordatoriosDatabase? = null

        fun obtenerInstancia(
            context: Context
        ): RecordatoriosDatabase {
            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    RecordatoriosDatabase::class.java,
                    "recordatorios_db"
                ).build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
