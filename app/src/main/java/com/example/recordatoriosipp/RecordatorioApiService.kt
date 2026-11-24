package com.example.recordatoriosipp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecordatorioApiService {

    @GET("recordatorios")
    suspend fun obtenerRecordatorios():
            Response<List<RecordatorioDto>>

    @PUT("recordatorios/{id}")
    suspend fun guardarRecordatorio(
        @Path("id") id: String,
        @Body recordatorio: RecordatorioDto
    ): Response<RecordatorioDto>
}
