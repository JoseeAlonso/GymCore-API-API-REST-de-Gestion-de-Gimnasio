# Gimnasio API
 
API REST para la gestión de un gimnasio, hecha como proyecto personal mientras aprendo Spring Boot en el ciclo de DAW. No es un proyecto de empresa ni nada parecido, es mi forma de aprender haciendo, equivocándome y volviendo a intentarlo.
 
Está en construcción activa. Lo subo así, sin terminar, porque prefiero que se vea el progreso real en vez de esperar a tener algo "perfecto" que probablemente nunca llegue si espero a eso.
 
## ¿Qué hace?
 
Gestiona tres cosas básicas de un gimnasio: clientes, entrenadores y clases. Una clase tiene un entrenador asignado y varios clientes apuntados. Nada muy complicado a nivel de negocio, pero me ha servido como excusa perfecta para tocar prácticamente todas las piezas de un backend con Spring Boot.
 
## Stack
 
- Java 17
- Spring Boot 4.1.0
- Maven
- H2 (base de datos en memoria, para desarrollo)
- Lombok
- Bean Validation
- springdoc-openapi (Swagger) para documentar los endpoints
- JUnit 5 + Mockito para testing
  
## Lo que ya funciona
 
**CRUD completo** de clientes, entrenadores y clases, con arquitectura en capas (Controller → Service → Repository), validaciones con Bean Validation, y manejo centralizado de errores con un `@RestControllerAdvice` (nada de try/catch repartido por los controllers).
 
**Testing**, que ha sido con diferencia donde más he aprendido:
- Tests unitarios con Mockito, mockeando los repositorios
- Tests de integración con `@SpringBootTest` y `MockMvc`, contra una base de datos H2 real
Los tests de integración me han enseñado algo que no esperaba: no solo sirven para comprobar que el código funciona, sino que te sacan a la luz bugs que los tests unitarios con mocks jamás detectarían. Me pasó literalmente dos veces en este proyecto:
 
- Un `.toList()` que devolvía una lista inmutable, y Hibernate explotando por dentro al intentar modificarla. Con mocks nunca lo hubiera visto, porque ahí yo controlo todo a mano.
- Un problema de codificación UTF-8 en Windows que hacía que los mensajes de error con tildes llegaran corruptos en el JSON (`vacÃ­o` en vez de `vacío`). Me tuvo bloqueado un buen rato hasta entender que el problema era de cómo Java compilaba el archivo, no de mi código en sí.
Documentación con Swagger disponible en `/swagger-ui/index.html` una vez levantas la aplicación.
 
## En lo que estoy ahora mismo
 
Spring Security + JWT. Ya tengo la entidad `Usuario` con sus roles, el repositorio, el encriptado de contraseñas con BCrypt y la conexión con Spring Security para que sepa buscar usuarios en mi base de datos. Me falta la parte de generar y validar los tokens, el filtro que los intercepte en cada petición, y los endpoints de login/registro.
 
Avisando desde ya: en cuanto tenga la clave de firmado de los JWT, esa clave no va a estar en este repo en texto plano. La moveré a variables de entorno antes de que eso pase.
 
## Lo que queda pendiente
 
- Terminar JWT (generación, validación, filtro, login)
- Proteger los endpoints según rol de usuario
- Perfiles de entorno (application.properties para dev y producción)
- Dockerizar el proyecto
  
## Por qué lo subo así, sin terminar
 
Porque creo que tiene más valor mostrar cómo se construye algo de verdad —con bugs raros, vueltas atrás, cosas que no entendía y tuve que repasar— que subir solo el resultado final pulido. Si alguien lo mira y tiene feedback, bienvenido sea.
