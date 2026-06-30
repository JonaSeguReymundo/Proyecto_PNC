# 📦 Warehouse Inventory API - Sistema de Gestión de Inventario y Almacenes (WMS)

Este proyecto consiste en una solución de backend robusta desarrollada en **Java con Spring Boot 3**, estructurada bajo una **Arquitectura N-Capas**. Está diseñada para gestionar la infraestructura física de almacenes (pasillos, racks, niveles, ubicaciones), catálogos de productos, flujos de mercancía (entradas, salidas, transferencias, reservas) y aplicar algoritmos inteligentes de negocio (FIFO, ROP, Clasificación ABC).

## 👥 Equipo de Desarrollo y Roles

- **Integrante 1: Jonatan Segura** — *Arquitectura base, Modelado de Datos (DER), Repositorios y Desafío de Escalabilidad (Patrón Strategy para Asignación de Ubicaciones).*
- **Integrante 2: Daniel Juarez** — *Capa de Seguridad, Autenticación y Autorización con JWT, Manejo Global de Excepciones, API REST y Sistema de Auditoría con AOP.*
- **Integrante 3 y 4: German Sanchez y Cristian Rodriguez** — *Revisión y optimización de código base, aseguramiento de calidad, Capa de Negocio Avanzada, Asignación FIFO de Lotes, Control de Reservas de Stock, Automatización de Expiraciones y Reportes de Inteligencia de Negocio (ROP / ABC).*

## 🎯 Objetivo del Proyecto y Justificación Técnica

El objetivo principal es resolver los problemas críticos de visibilidad de existencias, optimización de espacio físico y control de costos en entornos logísticos complejos.

### Justificación de la Arquitectura:

- **Separación de Responsabilidades (N-Capas):** División estricta en capas de Presentación (`Controller`), Negocio (`Service`), Acceso a Datos (`Repository`) y Dominio (`Entity`). Esto asegura que las reglas de negocio estén totalmente aisladas de los protocolos de transporte y de la persistencia física.
- **Uso Estricto de DTOs:** Ninguna entidad JPA se expone directamente hacia el cliente. Se manejan Java Records independientes para `Request` y `Response` para proteger la integridad del modelo y encapsular los datos de manera limpia.
- **Seguridad Avanzada:** Implementation de Spring Security acoplado a un mecanismo de tokens duales (Access Token de vida corta y Refresh Token de vida larga) gestionados mediante variables de entorno eficientes.
- **Auditoría No Invasiva (AOP):** Registro de operaciones críticas (`audit_log`) implementado con Programación Orientada a Aspectos, evitando duplicidad de código en los servicios de negocio.

## 🛠️ Tecnologías y Frameworks

- **Lenguaje:** Java 17
- **Framework Principal:** Spring Boot 3.x
- **Persistencia:** Spring Data JPA / Hibernate
- **Base de Datos:** PostgreSQL
- **Seguridad:** Spring Security + JSON Web Tokens (JWT)
- **Documentación:** SpringDoc OpenAPI (Swagger UI)
- **Validación:** Jakarta Validation (`@NotNull`, `@NotBlank`, `@Positive`, etc.)

## ⚙️ Requisitos Previos

- **Java Development Kit (JDK):** Versión 17 o superior.
- **Apache Maven:** Versión 3.8 o superior.
- **PostgreSQL:** Instancia local o remota en ejecución.

## 🚀 Instalación y Configuración del Entorno

### 1. Clonar el repositorio:

Bash

```
git clone [URL_DE_TU_REPOSITORIO]
cd warehouse-inventory-api
```

### 2. Configurar las Variables de Entorno (.env):

El proyecto utiliza inyección dinámica de propiedades mediante variables de entorno configuradas en el archivo `application.yaml`.

Crea un archivo llamado `.env` en la raíz del proyecto (basándote en tu archivo `.env.example`) y asigna tus valores locales correspondientes:

Code snippet

```
DB_URL=jdbc:postgresql://localhost:5432/tu_base_de_datos
DB_USER=tu_usuario_postgres
DB_PASSWORD=tu_contraseña_postgres

# Generar secret en consola con: openssl rand -base64 64
JWT_SECRET="mi_clave_secreta"
JWT_ACCESS_EXP_MS=3600000
JWT_REFRESH_EXP_MS=604800000
```

> 💡 **Nota:** Asegúrate de cargar o exportar estas variables en tu entorno de desarrollo o IDE (como IntelliJ IDEA o Eclipse) antes de levantar la aplicación para que Spring Boot pueda resolverlas de forma correcta.
> 

### 3. Compilar el Proyecto:

Bash

```
mvn clean install
```

## 🏃‍♂️ Ejecución de la Aplicación

Para iniciar el servidor de desarrollo en el puerto 8081 (especificado en la configuración del servidor), ejecuta el siguiente comando:

Bash

```
mvn spring-boot:run
```

El backend iniciará correctamente y estará escuchando peticiones en: `http://localhost:8081`

## 📚 Documentación Interactiva y Pruebas de la API

### 🛠️ Swagger UI (OpenAPI 3)

La documentación completa de los controladores, esquemas JSON y el botón interactivo de pruebas (*Authorize* para adjuntar tu token Bearer) se genera de manera automática al levantar el proyecto. Puedes acceder de forma directa ingresando al navegador en la siguiente ruta:

- 👉 **URL de Swagger:** [http://localhost:8081/swagger-ui.html](https://www.google.com/search?q=http://localhost:8081/swagger-ui.html)
- Los metadatos puros e independientes de la especificación en formato JSON se localizan en: [http://localhost:8081/api-docs](https://www.google.com/search?q=http://localhost:8081/api-docs)

### 🛸 Pruebas con Colección de Insomnia

Para agilizar la evaluación de los flujos del sistema (Login, creación de productos, ingresos de inventario, procesos de reservas, confirmación y liberación), se incluye en la raíz del repositorio el archivo de configuración correspondiente a la colección de endpoints listos para importar directamente en tu espacio de trabajo de Insomnia.
