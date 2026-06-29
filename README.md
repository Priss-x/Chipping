# 🛒 Chipping - E-Commerce Geek & Friki (Arquitectura de Microservicios)

¡Bienvenidos a Chipping! Este proyecto es una plataforma distribuida de comercio electrónico diseñada especialmente para la comunidad geek, friki y otaku. Nace de la pasión de dos aficionados por el mundo del coleccionismo y está enfocado en la venta de artículos de culto: mangas, figuras de acción, ropa temática, videojuegos, accesorios y artículos de colección.

El sistema implementa una arquitectura orientada a microservicios robusta, modular y desacoplada, utilizando el ecosistema de Spring Boot para asegurar que cada parte de la tienda funcione de forma independiente, garantizando alta disponibilidad, scalability y tolerancia a fallos. Como evolución clave del proyecto, se incorporó un **API Gateway** centralizado con seguridad **JWT (JSON Web Token)** para unificar el acceso y proteger los endpoints perimetrales de la plataforma.

## 🏗️ Estructura del Sistema (Microservicios y Puertos)

Para evitar un sistema monolítico pesado, Chipping divide sus responsabilidades en componentes especializados. Cada uno maneja su propia persistencia de datos de forma aislada y corre en un puerto asignado, centralizando el tráfico externo a través de la aduana del sistema:

* **API Gateway (Puerto 8086):** Punto único de entrada, seguridad JWT y enrutamiento inteligente.
* **Usuarios (Puerto 8093):** Registro y perfiles de clientes.
* **Productos (Puerto 8090):** Catálogo principal de la tienda.
* **Proveedores (Puerto 8091):** Administración de proveedores.
* **Inventario (Puerto 8092):** Control de stock físico y existencias.
* **Pedido (Puerto 8094):** Gestión de pedidos.
* **Notificaciones (Puerto 8095):** Alertas, eventos y tracking de la app.
* **Carro de Compras (Puerto 8096):** Gestión del carrito activo (mapeado en MySQL mediante la columna numérica `estado_id` conectada a estados lógicos como `'ACTIVO'`).
* **Pagos (Puerto 8097):** Procesamiento de transacciones.
* **Envíos (Puerto 8098):** Gestión logística y despachos.
* **Reseñas (Puerto 8099):** Opiniones y valoraciones de los clientes.

## 🔐 API Gateway y Seguridad JWT

El API Gateway (desarrollado con Spring Cloud Gateway) actúa como la frontera del sistema. En lugar de exponer los puertos internos directamente, los clientes interactúan únicamente con este único punto de entrada:

1.  **Autenticación (Login):** Se envían las credenciales de acceso al endpoint del Gateway.
    * `POST http://localhost:8086/auth/login`
    * **Body (JSON):**
        ```json
        {
            "username": "TU_USUARIO_CONFIGURADO",
            "password": "TU_PASSWORD_CONFIGURADA"
        }
        ```
    * **Respuesta:** Devuelve la firma autorizada (`{"token":"eyJhbG..."}`).
2.  **Autorización:** Posteriormente, todas las solicitudes protegidas de la malla utilizan la cabecera: `Authorization: Bearer TU_TOKEN`. El Gateway intercepta la petición, valida la firma criptográfica, extrae la información del usuario junto a su rol (`ADMIN` o `USER`) y determina el acceso antes de encaminar la solicitud al microservicio correspondiente.

## 🔄 Flujo Transaccional y Modelo de Operación

El ecosistema interactúa de forma dinámica a través del siguiente ciclo de vida de extremo a extremo:

