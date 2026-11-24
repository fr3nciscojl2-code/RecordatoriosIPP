package com.example.recordatoriosipp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion(
    preferenciasViewModel: PreferenciasViewModel
) {
    val navController = rememberNavController()

    val recordatoriosViewModel: RecordatoriosViewModel =
        viewModel()

    val preferencias by preferenciasViewModel
        .preferencias.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            PantallaRecordatorios(
                onCrearClick = {
                    navController.navigate("crear")
                },
                onRecordatorioClick = { id ->
                    navController.navigate("detalle/$id")
                },
                viewModel = recordatoriosViewModel,
                preferencias = preferencias,
                onConfiguracionClick = {
                    navController.navigate("configuracion")
                }
            )
        }

        composable("configuracion") {
            PantallaConfiguracion(
                viewModel = preferenciasViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable("crear") {
            PantallaCrearRecordatorio(
                onGuardar = {
                        titulo, descripcion, categoria, prioridad ->

                    recordatoriosViewModel.agregarRecordatorio(
                        titulo,
                        descripcion,
                        categoria,
                        prioridad
                    )

                    navController.popBackStack()
                },
                onCancelar = {
                    navController.popBackStack()
                }
            )
        }

        composable("detalle/{id}") { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getString("id")
                .orEmpty()

            val estado by recordatoriosViewModel
                .estado.collectAsStateWithLifecycle()

            val recordatorio = (estado as? EstadoApp.Exito)
                ?.recordatorios
                ?.find { it.id == id }

            if (recordatorio != null) {
                PantallaDetalleRecordatorio(
                    recordatorio = recordatorio,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onActualizar = { actualizado ->
                        recordatoriosViewModel
                            .actualizarRecordatorio(actualizado)
                    },
                    onEliminar = { recordatorioId ->
                        recordatoriosViewModel
                            .eliminarRecordatorio(recordatorioId)
                    }
                )
            }
        }
    }
}
