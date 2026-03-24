# PRD: Automatizador de Siniestros - InsurTech

## 1. Visión y Objetivos

### 1.1 Problema que resuelve

Las aseguradoras de autos gestionan mensualmente un alto volumen de reclamos por siniestros. En la mayoría de los casos, estos procesos dependen de revisiones manuales, lo que genera varios inconvenientes:

- **Tiempos de respuesta lentos:** Incluso los siniestros de baja complejidad pueden tardar entre 7 y 30 días hábiles en resolverse, cuando podrían gestionarse en mucho menos tiempo.
- **Costos operativos elevados:** Se destinan recursos humanos a la revisión de casos simples que no necesariamente requieren análisis especializado.
- **Experiencia negativa del cliente:** Los asegurados deben esperar largos periodos para recibir una compensación, lo que impacta negativamente en su percepción del servicio.
- **Inconsistencia en las decisiones:** Las decisiones pueden variar entre diferentes trabajadores, generando inequidad y falta de transparencia.

### 1.2 Visión del producto

Construir una plataforma de evaluación  automatizada de siniestros de autos que transforme el proceso actual de revisión manual en un flujo automatizado capaz de aprobar pagos de bajo riesgo en el menor tiempo posible, escalar a un especialista humano únicamente los casos que presenten indicadores de riesgo , y garantizar la trazabilidad completa de cada decisión para cumplimiento de las pólizas.

### 1.3 Objetivo general

Optimizar el proceso de gestión de reclamos de siniestros de autos automatizando la evaluación y aprobación de casos de bajo riesgo, reduciendo los tiempos de respuesta de días a minutos, liberando al equipo para enfocarse en casos que realmente requieran criterio humano, y garantizando decisiones consistentes y auditables que fortalezcan la confianza del asegurado en el servicio.

### 1.4 Objetivos específicos

- Reducir el tiempo de resolución de siniestros de baja complejidad de un promedio de 7-30 días hábiles a minutos, mediante la aprobación automatizada de reclamos que cumplan con las reglas de bajo riesgo definidas por el negocio, liberando la carga operativa del equipo para que concentren su tiempo en los casos que genuinamente requieran análisis humano.

- Estandarizar las decisiones de evaluación a través de un motor de reglas de negocio configurable que elimine la variabilidad entre revisores, asegurando que reclamos con las mismas características reciban siempre el mismo tratamiento.

- Garantizar la trazabilidad completa de cada decisión (automática o manual), registrando las reglas evaluadas, las banderas detectadas y la justificación de cada resolución.


### 1.5 Usuarios objetivo

| Usuario | Descripción | Necesidad principal |
|---------|-------------|---------------------|
| **Asegurado** | Persona con póliza de auto activa que sufre un siniestro | Registrar su reclamo y consultar el estado de su resolución |
| **Gestor** | Empleado que administra pólizas y resuelve reclamos escalados | Gestionar vehículos y pólizas (CRUD completo), y resolver los casos que levanten banderas rojas con contexto claro |
| **Administrador del Sistema** | Responsable de configurar reglas de negocio | Ajustar umbrales y reglas de evaluación sin necesidad de despliegue técnico |

## 2. Alcance del MVP

### 2.1 IN (Incluido en la primera versión)
1. **Registro de asegurados:** El gestor podrá registrar asegurados con la información (nombre, apellido, número de identificación, dirección, teléfono, correo electrónico).

2. **Registro de vehículos:** El gestor podrá registrar vehículos asociados a un asegurado con la información (marca, modelo, año, placa).

3. **Registro de pólizas:** El gestor podrá registrar pólizas vinculadas a un asegurado y a un vehículo, incluyendo (número, asegurado, valor asegurado, vigencia).

4. **Registro de reclamo de siniestro:** El asegurado registrar un reclamo proporcionando datos del incidente (fecha, descripción, monto estimado,ubicación, fotografías). El sistema asigna un número de seguimiento único.

5. **Validación automática de póliza y evaluación por reglas de negocio:** El sistema verifica que la póliza esté activa. Si la póliza no es válida, el reclamo se rechaza con motivo claro. Si es válida, aplica las siguientes reglas:

   - **Regla de monto:** 
      - Si el monto estimado es ≤ al deducible mínimo → no procede como reclamo, se descarta.
      - Si es > al deducible mínimo y < 20% del valor asegurado → candidato a aprobación automática. 
      - Si es ≥ 20% del valor asegurado → bandera roja, revisión manual.
      - El deducible se calcula como el mayor entre: 10% del monto del siniestro, 1% del valor asegurado, y $200.
   - **Regla de historial:** 
      - Si el asegurado tiene 0 siniestros en los últimos 12 meses → candidato a aprobación automática. 
      - Si tiene ≥ 2 siniestros en los últimos 12 meses → bandera roja, revisión manual.
   - **Regla de antigüedad de póliza:** 
       - Si la póliza tiene ≥ 30 días de vigencia → candidato a aprobación automática. 
       - Si tiene < 30 días → bandera roja, se envía a revisión manual.

