---
id: SPEC-001
status: IN_PROGRESS
feature: frontend-automation-framework
created: 2026-03-16
updated: 2026-03-16
author: spec-generator
version: "1.0"
related-specs: []
---

# Spec: Framework de Automatización Frontend con Serenity BDD

> **Estado:** `DRAFT` → aprobar con `status: APPROVED` antes de iniciar implementación.
> **Ciclo de vida:** DRAFT → APPROVED → IN_PROGRESS → IMPLEMENTED → DEPRECATED

---

## 1. REQUERIMIENTOS

### Descripción
Framework de automatización completo para testing de UI de una aplicación web de ticketing. Implementa Page Object Model (POM) con Page Factory usando Serenity BDD, Selenium WebDriver y Cucumber para pruebas End-to-End escalables y mantenibles.

### Requerimiento de Negocio
Crear un proyecto completo de automatización usando Serenity BDD que implemente Page Object Model (POM) con Page Factory, basándose en el plan de automatización frontend de una aplicación web de ticketing construida con Next.js 14, TypeScript y shadcn/ui components.

### Historias de Usuario

#### HU-01: Implementar Page Objects con Page Factory

```
Como:        Ingeniero QA Automation
Quiero:      Page Objects implementados con @FindBy annotations para todas las páginas del sistema de ticketing
Para:        Mantener una estructura escalable y reutilizable que encapsule elementos UI y método de interacción

Prioridad:   Alta
Estimación:  L
Dependencias: Ninguna
Capa:        Testing Framework
```

#### HU-02: Casos de Prueba BDD en Gherkin

```
Como:        Ingeniero QA Automation
Quiero:      Features en Gherkin que cubran flujos críticos del sistema (eventos, checkout, admin)
Para:        Ejecutar pruebas legibles tanto para equipo técnico como stakeholders de negocio

Prioridad:   Alta
Estimación:  M
Dependencias: HU-01
Capa:        Testing Framework
```

#### HU-03: Configuración Serenity BDD

```
Como:        Ingeniero QA Automation
Quiero:      Proyecto configurado con Serenity BDD, dependencies y reportes
Para:        Ejecutar pruebas y generar reportes visuales detallados de resultados

Prioridad:   Alta  
Estimación:  S
Dependencias: HU-01, HU-02
Capa:        Testing Framework
```

#### Criterios de Aceptación — HU-01

**Happy Path**
```gherkin
CRITERIO-1.1: Page Objects implementados correctamente
  Dado que:  Existe el plan de automatización con páginas y elementos identificados
  Cuando:    Se implementan los Page Objects con @FindBy para cada página
  Entonces:  Los Page Objects encapsulan elementos UI y métodos de interacción específicos de cada página
```

**Error Path**
```gherkin
CRITERIO-1.2: Manejo de elementos no encontrados
  Dado que:  Un Page Object intenta interactuar con un elemento inexistente
  Cuando:    Se ejecuta el método de interacción
  Entonces:  Serenity maneja el error y genera reporte detallado del fallo
```

#### Criterios de Aceptación — HU-02

**Happy Path**
```gherkin
CRITERIO-2.1: Features en Gherkin ejecutables
  Dado que:  Existen features (.feature files) con escenarios BDD
  Cuando:    Se ejecutan a través de Cucumber runner
  Entonces:  Se ejecutan correctamente y generan reportes Serenity
```

#### Criterios de Aceptación — HU-03

**Happy Path**
```gherkin
CRITERIO-3.1: Proyecto ejecutable completo
  Dado que:  El proyecto está configurado con build.gradle y serenity.conf
  Cuando:    Se ejecuta ./gradlew clean test
  Entonces:  Las pruebas se ejecutan y generan reportes en target/site/serenity/
```

### Reglas de Negocio
1. Todos los Page Objects deben usar @FindBy annotations (Page Factory pattern)
2. Los locators deben seguir el plan de automatización (CSS selectors específicos)
3. Features deben agruparse por funcionalidad: events.feature, checkout.feature, admin.feature
4. Step Definitions no deben contener lógica técnica, solo orquestar Page Objects
5. Configuración debe permitir ejecución en Chrome con WebDriverManager
6. Reportes deben incluir screenshots de fallos

---

## 2. DISEÑO

### Arquitectura del Proyecto

