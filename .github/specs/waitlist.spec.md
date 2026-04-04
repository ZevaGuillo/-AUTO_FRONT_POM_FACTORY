---
id: SPEC-002
status: DRAFT
feature: waitlist
created: 2026-04-04
updated: 2026-04-04
author: spec-generator
version: "1.0"
related-specs: []
---

# Spec: Sistema de Lista de Espera

> **Estado:** `DRAFT` → aprobar con `status: APPROVED` antes de iniciar implementación.
> **Ciclo de vida:** DRAFT → APPROVED → IN_PROGRESS → IMPLEMENTED → DEPRECATED

---

## 1. REQUERIMIENTOS

### Descripción
El sistema de lista de espera permite a usuarios autenticados registrarse para recibir notificaciones cuando se liberen asientos en secciones agotadas de eventos. El usuario puede ver si ya está registrado en la lista, confirmación visual, y cancelar su suscripción en cualquier momento.

### Requerimiento de Negocio
Los usuarios autenticados necesitan la capacidad de:
1. Ver una opción clara para unirse a la lista de espera cuando acceden a una sección con 0 asientos disponibles.
2. Recibir confirmación visual/toast cuando se registran exitosamente.
3. Ver el estado de su suscripción (banner informativo) y evitar registro duplicado.
4. Cancelar su suscripción con confirmación modal y actualización en tiempo real de la interfaz.

### Historias de Usuario

#### HU-001: Registro en lista de espera para sección agotada

```
Como:        Usuario autenticado
Quiero:      registrarme en la lista de espera cuando una sección está agotada
Para:        recibir notificación si se liberan asientos

Prioridad:   Alta
Estimación:  M
Dependencias: Autenticación de usuario
Capa:        Frontend
```

#### Criterios de Aceptación — HU-001

**CP_HU001_01: Happy Path — Registro exitoso en lista de espera**
```gherkin
Dado que soy un usuario autenticado en la plataforma Ticketing
Y accedo a la página de detalles del evento "Concierto Sinfónico"
  donde la sección "Platea Central" no tiene asientos disponibles
Y no tengo una suscripción activa previa para esa sección y evento
Cuando intento interactuar con la sección "Platea Central"
Entonces el sistema debe mostrar una opción visible y clara 
  para "Unirse a la lista de espera", 
  asociada específicamente a esa sección y evento
Cuando selecciono la opción para unirme y confirmo mi registro
Entonces el sistema debe registrar mi suscripción en la base de datos,
  asociándola a mi uid de Firebase, al evento "Concierto Sinfónico"
  y a la sección "Platea Central"
Y debo visualizar un mensaje de confirmación tipo toast:
  "¡Te has unido a la lista de espera! Te notificaremos si se liberan asientos."
Y el banner informativo "Ya estás en la lista de espera" debe aparecer
```

**CP_HU001_03: Error Path — Impedir registro duplicado**
```gherkin
Dado que soy un usuario autenticado que ya se encuentra registrado
  en la lista de espera para la sección "Platea Central"
  del evento "Concierto Sinfónico"
Cuando accedo nuevamente a la página de detalles de dicho evento y sección
Entonces el sistema no debe mostrar la opción para "Unirse a la lista de espera"
Y debe mostrar un indicador visual o banner 
  con el mensaje "Ya estás en la lista de espera"
Y no se debe crear un registro duplicado en WAITLIST_ENTRIES
```

---

#### HU-002: Visualizar estado de suscripción en lista de espera

```
Como:        Usuario autenticado con suscripción activa
Quiero:      ver el estado de mi registro en la lista de espera
Para:        saber si estoy notificado o si puedo cancelar

Prioridad:   Alta
Estimación:  S
Dependencias: HU-001
Capa:        Frontend
```

#### Criterios de Aceptación — HU-002

**CP_HU002_01: Banner informativo de suscripción activa**
```gherkin
Dado que estoy registrado en la lista de espera 
  para la sección "Platea Central" del evento "Concierto Sinfónico"
Cuando accedo a la página de detalles de dicho evento
Entonces debo visualizar un banner informativo claramente visible
  con el texto "Ya estás en la lista de espera"
Y el botón para "Unirme a la lista de espera" NO debe estar visible
Y debe haber un botón o opción visible para "Cancelar mi suscripción"
```

---

