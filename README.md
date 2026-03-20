# Framework de Automatización Frontend con Serenity BDD

## 📋 Descripción del Proyecto

Este proyecto implementa un **framework de automatización de pruebas end-to-end** para el sistema de ticketing frontend. Fue diseñado para probar aplicaciones web desarrolladas en **Next.js 14** con **TypeScript** y componentes **shadcn/ui**.

El framework utiliza metodologías de **Behavior-Driven Development (BDD)** permitiendo que las pruebas sean escritas en lenguaje natural (Gherkin) facilitando la comunicación entre equipos técnicos y de negocio.

### Objetivos del Proyecto

- Proporcionar una suite de pruebas automatizadas robusta y mantenible
- Cubrir los flujos principales del sistema de ticketing
- Generar reportes detallados y entendibles para stakeholders
- Facilitar la integración con pipelines de CI/CD

### Alcance

El framework cubre tres módulos principales:
1. **Módulo Público**: Visualización de eventos, selección de asientos, checkout
2. **Módulo Administrativo**: Gestión de eventos, asientos, métricas
3. **Módulo de Validación**: Seguridad, performance, accesibilidad

---

## 🏗️ Arquitectura

### Patrón Implementado: Page Object Model + Page Factory

```
src/test/java/com/ticketing/
├── pages/                 # Page Objects con @FindBy annotations
│   ├── BasePage.java      # Clase base con utilidades comunes
│   ├── public/            # Páginas públicas del usuario
│   │   ├── EventsPage.java
│   │   ├── EventDetailPage.java
│   │   └── CheckoutPage.java
│   └── admin/             # Páginas administrativas
│       ├── AdminLoginPage.java
│       ├── AdminDashboardPage.java
│       ├── CreateEventPage.java
│       ├── EditEventPage.java
│       └── ManageSeatsPage.java
├── steps/                 # Capa de orquestación con @Step
│   ├── EventsSteps.java
│   ├── CheckoutSteps.java
│   └── AdminSteps.java
├── stepdefinitions/       # Mapeo Gherkin → Steps
│   ├── EventsStepDefinitions.java
│   ├── CheckoutStepDefinitions.java
│   └── AdminStepDefinitions.java
├── utils/                 # Utilidades comunes
│   └── TestUtils.java
└── runners/               # Cucumber Runners
    └── RunCucumberTest.java

src/test/resources/
└── features/              # Features Gherkin con escenarios
    ├── events.feature     # 12 escenarios de eventos
    ├── checkout.feature   # 12 escenarios de checkout
    └── admin.feature      # 15 escenarios administrativos
```

### Flujo de Datos

```
Feature (Gherkin) → StepDefinitions → Steps (@Step) → Page Objects → WebDriver
```

## 🛠️ Stack Tecnológico

| Componente | Versión | Propósito |
|-----------|---------|-----------|
| **Serenity BDD** | 4.1.0 | Framework principal de BDD |
| **Gradle** | 8.5 | Herramienta de build |
| **Java** | 11 | Lenguaje base |
| **Selenium WebDriver** | 4.x | Automatización web |
| **Cucumber** | 7.18.0 | Sintaxis Gherkin |
| **JUnit 5** | 5.10.x | Framework de testing |
| **WebDriverManager** | 5.7.0 | Gestión automática de drivers |

## 🚀 Configuración Inicial

### Prerrequisitos

| Requisito | Versión Mínima | Verificación |
|-----------|----------------|--------------|
| **Java JDK** | 11 | `java -version` |
| **Gradle** | 8.5 | `./gradlew --version` |
| **Google Chrome** | última | `google-chrome --version` |
| **Git** | 2.x | `git --version` |

> **Nota**: Se recomienda usar JDK 17 para mejor rendimiento y compatibilidad.

### Instalación

1. **Clonar el repositorio:**
```bash
git clone <repository-url>
cd AUTO_FRONT_POM_FACTORY
```

