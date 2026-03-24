# Historias de Usuario - Evaluador Automatizado de Siniestros

---

## HU-001 – Registro de asegurado


**Como** gestor de seguros,  
**Quiero** registrar los datos personales de un asegurado en el sistema,  
**Para** que pueda ser vinculado a un vehículo y una póliza de seguro.

**Prioridad:** Alta  
**Story Points:** 3


### Criterios de aceptación

#### Escenario 1: Registro correcto
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando registra un asegurado con todos los datos requeridos (nombre, apellido, identificación, dirección, teléfono 
y correo electrónico)
Entonces el sistema guarda el asegurado correctamente
Y queda disponible para ser usado en una póliza o vehículo
```

#### Escenario 2:  Faltan datos obligatorios
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando intenta registrar un asegurado sin completar todos los campos obligatorios
Entonces el sistema no permite guardar el registro
Y muestra qué campos faltan
```

#### Escenario 3: Identificación registrada previamente
```gherkin
Dado que ya existe un asegurado con una identificación registrada
Cuando el gestor intenta registrar otro asegurado con la misma identificación
Entonces el sistema no permite guardar el registro
Y muestra un mensaje indicando que ya existe
```

#### Escenario 4: Correo electrónico formato inválido
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando ingresa un correo con formato inválido
Entonces el sistema no permite guardar el registro
Y muestra un mensaje indicando el error en el correo
```

### Casos de Prueba

#### CP001-HU-001: Registro exitoso con todos los datos válidos
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando registra un asegurado proporcionando nombre "Juan", apellido "Pérez", identificación "1712345678", dirección "Av. Amazonas N36-152", teléfono "0991234567" y correo electrónico "juan.perez@correo.com"
Y confirma el registro
Entonces el sistema crea el asegurado exitosamente
Y el asegurado "Juan Pérez" queda disponible en el listado de asegurados.
```

#### CP002-HU-001: Intento de registro sin completar el campo nombre
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando intenta registrar omitiendo el campo nombre pero apellido "Pérez", identificación "1712345678", dirección "Av. Amazonas N36-152", teléfono "0991234567", correo electrónico "juan.perez@correo.com"
Y confirma el registro
Entonces el sistema  notifica que el campo nombre es obligatorio
Y no se crea ningún registro de asegurado en el sistema
```

#### CP003-HU-001: Intento de registro sin completar ningún campo
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando omite todos los datos obligatorios
Y confirma el registro
Entonces el sistema notifica todos los campos obligatorios faltantes
Y no se crea ningún registro de asegurado en el sistema
```

#### CP004-HU-001: Intento de registro con identificación ya existente
```gherkin
Dado que el gestor está autenticado  en el sistema
Y existe un asegurado registrado con identificación "1712345678"
Cuando registra a un asegurado con nombre "María", apellido "López", identificación "1712345678"
Y completa los datos válidos
Y confirma el registro
Entonces el sistema notifica el error indicando que ya existe un asegurado con esa identificación
Y no se crea un nuevo registro de asegurado
```

#### CP005-HU-001: Intento de registro con correo electrónico en formato inválido
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando registra a un asegurado con nombre "Juan", apellido "Pérez", identificación "1712345679", dirección "Av. Amazonas N36-152", teléfono "0991234567", correo electrónico "juan.perez-correo"
Y confirma el registro
Entonces el sistema notifica el error indicando que el formato de correo electrónico no es válido
Y no se crea ningún registro de asegurado en el sistema
```

## HU-002: Consultar Asegurados

**Como** gestor de seguros,
**Quiero** consultar asegurados registrados en el sistema, tanto en listado como en detalle individual,
**Para** verificar su información antes de vincularlos a pólizas o vehículos.

**Prioridad:** Alta  
**Story Points:** 2


### Criterios de Aceptación (Gherkin)

#### Escenario 1: Consulta de listado de asegurados
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existen asegurados registrados
Cuando consulto la lista de asegurados
Entonces visualizo un listado que muestra nombre, apellido e identificación de cada asegurado
```
#### Escenario 2: Consulta del detalle de un asegurado específico
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe un asegurado registrado en el sistema
Cuando consulto el detalle de ese asegurado
Entonces visualizo nombre, apellido, identificación, dirección, teléfono y correo electrónico del asegurado
```

#### Escenario 3: Consulta de un asegurado inexistente
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento consultar el detalle de un asegurado que no existe en el sistema
Entonces el sistema me informa que no se encontró ningún asegurado con ese criterio
Y no se muestra información parcial ni de otro asegurado
```

### Casos de Prueba

#### CP001-HU-002: Visualización del listado con asegurados registrados
```gherkin
Dado que el gestor está autenticado  en el sistema
Y existen los asegurados "Juan Pérez" (ID: 1712345678) y "María López" (ID: 1798765432) registrados
Cuando consulta "Asegurados"
Entonces el sistema presenta un listado que contiene al menos a "Juan Pérez" y "María López"
Y cada fila muestra nombre, apellido e identificación del asegurado
```

#### CP002-HU-002: Consulta del detalle de un asegurado existente
```gherkin
Dado que el gestor está autenticado en el sistema
Y existe el asegurado "Juan Pérez" con identificación "1712345678"
Cuando busque al asegurado con identificación "1712345678"
Entonces el sistema muestra el detalle con nombre "Juan", apellido "Pérez", identificación "1712345678", dirección, teléfono y correo electrónico
```

