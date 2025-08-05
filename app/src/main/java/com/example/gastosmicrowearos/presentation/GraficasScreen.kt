package com.example.gastosmicrowearos.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficasScreen(gastos: List<Gasto>, onVolver: () -> Unit) {

    // ✅ Colores para categorías
    val colores = listOf(
        Color(0xFF1E88E5), // Azul - Comida
        Color(0xFFD32F2F), // Rojo - Transporte
        Color(0xFF388E3C), // Verde - Entretenimiento
        Color(0xFFFBC02D)  // Amarillo - Otros
    )

    // ✅ Estado para filtro de fechas
    var filtroSeleccionado by remember { mutableStateOf("Todo") }

    // ✅ Estado para presupuestos (editable)
    var presupuestos by remember {
        mutableStateOf(
            mutableMapOf(
                "Comida" to 500,
                "Transporte" to 300,
                "Entretenimiento" to 400,
                "Otros" to 200
            )
        )
    }

    // ✅ Variables temporales para actualizar presupuestos
    val nuevosPresupuestos = remember { mutableStateMapOf<String, String>() }

    // ✅ Filtrar gastos según el rango de fechas
    val gastosFiltrados = when (filtroSeleccionado) {
        "Hoy" -> gastos.filter { it.fecha == LocalDate.now().toString() }
        "Semana" -> {
            val hace7dias = LocalDate.now().minusDays(7)
            gastos.filter { LocalDate.parse(it.fecha) >= hace7dias }
        }
        "Mes" -> {
            val mesActual = LocalDate.now().monthValue
            gastos.filter { LocalDate.parse(it.fecha).monthValue == mesActual }
        }
        else -> gastos
    }

    // ✅ Agrupar gastos por categoría
    val categorias = gastosFiltrados.groupBy { it.categoria }
        .mapValues { entry -> entry.value.sumOf { it.monto } }

    // ✅ Detectar día con más gasto
    val diaMasGasto = gastosFiltrados
        .groupBy { it.fecha }
        .mapValues { entry -> entry.value.sumOf { it.monto } }
        .maxByOrNull { it.value }

    // ✅ Top 3 de gastos más altos
    val top3 = gastosFiltrados.sortedByDescending { it.monto }.take(3)

    // ✅ Total de gastos
    val total = categorias.values.sum()

    // ✅ Fondo degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color.Black, Color(0xFF121212))))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 📌 Título
            Text(
                "📊 Resumen de Gastos",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Botones de filtro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Hoy", "Semana", "Mes", "Todo").forEach { filtro ->
                    Button(
                        onClick = { filtroSeleccionado = filtro },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (filtroSeleccionado == filtro) Color(0xFF1E88E5) else Color(0xFF333333)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(filtro, color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 📝 Total de gastos
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total gastado", color = Color.Gray, fontSize = 12.sp)
                    Text("💰 $${total}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ✅ Mostrar día con más gasto
            if (diaMasGasto != null) {
                Text(
                    "📆 Día con más gasto: ${diaMasGasto.key} ($${diaMasGasto.value})",
                    color = Color.Yellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (gastosFiltrados.isEmpty()) {
                Text("No hay datos para mostrar", color = Color.White, fontSize = 14.sp)
            } else {
                // ✅ Gráfico circular
                Canvas(modifier = Modifier.size(180.dp)) {
                    var startAngle = -90f
                    val sizeArc = Size(size.width, size.height)

                    categorias.entries.forEachIndexed { index, entry ->
                        val sweepAngle = (entry.value.toFloat() / total.toFloat()) * 360f

                        drawArc(
                            color = colores[index % colores.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            size = sizeArc
                        )

                        startAngle += sweepAngle
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ✅ Leyenda con presupuestos editables
                Text("📋 Categorías y Presupuestos", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                categorias.keys.forEachIndexed { index, categoria ->
                    Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(colores[index % colores.size], shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("${categoria}: $${categorias[categoria] ?: 0}", color = Color.White, fontSize = 13.sp)
                        }

                        // ✅ Campo para cambiar presupuesto
                        OutlinedTextField(
                            value = nuevosPresupuestos[categoria] ?: presupuestos[categoria].toString(),
                            onValueChange = { nuevosPresupuestos[categoria] = it },
                            label = { Text("Presupuesto", color = Color.LightGray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF1E88E5),
                                unfocusedIndicatorColor = Color.DarkGray,
                                cursorColor = Color(0xFF1E88E5)
                            )
                        )

                        // ✅ Barra de progreso
                        val presupuesto = (nuevosPresupuestos[categoria]?.toIntOrNull() ?: presupuestos[categoria]) ?: 0
                        val gasto = categorias[categoria] ?: 0
                        val progreso = if (presupuesto > 0) (gasto.toFloat() / presupuesto) else 0f

                        LinearProgressIndicator(
                            progress = progreso.coerceAtMost(1f),
                            color = colores[index % colores.size],
                            trackColor = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .padding(horizontal = 4.dp)
                        )

                        Text(
                            text = "Presupuesto: $${presupuesto} | Gastado: $${gasto}",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 20.dp, bottom = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ✅ Ranking Top 3
                Text("🏆 Top 3 gastos más altos", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                top3.forEachIndexed { index, gasto ->
                    Text(
                        text = "${index + 1}️⃣ ${gasto.descripcion}: $${gasto.monto}",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // ✅ Botón de regresar
        Button(
            onClick = { onVolver() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.7f)
                .height(44.dp)
                .padding(bottom = 8.dp)
        ) {
            Text("⬅ Regresar", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