2. **Verificar Java:**
```bash
java -version
# Debe mostrar Java 11 o superior
```

3. **Verificar Gradle:**
```bash
./gradlew --version
```

4. **Descargar dependencias:**
```bash
./gradlew build --refresh-dependencies
```

### Configuración del Entorno

#### Variables de Entorno Requeridas

```bash
# URL base de la aplicación (requerido)
export BASE_URL=http://localhost:3000

# Entorno de ejecución (development, staging, production)
export ENVIRONMENT=development

# Configuración opcional de navegador
export BROWSER=chrome
export HEADLESS=false
export TIMEOUT=30
```

#### Configuración en serenity.conf

El archivo de configuración principal se encuentra en `src/test/resources/serenity.conf`:

```hocon
webdriver {
  driver = chrome
  baseurl = "http://localhost:3000"
  chrome {
    driver = webdriver.chrome.driver
    switches = "--start-maximized,--disable-extensions"
  }
}

serenity {
  browser.width = 1920
  browser.height = 1080
  take.screenshots = FOR_FAILURES
  logging = VERBOSE
  restapi.timeout = 5000
}

cucumber {
  filter.tags = "@smoke"
}
```

### Verificación de Configuración

```bash
# Verificar que las dependencias se resuelvan correctamente
./gradlew dependencies --configuration testRuntimeClasspath

# Verificar la estructura del proyecto
./gradlew tasks --all | grep -E "(test|build|run)"
```

### Ejecución de Prueba Inicial

```bash
# Ejecutar un test de smoke para verificar que todo funcione
./gradlew test -Dcucumber.options="--tags @smoke"
```

## 📋 Ejecutar Pruebas

### Preparación Previa

1. **Iniciar la aplicación** (si no está corriendo):
```bash
# En el proyecto frontend
cd ../tu-proyecto-frontend
npm run dev
# La app debe estar corriendo en http://localhost:3000
```

2. **Verificar conexión**:
```bash
curl http://localhost:3000
```

### Comandos Básicos

```bash
# Ejecutar todas las pruebas
./gradlew test

# Ejecutar y generar reportes Serenity
./gradlew test aggregate
```

### Ejecución por Tags

```bash
# Solo pruebas críticas
./gradlew test -Dcucumber.options="--tags @smoke"

# Pruebas positivas
./gradlew test -Dcucumber.options="--tags @positive"

# Pruebas negativas
./gradlew test -Dcucumber.options="--tags @negative"

# Pruebas de seguridad
./gradlew test -Dcucumber.options="--tags @security"

# Combinación de tags
./gradlew test -Dcucumber.options="--tags '@smoke and @positive'"
```

### Ejecución por Feature

```bash
# Solo eventos
./gradlew test -Dcucumber.options="src/test/resources/features/events.feature"

# Solo checkout
./gradlew test -Dcucumber.options="src/test/resources/features/checkout.feature"

# Solo administración
./gradlew test -Dcucumber.options="src/test/resources/features/admin.feature"
```

### Modos de Ejecución

```bash
# Modo verbose (más información en consola)
./gradlew test --info

# Modo debug (para debugging con breakpoints)
./gradlew test --debug

# Ejecución paralela (múltiples feature files)
./gradlew test -Pparallel=true
```

### Parametrización de Tests

```bash
# Ejecutar con datos específicos
./gradlew test -DtestEnvironment=staging

# Ejecutar con retry automático
./gradlew test -Dserenity.restart.browser.for.each.scenario=true

# Cambiar timeout de elementos
./gradlew test -Dwebdriver.timeouts.implicit.wait=5000
```

## 🌐 Configuración de Navegadores

### Chrome (Default)
```bash
# Headless
./gradlew test -Dwebdriver.chrome.driver.headless=true

# Con ventana visible
./gradlew test -Dwebdriver.chrome.driver.headless=false

# Tamaño específico
./gradlew test -Dwebdriver.chrome.driver.window.size=1920x1080
```