#### CP003-HU-002: Intento de consulta de un asegurado que no existe
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando intenta buscar un asegurado con identificación "0000000000" que no existe en el sistema
Entonces el sistema notifica que no se encontró ningún asegurado
```


## HU-003: Registrar Vehículo

**Como** gestor de seguros,  
**Quiero** registrar los datos de un vehículo en el sistema,  
**Para** que pueda ser asociado a una póliza de seguro.

**Prioridad:** Alta  
**Story Points:** 3



### Criterios de Aceptación (Gherkin)

#### Escenario 1: Registro exitoso de vehículo con datos completos
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando registro un vehículo proporcionando todos los campos obligatorios (marca, modelo, año y placa) con valores válidos
Y la placa del vehículo no existe previamente en el sistema
Entonces el sistema confirma que el vehículo fue registrado exitosamente
Y el vehículo queda disponible para ser asociado a una póliza
```

#### Escenario 2: Intento de registro con datos obligatorios faltantes
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento registrar un vehículo sin completar uno o más campos obligatorios
Entonces el sistema rechaza el registro
Y me indica cuáles son los campos obligatorios que faltan por completar
```

#### Escenario 3: Intento de registro con placa duplicada
```gherkin
Dado que soy un gestor autenticado en el sistema
Y ya existe un vehículo registrado con una placa determinada
Cuando intento registrar otro vehículo con esa misma placa
Entonces el sistema rechaza el registro
Y me informa que ya existe un vehículo registrado con esa placa
Y no se crea ningún registro nuevo en el sistema
```

#### Escenario 4: Intento de registro con formato de datos inválidos
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento registrar un vehículo con un campo que no cumple el formato esperado
Entonces el sistema rechaza el registro
Y me indica qué campo contiene el error y por qué el valor no es válido
Y no se crea ningún registro nuevo en el sistema
```

### Casos de Prueba

#### CP001-HU-003: Registro exitoso de vehículo con todos los datos válidos
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando registra un vehículo con marca "Chevrolet", modelo "Aveo", año "2022", placa "PBA-1234"
Y confirma el registro
Entonces el sistema notifica la confirmación "Vehículo registrado exitosamente"
Y el vehículo "Chevrolet Aveo" con placa "PBA-1234" queda registrado.
```

#### CP002-HU-003: Intento de registro sin completar el campo marca
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando intenta registrar omitiendo la marca pero modelo "Aveo", año "2022", placa "PBA-1235"
Y confirma el registro
Entonces el sistema notifica el error indicando que el campo marca es obligatorio
Y no se crea ningún registro de vehículo
```

#### CP003-HU-003: Intento de registro con placa ya existente
```gherkin
Dado que el gestor está autenticado  en el sistema
Y existe un vehículo registrado con placa "PBA-1234"
Cuando registra un vehiculo con la marca "Kia" , modelo "Rio", año "2023", placa "PBA-1234"
Y confirma el registro
Entonces el sistema notifica indicando que ya existe un vehículo con la placa "PBA-1234"
Y no se crea ningún registro nuevo
```

#### CP004-HU-003: Intento de registro con año en formato inválido
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando registra un vehículo con marca "Chevrolet" , modelo "Aveo", año "veintidos", placa "PBA-1236"
Y confirma el registro
Entonces el sistema notifica indicando que el campo año debe ser un valor numérico válido
Y no se crea ningún registro de vehículo
```

## HU-004: Consultar Vehículos

**Como** gestor de seguros,  
**Quiero** consultar la lista de vehículos y ver el detalle de uno específico,  
**Para** verificar datos antes de asociar una póliza.

**Prioridad:** Alta  
**Story Points:** 2

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Consulta de listado de vehículos
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existen vehículos registrados en el sistema
Cuando consulto la lista de vehículos
Entonces visualizo un listado que muestra marca, modelo, año y placa de cada vehículo
```

#### Escenario 2: Consulta de un vehículo existente
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe un vehículo registrado en el sistema
Cuando consulto el detalle de ese vehículo
Entonces visualizo marca, modelo, año, placa, chasis y motor del vehículo
```

#### Escenario 3: Consulta de un vehículo no existente
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento consultar el detalle de un vehículo que no existe en el sistema
Entonces el sistema me informa que no se encontró ningún vehículo con ese criterio
Y no se muestra información parcial ni de otro vehículo
```

### Casos de Prueba

#### CP001-HU-004: Visualización del listado de vehículos registrados
```gherkin
Dado que el gestor está autenticado en el sistema
Y existen los vehículos "Chevrolet Aveo" (placa: PBA-1234) y "Kia Rio" (placa: PBC-5678) registrados
Cuando consultan "Vehículos"
Entonces el sistema muestra un listado que contiene "Chevrolet Aveo" y "Kia Rio"
Y cada fila muestra marca, modelo, año y placa del vehículo
```

#### CP002-HU-004: Consulta del detalle de un vehículo existente
```gherkin
Dado que el gestor está autenticado  en el sistema
Y existe el vehículo "Chevrolet Aveo" con placa "PBA-1234"
Cuando busco información sobre el vehículo con placa "PBA-1234"
Entonces el sistema muestra el detalle con marca "Chevrolet", modelo "Aveo", año "2022" y placa "PBA-1234"
```

#### CP003-HU-004: Intento de consulta de un vehículo que no existe
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando consulta un vehículo con placa "ZZZ-0000" que no existe en el sistema
Entonces el sistema muestra notifica que no se encontró ningún vehículo
Y no se muestra información
```

## HU-005: Registrar Póliza de Seguro

**Como** gestor de seguros,  
**Quiero** registrar una póliza asociándola a un asegurado y a un vehículo previamente registrados,  
**Para** que el asegurado pueda presentar reclamos contra esa póliza.

**Prioridad:** Alta  
**Story Points:** 3

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Registro exitoso de póliza con datos completos y válidos
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe un vehículo registrado en el sistema
Y existe un asegurado registrado en el sistema
Y el número de póliza no existe previamente en el sistema
Cuando registro una póliza proporcionando todos los campos obligatorios con valores válidos
Entonces el sistema confirma que la póliza fue registrada exitosamente
Y la póliza queda vinculada al vehículo y al asegurado indicados
Y la póliza toma el estado ACTIVA
```

