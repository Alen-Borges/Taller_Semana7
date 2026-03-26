# TEST_PLAN.md

## Automatizador de Siniestros — InsurTech MVP
## 1. Identificación del Plan
 
| Campo | Detalle |
|---|---|
| **Nombre del proyecto** | Automatizador de Siniestros — InsurTech |
| **Sistema bajo prueba** | Sistema de evaluación automatizada de pagos de siniestros de autos |
| **Versión** | MVP v1.0 |
| **Fecha** | 26/03/2026 |

### Equipo
 Rol | Responsable |
|-----|-------------|
| QA | Jean Pierre Villacís |
| Dev| Alexis Borges|

## 2. Contexto
El sistema a probar es una plataforma de evaluación automatizada de siniestros de autos. Su propósito de negocio es transformar el proceso manual de gestión de reclamos, que hoy toma varios días, en un flujo donde los casos de bajo riesgo se resuelven en minutos mediante reglas de negocio, y los casos con indicadores de riesgo se escalan a un gestor humano que toma la decisión final.
 
El MVP cubre desde el registro de datos (asegurados, vehículos, pólizas) hasta el registro y consulta de reclamos por parte del asegurado. La evaluación automática por motor de reglas y la resolución manual por el gestor.
 
## 3. Alcance de las Pruebas

### 3.1 En alcance (historias incluidas)
 
| Historia | Nombre | SP |
|----------|--------|----|
| HU-001 | Registro de asegurado | 3 |
| HU-002 | Consultar asegurados | 2 |
| HU-003 | Registrar vehículo | 3 |
| HU-004 | Consultar vehículos | 2 |
| HU-005 | Registrar póliza de seguro | 3 | Funcional + integración API |
| HU-006 | Consultar pólizas | 2 | Funcional + rendimiento básico |
| HU-007 | Registro de reclamo de siniestro | 5 | Funcional + integración API + archivos |
| HU-009 | Evaluación de reclamo por reglas de deducible y monto | 5 |
| HU-013 | Consulta de estado de reclamo por el asegurado | 3 | Funcional + autorización por rol |
 
**Total Story Points en alcance:** 28 SP

### Fuera de alcance (historias excluidas de este ciclo)
| Historia | Nombre |
|----------|--------|
| HU-008 | Validación de póliza para procesamiento de reclamo |
| HU-010 | Evaluación por historial de siniestros |
| HU-011 | Panel del gestor de seguros |
| HU-012 | Resolución manual de reclamos escalados |

## 4. Estrategia de Pruebas

### 4.1 Niveles de pruebas

 **Pruebas unitarias:** Validaciones de servicios de negocio, cálculos y reglas aisladas. Para HU-009 esto es especialmente crítico: el cálculo del deducible involucra tres variables (10% del monto del siniestro, 1% del valor asegurado, $200 fijo).
 
**Pruebas de integración API:** Validación end-to-end de cada endpoint REST, incluyendo persistencia, respuestas HTTP, estructura JSON y manejo de errores. Para HU-009 se valida que el motor de reglas procese correctamente el reclamo después de su registro y que el estado final (DESCARTADO, continúa a siguiente fase, EN REVISIÓN MANUAL) sea consistente con las reglas. Framework: **Karate DSL**.
 
**Pruebas funcionales:** Escenarios de aceptación escritos en Gherkin y automatizados con **SerenityBDD + Cucumber**. Cada historia de usuario tiene sus escenarios definidos y sus casos de prueba listos para ser implementados como steps.
 
**Pruebas de rendimiento:** Escenarios de carga básica sobre endpoints de consulta con **k6**. Se medirán tiempos de respuesta bajo carga concurrente y se establecerá una línea base de rendimiento.
 
**Pruebas exploratorias:** Sesiones manuales orientadas a casos límite, formatos inesperados, combinaciones de datos y flujos no contemplados en los escenarios automatizados. Para HU-009 se prestará especial atención a montos en el borde exacto del deducible y del 20% del valor asegurado.

### 4.2 Cobertura por historia
 