**Nota:** Se realizo una investigación de las reglas de negocio de las principales aseguradoras del Ecuador y se determino que las reglas definidas son las mas adecuadas para el MVP.

6. **Panel del gestor de seguros:** El gestor puede ver los reclamos escalados de un siniestro con sus banderas rojas, y registrar su resolución (aprobar/rechazar) con justificación. Adiconalmente puede ver información del asegurado.

7. **Consulta de estado:** El asegurado puede consultar en cualquier momento el estado actual de su reclamo (aprobado, en revisión, rechazado) y el motivo de la decisión.

### 2.2 OUT (Excluido del MVP)

- Edición y eliminación de asegurados, vehículos y pólizas.
- Ajustes de las reglas del negocio, realizado por el administrador.
- Generar reportes de trazabilidad de los reclamos.
- Gestión de pagos y transferencias bancarias reales (se notifica la aprobación).
- Reportería avanzada y dashboards analíticos.

## 3. Riesgos de Negocio y Técnicos

## 3.1 Riesgos de Negocio

A continuación se describen los principales riesgos identificados para el MVP, junto con cómo se planea manejarlos.

---

**R001 – Desconfianza en decisiones automáticas**  
Existe el riesgo de que el asegurado no entienda por qué su reclamo fue aprobado o rechazado, especialmente cuando la decisión es automática. Esto puede generar reclamos, mala experiencia o pérdida de confianza en el sistema.  

**Cómo se maneja:**  
Se mostrará siempre una explicación simple del resultado, indicando las razones principales de la decisión (por ejemplo: monto, vigencia de póliza o historial).

---

**R002 – Reglas que no coinciden con la póliza real**  
Las reglas definidas en el MVP podrían no alinearse completamente con las condiciones reales de las pólizas, lo que puede provocar decisiones incorrectas o conflictos con el negocio.  

**Cómo se maneja:**  
Antes de salir a producción, las reglas serán validadas con el área de negocio para asegurar que reflejen correctamente las políticas del negocio.

---
**R003 – Detección limitada de fraude**  
Al tratarse de una primera versión, las reglas son básicas y podrían no detectar ciertos casos de fraude, como reclamos repetidos o patrones sospechosos.  

**Cómo se maneja:**  
Se mantendrán criterios altos para la aprobación automática y cualquier caso dudoso será enviado a revisión manual. Además, se podrán ir agregando reglas con el tiempo, para prevenir inconvenientes.

---

**R004 – Aprobaciones automáticas que generen pérdida económica**  
Si las reglas de bajo riesgo están mal calibradas, el sistema podría aprobar reclamos que en la práctica deberían haber sido revisados por un gestor, generando pagos indebidos acumulados que impacten financieramente a la aseguradora.

**Cómo se maneja:**  
Durante las primeras semanas de operación se realizará un muestreo manual de los casos aprobados automáticamente para comparar la decisión del sistema contra el criterio de un gestor . Si se detectan incosistencias se debe realizar ajustes.
 
 
---
 

## 3.2 Riesgos Técnicos

A continuación se describen algunos riesgos técnicos identificados en el sistema y cómo se planea manejarlos.

---

**T001 – Motor de reglas difícil de mantener**  
A medida que el sistema crece, las reglas pueden volverse difíciles de entender y mantener, especialmente si están implementadas directamente en el código. Esto puede generar errores y dificultar cambios futuros.  

**Cómo se maneja:**  
Se diseñará el motor de reglas de forma desacoplada y configurable, evitando tener lógica hardcodeada.

---

**T002 – Errores en condiciones límite**  
Los casos cercanos a los umbrales definidos (por ejemplo, montos justo en el límite) pueden generar decisiones incorrectas si no se manejan bien.  

**Cómo se maneja:**  
Se implementarán pruebas específicas para estos casos (valores límite) para asegurar que el sistema responda correctamente.

---

**T004 – Dependencia de datos ingresados por el usuario**  
El sistema toma decisiones basadas en la información que ingresa el usuario. Si estos datos son incorrectos o incompletos, la evaluación puede ser errónea.  

