# Historias Técnicas

## HT-001 – Configuración del Proyecto Base
`Story Points: 2`

**Justificación SP:** Infraestructura base sin lógica de negocio, pero es la base del proyecto y requiere configuración de entorno.

### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Inicializar proyecto Spring Boot con dependencias: Web, Data JPA, Validation, Security y JWT| Medio |
| 2 | Configurar `application.properties` con las variables de entorno para la conexión a la BD | Bajo |
| 3 | Crear `Dockerfile` para la aplicación Spring Boot | Bajo |
| 4 | Crear `docker-compose.yml` que levante la aplicación Spring Boot | Bajo |
| 5 | Implementar endpoint `GET /api/health` | Bajo |

### QA
**Justificación SP:** Validación de entorno e infraestructura sin lógica de negocio compleja.
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Validar que el proyecto compile sin errores ejecutando del clean compile | Bajo |
| 2 | Probar el levantamiento con docker-compose ejecutando docker-compose up y verificando que el contenedor arranque correctamente | Medio |
|3| Verificar que las variables de entorno se carguen correctamente desde el archivo de configuración | Bajo |



## HT-002 – Diseño de la Base de Datos
`Story Points: 3`

**Justificación SP:** Define el esquema completo del sistema con relaciones entre múltiples entidades y constraints de integridad referencial.

### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Definir entidades JPA: `Asegurado`, `Vehiculo`, `Poliza`, `Reclamo`, `ReclamoFotografia`, `ReclamoBandera` | Alto |
| 2 | Configurar relaciones `@OneToMany` y `@ManyToOne` entre entidades | Medio |
| 3 | Agregar contenedor PostgreSQL al `docker-compose.yml` con variables de entorno | Bajo |
| 4 | Configurar `spring.jpa.hibernate.ddl-auto` en `application.properties` para la generación automática de tablas | Bajo |
| 5 | Agregar constraint de integridad referencial: no eliminar asegurado con póliz
### QA
**Justificación SP:** Verificación de esquema, relaciones y restricciones de integridad en BD.
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Validar generación del esquema levantando la aplicación y verificando que todas las tablas se creen correctamente en PostgreSQL | Medio |
| 2 | Verificar restricciones de integridad intentando borrar registros con relaciones | Medio |
| 3 | Probar inserción de datos de prueba en cada entidad verificando que las relaciones funcionen correctamente | Medio |
| 4 | Validar que los campos obligatorios se respeten a nivel de base de datos | Bajo |




## HT-003 – Seguridad y Control de Acceso
`Story Points: 5`

**Justificación SP:** Involucra Spring Security, JWT, filtros de autenticación y control de acceso por roles.

### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear entidad `Usuario` con atributos (email, password, rol) | Bajo |
| 2 | Implementar repositorio `UsuarioRepository` | Bajo |
| 3 | Implementar `POST /api/v1/auth/login` que valida credenciales y retorna token JWT | Alto |
| 4 | Implementar filtro JWT para validar token en cada request protegido | Alto |
| 5 | Configurar `SecurityFilterChain` con reglas por rol: GESTOR, ASEGURADO | Medio |
| 6 | Hashear contraseñas antes de guardarlas | Bajo |
 
### QA
**Justificación SP:** Cubre flujo JWT, control de acceso por roles y almacenamiento seguro de contraseñas.
| # | Tarea | Esfuerzo |
|---|-------|----------|
|1 | Validar el flujo completo de login: hacer POST con credenciales válidas y verificar que se devuelva un token JWT | Medio |
| 2 | Probar login con credenciales inválidas y verificar que se devuelva un 404 con mensaje descriptivo | Bajo |
| 3 | Validar que los endpoints protegidos rechacen requests sin token | Medio |
| 4| Validar que las contraseñas se almacenen hasheadas en la base de datos y no en texto plano | Bajo |


# Historias de Usuario

## HU-001 – Registro de Asegurado
`Story Points: 3`

**Justificación SP:** Validaciones múltiples y manejo de varios escenarios de error.

### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear entidad JPA `Asegurado` con sus atributos (nombre, apellido, numeroIdentificacion, direccion, telefono, correoElectronico) | Bajo |
| 2 | Implementar repositorio `AseguradoRepository` | Bajo |
| 3 | Implementar validaciones de unicidad de identificacion y correo | Medio |
| 4 | Crear DTO `AseguradoDTO` | Bajo |
| 5 | Crear service `AseguradoService` con la logica de negocio para registrar asegurados | Medio |
| 6 | Implementar `POST /api/v1/asegurados` en el controller | Medio |
 
### QA
**Justificación SP:** En el rol de QA para esta historia de usuario refleja la necesidad de validar  las reglas de negocio y casos límite para asegurar la integridad de los datos.
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1| Diseñar la matriz de datos de prueba con diferentes combinaciones de datos válidos e inválidos: campos vacíos, identificaciones duplicadas, correos con formato incorrecto, y casos límite en longitudes de campos | Medio |
| 2| Escribir tests unitarios del servicio de negocio para verificar que las validaciones de unicidad y formato| Medio |
|3 | Crear tests de integración para el endpoint POST para probar el flujo completo desde el controller, validando respuestas HTTP y estructura de errores | Alto |
|4 | Ejecutar pruebas exploratorias sobre el registro utilizando pruebas automatizadas| Medio |
| 5 | Documentar hallazgos y casos de prueba ejecutados, registrando resultados obtenidos | Bajo |
## HU-002 – Consultar Asegurados
`Story Points: 2`
 
**Justificación SP:** Solo lectura con dos endpoints simples y manejo de un único caso de error.
 
### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Implementar `GET /api/v1/asegurados` que retorna lista con información básica de cada asegurado | Bajo |
| 2 | Implementar `GET /api/v1/asegurados/{id}` que retorna el detalle completo del asegurado | Bajo |
| 3 | Manejar excepción cuando el asegurado no existe | Bajo |
 
### QA
**Justificación SP:** En el rol de QA se concentra en verificar diferentes escenarios de consulta, casos límite y performance básica.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Generar datos de prueba en la base de datos para validar que el listado y la consulta individual funcionen  | Bajo |
| 2 | Escribir tests de integración para ambos endpoints verificando que GET devuelva la consulta correcta | Medio |
| 3 | Realizar pruebas de consultas masivas con diferentes volumenes validando el tiempo de respuesta del listado | Medio |

## HU-003 – Registrar Vehículo
`Story Points: 3`
 
**Justificación SP:** Parecido a HU-001, validaciones múltiples y manejo de varios escenarios de error.
 
### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear entidad JPA `Vehiculo` con atributos (marca, modelo, anio, placa) | Bajo |
| 2 | Implementar repositorio `VehiculoRepository` con validación de unicidad de placa | Bajo |
| 3 | Crear DTO `VehiculoRequestDTO` y `VehiculoResponseDTO` | Bajo |
| 4 | Implementar validaciones: campos obligatorios, formato de placa, año numérico positivo | Medio |
| 5 | Implementar `POST /api/v1/vehiculos` con respuesta HTTP 201 para creaciones exitosas | Medio |
| 6 | Manejar excepción de placa duplicada | Bajo |
 
### QA
**Justificación SP:** En el rol QA se enfoca en probar las validaciones y casos límite del formato de placa. La complejidad se justifica por las diferentes validaciones que se debe realizar por el formato de la placa en Ecuador
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar matriz de datos de prueba preparando casos con placas duplicadas, años inválidos (texto, negativos, futuros), campos obligatorios faltantes, y formatos de placa incorrectos | Medio |
| 2 | Escribir tests unitarios del servicio verificando validaciones de placas y formato de datos | Medio |
| 3 | Crear tests de integración del endpoint POST probando el flujo completo de registro incluyendo validaciones| Medio |
| 4 | Ejecutar pruebas exploratorias sobre campos de texto con diferentes combinaciones| Medio |

## HU-004 – Consultar Vehículos
`Story Points: 2`
 
**Justificación SP:** Solo lectura con dos endpoints simples y manejo de un único caso de error.
 
### DEV
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Implementar `GET /api/v1/vehiculos` que retorna lista con información básica de cada vehículo | Bajo |
| 2 | Implementar `GET /api/v1/vehiculos/{id}` que retorna el detalle completo del vehículo | Bajo |
| 3 | Manejar excepción cuando el vehículo no existe | Bajo |
 
