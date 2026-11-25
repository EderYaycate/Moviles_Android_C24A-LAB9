package com.yaycate.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.murocampus.data.PostRepository
import com.yaycate.murocampus.model.Comentario
import com.yaycate.murocampus.model.NuevoPost
import com.yaycate.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>?>(null)
    val detalle = _detalle.asStateFlow()

    private val _publicado = MutableStateFlow<UiState<Post>?>(null)
    val publicado = _publicado.asStateFlow()

    private val _borrado = MutableStateFlow<UiState<Unit>?>(null)
    val borrado = _borrado.asStateFlow()

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

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _publicado.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto)).fold(
                onSuccess = { _publicado.value = UiState.Exito(it) },
                onFailure = { _publicado.value = UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun limpiarPublicado() {
        _publicado.value = null
    }

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id).fold(
                onSuccess = {
                    _borrado.value = UiState.Exito(Unit)
                    _lista.update { estado ->
                        if (estado is UiState.Exito) UiState.Exito(estado.datos.filter { it.id != id }) else estado
                    }
                },
                onFailure = { _borrado.value = UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun limpiarBorrado() {
        _borrado.value = null
    }
}
