package com.yaycate.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.murocampus.ui.theme.AzulMarino
import com.yaycate.murocampus.ui.theme.BordeGris
import com.yaycate.murocampus.ui.theme.FondoTarjeta
import com.yaycate.murocampus.viewmodel.PostsViewModel
import com.yaycate.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(id) {
        vm.abrir(id)
    }

    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    var confirmar by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(borrado) {
        if (borrado is UiState.Exito) {
            vm.limpiarBorrado()
            onBorrado()
        }
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Borrar publicación?") },
            text = { Text("Se eliminará la publicación $id.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmar = false
                    vm.borrar(id)
                }) { Text("Borrar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }

    when (val state = estado) {
        null, UiState.Cargando -> Cargando(modifier = modifier.fillMaxSize())
        is UiState.Error -> ErrorConReintento(
            mensaje = state.mensaje,
            onReintentar = { vm.abrir(id) },
            modifier = modifier.padding(16.dp)
        )
        is UiState.Exito -> Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, BordeGris)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = state.datos.post.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.datos.post.body,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Comentarios (${state.datos.comentarios.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(8.dp))
            state.datos.comentarios.forEach { comentario ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = comentario.email,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AzulMarino
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = comentario.body,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { confirmar = true },
                enabled = borrado !is UiState.Cargando,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Borrar publicación", fontWeight = FontWeight.Bold)
            }
            if (borrado is UiState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (borrado as UiState.Error).mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
