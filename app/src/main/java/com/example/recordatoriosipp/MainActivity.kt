package com.example.recordatoriosipp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recordatoriosipp.ui.theme.RecordatoriosIPPTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val preferenciasViewModel: PreferenciasViewModel =
                viewModel()

            val preferencias by preferenciasViewModel
                .preferencias.collectAsStateWithLifecycle()

            RecordatoriosIPPTheme(
                darkTheme = preferencias.modoOscuro
            ) {
                Navegacion(preferenciasViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios(
    onCrearClick: () -> Unit,
    onRecordatorioClick: (String) -> Unit,
    viewModel: RecordatoriosViewModel,
    preferencias: PreferenciasUsuario,
    onConfiguracionClick: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    val sincronizando by viewModel.sincronizando
        .collectAsStateWithLifecycle()

    val mensajeSincronizacion by viewModel.mensajeSincronizacion
        .collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(mensajeSincronizacion) {
        mensajeSincronizacion?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.limpiarMensajeSincronizacion()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Mis Recordatorios") },
                actions = {
                    TextButton(onClick = onConfiguracionClick) {
                        Text("Ajustes")
                    }

                    IconButton(onClick = onCrearClick) {
                        Text("+")
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Button(
                    onClick = {
                        viewModel.sincronizarRecordatorios()
                    },
                    enabled = !sincronizando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (sincronizando) {
                        Text("Sincronizando...")
                    } else {
                        Text("Sincronizar recordatorios")
                    }
                }

                if (sincronizando) {
                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            when (val actual = estado) {

                is EstadoApp.Cargando -> {
                    item {
                        Text("Cargando recordatorios...")
                    }
                }

                is EstadoApp.Vacio -> {
                    item {
                        Text("Todavía no tienes recordatorios.")
                        Text("Presiona + para crear uno.")
                    }
                }

                is EstadoApp.Exito -> {

                    val filtrados = actual.recordatorios.filter {
                        preferencias.mostrarCompletados ||
                                !it.completado
                    }

                    val ordenados = when (preferencias.orden) {

                        OrdenRecordatorios.FECHA ->
                            filtrados.sortedWith(
                                compareBy<Recordatorio> {
                                    it.fechaLimite == null
                                }.thenBy {
                                    it.fechaLimite
                                }
                            )

                        OrdenRecordatorios.PRIORIDAD ->
                            filtrados.sortedByDescending {
                                it.prioridad.ordinal
                            }
                    }

                    if (ordenados.isEmpty()) {
                        item {
                            Text("No hay recordatorios para mostrar.")
                        }
                    }

                    items(
                        items = ordenados,
                        key = { it.id }
                    ) { recordatorio ->

                        TarjetaRecordatorio(
                            recordatorio = recordatorio,
                            onClick = {
                                onRecordatorioClick(recordatorio.id)
                            }
                        )
                    }
                }

                is EstadoApp.Error -> {
                    item {
                        Text(actual.mensaje)

                        Button(
                            onClick = {
                                viewModel.cargarRecordatorios()
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleLarge
            )

            Text("Categoría: ${recordatorio.categoria}")

            Text("Prioridad: ${recordatorio.prioridad}")

            Text(
                text = if (recordatorio.completado)
                    "Estado: Completado"
                else
                    "Estado: Pendiente"
            )

            recordatorio.fechaLimite?.let {
                Text("Fecha límite: $it")
            }
        }
    }
}