#### Escenario 2: Registro de póliza a un vehículo inexistente
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento registrar una póliza asociada a un vehículo que no existe en el sistema
Entonces el sistema rechaza el registro
Y me informa que el vehículo indicado no se encuentra registrado
Y no se crea ningún registro de póliza en el sistema
```

#### Escenario 3: Intento de registro con número de póliza duplicado
```gherkin
Dado que soy un gestor autenticado en el sistema
Y ya existe una póliza registrada con un número determinado
Cuando intento registrar otra póliza con ese mismo número
Entonces el sistema rechaza el registro
Y me informa que ya existe una póliza registrada con ese número
Y no se crea ningún registro nuevo en el sistema
```

#### Escenario 4: Intento de registro con rango de vigencia inválido
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento registrar una póliza con una fecha de fin anterior o igual a la fecha de inicio
Entonces el sistema rechaza el registro
Y me indica que el rango de vigencia no es válido
Y no se crea ningún registro de póliza en el sistema
```
### Casos de Prueba

#### CP001-HU-005: Registro exitoso de póliza con todos los datos válidos
```gherkin
Dado que el gestor está autenticado en el sistema
Y existe el asegurado "Juan Pérez" con identificación "1712345678"
Y existe el vehículo "Chevrolet Aveo" con placa "PBA-1234"
Cuando registra una póliza "POL-2026-001"
Y vincula al asegurado "Juan Pérez"
Y vincula el vehículo "Chevrolet Aveo - PBA-1234"
Y confirma el registro
Entonces el sistema notifica "Póliza registrada exitosamente"
```
#### CP002-HU-005: Intento de registro asociando un vehículo inexistente
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando intenta asociar la póliza a un vehículo que no existe en el sistema
Entonces el sistema notifica indicando que el vehículo indicado no se encuentra registrado
```

#### CP003-HU-005: Intento de registro con número de póliza duplicado
```gherkin
Dado que el gestor está autenticado  en el sistema
Y existe la póliza "POL-2026-001" registrada en el sistema
Cuando registra la póliza con número "POL-2026-001"
Y proporciona los demás datos válidos
Y confirma el registro la información
Entonces el sistema notifica indicando que ya existe una póliza con el número "POL-2026-001"
```

#### CP004-HU-005: Intento de registro con fecha fin anterior a fecha inicio
```gherkin
Dado que el gestor está autenticado  en el sistema
Cuando registra un asegurado con número de póliza "POL-2026-002"
Y proporciona valor asegurado "25000.00", fecha de inicio de vigencia "01/06/2026",
Y confirma el registro
Entonces el sistema notifica  el rango de fechas no es válido
```

#### CP005-HU-005: Intento de registro con valor asegurado menor o igual a cero
```gherkin
Dado que el gestor está autenticado en el sistema
Cuando registra un asegurado con número de póliza "POL-2026-003"
Y vincula un asegurado y vehículo válidos
Y ingresa fechas de vigencia válidas
Cuando registra un vehículo con valor asegurado "0"
Y confirma el registro
Entonces el sistema notifica que el valor asegurado no es válido
```

## HU-006: Consultar Pólizas

**Como** gestor de seguros,  
**Quiero** consultar pólizas registradas y ver el detalle de una específica,  
**Para** verificar estado, vigencia y vehículo asociado.

**Prioridad:** Alta  
**Story Points:** 2

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Consulta de listado
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existen pólizas registradas en el sistema
Cuando consulto la lista de pólizas
Entonces visualizo un listado que muestra número de póliza, asegurado, estado y fechas de vigencia de cada póliza
```

#### Escenario 2: Consulta del detalle de una póliza existente
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe una póliza registrada en el sistema
Cuando consulto el detalle de esa póliza
Entonces visualizo número de póliza, valor asegurado, fechas de vigencia, estado, y el vehículo y asegurado vinculados a ella
Y puedo identificar el vehículo y el asegurado vinculados a ella
```

#### Escenario 3: Consulta de una póliza no existente
```gherkin
Dado que soy un gestor autenticado en el sistema
Cuando intento consultar el detalle de una póliza que no existe en el sistema
Entonces el sistema me informa que no se encontró ninguna póliza con ese criterio
Y no se muestra información de la poliza.
```

### Casos de Prueba

#### CP001-HU-006: Visualización del listado de pólizas registradas
```gherkin
Dado que el gestor autenticado en el sistema
Y existen las pólizas "POL-2026-001" y "POL-2026-002" registradas
Cuando consulta las pólizas registradas
Entonces el sistema presenta un listado que contiene las pólizas "POL-2026-001" y "POL-2026-002"
Y muestra número de póliza, asegurado, estado y fechas de vigencia
```
#### CP002-HU-006: Consulta del detalle de una póliza existente
```gherkin
Dado que el gestor autenticado en el sistema
Y existe la póliza "POL-2026-001" vinculada al asegurado "Juan Pérez" y al vehículo "Chevrolet Aveo - PBA-1234"
Cuando busca a la póliza "POL-2026-001" en el listado
Entonces el sistema muestra el detalle con número "POL-2026-001", valor asegurado, fechas de vigencia, estado
```
#### CP003-HU-006: Intento de consulta de una póliza que no existe
```gherkin
Dado que el gestor ha autenticado en el sistema
Cuando intenta buscar la póliza "POL-0000-000" que no existe en el sistema
Entonces el sistema notifica que no se encontró ninguna póliza
Y no se muestra información
```

## HU-007: Registro de Reclamo de Siniestro

**Como** asegurado con una póliza de auto activa,  
**Quiero** registrar un reclamo de siniestro proporcionando los datos del incidente y fotografías en png o jpg,  
**Para** iniciar el proceso de evaluación y obtener una respuesta sobre mi compensación.

**Prioridad:** Alta  
**Story Points:** 5

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Registro exitoso con datos completos y válidos
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando registro un reclamo proporcionando todos los campos obligatorios con valores válidos
Y adjunto al menos una fotografía en formato png o jpg
Entonces el sistema confirma que el reclamo fue registrado exitosamente
Y el reclamo toma el estado REGISTRADO
Y el sistema me asigna un número de seguimiento único para ese reclamo
```

