package com.yaycate.murocampus.data

import com.yaycate.murocampus.data.remote.PostApi
import com.yaycate.murocampus.model.Comentario
import com.yaycate.murocampus.model.NuevoPost
import com.yaycate.murocampus.model.Post
import kotlinx.coroutines.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
    suspend fun pagina(userId: Int?, inicio: Int, limite: Int): Result<List<Post>>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> {
        return try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    override suspend fun borrar(id: Int): Result<Unit> = llamar { api.borrar(id) }

    override suspend fun pagina(userId: Int?, inicio: Int, limite: Int): Result<List<Post>> =
        llamar { api.pagina(userId, inicio, limite) }
}