#### Estructura de Directorios
```
src/
└── test/
    ├── java/
    │   └── com/ticketing/
    │       ├── pages/
    │       │   ├── BasePage.java
    │       │   ├── public/
    │       │   │   ├── EventsPage.java
    │       │   │   ├── EventDetailPage.java  
    │       │   │   └── CheckoutPage.java
    │       │   └── admin/
    │       │       ├── AdminLoginPage.java
    │       │       ├── AdminDashboardPage.java
    │       │       ├── CreateEventPage.java
    │       │       ├── EditEventPage.java
    │       │       └── ManageSeatsPage.java
    │       ├── steps/
    │       │   ├── EventsSteps.java
    │       │   ├── CheckoutSteps.java
    │       │   └── AdminSteps.java
    │       ├── stepdefinitions/
    │       │   ├── EventsStepDefinitions.java
    │       │   ├── CheckoutStepDefinitions.java
    │       │   └── AdminStepDefinitions.java
    │       ├── runners/
    │       │   └── RunCucumberTest.java
    │       └── utils/
    │           └── TestUtils.java
    └── resources/
        ├── features/
        │   ├── events.feature
        │   ├── checkout.feature
        │   └── admin.feature
        └── serenity.conf
```

### Arquitectura Serenity BDD

#### BasePage
| Clase | Archivo | Métodos Principales | Descripción |
|-------|---------|-------------------|-------------|
| `BasePage` | `pages/BasePage.java` | `waitForElement()`, `clickElement()`, `getText()`, `isElementVisible()` | Clase base con utilidades comunes para todos los Page Objects |

#### Páginas Públicas
| Page Object | Archivo | Elementos Principales | Métodos Principales |
|------------|---------|---------------------|-------------------|
| `EventsPage` | `pages/public/EventsPage.java` | `eventCards`, `pageTitle` | `getEventCards()`, `clickEventById()` |
| `EventDetailPage` | `pages/public/EventDetailPage.java` | `seatmapContainer`, `availableSeats`, `cartSidebar` | `selectFirstAvailableSeat()`, `clickAddToCart()` |
| `CheckoutPage` | `pages/public/CheckoutPage.java` | `orderItems`, `totalAmount`, `checkoutButton` | `getTotal()`, `proceedCheckout()` |

#### Páginas Admin
| Page Object | Archivo | Elementos Principales | Métodos Principales |  
|------------|---------|---------------------|-------------------|
| `AdminLoginPage` | `pages/admin/AdminLoginPage.java` | `emailInput`, `passwordInput`, `submitButton` | `login()`, `getErrorMessage()` |
| `CreateEventPage` | `pages/admin/CreateEventPage.java` | `nameInput`, `descriptionInput`, `submitButton` | `fillEventForm()`, `submitForm()` |

#### Steps Layer
| Steps Class | Archivo | Métodos Principales | Descripción |
|------------|---------|-------------------|-------------|
| `EventsSteps` | `steps/EventsSteps.java` | `navigateToHome()`, `verifyEventsDisplayed()` | Orquesta acciones de PageObjects para escenarios de eventos |
| `CheckoutSteps` | `steps/CheckoutSteps.java` | `addSeatToCart()`, `proceedWithCheckout()` | Orquesta flujo de carrito y checkout |
| `AdminSteps` | `steps/AdminSteps.java` | `loginAsAdmin()`, `createNewEvent()` | Orquesta funcionalidades administrativas |

### Features BDD

#### events.feature - Funcionalidad Pública
```gherkin
Feature: Events Management
  
  Scenario: Visualizar lista de eventos
    Given el usuario navega a la página de inicio
    When la página carga correctamente
    Then debe mostrar la lista de eventos disponibles
    And cada evento debe mostrar nombre, descripción, fecha y venue

  Scenario: Ver detalle de evento con seatmap
    Given el usuario está en la página de eventos
    When hace clic en un evento
    Then debe navegar a la página de detalle del evento
    And debe mostrar el mapa de asientos
```

#### checkout.feature - Proceso de Compra
```gherkin
Feature: Checkout Process

  Scenario: Añadir asiento al carrito
    Given el usuario tiene un asiento seleccionado
    When hace clic en "Add to Cart" 
    Then el asiento debe aparecer en el carrito
    And el contador del carrito debe incrementarse
```

#### admin.feature - Funcionalidad Admin
```gherkin
Feature: Admin Management

  Scenario: Login de administrador
    Given el usuario navega a /admin/login
    When ingresa credenciales válidas
    Then debe autenticarse correctamente
    And debe redirigir al dashboard admin
```

### Configuración Técnica