#### Escenario 2: Intento de registro sin seleccionar una póliza activa
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y no tengo ninguna póliza activa y vigente disponible para seleccionar
Cuando intento acceder al formulario de registro de reclamo
Entonces el sistema me informa que no es posible registrar reclamos sin una póliza activa y vigente
Y no se crea ningún registro de reclamo en el sistema
```

#### Escenario 3: Intento de registro con campos obligatorios faltantes
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando intento registrar un reclamo sin completar uno o más campos obligatorios
Entonces el sistema rechaza el registro
Y me indica cuáles son los campos obligatorios que faltan por completar
Y no se crea ningún registro de reclamo en el sistema
```

#### Escenario 4: Intento de registro con monto estimado inválido
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando intento registrar un reclamo con un monto estimado menor o igual a cero
Entonces el sistema rechaza el registro
Y me indica que el monto estimado debe ser un valor positivo
Y no se crea ningún registro de reclamo en el sistema
```

#### Escenario 5: Intento de registro con fecha de incidente fuera del período de vigencia
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando intento registrar un reclamo con una fecha de incidente posterior a la fecha actual o anterior al inicio de vigencia de la póliza
Entonces el sistema rechaza el registro
Y me indica que la fecha del incidente debe estar dentro del período de vigencia de la póliza y no puede ser futura
Y no se crea ningún registro de reclamo en el sistema
```

#### Escenario 6: Registro exitoso con fotografías válidas en formato permitido
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando registro un reclamo adjuntando fotografías en formato png o jpg
Entonces el sistema acepta las fotografías y las asocia al reclamo
Y el reclamo se registra exitosamente con las evidencias adjuntas
```

#### Escenario 7: Intento de registro con fotografías en formato no permitido
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando intento registrar un reclamo adjuntando archivos en un formato distinto a png o jpg
Entonces el sistema rechaza los archivos adjuntos
Y me indica que solo se permiten fotografías en formato png o jpg
Y no se crea ningún registro de reclamo en el sistema
```

