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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recordatoriosipp.ui.theme.RecordatoriosIPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RecordatoriosIPPTheme {
                PantallaRecordatorios()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Recordatorios") }
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
            items(listaRecordatorios) { recordatorio ->
                TarjetaRecordatorio(recordatorio)
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio) {
    Card(
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
        }
    }
}
