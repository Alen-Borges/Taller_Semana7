# Reality Check: Taller Semana 7
## Expectativa vs. Realidad — Ejecución Ágil, MVP y Estrategia de Pruebas

**Proyecto:** Sistema de Gestión de Seguros  
**Equipo:** Alexis Borges (DEV) · Jean Pierre Villacis (QA)  
**Período:** 25–26 de Marzo, 2026  

---

## 1. Análisis de Estimación vs. Realidad

| Bloque de Trabajo | SP Estimados | Horas Estimadas | Horas Reales | Desviación | Causa Principal |
|:---|:---:|:---:|:---:|:---:|:---|
| Integración `ms-authservice` + JWT | 5 | ~3.75 h | 3.5 h | -7% | Librería ya conocida |
| Frontend – ajuste de rutas y guards | 3 | ~2.25 h | 2.0 h | -11% | Componentes reutilizados |
| HU-001 a HU-005 (lógica de dominio) | 8 | ~6.0 h | 5.0 h | -17% | Flujo bien definido |
| HU-006 – Gestión de Pólizas | 3 | ~2.25 h | 3.5 h | **+56%** | Validación de fechas más compleja |
| HU-007 – Reclamos con foto | 5 | ~3.75 h | 6.0 h | **+60%** | Mock de fotos + validación de póliza |
| Estrategia QA + TEST_PLAN.md | 3 | ~2.25 h | 3.0 h | +33% | Definir criterios tomó más iteraciones |
| **TOTAL** | **27** | **~20 h** | **23 h** | **+15%** | |

---

## 2. ¿Qué tareas subestimamos y por qué?

### HU-007 — Registro de Reclamos (+60% sobre lo estimado)
Descubrimos que el mock del repositorio de fotos retornaba un falso positivo cuando la lista estaba vacía — un bug detectado gracias a los tests unitarios que habría llegado a integración sin detectarse. La complejidad de gestionar múltiples repositorios mockeados elevó el esfuerzo.

### Estrategia de QA y TEST_PLAN.md (+33% sobre lo estimado)
Definir criterios de salida medibles y alinear las responsabilidades entre DEV y QA requirió más sesiones de trabajo de las previstas inicialmente.

---

## 3. ¿El MVP construido es realmente valioso para el negocio?
**Sí.** El MVP cubre el flujo crítico: Autenticación → Asegurado → Vehículo → Póliza → Reclamo. 
Garantiza que ningún reclamo se registre sin póliza activa y que la evidencia fotográfica sea obligatoria, mitigando los riesgos de fraude más costosos desde el primer día.

---

## 4. ¿Cómo garantizó el QA la calidad en tan poco tiempo?

Se aplicó el principio de **Shift-Left**: validar lo antes posible al nivel más bajo.

- **Tests unitarios por servicio (JUnit 5 + Mockito):** 14 tests cubriendo los servicios de Asegurado, Vehículo, Póliza y Reclamo. Cada cambio fue validado inmediatamente, permitiendo corregir errores de lógica antes de cualquier despliegue.
- **Definición de Escenarios (Gherkin):** Se redactaron los criterios de aceptación en formato Given/When/Then para asegurar que el comportamiento del sistema coincida con la necesidad del negocio.

---

## 5. Lecciones del Proceso
1. Los SP de QA se subestiman porque no suelen incluir el diseño de datos de prueba.
2. Las dependencias entre entidades (Póliza-Reclamo) deben mapearse mejor en el planning.
3. La colaboración DEV-QA es más efectiva cuando los criterios de aceptación están claros antes de tirar la primera línea de código.

**Firmado:** Alexis Borges (DEV) & Jean Pierre Villacis (QA)  
**Fecha:** 26 de Marzo, 2026
