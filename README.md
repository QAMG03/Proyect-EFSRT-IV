# Ferretería Molina

Sistema web desarrollado con **Spring Boot**, **Thymeleaf** y **MySQL** para la gestión de una ferretería. El sistema permite administrar productos, clientes, usuarios, ventas, carrito de compras, movimientos de stock y generar reportes en PDF.

---

# Tecnologías utilizadas

* Java 21
* Spring Boot 3.5.3
* Spring Security
* Spring Data JPA
* Thymeleaf
* MySQL
* Maven
* JasperReports
* HTML5
* CSS3
* Bootstrap

---

# Guía de instalación

## Requisitos

Antes de ejecutar el proyecto, asegúrese de tener instalado:

* Java JDK 21
* Maven 3.9 o superior
* MySQL 8.0
* Un IDE compatible (IntelliJ IDEA, Eclipse o Spring Tool Suite)

---

## Clonar o abrir el proyecto

Descargue el proyecto o clónelo desde el repositorio y ábralo en su IDE. 

---

## Configurar la base de datos

Crear una base de datos llamada:

```sql
Ejecutar scrip SQl
```

Verifique que los datos del archivo `application.properties` sean correctos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ferreteria_db
spring.datasource.username=root
spring.datasource.password=12345
```

Si utiliza un usuario o contraseña diferente, modifique esos valores.

---

## Restaurar la base de datos

Importe el script SQL proporcionado con el proyecto (si corresponde). En caso contrario, al iniciar la aplicación Hibernate creará las tablas necesarias.

---

## Ejecutar el proyecto

Desde el IDE ejecute la clase principal del proyecto.

También puede ejecutarlo desde una terminal:

```bash
mvn spring-boot:run
```

Una vez iniciado, abra el navegador e ingrese a:

```
http://localhost:8080
```

---

# Manual de usuario

## Inicio de sesión

1. Acceder a la página principal.
2. Ingresar usuario y contraseña.
3. Presionar **Iniciar sesión**.

Según el rol asignado, el sistema mostrará las opciones correspondientes.

---

## Administrador

El administrador puede:

* Gestionar productos.
* Registrar, editar y eliminar clientes.
* Administrar usuarios.
* Registrar ventas.
* Consultar movimientos de stock.
* Generar reportes PDF.
* Visualizar el historial de ventas.

---

## Usuario

El usuario puede:

* Registrarse.
* Iniciar sesión.
* Visualizar el catálogo.
* Agregar productos al carrito.
* Confirmar compras.
* Consultar su historial de compras.

---

# Recursos necesarios

## Base de datos

MySQL 8.0 o superior.

## Navegador

* Google Chrome
* Microsoft Edge
* Mozilla Firefox

## Puerto utilizado

```
http://localhost:8080
```

---

# Estructura del proyecto

```
src
├── controller
├── entity
├── repository
├── model
├── usecase
├── config
├── templates
└── static
```

---

# Solución de problemas

### No inicia la aplicación

Verifique que MySQL esté ejecutándose.

### Error de conexión a la base de datos

Compruebe que el nombre de la base de datos, usuario y contraseña sean correctos en `application.properties`.

### Error al descargar reportes

Verifique que las dependencias de JasperReports se hayan descargado correctamente mediante Maven.

---

# Autor

Proyecto desarrollado con Spring Boot para la gestión integral de una ferretería.