### Firefox
```bash
./gradlew test -Dwebdriver.driver=firefox
```

### Edge
```bash
./gradlew test -Dwebdriver.driver=edge
```

## 📊 Reportes

### Ubicaciones de Reportes

| Tipo | Ubicación | Descripción |
|------|-----------|-------------|
| **Serenity HTML** | `target/site/serenity/index.html` | Reporte principal con detalles |
| **Cucumber JSON** | `target/cucumber-reports/Cucumber.json` | Datos en formato JSON |
| **JUnit XML** | `target/cucumber-reports/Cucumber.xml` | Compatible con CI/CD |
| **Screenshots** | `target/site/serenity/screenshots/` | Capturas automáticas |

### Generar Reportes

```bash
# Ejecutar pruebas y generar reportes
./gradlew clean test aggregate

# Solo generar reportes (si ya corrieron las pruebas)
./gradlew aggregate
```

### Abrir Reportes

```bash
# Linux/Mac
open target/site/serenity/index.html

# Windows
start target/site/serenity/index.html
```

## 🧪 Cobertura de Pruebas

### Funcionalidades Cubiertas

#### Módulo Eventos (12 escenarios)
- ✅ Carga y visualización de eventos
- ✅ Navegación a detalles de evento
- ✅ Visualización de mapa de asientos
- ✅ Selección de asientos disponibles
- ✅ Agregado al carrito
- ✅ Validación de asientos reservados
- ✅ Performance y accesibilidad

#### Módulo Checkout (12 escenarios)
- ✅ Navegación al checkout
- ✅ Formularios de información personal y pago
- ✅ Validación de campos requeridos
- ✅ Procesamiento de pagos
- ✅ Confirmación de compra
- ✅ Manejo de errores de validación
- ✅ Cálculo correcto de totales

#### Módulo Administración (15 escenarios)
- ✅ Autenticación administrativa
- ✅ Dashboard con métricas
- ✅ Creación de eventos
- ✅ Gestión y edición de eventos
- ✅ Generación automática de asientos
- ✅ Validación de formularios
- ✅ Protección de rutas privadas

### Tags de Organización

| Tag | Propósito | Escenarios |
|-----|-----------|------------|
| `@smoke` | Funcionalidades críticas | 22 |
| `@positive` | Casos de éxito esperados | 25 |
| `@negative` | Casos de error y validación | 14 |
| `@security` | Validaciones de seguridad | 4 |

Total: **39 escenarios** cubriendo flujos completos end-to-end.

## 🔧 Configuración Avanzada

### serenity.conf

```hocon
# Configuración en src/test/resources/serenity.conf
webdriver {
  driver = chrome
  chrome {
    driver = "/path/to/chromedriver"  # Opcional, WebDriverManager lo maneja
    switches = "--start-maximized,--disable-web-security"
  }
}

serenity {
  browser.width = 1920
  browser.height = 1080
  take.screenshots = FOR_FAILURES
  logging = VERBOSE
}
```

### Variables de Entorno

```bash
# Base URL de la aplicación
export SERENITY_WEBDRIVER_BASE_URL=http://localhost:3000

# Driver específico
export SERENITY_WEBDRIVER_DRIVER=chrome

# Modo headless
export SERENITY_WEBDRIVER_CHROME_SWITCHES="--headless,--no-sandbox"
```

## 🏗️ Estructura Detallada

### BasePage.java
Clase base que proporciona:
- Estrategias de espera (`waitForElement`, `waitForClickable`)
- Interacciones comunes (`clickElement`, `getText`, `scrollTo`)
- Utilidades de verificación (`isElementVisible`, `isElementEnabled`)

### Page Objects
Implementan **Page Factory** con:
- `@FindBy` annotations para localización de elementos
- `WebElementFacade` para interacciones mejoradas
- `@DefaultUrl` para navegación automática
- Métodos de negocio específicos de cada página

