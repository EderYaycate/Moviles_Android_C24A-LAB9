package com.yaycate.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.murocampus.ui.theme.AzulMuro
import com.yaycate.murocampus.viewmodel.PostsViewModel
import com.yaycate.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val hayMas by vm.hayMas.collectAsStateWithLifecycle()
    val cargandoMas by vm.cargandoMas.collectAsStateWithLifecycle()
    val errorMas by vm.errorMas.collectAsStateWithLifecycle()
    var busqueda by rememberSaveable { mutableStateOf("") }

    val colorChip = FilterChipDefaults.filterChipColors(
        selectedContainerColor = AzulMuro,
        selectedLabelColor = Color.White
    )

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar por título") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filtro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") },
                    colors = colorChip
                )
            }
            items((1..10).toList()) { id ->
                FilterChip(
                    selected = filtro == id,
                    onClick = { vm.filtrar(id) },
                    label = { Text("Usuario $id") },
                    colors = colorChip
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            when (val state = estado) {
                UiState.Cargando -> Cargando(modifier = Modifier.fillMaxSize())
                is UiState.Error -> ErrorConReintento(
                    mensaje = state.mensaje,
                    onReintentar = vm::cargar,
                    modifier = Modifier.padding(16.dp)
                )
                is UiState.Exito -> {
                    val resultados = state.datos.filter {
                        it.title.contains(busqueda.trim(), ignoreCase = true)
                    }
                    when {
                        state.datos.isEmpty() -> Text(
                            text = "Sin publicaciones",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        resultados.isEmpty() -> Text(
                            text = "Sin resultados para \"${busqueda.trim()}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        else -> LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(resultados) { post ->
                                TarjetaPost(post = post, onClick = { onAbrir(post.id) })
                            }
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (cargandoMas) {
                                        CircularProgressIndicator(
                                            color = AzulMuro,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    } else if (hayMas) {
                                        Button(
                                            onClick = vm::cargarMas,
                                            colors = ButtonDefaults.buttonColors(containerColor = AzulMuro),
                                            shape = RoundedCornerShape(50)
                                        ) {
                                            Text("Cargar más", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    errorMas?.let {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