#### HU-003: Cancelación de suscripción en lista de espera

```
Como:        Usuario autenticado con suscripción activa
Quiero:      cancelar mi suscripción a la lista de espera
Para:        dejar de recibir notificaciones para esa sección

Prioridad:   Alta
Estimación:  S
Dependencias: HU-002
Capa:        Frontend
```

#### Criterios de Aceptación — HU-003

**CP_HU003_02: Cancelación exitosa y actualización de UI**
```gherkin
Dado que estoy registrado en la lista de espera
  para la sección "Platea Central" del evento "Concierto Sinfónico"
Y visualizo el banner informativo con opción para cancelar
Cuando hago clic en "Cancelar mi suscripción"
Entonces debe aparecer un modal de confirmación
  con un mensaje como "¿Estás seguro de que deseas cancelar 
  tu suscripción a la lista de espera?"
Cuando confirmo la cancelación haciendo clic en "Confirmar"
Entonces el estado de mi suscripción en la base de datos
  debe cambiar a "cancelled"
Y debo visualizar una notificación toast con el mensaje exacto:
  "Has cancelado tu suscripción a la lista de espera."
Y el banner informativo debe desaparecer de la página
Y el botón para "Unirme a la lista de espera" debe volver
  a estar visible y funcional
Y la página no requiere ser recargada para reflejar estos cambios
```

**CP_HU003_03: Cancelación anulada (usuario cierra el modal)**
```gherkin
Dado que el modal de confirmación de cancelación está visible
Cuando hago clic en "Cancelar" o fuera del modal
Entonces el modal debe cerrarse sin cambios
Y mi suscripción continúa activa en la base de datos
Y el banner y estado visual permanecen igual
```

---

### Reglas de Negocio

1. **Unicidad de suscripción**: Un usuario NO puede tener más de un registro activo (`status = 'active'`) para la misma combinación (usuario + evento + sección).

2. **Estados permitidos de suscripción**:
   - `active`: Usuario registrado, en espera de notificación.
   - `cancelled`: Usuario canceló su suscripción voluntariamente.
   - `notified`: Usuario fue notificado (reservado para fase 2).

3. **Visibilidad condicional**: La opción para "Unirse a la lista de espera" solo se muestra si:
   - La sección tiene 0 asientos disponibles.
   - El usuario NO tiene una suscripción con `status = 'active'` para esa sección y evento.

4. **Campos obligatorios**: Toda suscripción captura y almacena:
   - `uid` del usuario (Firebase Auth).
   - `event_id` del evento.
   - `section_id` de la sección.
   - `status` de la suscripción.
   - `created_at` y `updated_at` (timestamps UTC).

5. **Terminología canónica**: 
   - Se usa "lista de espera" (no "waiting list" ni "queue").
   - Se usa "suscripción" para el registro del usuario (no "registro").
   - Se usa uid de Firebase (no ID técnico, `_id` ni `id` nativo de DB).

---

## 2. DISEÑO

### Modelos de Datos

#### Entidades afectadas

| Entidad | Almacén | Cambios | Descripción |
|---------|---------|---------|-------------|
| `WAITLIST_ENTRIES` | Colección MongoDB | nueva | Registros de usuario en listas de espera |
| `Event` | Colección MongoDB | existente | Relación con eventos (lectura) |
| `Section` | Colección MongoDB | existente | Asientos de sección (lectura de disponibilidad) |

#### Esquema: WAITLIST_ENTRIES

```json
{
  "_id": "ObjectId (auto-generado)",
  "uid": "string (Firebase Auth UID)",
  "event_id": "string (referencia a Event)",
  "section_id": "string (referencia a Section)",
  "status": "enum ['active', 'cancelled', 'notified']",
  "created_at": "datetime (UTC)",
  "updated_at": "datetime (UTC)"
}
```

#### Índices / Constraints

| Índice | Campos | Tipo | Justificación |
|--------|--------|------|--------------|
| Único compuesto | `(uid, event_id, section_id, status)` | Único | Prevenir duplicados activos para mismo usuario/evento/sección |
| Búsqueda rápida | `(uid, status)` | Ordinario | Listar suscripciones activas de un usuario |
| Búsqueda por evento | `(event_id, section_id)` | Ordinario | Obtener lista de espera para notificaciones (fase 2) |