### Steps Layer
Capa de orquestación con:
- `@Step` annotations para reportes detallados
- Inyección automática de Page Objects
- Lógica de flujos de negocio
- Logging y captura de evidencias

### Step Definitions
Mapean sintaxis Gherkin a métodos Java:
- Expresiones regulares para parámetros
- Validaciones con Assert
- Manejo de estados de prueba

## 🐛 Debugging y Troubleshooting

### Problemas Comunes

#### 1. Chrome no se encuentra
```bash
# Verificar instalación
google-chrome --version

# Configurar path manualmente en serenity.conf
webdriver.chrome.driver = "/usr/bin/google-chrome"
```

#### 2. Elemento no encontrado
```java
// Debug en Page Objects
@FindBy(css = "[data-testid='element']")
private WebElementFacade element;

// Verificar que el selector CSS sea correcto
// Usar estrategias de espera en BasePage
```

#### 3. Timeouts
```java
// Incrementar timeouts en BasePage
private static final int DEFAULT_TIMEOUT = 10;
private static final int EXTENDED_TIMEOUT = 30;
```

#### 4. Fallos de Step Definitions
```bash
# Ejecutar en modo dry-run para verificar mapeo
./gradlew test -Dcucumber.options="--dry-run"

# Verificar expresiones regulares en @Given, @When, @Then
```

### Logs y Debug

```bash
# Habilitar logs verbosos
./gradlew test -Dserenity.logging=VERBOSE

# Solo mostrar errores
./gradlew test -Dserenity.logging=QUIET

# Logs de Selenium
./gradlew test -Dselenium.logging=true
```

## 📈 Integración CI/CD

### Jenkins
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh './gradlew clean test aggregate'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site/serenity',
                        reportFiles: 'index.html',
                        reportName: 'Serenity Report'
                    ])
                }
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: E2E Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - name: Run Tests
        run: ./gradlew clean test aggregate
      - name: Upload Reports
        uses: actions/upload-artifact@v3
        with:
          name: serenity-reports
          path: target/site/serenity/
```

## 🔄 Mantenimiento

### Actualización de Dependencias
```bash
# Verificar versiones outdated
./gradlew dependencyUpdates

# Actualizar Gradle wrapper
./gradlew wrapper --gradle-version=8.6
```

### Agregar Nuevos Tests

1. **Crear nuevo Page Object:**
```java
@DefaultUrl("page:/new-page")
public class NewPage extends BasePage {
    @FindBy(css = "[data-testid='new-element']")
    private WebElementFacade newElement;
    
    public void interact() {
        clickElement(newElement);
    }
}
```

2. **Crear Steps correspondientes:**
```java
public class NewSteps {
    @Steps
    private NewPage newPage;
    
    @Step("Interact with new page")
    public void interactWithNewPage() {
        newPage.interact();
    }
}
```

3. **Crear Step Definitions:**
```java
@Steps
private NewSteps newSteps;

@When("user interacts with new page")
public void userInteractsWithNewPage() {
    newSteps.interactWithNewPage();
}
```

4. **Agregar Feature:**
```gherkin
Feature: New functionality
  Scenario: New test case
    Given user is on new page
    When user interacts with new page
    Then new result should be visible
```

## 📚 Referencias

- [Serenity BDD Documentation](http://serenity-bdd.info/)
- [Cucumber Documentation](https://cucumber.io/docs)
- [Selenium WebDriver Documentation](https://selenium.dev/)
- [Page Object Model Pattern](https://selenium.dev/documentation/test_practices/encouraged/page_object_models/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

## 👥 Contribución

Para contribuir al framework:

1. Fork el repositorio
2. Crear branch para feature: `git checkout -b feature/nueva-funcionalidad`
3. Seguir las convenciones de código establecidas
4. Agregar tests para nuevas funcionalidades
5. Verificar que todos los tests pasen: `./gradlew test`
6. Crear Pull Request con descripción detallada

---

**Desarrollado con ❤️ para automatización de pruebas frontend**
