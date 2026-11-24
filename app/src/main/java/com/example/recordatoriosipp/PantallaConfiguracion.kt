package com.example.recordatoriosipp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConfiguracion(
    viewModel: PreferenciasViewModel,
    onVolver: () -> Unit
) {
    val preferencias by
    viewModel.preferencias.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                "Preferencias de usuario",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Modo oscuro")

                Switch(
                    checked = preferencias.modoOscuro,
                    onCheckedChange = {
                        viewModel.cambiarModoOscuro(it)
                    }
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mostrar completados")

                Switch(
                    checked = preferencias.mostrarCompletados,
                    onCheckedChange = {
                        viewModel.cambiarMostrarCompletados(it)
                    }
                )
            }

            HorizontalDivider()

            Text(
                "Ordenar recordatorios",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = preferencias.orden ==
                            OrdenRecordatorios.FECHA,
                    onClick = {
                        viewModel.cambiarOrden(
                            OrdenRecordatorios.FECHA
                        )
                    }
                )
                Text("Por fecha")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = preferencias.orden ==
                            OrdenRecordatorios.PRIORIDAD,
                    onClick = {
                        viewModel.cambiarOrden(
                            OrdenRecordatorios.PRIORIDAD
                        )
                    }
                )
                Text("Por prioridad")
            }
        }
    }
}