| HU | Casos automatizados (Serenity+Cucumber) | Tests de API (Karate) | Rendimiento (k6) | Exploratoria |
|----|------------------------------------------|-----------------------|-------------------|--------------|
| HU-001 | CP001 a CP005 (5 casos) | POST /asegurados | — | Sí |
| HU-002 | CP001 a CP003 (3 casos) | GET /asegurados, GET /asegurados/{id} | Sí | — |
| HU-003 | CP001 a CP004 (4 casos) | POST /vehiculos | — | Sí |
| HU-004 | CP001 a CP003 (3 casos) | GET /vehiculos, GET /vehiculos/{id} | Sí | — |
| HU-005 | CP001 a CP005 (5 casos) | POST /polizas | — | Sí |
| HU-006 | CP001 a CP003 (3 casos) | GET /polizas, GET /polizas/{id} | Sí | — |
| HU-007 | CP001 a CP008 (7 casos) | POST /reclamos (multipart) | — | Sí |
| HU-009 | CP001 a CP003 (3 casos) + casos de borde adicionales | Evaluación automática post-registro | — | Sí |
| HU-013 | CP001 a CP005 (5 casos) | GET /reclamos/{num}/estado | Sí | Sí |

## 5. Criterios de Entrada y Salida
### 5.1 Criterios de entrada (para iniciar pruebas de una HU)
 
- El código de la HU está completo y mergeado en la rama de desarrollo.
- El entorno de pruebas está levantado y accesible (Docker + PostgreSQL).
- Las pruebas unitarias del desarrollador pasan al 100%.
- El endpoint está documentado y responde al smoke test (200 en health check).
- Los datos de prueba están cargados o existe un script de seed disponible.
- La HU tiene criterios de aceptación y casos de prueba definidos.
 
### 5.2 Criterios de salida (para dar una HU por probada)
 
- Todos los casos de prueba automatizados pasan (Serenity + Cucumber y Karate).
- Para HU-009: todos los escenarios de la matriz de deducible pasan y los valores límite están cubiertos sin discrepancia.
- Las evidencias están generadas: reporte de Serenity BDD, reporte de Karate, capturas o video de pruebas exploratorias.
- El reporte de ejecución está actualizado en el repositorio de pruebas.

## 6. Entorno de Pruebas
| Componente | Configuración |
|------------|---------------|
| **Aplicación** | Spring Boot (Java 17+) en contenedor Docker |
| **Base de datos** | PostgreSQL en contenedor Docker (docker-compose) |
| **Orquestación** | docker-compose.yml con servicios app + db |
| **Datos de prueba** | Script SQL de seed con asegurados, vehículos, pólizas y reclamos predefinidos |
| **Autenticación** | Tokens JWT generados con credenciales de prueba (rol GESTOR y rol ASEGURADO) |

## 7. Herramientas
 
| Herramienta | Propósito | Alcance |
|-------------|-----------|---------|
| **SerenityBDD + Cucumber** | Automatización de pruebas funcionales (BDD) | Escenarios de aceptación de cada HU en Gherkin |
| **Karate DSL** | Pruebas de integración y contrato de API | Validación de endpoints REST, estructura JSON, códigos HTTP |
| **k6** | Pruebas de rendimiento y carga | Endpoints de consulta (GET) bajo concurrencia |
| **Docker + docker-compose** | Entorno de pruebas reproducible | Levantar app + BD idéntico en cada ejecución |
| **Git** | Control de versiones de scripts de prueba | Repositorios independientes de pruebas |
| **GitHub Issues** | Gestión de defectos e incidencias | Reporte, seguimiento y priorización de bugs |

## 8. Roles y Responsabilidades
 
### QA 
 
- Diseño del plan de pruebas y matrices de datos.
- Implementación de escenarios automatizados (SerenityBDD + Cucumber).
- Implementación de pruebas de API (Karate).
- Diseño y ejecución de pruebas de rendimiento (k6).
- Ejecución de pruebas exploratorias.
- Reporte de bugs con evidencia (pasos, datos, resultado esperado vs obtenido).
- Validación de criterios de entrada antes de iniciar cada HU.
 
### DEV 
 
- Corrección de bugs reportados por QA.
- Soporte en la preparación del entorno y datos de prueba.
- Revisión conjunta de criterios de aceptación con QA antes de cada microsprint.

## 9. Cronograma

## 10. Entregables de Prueba
 
### 10.1 Artefactos por microsprint
 
