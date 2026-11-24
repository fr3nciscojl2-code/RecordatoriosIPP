package com.example.recordatoriosipp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCrearRecordatorio(
    onGuardar: (String, String, String, String) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }

    val formularioValido =
        titulo.trim().length >= 4 &&
                descripcion.length <= 200 &&
                categoria.isNotEmpty() &&
                prioridad.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Nuevo recordatorio",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            isError = titulo.isNotEmpty() &&
                    titulo.trim().length < 4,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            isError = descripcion.length > 200,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Categoría")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Personal", "Trabajo", "Estudio").forEach { opcion ->
                FilterChip(
                    selected = categoria == opcion,
                    onClick = { categoria = opcion },
                    label = { Text(opcion) }
                )
            }
        }

        Text("Prioridad")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("BAJA", "MEDIA", "ALTA").forEach { opcion ->
                FilterChip(
                    selected = prioridad == opcion,
                    onClick = { prioridad = opcion },
                    label = { Text(opcion) }
                )
            }
        }

        Button(
            onClick = {
                onGuardar(titulo.trim(), descripcion, categoria, prioridad)
            },
            enabled = formularioValido
        ) {
            Text("Guardar")
        }

        OutlinedButton(onClick = onCancelar) {
            Text("Cancelar")
        }
    }
}
