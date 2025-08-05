package com.example.gastosmicrowearos.presentation

data class Gasto(
    val monto: Int,
    val categoria: String,
    val descripcion: String, // Descripción del gasto (detalle)
    val fecha: String,       // Fecha del gasto
    val hora: String         // Hora del gasto
)