#### Escenario 8: Intento de registro sin adjuntar fotografías
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo una póliza activa y vigente
Cuando intento registrar un reclamo sin adjuntar ninguna fotografía
Entonces el sistema rechaza el registro
Y me indica que es obligatorio adjuntar al menos una fotografía como evidencia del siniestro
Y no se crea ningún registro de reclamo en el sistema
```

### Casos de Prueba

#### CP001-HU-007: Registro exitoso de reclamo con todos los datos válidos y fotografía jpg
```gherkin
Dado que el asegurado "Juan Pérez" autenticado en el sistema
Y tiene la póliza "POL-2026-001" activa y vigente con valor asegurado de $25,000.00
Cuando registra una póliza con fecha del incidente "15/03/2026", descripción "Colisión", monto estimado "3500.00", ubicación "Av. Amazonas y Av. Naciones Unidas, Quito"
Y adjunta el archivo "foto_siniestro.jpg"
Y realiza el registro del reclamo
Entonces el sistema notifica "Reclamo registrado exitosamente"
Y el sistema asigna un número de seguimiento al reclamo
Y el reclamo aparece como "REGISTRADO"
```
#### CP002-HU-007: Intento de registro sin póliza activa disponible
```gherkin
Dado que el asegurado "Carlos Ruiz" autenticado en el sistema
Y no tiene ninguna póliza activa y vigente
Cuando intenta acceder al registro de reclamos
Entonces el sistema notifica que no es posible registrar reclamos sin una póliza activa y vigente
```
#### CP003-HU-007: Intento de registro sin completar el campo descripción
```gherkin
Dado que el asegurado "Juan Pérez" autenticado en el sistema
Y tiene la póliza "POL-2026-001" activa y vigente
Cuando selecciona la póliza "POL-2026-001"
Cuando registra una póliza con fecha del incidente "15/03/2026"
Y omite el campo descripción
Cuando registra una póliza con monto estimado "3500.00", ubicación "Quito"
Y adjunta el archivo "foto_siniestro.jpg"
Y registro un reclamo
Entonces el sistema notifica que el campo descripción es obligatorio
Y no se crea ningún registro de reclamo
```
#### CP004-HU-007: Intento de registro con monto estimado igual a cero
```gherkin
Dado que el asegurado "Juan Pérez" autenticado en el sistema
Y tiene la póliza "POL-2026-001" activa y vigente
Cuando selecciona la póliza "POL-2026-001"
Cuando registra una póliza con fecha del incidente "15/03/2026", descripción "Colisión leve", monto estimado "0", ubicación "Quito"
Y adjunta el archivo "foto_siniestro.jpg"
Y registro un reclamo
Entonces el sistema notifica que el monto estimado debe ser un valor positivo
Y no se crea ningún registro de reclamo
```

#### CP005-HU-007: Intento de registro con fecha de incidente en el futuro
```gherkin
Dado que el asegurado "Juan Pérez" autenticado en el sistema
Y tiene la póliza "POL-2026-001" activa y vigente
Y la fecha actual es "17/03/2026"
Cuando selecciona la póliza "POL-2026-001"
Cuando registra una póliza con fecha del incidente "25/12/2026"
Y completa el resto de campos con datos válidos
Y adjunta el archivo "foto_siniestro.jpg"
Y registro un reclamo
Entonces el sistema notifica que la fecha del incidente no puede ser en el futuro
Y no se crea ningún registro de reclamo
```
#### CP006-HU-007: Intento de registro con archivo en formato PDF
```gherkin
Dado que el asegurado "Juan Pérez" autenticado en el sistema
Y tiene la póliza "POL-2026-001" activa y vigente
Cuando completa todos los campos obligatorios con datos válidos
Y adjunta el archivo "documento.pdf"
Y registro un reclamo
Entonces el sistema notifica que solo se permiten fotografías en formato png o jpg
Y no se crea ningún registro de reclamo
```
#### CP008-HU-007: Intento de registro sin adjuntar ninguna fotografía
```gherkin
Dado que el asegurado "Juan Pérez" autenticado en el sistema
Y tiene la póliza "POL-2026-001" activa y vigente
Cuando completa todos los campos obligatorios con datos válidos
Y no adjunta ninguna fotografía
Y registro un reclamo
Entonces el sistema notifica indicando que es obligatorio adjuntar al menos una fotografía
Y no se crea ningún registro de reclamo
```

## HU-008: Validación de póliza para procesamiento de reclamo

**Como** sistema de evaluación de siniestros,  
**Quiero** validar automáticamente la elegibilidad de la póliza al momento de procesar un reclamo,  
**Para** descartar de inmediato los reclamos que no cumplen con las condiciones de la poliza

**Prioridad:** Alta  
**Story Points:** 5

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Rechazo por póliza no vigente
```gherkin
Dado que existe un reclamo registrado en el sistema
Y la póliza asociada a ese reclamo no está activa ni vigente
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo queda en estado de rechazo
Y se registra el motivo correspondiente a póliza no vigente
Y no se continúa con ninguna evaluación adicional sobre ese reclamo
```

#### Escenario 2: Escalamiento por antigüedad insuficiente de la póliza
```gherkin
Dado que existe un reclamo registrado en el sistema
Y la póliza asociada está activa y vigente
Y la póliza tiene una antigüedad menor a 30 días desde su fecha de inicio de vigencia
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo se escala a revisión manual
Y se registra la bandera correspondiente a antigüedad insuficiente (menor a 30 días)
Y no se continúa con la evaluación por reglas de monto e historial
```

#### Escenario 3: Póliza elegible — Continúa a evaluación por reglas
```gherkin
Dado que existe un reclamo registrado en el sistema
Y la póliza asociada está activa, vigente y con antigüedad igual o mayor a 30 días
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo queda habilitado para continuar con la evaluación por reglas de la poliza
Y no se registra ninguna bandera ni motivo de rechazo en esta fase
```

### Casos de Prueba

#### CP001-HU-008: Rechazo automático por póliza con estado inactivo
```gherkin
Dado que existe un reclamo registrado con número de seguimiento "REC-2026-001"
Y la póliza asociada "POL-2025-050" tiene estado "INACTIVA"
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo "REC-2026-001" cambia al estado "RECHAZADO"
Y el motivo registrado indica "Póliza no vigente"
```
#### CP002-HU-008: Rechazo automático por póliza con vigencia expirada
```gherkin
Dado que existe un reclamo registrado con número de seguimiento "REC-2026-002"
Y la póliza asociada "POL-2025-010" tiene fecha de fin de vigencia "31/12/2025"
Y la fecha actual es "17/03/2026"
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo "REC-2026-002" cambia al estado "RECHAZADO"
Y el motivo registrado indica "Póliza no vigente"
```
#### CP003-HU-008: Escalamiento por antigüedad de póliza menor a 30 días
```gherkin
Dado que existe un reclamo registrado con número de seguimiento "REC-2026-003"
Y la póliza asociada "POL-2026-020" está activa con fecha de inicio de vigencia "01/03/2026"
Y la fecha del reclamo es "17/03/2026" 
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo "REC-2026-003" cambia al estado "EN REVISIÓN MANUAL"
Y se registra la bandera "Antigüedad de póliza insuficiente (menor a 30 días)"
```
#### CP004-HU-008: Póliza elegible con antigüedad mayor a 30 días
```gherkin
Dado que existe un reclamo registrado con número de seguimiento "REC-2026-005"
Y la póliza asociada "POL-2026-001" está activa con fecha de inicio de vigencia "01/01/2026"
Y la fecha del reclamo es "17/03/2026" 
Cuando el sistema ejecuta la validación de elegibilidad de la póliza
Entonces el reclamo "REC-2026-005" queda habilitado para continuar con la evaluación por reglas de negocio
Y no se registra ninguna bandera ni motivo de rechazo
```
## HU-009: Evaluación de reclamo por reglas de deducible y monto

**Como** sistema de evaluación de siniestros,  
**Quiero** evaluar automáticamente cada reclamo elegible aplicando las reglas de deducible y monto respecto al valor asegurado,  
**Para** resolver sin intervención manual los reclamos de bajo riesgo por monto y escalar los que superen el umbral definido.

**Prioridad:** Alta  
**Story Points:** 5

**Nota:** El deducible se calcula como el mayor entre: 10% del monto del siniestro, 1% del valor asegurado y $200. El umbral de escalamiento por monto es el 20% del valor asegurado (según reglas definidas en el PRD sección 2.1).

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Descarte por monto menor o igual al deducible calculado
```gherkin
Dado que existe un reclamo elegible para evaluación
Y el monto estimado del reclamo es menor o igual al deducible calculado (el mayor entre 10% del monto del siniestro, 1% del valor asegurado y $200)
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo queda en estado de descarte
Y se registra el motivo indicando que el monto no supera el deducible aplicable
```

#### Escenario 2: Aprobación automática por monto dentro del rango permitido
```gherkin
Dado que existe un reclamo elegible para evaluación
Y el monto estimado supera el deducible calculado
Y el monto estimado es menor al 20% del valor asegurado
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo queda habilitado para continuar con la siguiente fase de evaluación
Y no se registra ninguna bandera de escalamiento por monto
```

#### Escenario 3: Escalamiento por monto que supera el umbral del valor asegurado
```gherkin
Dado que existe un reclamo elegible para evaluación
Y el monto estimado supera el deducible calculado
Y el monto estimado es igual o mayor al 20% del valor asegurado
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo se escala a revisión manual
Y se registra la bandera correspondiente al monto elevado respecto al valor asegurado
```

### Casos de Prueba

#### CP001-HU-009: Descarte por monto menor al deducible
```gherkin
Dado que existe un reclamo elegible con monto estimado de $150.00
Y la póliza asociada tiene un valor asegurado de $25,000.00
Y el deducible calculado es $250.00 
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo queda en estado "DESCARTADO"
Y el motivo registrado indica que el monto no supera el deducible aplicado de $250.00
```
#### CP002-HU-009: Descarte por monto igual al deducible calculado
```gherkin
Dado que existe un reclamo elegible con monto estimado de $250.00
Y la póliza asociada tiene un valor asegurado de $25,000.00
Y el deducible calculado es $250.00 
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo queda en estado "DESCARTADO"
Y el motivo registrado indica que el monto no supera el deducible aplicado de $250.00
```
#### CP003-HU-009: Aprobación por monto que supera el deducible y está por debajo del 20% del valor asegurado
```gherkin
Dado que existe un reclamo elegible con monto estimado de $3,500.00
Y la póliza asociada tiene un valor asegurado de $25,000.00
Y el deducible calculado es $350.00
Y el 20% del valor asegurado es $5,000.00
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo continúa a la evaluación por historial de siniestros
Y no se registra ninguna bandera de escalamiento por monto
```

## HU-010: Evaluación de reclamo por historial de siniestros

**Como** sistema de evaluación de siniestros,  
**Quiero** evaluar el historial de siniestros recientes del asegurado para los reclamos que pasaron la validación de monto,  
**Para** aprobar automáticamente los reclamos sin indicadores de riesgo por historial y escalar los que presenten frecuencia elevada.

**Prioridad:** Alta  
**Story Points:** 3


### Criterios de Aceptación (Gherkin)

#### Escenario 1: Aprobación automática sin banderas de historial
```gherkin
Dado que existe un reclamo que pasó la evaluación de monto sin banderas
Y el asegurado tiene 0 siniestros registrados en los últimos 12 meses
Cuando el motor de reglas evalúa el historial del asegurado
Entonces el reclamo queda aprobado automáticamente
Y se registra el monto aprobado y el deducible aplicado
```

#### Escenario 2: Escalamiento por historial de siniestros recientes
```gherkin
Dado que existe un reclamo que pasó la evaluación de monto sin banderas
Y el asegurado tiene 2 o más siniestros registrados en los últimos 12 meses
Cuando el motor de reglas evalúa el historial del asegurado
Entonces el reclamo se escala a revisión manual
Y se registra la bandera correspondiente al historial de siniestros recientes
```

#### Escenario 3: Escalamiento por múltiples banderas activas simultáneas
```gherkin
Dado que existe un reclamo elegible para evaluación
Y el reclamo activa más de una bandera de escalamiento (monto e historial)
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo se escala a revisión manual
Y se registran todas las banderas activas que motivaron el escalamiento
```

### Casos de Prueba
 
#### CP001-HU-010: Aprobación automática con historial limpio (0 siniestros en 12 meses)
```gherkin
Dado que existe el reclamo "REC-2026-005" con monto estimado de $3,500.00
Y la póliza asociada "POL-2026-001" tiene un valor asegurado de $25,000.00
Y el reclamo pasó la evaluación de monto sin banderas
Y el asegurado "Juan Pérez" tiene 0 siniestros registrados en los últimos 12 meses
Cuando el motor de reglas evalúa el historial del asegurado
Entonces el reclamo "REC-2026-005" cambia al estado "APROBADO AUTOMÁTICAMENTE"
Y se registra el monto aprobado de $3,150.00
```
#### CP002-HU-010: Escalamiento por historial con exactamente 2 siniestros en 12 meses
```gherkin
Dado que existe el reclamo "REC-2026-012" con monto estimado de $4,000.00
Y el reclamo pasó la evaluación de monto sin banderas
Y el asegurado "Carlos Ruiz" tiene 2 siniestros registrados en los últimos 12 meses
Cuando el motor de reglas evalúa el historial del asegurado
Entonces el reclamo "REC-2026-012" cambia al estado "EN REVISIÓN MANUAL"
Y se registra la bandera "Historial de siniestros recientes"
```
#### CP003-HU-010: Escalamiento por múltiples banderas (monto elevado + historial)
```gherkin
Dado que existe el reclamo "REC-2026-014" con monto estimado de $6,000.00
Y la póliza asociada tiene un valor asegurado de $25,000.00
Y el reclamo tiene la bandera "Monto elevado respecto al valor asegurado" de la evaluación anterior
Y el asegurado "Carlos Ruiz" tiene 3 siniestros registrados en los últimos 12 meses
Cuando el motor de reglas evalúa el reclamo
Entonces el reclamo "REC-2026-014" cambia al estado "EN REVISIÓN MANUAL"
Y se registran las banderas "Monto elevado respecto al valor asegurado" e "Historial de siniestros recientes"
```

## HU-011: Panel del gestor de seguros

**Como** gestor de seguros,  
**Quiero** visualizar los reclamos escalados a revisión manual con sus datos registrados,
**Para** identificar rápidamente los casos que requieren mi criterio y las razones del escalamiento.

**Prioridad:** Alta  
**Story Points:** 3

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Visualización del listado de reclamos escalados
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existen reclamos en estado de revisión manual pendientes de resolución
Cuando accedo al panel de reclamos escalados
Entonces visualizo un listado que muestra número de seguimiento, asegurado, monto estimado, fecha del incidente y banderas de escalamiento de cada reclamo
```
#### Escenario 2: Panel sin reclamos pendientes
```gherkin
Dado que soy un gestor autenticado en el sistema
Y no existen reclamos en estado de revisión manual pendientes de resolución
Cuando accedo al panel de reclamos escalados
Entonces el sistema me informa que no hay reclamos pendientes de revisión
```
### Casos de Prueba
 
