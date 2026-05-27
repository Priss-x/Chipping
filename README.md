# 🛒 Chipping - E-Commerce Geek & Friki (Arquitectura de Microservicios)

¡Bienvenidos a **Chipping**! Este proyecto es una plataforma distribuida de comercio electrónico diseñada especialmente para la comunidad geek, friki y otaku. Nace de la pasión de dos aficionados por el mundo del coleccionismo y está enfocado en la venta de artículos de culto: mangas, figuras de acción, ropa temática y accesorios coleccionables.

El sistema implementa una arquitectura orientada a microservicios robusta, modular y desacoplada, utilizando el ecosistema de Spring Boot para asegurar que cada parte de la tienda funcione de forma independiente, garantizando alta disponibilidad, escalabilidad y tolerancia a fallos.

---

## 🏗️ Estructura del Sistema (Microservicios y Puertos)

Para evitar un sistema monolítico pesado, Chipping divide sus responsabilidades en componentes especializados. Cada uno maneja su propia persistencia de datos de forma aislada y corre en un puerto asignado:

* **Usuarios (`usuarios` - Puerto 8093)**
* **Productos (`productos` - Puerto 8090)**
* **Proveedores (`proveedores` - Puerto 8091)**
* **Inventario (`inventario` - Puerto 8092)**
* **Pedido (`pedido` - Puerto 8094)**
* **Notificaciones (`notificaciones` - Puerto 8095)**
* **Carro de Compras (`carrocompra` - Puerto 8096)**
* **Pagos (`pagos` - Puerto 8097)**
* **Envíos (`envios` - Puerto 8098)**
* **Reseñas (`resenas` - Puerto 8099)**

---

## 🔄 Flujo Transaccional y Modelo de Operación

El ecosistema interactúa de forma dinámica a través del siguiente ciclo de vida de extremo a extremo:

1. **Registro y Autenticación:** El usuario se registra o inicia sesión $\rightarrow$ `usuarios (8093)`
2. **Navegación:** El usuario busca productos $\rightarrow$ `productos (8090)`, el cual consulta internamente a `proveedores (8091)`
3. **Selección:** El usuario agrega artículos al carro $\rightarrow$ `carrocompra (8096)`, validando existencias de forma síncrona en `inventario (8092)`
4. **Control de Alertas:** Si la bodega detecta bajo stock $\rightarrow$ `inventario (8092)` notifica de forma **asíncrona** a `notificaciones (8095)` para activar el reabastecimiento con el proveedor.
5. **Compra:** El usuario confirma su carrito y crea el pedido $\rightarrow$ `pedido (8094)`, leyendo los datos consolidados de `carrocompra (8096)`
6. **Transacción Financiera:** El usuario realiza el pago $\rightarrow$ `pagos (8097)`, validando el monto y estado generado en `pedido (8094)`
7. **Logística:** El operador logístico procesa la orden y crea el envío $\rightarrow$ `envios (8098)`, cambiando el estado de `pedido (8094)`
8. **Entrega:** El cliente recibe su producto en destino $\rightarrow$ `envios (8098)` e informa la recepción exitosa a `pedido (8094)`
9. **Fidelización:** El cliente deja una opinión de su artículo $\rightarrow$ `resenas (8099)`, validando que el ítem exista en `productos (8090)`
10. **Trazabilidad:** Durante todo el viaje, el cliente recibe actualizaciones del tracking $\rightarrow$ `notificaciones (8095)`

---

## 🛠️ Tecnologías y Stack Utilizado

- **Entorno de Desarrollo:** IntelliJ IDEA.
- **Backend Core:** Java 17+ y Spring Boot 3.x.
- **Base de Datos:** MySQL / MariaDB gestionado de forma local mediante **XAMPP**.
- **Gestión de Ciclo de Vida de BD:** **Flyway**, automatizando la creación y migración de tablas al arrancar cada servicio.
- **Validación de Datos:** Jakarta Validation para asegurar payloads limpios a nivel de controlador (`@NotBlank`, `@Min`).
- **Pruebas de Integración:** Postman (colecciones HTTP/REST organizadas por puertos).

---

## ⚙️ Requisitos Previos

Para levantar y auditar este ecosistema en tu máquina local, necesitas:
1. **Java Development Kit (JDK) 17** o superior.
2. **XAMPP** (con los servicios de Apache y MySQL activos).
3. **IntelliJ IDEA** instalado.
4. **Postman Desktop** (para la simulación y testeo de la pasarela de endpoints).

---

## 🚀 Cómo Ejecutar

### 1. Inicializar Persistencia (XAMPP)
1. Inicia el panel de **XAMPP** y enciende **Apache** y **MySQL**.
2. Dirígete a `http://localhost/phpmyadmin/` y crea los esquemas vacíos para tus microservicios (`usuarios_db`, `productos_db`, etc.).
3. Al arrancar las aplicaciones en el IDE, **Flyway** detectará el historial e inyectará de manera automática las tablas correspondientes.

### 2. Despliegue de Servicios
1. Abre el espacio de trabajo en **IntelliJ IDEA**.
2. Ejecuta las clases con la anotación `@SpringBootApplication` de cada uno de los microservicios.
3. El sistema estará listo para escuchar peticiones en la malla de puertos asignada (desde el `8090` al `8099`).

### 3. Probar los Flujos en Postman (algunos ejemplos)
Importa tus colecciones en **Postman** y ejecuta las peticiones en el siguiente orden lógico para simular la experiencia completa de la tienda:

1. **Registrar Usuario:** `POST http://localhost:8093/api/usuarios/registrar`  
   *Registra tu cuenta de cliente en la plataforma.*
2. **Login de Usuario:** `POST http://localhost:8093/api/usuarios/login`  
   *Inicia sesión y captura el `usuarioId` generado.*
3. **Agregar al Carro:** `POST http://localhost:8096/carro/{usuarioId}/items`  
   *Añade tus mangas o figuras favoritas al carro y observa cómo se altera síncronamente el stock en el puerto de Inventario (`8092`).*
4. **Crear Pedido:** `POST http://localhost:8094/pedido`  
   *Confirma el carro de compras para transformarlo en una orden formal.*
