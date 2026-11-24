package com.example.recordatoriosipp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleRecordatorio(
    recordatorio: Recordatorio,
    onVolver: () -> Unit,
    onActualizar: (Recordatorio) -> Unit = {},
    onEliminar: (String) -> Unit = {}
) {
    var editando by remember { mutableStateOf(false) }
    var confirmarEliminar by remember { mutableStateOf(false) }

    var titulo by remember(recordatorio) {
        mutableStateOf(recordatorio.titulo)
    }

    var descripcion by remember(recordatorio) {
        mutableStateOf(recordatorio.descripcion)
    }

    var categoria by remember(recordatorio) {
        mutableStateOf(recordatorio.categoria)
    }

    var prioridad by remember(recordatorio) {
        mutableStateOf(recordatorio.prioridad)
    }

    var menuPrioridad by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del recordatorio") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (editando) {

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box {
                    OutlinedButton(
                        onClick = { menuPrioridad = true }
                    ) {
                        Text("Prioridad: $prioridad")
                    }

                    DropdownMenu(
                        expanded = menuPrioridad,
                        onDismissRequest = {
                            menuPrioridad = false
                        }
                    ) {
                        Prioridad.entries.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.name) },
                                onClick = {
                                    prioridad = opcion
                                    menuPrioridad = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        onActualizar(
                            recordatorio.copy(
                                titulo = titulo.trim(),
                                descripcion = descripcion,
                                categoria = categoria.trim(),
                                prioridad = prioridad
                            )
                        )
                        editando = false
                    },
                    enabled = titulo.trim().length >= 4 &&
                            descripcion.length <= 200 &&
                            categoria.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }

                OutlinedButton(
                    onClick = {
                        titulo = recordatorio.titulo
                        descripcion = recordatorio.descripcion
                        categoria = recordatorio.categoria
                        prioridad = recordatorio.prioridad
                        editando = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }

            } else {

                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text("Descripción: ${recordatorio.descripcion}")
                Text("Categoría: ${recordatorio.categoria}")
                Text("Prioridad: ${recordatorio.prioridad}")

                Text(
                    if (recordatorio.completado)
                        "Estado: Completado"
                    else
                        "Estado: Pendiente"
                )

                Text(
                    "Fecha límite: ${recordatorio.fechaLimite ?: "Sin fecha"}"
                )

                HorizontalDivider()

                Button(
                    onClick = {
                        onActualizar(
                            recordatorio.copy(
                                completado = !recordatorio.completado
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (recordatorio.completado)
                            "Marcar como pendiente"
                        else
                            "Marcar como completado"
                    )
                }

                OutlinedButton(
                    onClick = { editando = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar recordatorio")
                }

                Button(
                    onClick = { confirmarEliminar = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar recordatorio")
                }
            }
        }
    }

    if (confirmarEliminar) {
        AlertDialog(
            onDismissRequest = {
                confirmarEliminar = false
            },
            title = { Text("Eliminar recordatorio") },
            text = {
                Text("¿Estás seguro de que deseas eliminar este recordatorio?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarEliminar = false
                        onEliminar(recordatorio.id)
                        onVolver()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmarEliminar = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
