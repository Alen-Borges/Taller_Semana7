# Documentación de Endpoints para el Frontend

Esta guía resume los recursos disponibles en el **Core API** para la gestión de asegurados, vehículos y pólizas.

**URL Base:** `http://localhost:8080/api/v1`  
**Formato de Datos:** `application/json`

---

## 👥 Asegurados
Gestión de clientes en el sistema.

### Registrar Asegurado
`POST /asegurados`

**Body:**
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "numeroIdentificacion": "0912345678",
  "direccion": "Av. Siempre Viva 123",
  "telefono": "0991234567",
  "correoElectronico": "juan.perez@example.com"
}
```

### Consultar Lista
`GET /asegurados`  
_Retorna un arreglo `[]` con todos los asegurados._

### Detalle por ID
`GET /asegurados/{id}`

---

## 🚗 Vehículos
Gestión de autos vinculados a un asegurado.

### Registrar Vehículo
`POST /vehiculos`

**Body:**
```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2024,
  "placa": "PBA-1234",
  "aseguradoId": 1
}
```

### Consultar Lista
`GET /vehiculos`  
_Retorna el listado de vehículos. Incluye el nombre completo del asegurado dueño en el campo `aseguradoNombreCompleto`._

### Detalle por ID
`GET /vehiculos/{id}`

---

## 📄 Pólizas
Gestión de contratos de seguros.

### Registrar Póliza
`POST /polizas`

**Body:**
```json
{
  "numero": "POL-2024-001",
  "aseguradoId": 1,
  "vehiculoId": 1,
  "valorAsegurado": 15000.50,
  "vigenciaInicio": "2024-03-24",
  "vigenciaFin": "2025-03-24"
}
```

### Consultar Lista
`GET /polizas`  
_Retorna el listado de pólizas. Incluye el estado (ACTIVA por defecto) y datos básicos legibles del asegurado y vehículo._

### Detalle por ID
`GET /polizas/{id}`

---

## 🛠 Códigos de Estado Comunes

| Código | Descripción | Explicación |
| :--- | :--- | :--- |
| `200 OK` | Éxito | La consulta fue realizada correctamente. |
| `201 Created` | Creado | El recurso se guardó exitosamente en la base de datos. |
| `400 Bad Request` | Error de Validación | Los datos enviados son incorrectamente estructurados o violan reglas de negocio (ej. placa duplicada, fecha fin < inicio). |
| `404 Not Found` | No Encontrado | El ID del recurso consultado no existe (ej. consultar un vehículo o asegurado inexistente). |

---

## 🔍 Health Check
Endpoint rápido para validar que el servidor está arriba:  
`GET http://localhost:8080/api/health` -> `{"status": "UP"}`
