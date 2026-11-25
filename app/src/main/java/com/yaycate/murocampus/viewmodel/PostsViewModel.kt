package com.yaycate.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.murocampus.data.PostRepository
import com.yaycate.murocampus.model.Comentario
import com.yaycate.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>?>(null)
    val detalle = _detalle.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = _usuarioFiltro.value?.let { repo.porUsuario(it) } ?: repo.listar()
            resultado.fold(
                onSuccess = { _lista.value = UiState.Exito(it) },
                onFailure = { _lista.value = UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val post = repo.detalle(id)
            val comentarios = repo.comentarios(id)
            val error = post.exceptionOrNull() ?: comentarios.exceptionOrNull()
            _detalle.value = if (error != null) {
                UiState.Error(mensajeDe(error))
            } else {
                UiState.Exito(DetalleUi(post.getOrThrow(), comentarios.getOrThrow()))
            }
        }
    }
}