### QA
**Justificación SP:** Para rol de QA incluye realizar diferentes tipos de test para verificar que las consultas para buscar vehículos se ejecuten de manera correcta.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Preparar datos de prueba creando vehículos de diferentes marcas y años para validar| Bajo |
| 2 | Escribir tests de integración verificando que ambos endpoints devuelvan los datos correctos y en el formato esperado | Medio |
| 3 | Validar comportamiento con vehículo inexistente probando que la consulta devuelva 404 | Bajo |

## HU-005: Registrar Póliza de Seguro 
`Story Points: 3` 

### DEV

**Justificación SP:** El esfuerzo de DEV es medio-alto por las múltiples validaciones de negocio: unicidad, relaciones entre entidades, rangos de fechas y estado automático.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear entidad JPA `Poliza` con atributos (numero, aseguradoId, vehiculoId, valorAsegurado, vigenciaInicio, vigenciaFin, estado) usando anotaciones `@ManyToOne` para las relaciones | Medio |
| 2 | Implementar repositorio `PolizaRepository` incluyendo método `findByNumero()` para validar unicidad | Bajo |
| 3 | Crear service `PolizaService` con la lógica de negocio para registrar pólizas: unicidad del número, existencia del asegurado y vehículo, rango de fechas válido y valor asegurado > 0 | Alto |
| 4 | Crear DTO `PolizaRequestDTO` con validaciones para número, fechas, valor asegurado y referencias a asegurado y vehículo | Medio |
| 5 | Implementar `POST /api/v1/polizas` en el controller persistiendo la póliza con estado `ACTIVA` | Medio |
| 6 | Implementar validación de fechas de vigencia validando que fecha_fin > fecha_inicio | Medio |

### QA
**Justificación SP:** Para rol QA se concentra en probar todas las combinaciones de validaciones y relaciones, requiriendo tests de integración complejos.
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar matriz de datos de prueba preparando casos con números duplicados, asegurados, vehículos , rangos de fechas validos e inválidos, valores asegurados negativos o cero | Medio |
| 2 | Escribir tests unitarios del servicio verificando todas las validaciones de negocio  | Medio |
|3 | Crear tests de integración probando el flujo completo desde el controller, validando las relaciones con asegurado y vehículo | Alto |
| 4 | Validar cálculo automático del estado verificando que la póliza quede automáticamente en estado ACTIVA posterior al registro | Bajo |
| 5 | Ejecutar pruebas exploratorias de fechas límite probando fechas en el límite y diferentes variantes de fechas | Medio |

## HU-006: Consultar Pólizas
`Story Points: 2`

### DEV

**Justificación SP:** Solo lectura con carga de relaciones entre entidades; la complejidad es baja al reutilizar repositorios ya existentes.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Agregar métodos de consulta al repositorio asegurando que se carguen las relaciones necesarias con `findAll()` y `findById()` | Bajo |
| 2 | Implementar service de consulta con métodos para listado y detalle, incluyendo información del asegurado y vehículo asociados | Medio |
| 3 | Crear DTOs de response con datos de la póliza y resúmenes del asegurado y vehículo vinculados | Medio |
| 4 | Exponer `GET /api/v1/polizas` para listado y `GET /api/v1/polizas/{id}` para detalle en el controller | Medio |

### QA
**Justificación SP:** Para rol de QA se enfoca en validar la consultas correctas obteniendo información completa de las polizas.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Preparar datos de prueba creando pólizas con diferentes estados (ACTIVA, INACTIVA) y fechas  para validar consultas | Bajo |
| 2 | Escribir tests de integración verificando que los endpoints devuelvan correctamente la información de la póliza| Alto |
| 3 | Validar comportamiento con póliza inexistente, esperando un 404 | Bajo |
| 4 | Verificar carga de relaciones asegurando que no haya problemas al realizar varias consultas | Medio |

## HU-007: Registro de Reclamo de Siniestro
`Story Points: 5`

### DEV