---

### API Endpoints

#### POST `/api/v1/waitlist`

**Descripción**: Registra un usuario en la lista de espera para una sección agotada.

**Auth requerida**: Sí (Firebase idToken en header `Authorization: Bearer <token>`)

**Request Body**:
```json
{
  "event_id": "string",
  "section_id": "string"
}
```

**Response 201 (Created)**:
```json
{
  "id": "string (ObjectId)",
  "uid": "string",
  "event_id": "string",
  "section_id": "string",
  "status": "active",
  "created_at": "2026-04-04T12:00:00Z",
  "updated_at": "2026-04-04T12:00:00Z"
}
```

**Response 409 (Conflict) — Duplicado**:
```json
{
  "error": "WAITLIST_DUPLICATE",
  "message": "Ya estás registrado en la lista de espera para esta sección",
  "code": 409
}
```

**Response 400 (Bad Request) — Campos inválidos**:
```json
{
  "error": "INVALID_INPUT",
  "message": "event_id y section_id son obligatorios",
  "code": 400
}
```

---

#### GET `/api/v1/waitlist/{uid}/{event_id}/{section_id}`

**Descripción**: Obtiene el estado de suscripción de un usuario en una sección específica.

**Auth requerida**: Sí

**Response 200 (OK)**:
```json
{
  "id": "string (ObjectId)",
  "uid": "string",
  "event_id": "string",
  "section_id": "string",
  "status": "active",
  "created_at": "2026-04-04T12:00:00Z",
  "updated_at": "2026-04-04T12:00:00Z"
}
```

**Response 404 (Not Found)**:
```json
{
  "error": "NOT_FOUND",
  "message": "No hay registro de suscripción para esta combinación",
  "code": 404
}
```

---

#### DELETE `/api/v1/waitlist/{id}`

**Descripción**: Marca un registro de lista de espera como cancelado.

**Auth requerida**: Sí (el usuario debe ser el propietario)

**Response 200 (OK)**:
```json
{
  "id": "string (ObjectId)",
  "uid": "string",
  "event_id": "string",
  "section_id": "string",
  "status": "cancelled",
  "created_at": "2026-04-04T12:00:00Z",
  "updated_at": "2026-04-04T13:00:00Z"
}
```

**Response 404 (Not Found)**:
```json
{
  "error": "NOT_FOUND",
  "message": "No existe registro con ese ID",
  "code": 404
}
```

**Response 403 (Forbidden) — Sin autorización**:
```json
{
  "error": "FORBIDDEN",
  "message": "No tienes permiso para cancelar esta suscripción",
  "code": 403
}
```

---

### Flujo de Frontend

#### Componentes principales

1. **WaitlistBanner** (renderizado en Event Detail Page)
   - Props: `uid`, `eventId`, `sectionId`, `onCancel`
   - Estados: `inactive` (opción para unirse), `active` (banner + botón cancelar), `loading`
   - Acciones: Clic en "Unirse" → Modal de confirmación → POST `/api/v1/waitlist`

2. **WaitlistModal** (confirmación de registro)
   - Props: `onConfirm`, `onCancel`, `eventName`, `sectionName`
   - Muestra: Título descriptivo + botones "Confirmar" / "Cancelar"
   - Acción: `onConfirm` → spinner → toast de éxito

3. **CancellationModal** (confirmación de cancelación)
   - Props: `onConfirm`, `onCancel`, `eventName`, `sectionName`
   - Muestra: Advertencia + botones "Confirmar" / "Cancelar"
   - Acción: `onConfirm` → spinner → DELETE /api/v1/waitlist/{id} → toast de éxito

#### Flujo de estados en UI

```
[No registrado]
    ↓
[Clic en "Unirse a la lista de espera"]
    ↓ (abre WaitlistModal)
[Clic en "Confirmar" en modal]
    ↓ (POST /api/v1/waitlist)
[Esperando respuesta...]
    ↓
[✓ Registro exitoso]
    ↓
[Renderizar WaitlistBanner con estado activo]
    ↓
[Mostrar botón "Cancelar mi suscripción"]
    ↓ (usuario hace clic en cancelar)
[Abre CancellationModal]
    ↓
[Clic en "Confirmar"]
    ↓ (DELETE /api/v1/waitlist/{id})
[Esperando respuesta...]
    ↓
[✓ Cancelación exitosa]
    ↓
[Resetear a estado "No registrado"]
    ↓
[Mostrar opción "Unirse a la lista de espera"]
```

