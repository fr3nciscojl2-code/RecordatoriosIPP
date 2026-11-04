package com.example.recordatoriosipp

val primerRecordatorio = Recordatorio(
    titulo = "Estudiar Kotlin",
    descripcion = "Repasar las data classes",
    categoria = "Estudios",
    prioridad = Prioridad.ALTA,
    completado = false
)

val listaRecordatorios = listOf(
    primerRecordatorio,

    Recordatorio(
        titulo = "Comprar alimentos",
        descripcion = "Comprar frutas y verduras",
        categoria = "Personal",
        prioridad = Prioridad.MEDIA,
        completado = false
    ),

    Recordatorio(
        titulo = "Entregar Actividad IPP",
        descripcion = "Finalizar Programacion Mobile",
        categoria = "Estudios",
        prioridad = Prioridad.ALTA,
        completado = true
    )
)

val categoriasUnicas = listaRecordatorios
    .map { it.categoria }
    .toSet()

val coloresCategorias = mapOf(
    "Estudios" to "Azul",
    "Personal" to "Verde",
    "Trabajo" to "Naranja"
)

val recordatoriosPendientes = listaRecordatorios.filter {
    it.completado == false
}

val recordatoriosAltaPrioridad = listaRecordatorios.filter {
    it.prioridad == Prioridad.ALTA
}

fun probarValidaciones() {
    println("PRUEBA IPP: La funcion se esta ejecutando")

    try {
        val recordatorioPrueba = Recordatorio(
            titulo = " ",
            descripcion = "Repasar las data classes",
            categoria = "Estudios",
            prioridad = Prioridad.ALTA,
            completado = false
        )

        println(recordatorioPrueba)
    } catch (e: IllegalArgumentException) {
        println("Error detectado: ${e.message}")
    }
}