**Justificación SP:** Gestión de archivos multipart, generación de número de seguimiento y persistencia transaccional.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear entidad JPA `Reclamo` con atributos (poliza_id, fecha_incidente, descripcion, monto_estimado, ubicacion, estado, numero_seguimiento) | Medio |
| 2 | Crear entidad JPA `ReclamoFotografia` con relación `@OneToMany` hacia Reclamo para almacenar la URL de cada imagen | Medio |
| 3 | Configurar repositorios para Reclamo y ReclamoFotografia con métodos personalizados necesarios | Bajo |
| 4 | Implementar service de almacenamiento de archivos que valide formato PNG/JPG, tamaño y guarde las imágenes en el filesystem | Alto |
| 5 | Generar número de seguimiento único con formato "REC-YYYY-NNN" de forma secuencial | Medio |
| 6 | Implementar service de registro de reclamos validando: póliza activa, campos obligatorios, monto > 0 y fecha del incidente dentro de la vigencia | Alto |
| 7 | Crear DTO de request que soporte subida de archivos múltiples junto con los datos del reclamo | Medio |
| 8 | Exponer `POST /api/v1/reclamos` en el controller recibiendo multipart/form-data con datos y fotografías | Medio |
| 9 | Validar formato de archivos adjuntos aceptando solo PNG y JPG, rechazando otros formatos con mensaje claro | Medio |
| 10 | Implementar persistencia transaccional con `@Transactional` para revertir el reclamo si falla el guardado de fotografías | Medio |


### QA
**Justificación SP:** Para rol QA el esfuerzo es alto porque requiere probar múltiples escenarios de archivos, validar la transaccionalidad y asegurar que no haya casos límite sin cubrir.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar matriz de datos de prueba preparando casos con pólizas inactivas, fechas futuras, montos cero o negativos, fotografías en formatos incorrectos, sin fotografías | Medio |
| 2 | Escribir tests unitarios del servicio verificando todas las validaciones y número de seguimiento | Alto |
| 3 | Crear tests de integración del endpoint probando el flujo completo incluyendo la subida de fotografias | Alto |
| 4 | Validar almacenamiento de fotografías verificando que las fotografías se guarden correctamente | Medio |
| 5 | Probar límites de tamaño del tamaño de los archivos de fotografías, validando los posibles casos | Medio |
| 6 | Realizar pruebas exploratorias de formatos de imagen probando con archivos, con extensión cambiada diferente | Medio |
| 7 | Validar mensajes de error descriptivos asegurando que cada tipo de error devuelva un mensaje claro  | Medio |

## HU-008: Validación de póliza para procesamiento de reclamo 
`Story Points: 5`

### DEV

**Justificación SP:** Motor de reglas extensible con múltiples reglas encadenadas y logs de auditoría.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar interfaz del motor de reglas con contrato genérico para evaluar reglas de negocio sobre un reclamo | Medio |
| 2 | Implementar regla de vigencia de póliza verificando si está activa y si la fecha del incidente está dentro del rango | Medio |
| 3 | Implementar regla de antigüedad de póliza calculando la diferencia entre inicio de vigencia y fecha del reclamo, escalando si es < 30 días | Medio |
| 4 | Crear entidad JPA `ReclamoBandera` para registrar las banderas rojas de cada reclamo con su descripción | Medio |
| 5 | Implementar service coordinador que orqueste las reglas en orden: vigencia → antigüedad → monto e historial | Alto |
| 6 | Actualizar service de registro de reclamos para que invoque automáticamente al motor de reglas tras persistir el reclamo | Medio |
| 7 | Implementar cambio de estado del reclamo con métodos para actualizar a RECHAZADO, EN REVISIÓN MANUAL, etc. y registrar motivos | Medio |
| 8 | Agregar logs de auditoría con logging detallado de cada evaluación de regla para trazabilidad y debugging | Medio |
 
### QA
**Justificación SP:** Para rol QA el esfuerzo  es alto porque requiere probar cada regla de forma aislada y en combinación de validar la póliza.
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar casos de prueba para cada regla preparando datos con pólizas inactivas, vencidas, con antigüedad definidas en las reglas de las polizas | Medio |
| 2 | Escribir tests unitarios de cada regla probando las reglas de forma aislada con diferentes estados de póliza y fechas | Alto |
| 3 | Crear tests de integración del flujo completo verificando que al registrar un reclamo | Alto |
| 4 | Validar que se registren las banderas correctamente verificando que cuando un reclamo se escala| Medio |
| 5 | Probar casos límite de fechas validas al regustrar un reclamo | Medio |