#### CP001-HU-011: Visualización del listado con reclamos escalados pendientes
```gherkin
Dado que el gestor "Ana García" autenticado  en el sistema
Y existen los siguientes reclamos en estado de revisión manual
Cuando accede al panel de reclamos escalados
Entonces el sistema muestra un listado con los reclamos "REC-2026-003" y "REC-2026-007"
Y para el reclamo "REC-2026-003" se muestra asegurado "Juan Pérez", monto "$3,500.00", fecha "15/03/2026" y bandera "Antigüedad de póliza insuficiente"
```
 
#### CP002-HU-011: Panel sin reclamos pendientes de revisión
```gherkin
Dado que el gestor ha autenticado en el sistema
Y no existen reclamos en estado de revisión manual en el sistema
Cuando accede al panel de reclamos escalados
Entonces el sistema muestra un mensaje indicando "No hay reclamos pendientes de revisión"
Y no se muestra ningún listado de reclamos
```

## HU-012: Resolución manual de reclamos escalados
**Como** gestor de seguros,  
**Quiero** registrar mi decisión de aprobación o rechazo sobre un reclamo escalado junto con una justificación,
**Para** resolver los casos que requieren criterio humano dejando trazabilidad de la decisión.

**Prioridad:** Alta  
**Story Points:** 3
### Criterios de Aceptación (Gherkin)
 
