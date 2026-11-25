# PROMPTS.md

Registro de los prompts usados en la Fase 2 (rama `mejora-ia`) del laboratorio MuroCampus.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| [pegar prompt de borrado] | [pegar respuesta de Gemini] | Agregué un diálogo de confirmación antes de borrar. Al borrarse con éxito, la publicación se quita de la lista local en el ViewModel. | La API de prueba simula el borrado y no elimina nada del servidor, así que la lista local debe actualizarse para que el cambio se vea. El diálogo evita borrar por error. |
| [pegar prompt de búsqueda] | [pegar respuesta de Gemini] | El filtro se hace en la pantalla sobre las publicaciones ya cargadas, sin llamada nueva a la red. Muestro "Sin resultados" cuando no hay coincidencias. | La búsqueda no requiere cambiar el Repository ni la API. El filtro local es lo más simple para esta práctica. |
| [pegar prompt de paginación] | [pegar respuesta de Gemini] | Pido 20 publicaciones por página con `_start` y `_limit`. Hice opcional el parámetro `userId` en la misma función, para que el filtro por usuario también pagine. | La paginación cambia el Repository y la API, como pide la regla. Un solo endpoint sirve para la lista general y para el filtro. |
| [pegar prompt de color] | [pegar respuesta de Gemini] | Creé un esquema propio de Material 3 con primario azul marino, y un degradado en la barra superior. Renombré `AzulMuro` a `AzulMarino` en todo el proyecto. | El nombre de la constante debe coincidir con su valor. Material 3 permite cambiar el esquema sin tocar cada pantalla. |
| [pegar prompt de manejo de errores] | [pegar respuesta de Gemini] | Moví `mensajeDe` a `data/remote`, y el repositorio envuelve el error en `ErrorPost`. El ViewModel ahora usa solo el mensaje del error. | La regla prohíbe Retrofit y `HttpException` en el ViewModel. El mapeo de errores pertenece a la capa de datos. |