## HU-009: Evaluación de reclamo por reglas de deducible y monto
`Story Points: 5`

### DEV

**Justificación SP:** Cálculos financieros con tres variables para el deducible e integración en el coordinador.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Implementar cálculo del deducible como el mayor entre: 10% del monto del siniestro, 1% del valor asegurado, y $200 | Medio |
| 2 | Implementar regla de descarte por monto bajo comparando el monto estimado con el deducible y descartando si es menor o igual | Medio |
| 3 | Implementar regla de monto elevado calculando el 20% del valor asegurado y escalando si el monto es >= a ese umbral | Medio |
| 4 | Agregar campos de deducible calculado y monto aprobado en la entidad Reclamo | Bajo |
| 5 | Integrar reglas de monto en el coordinador para que se ejecuten después de validar la póliza | Medio |
| 6 | Implementar registro del motivo de descarte cuando un reclamo es descartado por monto bajo | Medio |
| 7 | Asegurar que los cálculos se hagan en memoria sin consultas adicionales innecesarias a la base de datos | Medio |

### QA
**Justificación SP:** Para rol QA es alto porque requiere probar  los casos límite y la precisión de los cálculos , especialmente dado que son cálculos financieros. 
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar casos de prueba para el cálculo del deducible preparando escenarios con diferentes tipos de condiciones| Medio |
|2 | Crear tests unitarios del cálculo del deducible verificando con diferentes combinaciones  | Medio |
| 3 | Escribir tests para la regla de descarte probando casos donde el monto es menor, igual o mayor al deducible | Medio |
| 4 | Escribir tests para la regla de monto elevado probando casos diferentes de montos | Medio |
| 5 | Crear tests de integración del flujo completo verificando que un reclamo pase por todas las validaciones | Alto |
| 6 | Validar casos límite de montos probando con montos exactamente iguales y variados para evaluar reclamos | Medio |

##  HU-010: Evaluación de reclamo por historial de siniestros 
`Story Points: 3`

### DEV

**Justificación SP:** Consulta de historial con rango de fechas, dos reglas de evaluación y aprobación automática final.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear consulta de historial en el repositorio de Reclamos que cuente los siniestros de un asegurado en los últimos 12 meses | Medio |
| 2 | Implementar regla de historial limpio aprobando automáticamente si el asegurado tiene 0 siniestros en los últimos 12 meses | Medio |
| 3 | Implementar regla de historial con banderas escalando a revisión manual si el asegurado tiene >= 2 siniestros en los últimos 12 meses | Medio |
| 4 | Integrar reglas de historial en el coordinador para que se ejecuten después de las reglas de monto | Medio |
| 5 | Implementar aprobación automática final cambiando el estado a APROBADO y calculando el monto aprobado (monto estimado - deducible) cuando todas las reglas pasen | Medio |
| 6 | Agregar campo de fecha de resolución con timestamp de cuándo se resolvió el reclamo (automática o manualmente) | Bajo |

### QA
**Justificación SP:** Para rol QA se enfoca en probar combinaciones de historial y verificar el flujo end-to-end desde el registro hasta la aprobación. 
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Diseñar casos de prueba para historial preparando escenarios diferentes| Medio |
| 2 | Escribir tests unitarios de la consulta de historial verificando que el conteo de siniestros en rango de fechas | Medio |
| 3 | Crear tests para la regla de historial sin siniestros y aprovación | Medio |
| 4 | Crear tests para la regla de historial con banderas de siniestros| Medio |
| 5 | Validar el flujo completo de aprobación automática verificando que un reclamo que pasa todas las reglas  | Alto |
##  HU-011: Panel del gestor de seguros
`Story Points: 3`

### DEV

