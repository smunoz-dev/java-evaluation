
# Register API

API RESTful para la gestión de usuarios, autenticación y registro, desarrollada con Java, Spring Boot y Maven.
Para probar los endpoints, puedes utilizar herramientas como Postman utlilizando el archivo 
`register-api.postman_collection.json` incluido en el proyecto.

La forma de probarlo es primero llamando al API de registro de usuario `POST /api/user`, luego al de login `POST /api/user/login`
y finalmente al de obtención de usuario `GET /api/user/{id}`.

## Características

- Registro y autenticación de usuarios con JWT
- Documentación interactiva con Swagger/OpenAPI
- Seguridad con Spring Security y JWT
- Validación de datos y manejo de errores personalizados

## Requisitos

- Java 17+
- Maven 3.8+
- Base de datos compatible (H2, PostgreSQL, etc.)

## Instalación

1. Clona el repositorio:

```
git clone https://github.com/smunoz-dev/register-api.git
cd register-api
```

2. Construye el proyecto:
```
mvn clean install   
```

3. Ejecuta la aplicación:
```
mvn spring-boot:run
```

## Endpoints principales

- `POST /api/user` — Crear usuario
- `POST /api/user/login` — Autenticación y obtención de token JWT
- `GET /api/user` — Listar usuarios
- `GET /api/user/{id}` — Obtener usuario por ID

## Endpoints Ejemplos

- `POST /api/user` 

```
{
    "name": "test",
    "email": "test@test.com",
    "password": "Password1$",
    "phones": [
        {
            "number": "1234569",
            "citycode": "72",
            "countrycode": "56"
        },
        {
            "number": "7654323",
            "citycode": "72",
            "countrycode": "56"
        }
    ]
}
```
- `POST /api/user/login`

```
{
    "email": "test@test.com",
    "password": "Password1$"
}
```
## Documentación Swagger

Accede a la documentación y prueba los endpoints en:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Configuración

- Variables de entorno y propiedades en `src/main/resources/application.properties`
- CORS habilitado para `http://localhost:8080` (ajustable en `SecurityConfiguration`)

## Pruebas

Ejecuta los tests con:
```
mvn test
```

## Licencia

MIT
```