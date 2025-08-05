package com.example.gastosmicrowearos.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterGastoScreen(
    onFinish: () -> Unit = {},
    viewModel: GastoViewModel = viewModel()
) {
    var montoTexto by remember { mutableStateOf("") }
    var detalle by remember { mutableStateOf(TextFieldValue("")) }
    var categoriaSeleccionada by remember { mutableStateOf("") }
    var mostrarGraficas by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val categorias = listOf("Comida", "Transporte", "Entretenimiento", "Vivienda", "Medicina", "Otros")
    val gastos = viewModel.gastos
    val azulMarino = Color(0xFF001F3F)
    val textColor = Color.White

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (mostrarGraficas) {
        GraficasScreen(gastos = gastos) {
            mostrarGraficas = false
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 72.dp)
        ) {

            // 💲 Monto
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payments, contentDescription = "Monto", tint = Color.Green)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Monto del Gasto", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Spacer(modifier = Modifier.height(6.dp))

                TextField(
                    value = montoTexto,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            montoTexto = it
                        }
                    },
                    placeholder = { Text("Ej: 1200", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                        .background(Color(0xFF1C1C1C), RoundedCornerShape(12.dp))
                        .padding(horizontal = 4.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = azulMarino,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 📝 Detalle
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = "Detalle", tint = Color.Cyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Detalle del Gasto", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Spacer(modifier = Modifier.height(6.dp))

                TextField(
                    value = detalle,
                    onValueChange = { detalle = it },
                    placeholder = { Text("¿En qué gastaste?", color = Color.Gray) },
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                        .background(Color(0xFF1C1C1C), RoundedCornerShape(12.dp))
                        .padding(horizontal = 4.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = azulMarino,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 🏷 Categorías en horizontal
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Category, contentDescription = "Categoría", tint = Color.Yellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Categoría", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(categorias) { categoria ->
                        val isSelected = categoria == categoriaSeleccionada
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) azulMarino else Color(0xFF444444),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .clickable { categoriaSeleccionada = categoria }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = categoria,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 📜 Historial
            if (gastos.isNotEmpty()) {
                item {
                    Text("Historial", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { mostrarGraficas = true },
                        colors = ButtonDefaults.buttonColors(containerColor = azulMarino),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(40.dp)
                            .shadow(6.dp, RoundedCornerShape(20.dp))
                    ) {
                        Text("📊 Ver Gráficas", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(gastos.reversed()) { gasto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("💰 $${gasto.monto}", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("📄 Detalle: ${gasto.descripcion}", color = Color.LightGray, fontSize = 12.sp)
                            Text("🏷 Categoría: ${gasto.categoria}", color = Color.Cyan, fontSize = 12.sp)
                            Text("⏰ Fecha: ${gasto.fecha} ${gasto.hora}", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // ✅ Botón Finalizar
        Button(
            onClick = {
                try {
                    val monto = montoTexto.toIntOrNull() ?: 0
                    if (monto <= 0) {
                        Toast.makeText(context, "El monto debe ser mayor que 0", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (detalle.text.isBlank()) {
                        Toast.makeText(context, "Agrega un detalle del gasto", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (categoriaSeleccionada.isEmpty()) {
                        Toast.makeText(context, "Selecciona una categoría", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                    val fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                    viewModel.agregarGasto(
                        monto = monto,
                        categoria = categoriaSeleccionada,
                        descripcion = detalle.text,
                        fecha = fecha,
                        hora = hora
                    )

                    Toast.makeText(
                        context,
                        "Guardado: $$monto\n$fecha $hora\nCategoría: $categoriaSeleccionada\nDetalle: ${detalle.text}",
                        Toast.LENGTH_SHORT
                    ).show()

                    montoTexto = ""
                    detalle = TextFieldValue("")
                    categoriaSeleccionada = ""

                    onFinish()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.9f)
                .height(46.dp)
                .padding(bottom = 10.dp)
                .shadow(6.dp, RoundedCornerShape(24.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = azulMarino)
        ) {
            Text("✅ Listo", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