---

## 3. LISTA DE TAREAS

### Backend Developer

- [ ] Crear modelo Pydantic `WaitlistEntryCreate` y `WaitlistEntryResponse`
- [ ] Crear repositorio `WaitlistRepository` con métodos:
  - [ ] `create_entry(uid, event_id, section_id)` → valida unicidad
  - [ ] `get_entry_by_composite_key(uid, event_id, section_id)` → retorna entry o None
  - [ ] `get_entry_by_id(id)` → retorna entry o None
  - [ ] `cancel_entry(id)` → actualiza status a "cancelled"
- [ ] Crear servicio `WaitlistService` con lógica de negocio:
  - [ ] `register_waitlist_entry(uid, event_id, section_id)` → validar duplicado
  - [ ] `get_user_waitlist_entry(uid, event_id, section_id)`
  - [ ] `cancel_waitlist_entry(id, uid)` → validar propiedad del registro
- [ ] Crear router `/api/v1/waitlist` con endpoints:
  - [ ] POST → crear suscripción
  - [ ] GET /{uid}/{event_id}/{section_id} → consultar estado
  - [ ] DELETE /{id} → cancelar suscripción
- [ ] Crear índices MongoDB en colección `WAITLIST_ENTRIES`
- [ ] Tests unitarios para servicio y repositorio
- [ ] Tests de integración para endpoints

### Frontend Developer

- [ ] Crear página/componente `EventDetailPage` (si no existe) con Vista de Detalles del Evento
- [ ] Crear componente `WaitlistBanner`:
  - [ ] Renderizar opción "Unirse a la lista de espera" cuando section.available_seats === 0
  - [ ] Renderizar banner "Ya estás en la lista de espera" cuando user está registrado
  - [ ] Botón "Cancelar mi suscripción" en modo activo
  - [ ] Estados de carga (loading, error)
- [ ] Crear modal `WaitlistModal` (confirmación de registro):
  - [ ] Título descriptivo
  - [ ] Botones "Confirmar" / "Cancelar"
  - [ ] Spinner en confirmación
- [ ] Crear modal `CancellationModal` (confirmación de cancelación):
  - [ ] Advertencia al usuario
  - [ ] Botones "Confirmar" / "Cancelar"
  - [ ] Spinner en confirmación
- [ ] Crear hook `useWaitlist(eventId, sectionId)`:
  - [ ] Consultar estado: GET `/api/v1/waitlist/{uid}/{event_id}/{section_id}`
  - [ ] Registrar: POST `/api/v1/waitlist`
  - [ ] Cancelar: DELETE `/api/v1/waitlist/{id}`
  - [ ] Manejo de errores y estados
- [ ] Integrar componentes en `EventDetailPage`
- [ ] Estilos CSS/Tailwind para componentes (modo claro + oscuro)
- [ ] Tests unitarios para componentes y hook

### QA Agent

- [ ] Generar escenarios Gherkin extendidos (edge cases, errores de red, etc.)
- [ ] Definir datos de prueba (eventos, secciones, usuarios de test)
- [ ] Crear plan de riesgos (ASD: Alto/Medio/Bajo)
- [ ] Crear plan de pruebas de performance:
  - [ ] Carga máxima de suscripciones por evento
  - [ ] Tiempo de respuesta en picos
  - [ ] Comportamiento de cancelación masiva
- [ ] Plan de pruebas E2E con Cypress/Playwright
- [ ] Validación de estados en BD post-test

---

## Notas y Consideraciones

- **Phase 2**: Notificación automática cuando se liberen asientos (webhook + email).
- **Phase 2**: Dashboard de lista de espera para administradores.
- **Phase 2**: Análisis de comportamiento: usuarios activos en listas, tasa de conversión, etc.
- **Security**: Validar que `uid` del token JWT coincida con uid de la suscripción antes de actualizar/eliminar.
- **Observabilidad**: Loguear todas las acciones (registro, cancelación) con contexto del usuario y evento.

---

**Status**: DRAFT  
**Próximo paso**: Enviar a usuario para aprobación → cambiar a `APPROVED` → Fase 2 (implementación paralela)