#### Escenario 1: Aprobación manual de un reclamo con justificación
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe un reclamo en estado de revisión manual
Cuando decido aprobar el reclamo y registro mi justificación
Entonces el reclamo cambia al estado de aprobado por gestor
Y el sistema registra la fecha y la justificación ingresada
```
#### Escenario 2: Rechazo manual de un reclamo con justificación
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe un reclamo en estado de revisión manual
Cuando decido rechazar el reclamo y registro mi justificación
Entonces el reclamo cambia al estado de rechazado por gestor
Y el sistema registra la fecha y la justificación ingresada
```
#### Escenario 3: Intento de resolución sin justificación
```gherkin
Dado que soy un gestor autenticado en el sistema
Y existe un reclamo en estado de revisión manual
Cuando intento aprobar o rechazar el reclamo sin ingresar una justificación
Entonces el sistema rechaza la operación
Y me indica que la justificación es obligatoria para registrar una resolución
Y el estado del reclamo permanece sin cambios
```
### Casos de Prueba
 
#### CP001-HU-012: Aprobación exitosa de reclamo escalado con justificación
```gherkin
Dado que el gestor esta autenticado  en el sistema
Y existe el reclamo "REC-2026-003" en estado "EN REVISIÓN MANUAL" del asegurado "Juan Pérez"
Y el reclamo tiene la bandera "Antigüedad de póliza insuficiente"
Cuando trabaja con el reclamo "REC-2026-003"
Y Aprueba
Cuando registra una póliza con justificación "Verificada documentación del asegurado."
Y confirma la resolución
Entonces el reclamo "REC-2026-003" cambia al estado "APROBADO POR GESTOR"
Y el sistema registra el responsable, la fecha "18/03/2026" y la justificación ingresada
```
#### CP002-HU-012: Rechazo de reclamo escalado con justificación
```gherkin
Dado que el gestor "Ana García" esta autenticado  en el sistema
Y existe el reclamo "REC-2026-007" en estado "EN REVISIÓN MANUAL" del asegurado "María López"
Y el reclamo tiene la bandera "Monto elevado respecto al valor asegurado"
Cuando trabaja con el reclamo "REC-2026-007"
Y Rechaza
Cuando registra un reclamo con justificación "Las fotografías no corresponden con la descripción."
Y confirma la resolución
Entonces el reclamo "REC-2026-007" cambia al estado "RECHAZADO POR GESTOR"
Y el sistema registra al gestor , la fecha "18/03/2026" y la justificación ingresada
```


## HU-013: Consulta de estado de reclamo por el asegurado

**Como** asegurado,  
**Quiero** consultar el estado actual de mi reclamo y conocer el motivo de la decisión tomada,
**Para** tener visibilidad del proceso sin necesidad de contactar a la aseguradora.

**Prioridad:** Media  
**Story Points:** 3

### Criterios de Aceptación (Gherkin)

