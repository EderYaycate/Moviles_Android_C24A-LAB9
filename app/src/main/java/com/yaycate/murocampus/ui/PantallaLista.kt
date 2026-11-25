package com.yaycate.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.murocampus.viewmodel.PostsViewModel
import com.yaycate.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    when (val state = estado) {
        UiState.Cargando -> Cargando(modifier = modifier.fillMaxSize())
        is UiState.Error -> ErrorConReintento(
            mensaje = state.mensaje,
            onReintentar = vm::cargar,
            modifier = modifier.fillMaxSize().padding(16.dp)
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.datos) { post ->
                TarjetaPost(post = post)
            }
        }
    }
}
