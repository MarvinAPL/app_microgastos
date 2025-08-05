package com.example.gastosmicrowearos.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/**
 * ViewModel para manejar la lógica y estado de los gastos.
 */
class GastoViewModel : ViewModel() {

    // Lista observable de gastos, Compose detecta cambios automáticamente.
    private val _gastos = mutableStateListOf<Gasto>()
    val gastos: List<Gasto> get() = _gastos

    /**
     * Agrega un nuevo gasto a la lista si es válido.
     * Incluye la descripción (detalle del gasto).
     */
    fun agregarGasto(
        monto: Int,
        categoria: String,
        descripcion: String,
        fecha: String,
        hora: String
    ) {
        if (monto > 0 && categoria.isNotBlank() && descripcion.isNotBlank()) {
            _gastos.add(Gasto(monto, categoria, descripcion, fecha, hora))
        }
    }

    // Elimina un gasto existente.
    fun eliminarGasto(gasto: Gasto) {
        _gastos.remove(gasto)
    }

    // Limpia todos los gastos almacenados.
    fun limpiarGastos() {
        _gastos.clear()
    }

    // Calcula el total de los gastos.
    fun obtenerTotal(): Int {
        return _gastos.sumOf { it.monto }
    }
}
