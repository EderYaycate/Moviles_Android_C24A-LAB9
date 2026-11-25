# PROMPTS.md

Registro de los prompts usados en la Fase 2 (rama `mejora-ia`) del laboratorio MuroCampus.

| Prompt que escribí | Qué respondió | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Pedir los pasos para borrar una publicación desde la pantalla de detalle, usando `@DELETE` en la API y con confirmación. | Propuso un método `borrar` en la interfaz de Retrofit, un método en el repositorio que devuelve `Result`, y un diálogo de confirmación antes de llamar al ViewModel. | Quité la navegación directa de la respuesta y dejé que la lista local elimine la publicación cuando el borrado sale bien. | JSONPlaceholder simula el borrado, así que la lista debe actualizarse localmente para que el usuario vea el cambio. |
| Pedir una búsqueda por título en la lista de publicaciones. | Sugirió un campo de texto encima de los filtros y un filtro sobre la lista ya cargada, sin hacer otra llamada a la red. | Mantuve el filtro local y añadí el mensaje "Sin resultados" cuando no hay coincidencias. | La API de prueba no ofrece búsqueda por texto, así que filtrar lo ya cargado es lo más simple. |
| Pedir paginación simple con `_start` y `_limit`, con un botón para cargar más. | Propuso pedir de 10 en 10 con `@Query("_start")` y `@Query("_limit")`, y acumular las páginas en el estado del ViewModel. | Hice que el parámetro `userId` sea opcional para que el filtro por usuario también use paginación. | Así el filtro por usuario y la paginación funcionan juntos sin dos rutas distintas. |
| Pedir un tema en azul marino con degradado usando Material 3. | Recomendó un esquema de colores propio con `lightColorScheme` y un `Brush` de degradado para la barra superior. | Renombré `AzulMuro` a `AzulMarino` en todo el proyecto para que el nombre coincida con el color. | Evita que el nombre de una constante contradiga su valor. |