* **Registro y Autenticación:** El usuario se registra o inicia sesión para obtener su token JWT de acceso seguro → `Gateway (8086)`.
* **Navegación:** El usuario busca productos, tomos de manga o figuras de acción coleccionables → `Productos (8090)`, el cual consulta internamente a `Proveedores (8091)`.
* **Selección:** El usuario agrega artículos al carro → `Carro de Compras (8096)`, validando existencias de forma síncrona en `Inventario (8092)`.
* **Control de Alertas:** Si la bodega detecta bajo stock → `Inventario (8092)` notifica de forma asíncrona a `Notificaciones (8095)` para activar el reabastecimiento con el proveedor.
* **Compra:** El usuario confirma su carrito y crea el pedido → `Pedido (8094)`, leyendo los datos consolidados de `Carro de Compras (8096)`.
* **Transacción Financiera:** El usuario realiza el pago → `Pagos (8097)`, validando el monto y estado generado en `Pedido (8094)`.
* **Logística:** El operador logístico procesa la orden y crea el envío → `Envíos (8098)`, cambiando el estado de `Pedido (8094)`.
* **Entrega:** El cliente recibe su producto en destino → `Envíos (8098)` e informa la recepción exitosa a `Pedido (8094)`.
* **Fidelización:** El cliente deja una opinión de su artículo → `Reseñas (8099)`, validando que el ítem exista en `Productos (8090)`.
* **Trazabilidad:** Durante todo el viaje, el cliente recibe actualizaciones del tracking → `Notificaciones (8095)`.

## 🛠️ Tecnologías y Stack Utilizado

* **Entorno de Desarrollo:** IntelliJ IDEA.
* **Backend Core:** Java 17 y Spring Boot 3.x / 4.0.6.
* **Ecosistema Cloud & Seguridad:** Spring Cloud Gateway, Spring Security y JSON Web Token (JWT).
* **Base de Datos:** MySQL / MariaDB gestionado de forma local mediante XAMPP.
* **Gestión de Ciclo de Vida de BD:** Flyway, automatizando la creación y migración de tablas al arrancar cada servicio.
* **Validación de Datos:** Jakarta Validation para asegurar payloads limpios a nivel de controlador (`@NotBlank`, `@Min`).
* **Mapeo de Datos:** Hibernate y **Lombok** sincronizado en el compilador de Maven mediante `annotationProcessorPaths`.
* **Pruebas de Integración:** Postman (colecciones HTTP/REST unificadas en el puerto del Gateway) y JUnit 5.

## ⚙️ Requisitos Previos

Para levantar y auditar este ecosistema en tu máquina local, necesitas:

* Java Development Kit (JDK) 17 o superior.
* Maven.
* XAMPP (con los servicios de Apache y MySQL activos).
* IntelliJ IDEA instalado.
* Postman Desktop (para la simulación y testeo de la pasarela de endpoints).

## 🚀 Cómo Ejecutar

1.  **Inicializar Persistencia (XAMPP):** Inicia el panel de XAMPP y enciende Apache y MySQL. Dirígete a `http://localhost/phpmyadmin/` y crea los esquemas vacíos para tus microservicios (`usuarios_db`, `productos_db`, etc.). Al arrancar las aplicaciones en el IDE, Flyway detectará el historial e inyectará de manera automática las tablas y datos correspondientes.
    *(Asegúrate de que en el módulo de carro la tabla use la columna relacional numérica `estado_id` e inicializa el id `1` con el nombre `ACTIVO` en la tabla de estados).*
2.  **Despliegue de Servicios:** Abre el espacio de trabajo en el IDE. Ejecuta las clases con la anotación `@SpringBootApplication` de cada uno de los microservicios, asegurándote de encender primero el **`gateway-service`** (`8086`) para habilitar el perímetro seguro.
3.  **Probar los Flujos en Postman:** Importa tus colecciones y ejecuta las peticiones enviando las solicitudes de forma unificada a través de la pasarela del Gateway:
    * **Obtener Token JWT (Login):** `POST http://localhost:8086/auth/login` con tus credenciales configuradas.
    * **Consumir Rutas Protegidas:** Adjunta en los headers de tus peticiones la clave `Authorization: Bearer TU_TOKEN`.
    * **Listar Catálogo:** `GET http://localhost:8086/productos` para traer los artículos directo de la base de datos de inventario.
    * **Agregar al Carro:** `POST http://localhost:8086/carro/1/items` para insertar tus mangas o figuras favoritas en el carrito.

## 🧪 Escenarios de Validación

La suite de pruebas en Postman y JUnit 5 verifica los siguientes comportamientos críticos del API Gateway:

* Generación correcta y segura del formato JWT.
* Validación criptográfica (rechazo de tokens manipulados, mal estructurados o expirados).
* Extracción dinámica del usuario y control de accesos restringido basándose en el rol del usuario para proteger las operaciones críticas del inventario.