**Justificación SP:** Consultas complejas con múltiples relaciones cargadas y control de acceso por rol.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear consulta en el repositorio que filtre reclamos por estado "EN REVISIÓN MANUAL" | Medio |
| 2 | Implementar service de consulta de reclamos escalados que devuelva el listado con sus banderas e información del asegurado | Alto |
| 3 | Crear DTO de response con número de seguimiento, datos del asegurado, monto, fecha y banderas activas | Medio |
| 4 | Exponer `GET /api/v1/reclamos/escalados` protegido con rol GESTOR que devuelva el listado de reclamos en revisión manual | Medio |
| 5 | Implementar consulta de detalle de reclamo con toda la información: póliza, asegurado, vehículo y fotografías | Alto |
| 6 | Exponer `GET /api/v1/reclamos/{numeroSeguimiento}/detalle` para que el gestor vea toda la información de un reclamo específico | Medio |

### QA
**Justificación SP:** El esfuerzo del rol QA se concentra en validar la autorizacióny la integridad de los datos complejos. .
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Preparar datos de prueba con reclamos escalados creando múltiples reclamos en estado de revisión manual | Medio |
| 2 | Escribir tests de integración del endpoint de listado verificando que el endpoint devuelva correctamente los reclamos  | Medio |
| 3 | Ejecutar pruebas exploratorias del panel navegando manualmente por el panel verificando que todos los datos se muestren correctamente | Medio |

##  HU-012: Resolución de reclamo escalado por el gestor
`Story Points: 3`

### DEV

**Justificación SP:** Resolución manual con validaciones, auditoría del gestor y placeholder de notificación.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Crear DTO de request para resolución con la decisión (aprobar/rechazar) y la justificación obligatoria | Medio |
| 2 | Implementar service de resolución manual que actualice el estado, registre la justificación, el gestor responsable y la fecha de resolución | Alto |
| 3 | Validar que la justificación no esté vacía antes de procesar la resolución | Bajo |
| 4 | Exponer `PUT /api/v1/reclamos/{numeroSeguimiento}/resolucion` protegido con rol GESTOR para registrar la decisión manual | Medio |
| 5 | Agregar campos de auditoría en la entidad Reclamo para almacenar el ID del gestor que tomó la decisión | Medio |
| 6 | Implementar notificación al asegurado como placeholder registrando un log por ahora, sin envío real de emails | Medio |

### QA
**Justificación SP:** El esfuerzo QA se enfoca en validar la autorización, las justificaciones, y la trazabilidad completa. 
| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1| Diseñar casos de prueba para aprobación y rechazo preparando para cada escenario | Medio |
| 2 | Escribir tests de validación de justificación verificando que el sistema rechace dependiendo la justificación | Medio |
| 3 | Crear tests de integración del endpoint probando el flujo completo | Alto |
| 4 | Ejecutar pruebas exploratorias de justificaciones probando diferentes textos. | Medio |
##  HU-013: Consulta de estado de reclamo por el asegurado
`Story Points: 3`

### DEV

**Justificación SP:** Filtro de autorización por asegurado y formateo de mensajes según estado.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Implementar consulta en el repositorio que busque un reclamo por su número de seguimiento único | Bajo |
| 2 | Implementar service de consulta de estado que devuelva el estado, motivo de decisión (si aplica) y detalles del reclamo | Medio |
| 3 | Crear DTO de response con estado, motivo, monto aprobado (si aplica) y deducible | Medio |
| 4 | Exponer `GET /api/v1/reclamos/{numeroSeguimiento}/estado` protegido con rol ASEGURADO permitiendo consultar solo sus propios reclamos | Medio |
| 5 | Implementar filtro de autorización asegurando que un asegurado solo pueda ver sus propios reclamos | Alto |
| 6 | Agregar formateo de motivos devolviendo mensajes amigables para cada tipo de resolución (aprobado, rechazado, en revisión, descartado) | Medio |

### QA
**Justificación SP:** Valida autorización por asegurado y mensajes descriptivos por tipo de resolución.

| # | Tarea | Esfuerzo |
|---|-------|----------|
| 1 | Preparar casos de prueba para cada estado posible creando reclamos en todos los estados| Medio |
|2| Escribir tests de integración del endpoint verificando que el endpoint devuelva la información según el estado del reclamo | Medio |
| 3| Verificar mensajes de decisión descriptivos asegurando que cada estado devuelva un mensaje claro | Medio |
| 4 | Ejecutar pruebas exploratorias de diferentes estados verificando su visualización | Medio |