#### Escenario 1: Consulta de reclamo aprobado automáticamente
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo un reclamo que fue aprobado automáticamente
Cuando consulto el estado de ese reclamo
Entonces visualizo la información correspondiente al estado de aprobación
Y puedo identificar el monto
```
#### Escenario 2: Consulta de reclamo en revisión manual
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo un reclamo que se encuentra en revisión manual
Cuando consulto el estado de ese reclamo
Entonces visualizo que el reclamo está siendo revisado
Y se me indica un mensaje de que esta en revisión manual
```
#### Escenario 3: Consulta de reclamo descartado
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo un reclamo que fue descartado
Cuando consulto el estado de ese reclamo
Entonces visualizo que el reclamo no procede
Y se me muestra el motivo por el que fue descartado
```
#### Escenario 4: Consulta de reclamo rechazado por gestor
```gherkin
Dado que soy un asegurado autenticado en el sistema
Y tengo un reclamo que fue rechazado por un gestor
Cuando consulto el estado de ese reclamo
Entonces visualizo que el reclamo fue rechazado
Y se me muestra el motivo registrado por el gestor
```
### Casos de Prueba
 
#### CP001-HU-013: Consulta de reclamo aprobado automáticamente
```gherkin
Dado que el asegurado "Juan Pérez" autenticado  en el sistema
Y tiene el reclamo "REC-2026-005" que fue aprobado automáticamente con monto aprobado de $3,150.00 y deducible aplicado de $350.00
Cuando consulta el estado del reclamo "REC-2026-005"
Entonces el sistema muestra el estado "APROBADO"
Y se muestra el monto aprobado "$3,150.00" y el deducible aplicado "$350.00"
```
#### CP002-HU-013: Consulta de reclamo en revisión manual
```gherkin
Dado que el asegurado "Juan Pérez" autenticado  en el sistema
Y tiene el reclamo "REC-2026-003" que se encuentra en estado "EN REVISIÓN MANUAL"
Cuando consulta el estado del reclamo "REC-2026-003"
Entonces el sistema muestra el estado "EN REVISIÓN MANUAL"
Y se muestra el mensaje "Su reclamo se encuentra en revisión manual"
```
#### CP003-HU-013: Consulta de reclamo descartado por monto inferior al deducible
```gherkin
Dado que el asegurado "María López" autenticado  en el sistema
Y tiene el reclamo "REC-2026-008" que fue descartado con motivo "El monto no supera el deducible"
Cuando consulta el estado del reclamo "REC-2026-008"
Entonces el sistema muestra el estado "DESCARTADO"
Y se muestra el motivo "El monto no supera el deducible"
```
#### CP004-HU-013: Consulta de reclamo rechazado por gestor con justificación
```gherkin
Dado que el asegurado "María López" esta autenticado en el sistema
Y tiene el reclamo "REC-2026-007" que fue rechazado por el gestor "Ana García" con justificación "Las fotografías no corresponden con la descripción del siniestro reportado."
Cuando consulta el estado del reclamo "REC-2026-007"
Entonces el sistema muestra el estado "RECHAZADO POR GESTOR"
Y se muestra el motivo "Las fotografías no corresponden con la descripción del siniestro reportado."
```
#### CP005-HU-013: Consulta de reclamo rechazado por póliza no vigente
```gherkin
Dado que el asegurado "Carlos Ruiz" esta autenticado en el sistema
Y tiene el reclamo "REC-2026-002" que fue rechazado con motivo "Póliza no vigente"
Cuando consulta el estado del reclamo "REC-2026-002"
Entonces el sistema muestra el estado "RECHAZADO"
Y se muestra el motivo "Póliza no vigente"
```

## Historias Técnicas y de Arquitectura


### HT-001: Configuración del Proyecto Base

**Como** arquitecto de software
**Quiero** configurar el esqueleto del proyecto con el stack definido
**Para** que el equipo pueda empezar a trabajar sobre una base común

**Stack:** Java 17+, Spring Boot (Web, Data JPA, Validation), Docker + docker-compose

**Criterios de Aceptación**

- El proyecto compila sin errores
- Existe un `docker-compose.yml` que levanta la aplicación
- Incluye manejo global de excepciones (Controller Advice)
- `GET /api/health` responde HTTP 200 OK

---

### HT-002: Diseño de la Base de Datos

**Como** desarrollador backend
**Quiero** implementar el esquema relacional en PostgreSQL con sus entidades JPA
**Para** persistir los datos del sistema de forma estructurada

**Stack:** PostgreSQL, Hibernate / Spring Data JPA

**Tablas principales:**
- `asegurados` — id, nombre, apellido, numero_identificacion, direccion, telefono, correo_electronico
- `vehiculos` — id, marca, modelo, anio, placa, chasis, motor
- `polizas` — id, numero, asegurado_id, vehiculo_id, valor_asegurado, vigencia_inicio, vigencia_fin, estado
- `reclamos` — id, poliza_id, fecha_incidente, descripcion, monto_estimado, ubicacion, estado, resolucion, justificacion, numero_seguimiento
- `reclamo_fotografias` — id, reclamo_id, url_fotografia
- `reclamo_banderas` — id, reclamo_id, descripcion_bandera

**Criterios de Aceptación**

- El `docker-compose.yml` incluye el contenedor de PostgreSQL configurado

- La aplicación se conecta a la base de datos al arrancar

- Las tablas se generan correctamente al iniciar

- No se puede eliminar un asegurado con pólizas vinculadas

---

### HT-003: Endpoints REST

**Como** desarrollador backend
**Quiero** crear los controladores REST para las entidades del MVP
**Para** soportar las operaciones CRUD y el flujo completo de reclamos

**Endpoints principales:**
CRUD completo en `/api/v1/asegurados`, `/api/v1/vehiculos` y `/api/v1/polizas`

Reclamos:
- `POST /api/v1/reclamos` — registrar reclamo
- `GET /api/v1/reclamos/{numeroSeguimiento}/estado` — consultar estado
- `PUT /api/v1/reclamos/{numeroSeguimiento}/resolucion` — aprobar o rechazar con justificación

**Criterios de Aceptación**

- Existen controllers para todas las entidades
- Los endpoints de reclamos cubren el ciclo completo: registro, consulta, escalado y resolución
- Los endpoints validan campos obligatorios y responden en JSON
- Respuestas exitosas devuelven HTTP 200 o 201
- Respuestas erróneas devuelven HTTP 400 o 404 con mensaje descriptivo

### HT-004: Seguridad y Control de Acceso

**Como** arquitecto de software
**Quiero** implementar autenticación y autorización en la API
**Para** que solo usuarios autenticados puedan operar según su rol

**Stack:** Spring Security, JWT

**Roles:**
- `GESTOR` — acceso completo a vehículos, pólizas, asegurados y resolución de reclamos
- `ASEGURADO` — solo puede registrar y consultar sus propios reclamos

**Criterios de Aceptación**

- Los endpoints protegidos rechazan peticiones sin token válido con HTTP 401
- Un usuario no puede operar sobre recursos de otro rol con HTTP 403
- El token JWT expira y no puede reutilizarse una vez vencido
- Existe un endpoint `POST /api/v1/auth/login` que devuelve el token