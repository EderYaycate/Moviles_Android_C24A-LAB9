# MuroCampus

Aplicación Android en Kotlin y Jetpack Compose que consume el servicio web REST de JSONPlaceholder (`https://jsonplaceholder.typicode.com/`). Permite listar publicaciones, filtrarlas por usuario, ver el detalle con sus comentarios y crear una publicación nueva.

## Funcionalidades

- Lista de publicaciones con los estados Cargando, Éxito y Error, con botón **Reintentar**.
- Filtro por usuario: Todos o Usuario 1 a Usuario 10.
- Detalle de la publicación con sus comentarios.
- Creación de una publicación con `POST`.

## Estructura de paquetes

```
com.yaycate.murocampus
├── MainActivity.kt
├── model/
│   └── Modelos.kt              Post, Comentario, NuevoPost
├── data/
│   ├── PostRepository.kt       interface PostRepository y PostRepositoryRemote
│   └── remote/
│       ├── PostApi.kt          interface Retrofit con las rutas
│       └── RetrofitClient.kt   OkHttp, conversor JSON y Retrofit
├── viewmodel/
│   ├── PostsViewModel.kt       estados de lista, filtro, detalle y publicación
│   ├── PostsViewModelFactory.kt
│   └── UiState.kt              Cargando, Exito y Error, y mensajeDe()
└── ui/
    ├── Componentes.kt          TarjetaPost, Cargando, ErrorConReintento
    ├── PantallaLista.kt
    ├── PantallaDetalle.kt
    ├── PantallaCrear.kt
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (ui/)
  │  eventos: cargar, filtrar, abrir, publicar
  ▼
PostsViewModel  ──  StateFlow<UiState<...>>
  │  conoce solo la interface PostRepository
  ▼
PostRepository (interface)  ──  devuelve Result<...>
  │
  ▼
PostRepositoryRemote  ──  captura excepciones de red
  │
  ▼
PostApi (Retrofit + OkHttp)  ──  https://jsonplaceholder.typicode.com/
```

- La **View** solo conoce al ViewModel y dibuja el estado con un `when`.
- El **ViewModel** no conoce Retrofit ni las excepciones de red: transforma el `Result` en un `UiState`.
- El **Repository** convierte cada excepción en `Result.failure`, y deja pasar la cancelación de la corrutina.

## Capturas

**Lista de publicaciones**
![Lista](docs/capturas/lista.png)

**Detalle con comentarios**
![Detalle](docs/capturas/detalle.png)

**Filtro por usuario**
![Filtro](docs/capturas/filtro.png)

**Crear publicación**
![Crear](docs/capturas/crear.png)

**Error de conexión**
![Error](docs/capturas/error.png)

## Para pensar

1. **¿Qué diferencia hay entre GET y POST? ¿Por qué `crear` lleva `@Body` y `porUsuario` lleva `@Query`?**
   GET pide datos sin modificar nada en el servidor; POST envía datos para crear un recurso nuevo. `crear` lleva `@Body` porque el JSON viaja en el cuerpo de la petición. `porUsuario` lleva `@Query` porque `userId` es un filtro que va en la URL (`posts?userId=2`), no un dato que se envía.

2. **¿Por qué la ruta es `"posts"` y no `"/posts"`?**
   La URL base ya termina en `/`. Si la ruta empezara con `/`, reemplazaría el camino de la base en lugar de añadirse a él.

3. **¿Por qué el Repository devuelve `Result` en vez de dejar que la excepción llegue al ViewModel?**
   Así el ViewModel y la pantalla no dependen de Retrofit ni de las excepciones de red. Reciben éxito o error, y la fuente de datos puede cambiar sin tocar la interfaz.

4. **¿Qué pasa si olvidas el permiso de INTERNET? ¿Y si olvidas `ignoreUnknownKeys` y el servidor agrega un campo nuevo?**
   Sin el permiso, la app compila pero nunca se conecta, y aparece un error de red aunque haya internet. Sin `ignoreUnknownKeys`, un campo nuevo en la respuesta hace fallar la deserialización con `SerializationException`, que la app muestra como "Respuesta inesperada".

5. **¿Por qué la pantalla de lista usa un `when` sobre `UiState` y no un `if` con tres variables?**
   Con `UiState` cada estado es excluyente y el compilador obliga a cubrir los tres casos. Con varias variables booleanas podrían existir combinaciones imposibles, por ejemplo cargando y con error a la vez.

6. **JSONPlaceholder no guarda los `POST`. ¿Cómo cambiaría tu app si usaras un servicio real?**
   La pantalla y el ViewModel casi no cambiarían: solo la implementación de `PostApi` y, si hace falta, la autenticación en `RetrofitClient`. Al crear una publicación, la app volvería a cargar la lista desde el servidor para mostrar el registro guardado.
