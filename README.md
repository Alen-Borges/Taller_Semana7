# 🚗 Automatizador de Siniestros — InsurTech MVP
**Taller Semana 7: Expectativa vs. Realidad — Ejecución Ágil y Estrategia de Pruebas**

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

Este proyecto nace del choque con la realidad de llevar al desarrollo el análisis de negocio previamente definido de pólizas de seguros del Ecuador. Se trata del MVP crítico de un sistema de evaluación automatizada de pagos de siniestros automotrices.

Está construido en un ecosistema de microservicios usando **Spring Boot** para el backend y **React** para el frontend, priorizando las Historias de Usuario más cruciales para habilitar el reporte de siniestros, su evaluación automática mediante un motor de reglas y la revisión del estado por parte del asegurado.

---

## 👥 Equipo y Roles

- 👩‍💻 **DEV (Desarrollo & Arquitectura):** Alexis Borges 
- 🕵️‍♂️ **QA (Estrategia de Calidad & Automatización):** Jean Pierre Villacis

---

## 🎯 Entregables Finales y Enlaces Requeridos

Cumpliendo con la rúbrica de evaluación del Taller Semana 7, centralizamos aquí todos los artefactos generados en la dinámica de *Micro-Sprints*:

1. 📌 **Tablero Ágil:** https://github.com/users/Alen-Borges/projects/4
2. ⏱️ **Retrospectiva & Time-Tracking:** [`REALITY_CHECK.md`](./REALITY_CHECK.md) *(Documento de contrastación de Story Points vs Realidad)*
3. 📋 **Plan de Pruebas Formal:** [`TEST_PLAN.md`](./TEST_PLAN.md)
4. 🧪 **Matriz de Casos BDD:** [`TEST_CASES.md`](./TEST_CASES.md)
5. 🤖 **Repositorio Pruebas Karate:** https://github.com/JeanVillacis/Semana7_PruebasKarate
6. 🤖 **Repositorio Pruebas Screenplay Serenity BDD:** https://github.com/JeanVillacis/Semena7_Serenity
7. 🤖 **Repositorio Pruebas Rendimiento:** https://github.com/JeanVillacis/Semana7_PruebasK6
---


## 🏗️ Arquitectura del MVP

El MVP excluye por priorización estratégica (Time-to-Market) las vistas y resoluciones asíncronas del perfil Gestor (HU-008, 010, 011, 012). Consta de los siguientes módulos implementados y orquestados:

- **`ms-apigateway`**: Puerta de enlace y filtro global con validación de tokens JWT.
- **`ms-authservice`**: Módulo de autenticación de credenciales, Login y emisión de JWT.
- **`ms-coreservice`**: Lógica de negocio (CRUDs y operativas relacionales de Asegurados, Vehículos, Pólizas y subida de Reclamos con fotografías multipart).
- **`ms-evaluacion`**: Motor de reglas de negocio que orquesta la elegibilidad del reclamo, deducciones y escalamiento según montos financieros de la póliza.
- **`frontend`**: Aplicación React SPA que consume de manera orquestada la infraestructura.

---

## 🚀 Cómo inicializar el proyecto en local

### Pre-requisitos
- Tener instalado **Docker** y **Docker Compose**.
- Java 17 y Node.js (opcional si deseas correr localmente fuera de contenedor).

### Pasos de Ejecución

1. Clonar el repositorio.
2. Posicionarse en la raíz del proyecto.
3. Compilar los microservicios si es necesario (el docker-compose se encarga de empaquetar, pero si requieres forzar construcción local usa Maven/Gradle en cada módulo `ms-*`).
4. Ejecutar el orquestador:
   ```bash
   docker-compose up --build -d
   ```
5. Acceder a los distintos componentes:
   - **Frontend App:** `http://localhost:5173` o `http://localhost:80` (Varía según configuración frontend)
   - **API Gateway (Todas las peticiones backend entran por aquí):** `http://localhost:8080/api/v1/...`
   - **Base de Datos Postgres (Interna en docker):** `Puerto 5432`

---

## 🧩 Flujo Básico del MVP

1. **Autenticación**: Iniciar sesión vía `/api/v1/auth/login` recibiendo el `Bearer Token`.
2. **Entidades Base**: Registrar/Consultar vehículo, asegurado y pólizas vía `ms-coreservice`.
3. **Registro de Reclamo**: Emitir póliza y enviar fotos del incidente para recibir un Tracking Number y evaluación inicial del deducible vía el motor de reglas en `ms-evaluacion`.
4. **Consulta**: Usar el endpoint de estado de reclamo del asegurado simulando un polling de resolución rápida.

---


## 🧪 Estrategia de Pruebas — Cobertura QA

Como parte de la estrategia de calidad definida desde el inicio del proyecto (*Shift-Left Testing*), se implementaron **tres capas de pruebas automatizadas** que cubren distintos niveles del stack:

### 1. 🔗 Pruebas de API — Karate Framework
Pruebas funcionales de integración sobre los endpoints REST del API Gateway, validando contratos, autenticación JWT, flujos de negocio completos y manejo de errores.
- Repositorio: https://github.com/JeanVillacis/Semana7_PruebasKarate
- Incluye: escenarios BDD en `.feature`, reportes HTML generados por Karate.

### 2. 🎭 Pruebas E2E — Serenity BDD (Screenplay Pattern)
Pruebas de aceptación automatizadas sobre la interfaz web usando el patrón Screenplay con Serenity BDD, validando los flujos críticos desde la perspectiva del usuario final (Login, registro de reclamo, consulta de estado).
- Repositorio: https://github.com/JeanVillacis/Semena7_Serenity
- Incluye: informe Serenity con narrativa BDD, capturas de pantalla, trazabilidad de historias de usuario y estadísticas de cobertura.

### 3. ⚡ Pruebas de Rendimiento — k6
Pruebas de carga y estrés sobre los endpoints más críticos del sistema, evaluando tiempos de respuesta, throughput y comportamiento bajo concurrencia.
- Repositorio: https://github.com/JeanVillacis/Semana7_PruebasK6
- Incluye: scripts k6, umbrales de aceptación definidos, resumen de métricas (p95, RPS, tasa de error).

> **Nota para el evaluador:** Cada repositorio de pruebas contiene su propio `README` con instrucciones de ejecución e interpretación de resultados. Los informes generados se encuentran disponibles dentro de cada repositorio bajo la carpeta `target/site/serenity/` (Serenity) y como salida de consola/exportación JSON (k6).

---

*Proyecto desarrollado para demostrar adaptabilidad, calidad garantizada desde Shift-Left Testing, y entregas de gran valor funcional en ciclos sumamente cortos.*
