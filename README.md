# MICROSERVICIO DE POSTS
> Este microservicio se encarga de gestionar los post, realizados por el usuario para que el contenido 
> pueda ser visualizado por los demas integrantes de la plataforma.

## Tabla de recursos 
| NOMBRE                                         | RUTA                 | PETICION | PARAMETROS | CUERPO                                                                    | 
|------------------------------------------------|----------------------|----------|------------|---------------------------------------------------------------------------|
| Listar posts general                           | /v1/posts            | GET      | NINGUNO    | NINGUNO                                                                   |
| Obtener post por id                            | /v1/posts/{id}       | GET      | NINGUNO    | NINGUNO                                                                   |
| Crear post por usuario                         | /v1/posts            | POST     | NINGUNO    | {<br/>"content":"Nuevo Post"<br/>}                                        |
| Actualizar post del usuario                    | /v1/posts/{id}       | PUT      | NINGUNO    | {<br/>"content":"Post editado"<br/>}                                      |
| Eliminar post del usuario                      | /v1/posts/{id}       | DELETE   | NINGUNO    | NINGUNO                                                                   |
| Verificar existencia de post del usuario       | /v1/posts/{id}/verify | GET      | NINGUNO    | NINGUNO                                                                   |
| Crear Tipo de objeto (Reaccion) del usuario    | /v1/posts/data       | POST     | NINGUNO    | {<br/>"postId":"6804498d871f48237c0f5e40",<br/> "typeTarget":"LIKE"<br/>} |
| Eliminar Tipo de objeto (Reaccion) del usuario | /v1/posts/data/{id}  | DELETE   | NINGUNO    | NINGUNO                                                                   |
| Generar media SASURL                           | /v1/posts/media      | POST     | NINGUNO    | {<br/>"filenames":["image.jpg"]<br/>}                                          |

## Stack
* SPRING BOOT
* SPRING WEBFLUX
* SPRING DATA
* MONGODB
* ACTUACTOR
* AZURE SERVICE BUS
* AZURE STORAGE ACCOUNT
* LOMBOK
* MAPSTRUCT
* MOCKITO
* JUNIT