**Cómo se maneja:**  
Se aplicarán validaciones en los campos obligatorios y se solicitará evidencia (como fotografías) para respaldar la información del reclamo.

---
 
**T005 – Vulnerabilidad en el acceso a datos sensibles**  
El sistema maneja información personal de los asegurados  e información financiera. Si no se implementan controles de acceso adecuados, esta información podría quedar expuesta a usuarios no autorizados.
 
**Cómo se maneja:**  
Se implementará autenticación y autorización basada en roles (asegurado, gestor) desde el MVP. Los endpoints de la API serán protegidos para que cada rol acceda únicamente a la información que le corresponde. Las contraseñas se almacenarán con hashing seguro y se utilizará HTTPS para toda la comunicación.

---
## 3.3 Matriz de Riesgos de Negocio

| ID | Riesgo | Probabilidad | Impacto | Prioridad | Estrategia de mitigación |
|----|--------|:------------:|:-------:|:---------:|--------------------------|
| R001 | Desconfianza en decisiones automáticas | Alta | Alto | **Crítica** | Mostrar justificación clara en cada decisión con lenguaje simple para el asegurado. |
| R002 | Reglas desalineadas con la póliza real | Media | Alto | **Alta** | Validar reglas con el área de negocio antes de producción. Revisión periódica post-lanzamiento. |
| R003 | Detección limitada de fraude | Media | Alto | **Alta** | Umbrales conservadores para aprobación automática. Casos dudosos se escalan a revisión manual. |
| R004 | Aprobaciones que generen pérdida económica | Media | Crítico | **Crítica** | Muestreo manual en las primeras semanas. Ajuste de umbrales si se detectan inconsistencias. |

---

## 3.4 Matriz de Riesgos Técnicos
 
| ID | Riesgo | Probabilidad | Impacto | Prioridad | Estrategia de mitigación |
|----|--------|:------------:|:-------:|:---------:|--------------------------|
| T001 | Motor de reglas difícil de mantener | Media | Alto | **Alta** | Diseño desacoplado y configurable. Evitar lógica hardcodeada en el código fuente. |
| T002 | Errores en condiciones límite | Alta | Alto | **Crítica** | Pruebas unitarias específicas para valores en los umbrales exactos y en sus bordes inmediatos. |
| T004 | Datos incorrectos ingresados por el usuario | Alta | Medio | **Alta** | Validaciones en campos obligatorios. Evidencia fotográfica como respaldo del reclamo. |
| T005 | Vulnerabilidad en datos sensibles | Media | Crítico | **Crítica** | Autenticación por roles, HTTPS obligatorio, hashing de contraseñas, endpoints protegidos por rol. |


### 3.5 Visión Preventiva
 
 
 
#### Líneas de acción preventiva


**1. Probar desde el inicio, no al final**  
Las reglas de negocio se convertirán en casos de prueba antes de empezar a programar. Cada regla definida en la sección 2.1 tendrá sus propios escenarios: el flujo esperado, casos límite y situaciones de error. 

**2. Cobertura enfocada en umbrales y combinaciones**  
El motor de reglas no evalúa condiciones de forma aislada, sino en conjunto (monto, historial, antigüedad, etc.). Por eso, además de probar cada regla individualmente, se van a cubrir combinaciones entre ellas.

**3. Uso de datos de prueba realistas**  
Se armará un set de datos que represente situaciones reales: pólizas nuevas, clientes con historial de siniestros, montos justo en los límites del deducible y pólizas próximas a vencer.

**4. Trazabilidad como parte obligatoria del sistema**  
La trazabilidad no se tratará como algo opcional. Cada decisión, ya sea automática o manual, debe quedar registrada con las reglas evaluadas, las alertas detectadas y su justificación. 

**5. Revisión conjunta entre QA y negocio**  
Antes de iniciar cada ciclo de pruebas, QA contrastará las reglas implementadas con lo que realmente definió el negocio. La idea es detectar malentendidos antes de que se conviertan en fallos funcionales. No reemplaza las pruebas, pero ayuda a reducir errores desde etapas tempranas.

**6. Manejo de fallos del motor de reglas**  
Se definirá un comportamiento claro para cuando el motor de reglas falle (por ejemplo, ante una excepción o un timeout). En lugar de tomar una decisión automática, el sistema enviará el caso a revisión manual y registrará el problema. Esto evita que un error técnico termine en una decisión incorrecta.



