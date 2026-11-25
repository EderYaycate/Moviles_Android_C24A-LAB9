package com.yaycate.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.murocampus.viewmodel.PostsViewModel
import com.yaycate.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val estado by vm.publicado.collectAsStateWithLifecycle()

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { vm.publicar(titulo, contenido) },
            enabled = titulo.isNotBlank() && contenido.isNotBlank() && estado !is UiState.Cargando
        ) {
            Text("Publicar")
        }
        val s = estado
        if (s is UiState.Exito) {
            Text(
                text = "Publicado con id ${s.datos.id}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (s is UiState.Error) {
            Text(
                text = s.mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