| Entregable | Formato | Frecuencia |
|------------|---------|------------|
| Reporte de ejecución SerenityBDD | HTML (generado automático) | Por microsprint |
| Reporte de ejecución Karate | HTML (generado automático) | Por microsprint |
| Reporte de rendimiento k6 | JSON / HTML summary | Por microsprint (endpoints de consulta) |
| Evidencia de pruebas exploratorias | Capturas de pantalla o video corto | Por HU explorada |
| Reporte de bugs / incidencias |  GitHub Issues o md con el reporte| Continuo |
 
### 10.2 Entregables finales del ciclo
 
| Entregable | Descripción |
|------------|-------------|
| **Repositorio de pruebas funcionales** | Proyecto SerenityBDD + Cucumber con los `.feature` y steps. |
| **Repositorio de pruebas de API** | Proyecto Karate con los `.feature` de contrato y validación de endpoints. Repositorio independiente. |
| **Scripts de rendimiento** | Archivos `.js` de k6 para los escenarios de carga. Pueden vivir en el mismo repo de pruebas de API o en uno aparte. |
| **Reporte consolidado de ejecución** | Reporte .md con: total de casos ejecutados, pasados, fallidos, bloqueados; defectos abiertos por severidad; cobertura por HU. |
| 

 
### 10.3 Estructura de repositorios

Se realizara repositorios independientes para cada tipo de prueba, estrucutrados de la siguiente manera:
 
**Pruebas funcionales con SerenityBDD + Cucumber**
```
qa-siniestros-funcional/
├── src/test/resources/features/
│   ├── HU001_registro_asegurado.feature
│   ├── HU002_consultar_asegurados.feature
│   ├── HU003_registrar_vehiculo.feature
│   ├── HU004_consultar_vehiculos.feature
│   ├── HU005_registrar_poliza.feature
│   ├── HU006_consultar_polizas.feature
│   ├── HU007_registro_reclamo.feature
│   ├── HU009_evaluacion_deducible_monto.feature
│   └── HU013_consulta_estado_reclamo.feature
├── src/test/java/steps/
├── serenity.conf
└── pom.xml
 
```

**Pruebas de API con Karate DSL**
```
qa-siniestros-api/                
├── src/test/java/
│   ├── asegurados/
│   ├── vehiculos/
│   ├── polizas/
│   ├── reclamos/
│   ├── evaluacion/
│   └── karate-config.js
└── pom.xml
 
```

**Pruebas de rendimiento con k6**
```
qa-siniestros-performance/       
├── scripts/
│   ├── consulta_asegurados.js
│   ├── consulta_vehiculos.js
│   ├── consulta_polizas.js
│   └── consulta_estado_reclamo.js
└── README.md
```

## 11. Riesgos y Contingencias
 
### 11.1 Riesgos de producto
 
| ID | Riesgo | Probabilidad | Impacto | Mitigación |
|----|--------|:------------:|:-------:|------------|
| RP-01 | Reglas de validación implementadas no coinciden con los criterios de aceptación definidos en las HU | Media | Alto | Revisión conjunta QA + DEV de criterios de aceptación antes de cada microsprint. |
| RP-02 | Errores en valores límite (montos en cero, fechas exactas en el borde de vigencia, placas al límite del formato) | Alta | Alto | Casos de prueba específicos para boundary values en cada HU. Los CP ya contemplan estos escenarios. |
| RP-03 | Datos de prueba insuficientes o no representativos para detectar defectos reales | Media | Medio | Uso de datos realistas (nombres, cédulas, placas, montos representativos). |


### 11.2 Riesgos de proyecto
 
| ID | Riesgo | Probabilidad | Impacto | Mitigación |
|----|--------|:------------:|:-------:|------------|
| RPY-01 | Entorno de pruebas inestable o no disponible al iniciar un microsprint | Media | Alto | Smoke test obligatorio al inicio de cada microsprint. Documentar el proceso de levantamiento para que cualquier miembro lo ejecute. |
| RPY-02 | Desarrollo de las HU no está completo al iniciar el microsprint correspondiente | Media | Alto | Criterios de entrada estrictos. Si la HU no cumple, se pospone al siguiente microsprint y se reajusta el cronograma. |
| RPY-03 | Defectos críticos en microsprint 1 consumen tiempo del microsprint 2 | Media | Medio | Priorización: los defectos bloqueantes se corrigen antes de avanzar. |




 