#### build.gradle
```gradle
plugins {
    id 'java'
    id 'net.serenity-bdd.serenity-gradle-plugin' version '4.1.0'
}

dependencies {
    implementation 'net.serenity-bdd:serenity-core:4.1.0'
    implementation 'net.serenity-bdd:serenity-junit:4.1.0'
    implementation 'net.serenity-bdd:serenity-cucumber:4.1.0'
    implementation 'net.serenity-bdd:serenity-ensure:4.1.0'
    implementation 'net.serenity-bdd:serenity-screenplay-rest:4.1.0'
    implementation 'io.github.bonigarcia:webdrivermanager:5.7.0'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.1'
    testImplementation 'org.assertj:assertj-core:3.25.3'
}

test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
    systemProperties System.getProperties()
}

serenity {
    reports = ['single-page-html', 'json']
}
```

#### serenity.conf
```hocon
webdriver {
  driver = chrome
  autodownload = true
}

chrome {
  switches = "--no-sandbox,--disable-dev-shm-usage"
}

serenity {
  take.screenshots = FOR_FAILURES
  report.encoding = UTF-8
  verbose.steps = true
}
```

### Arquitectura y Dependencias
- **Framework base**: Serenity BDD 4.1.0
- **WebDriver**: Selenium con WebDriverManager
- **BDD**: Cucumber integration
- **Build tool**: Gradle
- **Target URL**: http://localhost:3000 (desarrollo)

#### Runner Configuration
```java
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.ticketing.stepdefinitions",
    plugin = {"json:target/destination/cucumber.json"}
)
public class RunCucumberTest {}
```

### Notas de Implementación
> Los Page Objects deben extender PageObject de Serenity y usar @DefaultUrl para páginas con ruta fija. BasePage debe contener métodos utilitarios comunes. Steps actúan como orquestadores entre StepDefinitions y PageObjects. No usar DriverFactory - Serenity maneja WebDriver automáticamente.

---

## 3. LISTA DE TAREAS

> Checklist accionable para todos los agentes. Marcar cada ítem (`[x]`) al completarlo.
> El Orchestrator monitorea este checklist para determinar el progreso.

### Testing Framework

#### Configuración Base del Proyecto
- [ ] Crear estructura de directorios src/test/java (NO src/main/java)
- [ ] Configurar build.gradle con plugin Serenity y dependencias completas
- [ ] Crear serenity.conf con configuración WebDriver y timeouts
- [ ] Implementar TestUtils para utilidades generales

#### Framework Base
- [ ] Implementar BasePage con métodos utilitarios (waitForElement, clickElement, getText)
- [ ] Configurar PageObject base extendiendo de Serenity PageObject
- [ ] Definir constantes de timeout y URLs en configuración
- [ ] Establecer patrones de naming y estructuras base

#### Page Objects - Páginas Públicas
- [ ] Implementar EventsPage extendiendo BasePage con @DefaultUrl("/")
- [ ] Implementar EventDetailPage con elementos seatmap y cart
- [ ] Implementar CheckoutPage con order items y checkout button
- [ ] Añadir @FindBy elements y métodos de interacción básicos

#### Page Objects - Páginas Admin  
- [ ] Implementar AdminLoginPage con @DefaultUrl("/admin/login")
- [ ] Implementar AdminDashboardPage básico
- [ ] Implementar CreateEventPage con formulario completo
- [ ] Implementar EditEventPage y ManageSeatsPage
- [ ] Validar todos los Page Objects extienden BasePage

#### Steps Layer (Orquestadores)
- [ ] Implementar EventsSteps orquestando EventsPage y EventDetailPage
- [ ] Implementar CheckoutSteps orquestando CheckoutPage
- [ ] Implementar AdminSteps orquestando páginas admin
- [ ] Añadir @Step annotations para reporting detallado

#### Features BDD
- [ ] Crear events.feature con escenarios happy path y negativos
- [ ] Crear checkout.feature con flujo completo de compra
- [ ] Crear admin.feature con login y gestión eventos
- [ ] Validar sintaxis Gherkin y organización por funcionalidad

#### Step Definitions
- [ ] Implementar EventsStepDefinitions usando EventsSteps
- [ ] Implementar CheckoutStepDefinitions usando CheckoutSteps
- [ ] Implementar AdminStepDefinitions usando AdminSteps
- [ ] Configurar @ThucydidesSteps injection para Steps

#### Runner y Ejecución
- [ ] Crear RunCucumberTest con @RunWith(CucumberWithSerenity.class)
- [ ] Configurar @CucumberOptions con features y glue paths
- [ ] Validar ejecución con ./gradlew clean test aggregate
- [ ] Verificar generación de reportes en target/site/serenity/

#### Documentación
- [ ] Crear README.md con arquitectura Serenity explicada
- [ ] Documentar comandos de ejecución y ubicación de reportes
- [ ] Incluir ejemplos de Page Object → Steps → StepDefinitions
- [ ] Añadir guía de troubleshooting y mejores prácticas