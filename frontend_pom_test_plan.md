# Plan de Automatización Frontend - POM + Page Factory

## 1. Arquitectura Frontend

### Stack Tecnológico
- **Framework**: Next.js 14 (App Router)
- **Lenguaje**: TypeScript
- **UI Components**: shadcn/ui
- **Testing**: Serenity BDD con WebDriverManager
- **Patrón**: Page Object Model (POM) + Page Factory

### Estructura de Rutas
```
/                           - Home (Lista de eventos)
/events/[eventId]          - Detalle de evento con seatmap
/checkout                  - Carrito y checkout
/admin/login               - Login de administrador
/admin/dashboard           - Dashboard admin
/admin/events              - Gestión de eventos
/admin/events/create       - Crear nuevo evento
/admin/events/[eventId]/edit    - Editar evento
/admin/events/[eventId]/seats   - Gestión de asientos
```

---

## 2. Páginas Detectadas

### 2.1 Páginas Públicas

| Página | Ruta | Descripción |
|--------|------|-------------|
| EventsPage | `/` | Lista de eventos disponibles |
| EventDetailPage | `/events/[eventId]` | Detalle con selección de asientos |
| CheckoutPage | `/checkout` | Carrito y proceso de pago |

### 2.2 Páginas de Administración

| Página | Ruta | Descripción |
|--------|------|-------------|
| AdminLoginPage | `/admin/login` | Formulario de autenticación admin |
| AdminDashboardPage | `/admin/dashboard` | Panel principal admin |
| AdminEventsPage | `/admin/events` | Listado de eventos admin |
| CreateEventPage | `/admin/events/create` | Formulario de creación de eventos |
| EditEventPage | `/admin/events/[eventId]/edit` | Formulario de edición |
| ManageSeatsPage | `/admin/events/[eventId]/seats` | Generación de asientos |

---

## 3. Elementos UI Relevantes

### 3.1 Componentes Reutilizables

| Componente | Descripción | Locators sugeridos |
|------------|-------------|-------------------|
| Navbar | Navegación principal | `header nav`, `.navbar` |
| EventCard | Card de evento en lista | `.event-card`, `[data-event-id]` |
| SeatMap | Mapa visual de asientos | `.seatmap`, `[data-seatmap]` |
| SeatButton | Asiento individual | `.seat-button`, `[data-seat-id]` |
| CartSidebar | Carrito lateral | `.cart-sidebar`, `#cart` |
| Button | Botones (variantes: primary, ghost, destructive) | `button`, `.btn-*` |
| Input | Campos de formulario | `input[type="text"]`, `input[type="email"]` |
| Card | Contenedores de contenido | `.card`, `.card-content` |

### 3.2 Elementos por Página

#### EventsPage (`/`)
- **Elementos**:
  - `h1` - Título "Events"
  - `.event-card` - Lista de cards de eventos
  - `.event-card h3` - Nombre del evento
  - `.event-card p` - Descripción
  - `.event-card [data-event-date]` - Fecha
  - `.event-card [data-event-venue]` - Venue
  - `.event-card a` - Link a detalle

#### EventDetailPage (`/events/[eventId]`)
- **Elementos**:
  - `[data-event-name]` - Nombre del evento
  - `[data-event-description]` - Descripción
  - `[data-event-date]` - Fecha
  - `[data-event-venue]` - Lugar
  - `.seatmap` - Contenedor del mapa de asientos
  - `.seat-button.available` - Asientos disponibles
  - `.seat-button.reserved` - Asientos reservados
  - `.seat-button.selected` - Asientos seleccionados
  - `[data-price]` - Precio del asiento
  - Botón "Add to Cart"
  - Carrito lateral

#### CheckoutPage (`/checkout`)
- **Elementos**:
  - `[data-order-items]` - Lista de items
  - `[data-total-amount]` - Total
  - `[data-checkout-button]` - Botón de checkout
  - `[data-payment-form]` - Formulario de pago

#### AdminLoginPage (`/admin/login`)
- **Elementos**:
  - `#email` - Campo email
  - `#password` - Campo contraseña
  - `button[type="submit"]` - Botón login
  - `[data-show-password]` - Toggle mostrar contraseña
  - `[data-error-message]` - Mensaje de error
  - `[data-auto-fill]` - Auto-completar credenciales

#### CreateEventPage (`/admin/events/create`)
- **Elementos**:
  - `#name` - Nombre del evento
  - `#description` - Descripción
  - `#eventDate` - Fecha del evento
  - `#venue` - Lugar
  - `#maxCapacity` - Capacidad máxima
  - `#basePrice` - Precio base
  - `button[type="submit"]` - Crear evento
  - `[data-error-message]` - Error

---

## 4. Casos de Prueba POM

### 4.1 Casos Positivos

#### TC-POM-001: Visualizar lista de eventos
```gherkin
Given el usuario navega a la página de inicio
When la página carga correctamente
Then debe mostrar la lista de eventos disponibles
And cada evento debe mostrar nombre, descripción, fecha y venue
```

#### TC-POM-002: Ver detalle de evento con seatmap
```gherkin
Given el usuario está en la página de eventos
When hace clic en un evento
Then debe navegar a la página de detalle del evento
And debe mostrar el mapa de asientos
```

#### TC-POM-003: Seleccionar asiento disponible
```gherkin
Given el usuario está en la página de detalle de un evento
When hace clic en un asiento disponible
Then el asiento debe cambiar a estado "selected"
And el precio debe mostrarse en el carrito
```

#### TC-POM-004: Añadir asiento al carrito
```gherkin
Given el usuario tiene un asiento seleccionado
When hace clic en "Add to Cart"
Then el asiento debe aparecer en el carrito
And el contador del carrito debe incrementarse
```

#### TC-POM-005: Checkout exitosos
```gherkin
Given el usuario tiene items en el carrito
When completa el checkout
Then el sistema debe procesar el pago
And debe mostrar confirmación de la compra
```

#### TC-POM-006: Login de administrador
```gherkin
Given el usuario navega a /admin/login
When ingresa credenciales válidas
Then debe autenticarse correctamente
And debe redirigir al dashboard admin
```

#### TC-POM-007: Crear evento como admin
```gherkin
Given el usuario está autenticado como admin
And navega a /admin/events/create
When completa el formulario con datos válidos
Then el evento debe crearse exitosamente
```

---

### 4.2 Casos Negativos

#### TC-POM-008: Login con credenciales inválidas
```gherkin
Given el usuario navega a /admin/login
When ingresa email o contraseña incorrectos
Then debe mostrar mensaje de error
And debe mantenerse en la página de login
```

#### TC-POM-009: Intentar seleccionar asiento reservado
```gherkin
Given el usuario está en la página de detalle
When intenta hacer clic en un asiento reservado
Then no debe permitir selección
And debe mostrar indicador visual de reservado
```

#### TC-POM-010: Checkout con carrito vacío
```gherkin
Given el usuario está en /checkout
And el carrito está vacío
When intenta hacer checkout
Then debe mostrar mensaje de error
And no debe permitir procesar
```

#### TC-POM-011: Crear evento con datos inválidos
```gherkin
Given el usuario está en el formulario de creación
When deja campos requeridos vacíos
Then debe mostrar mensajes de validación
And no debe enviar el formulario
```

#### TC-POM-012: Navegación sin autenticación a página admin
```gherkin
Given el usuario no está autenticado
When intenta acceder a /admin/dashboard
Then debe redirigir a /admin/login
```

---

## 5. Page Objects Sugeridos

### 5.1 Page Objects Públicos

```java
// src/test/java/com/ticketing/pages/EventsPage.java
public class EventsPage {
    @FindBy(css = ".event-card")
    private List<WebElement> eventCards;
    
    @FindBy(css = "h1")
    private WebElement pageTitle;
    
    public List<WebElement> getEventCards() { return eventCards; }
    public void clickEventById(String eventId) { ... }
}

// src/test/java/com/ticketing/pages/EventDetailPage.java
public class EventDetailPage {
    @FindBy(css = ".seatmap")
    private WebElement seatmapContainer;
    
    @FindBy(css = ".seat-button.available")
    private List<WebElement> availableSeats;
    
    @FindBy(css = ".seat-button.selected")
    private List<WebElement> selectedSeats;
    
    @FindBy(css = "[data-add-to-cart]")
    private WebElement addToCartButton;
    
    @FindBy(css = ".cart-sidebar")
    private WebElement cartSidebar;
}

// src/test/java/com/ticketing/pages/CheckoutPage.java
public class CheckoutPage {
    @FindBy(css = "[data-order-items]")
    private WebElement orderItems;
    
    @FindBy(css = "[data-total-amount]")
    private WebElement totalAmount;
    
    @FindBy(css = "[data-checkout-button]")
    private WebElement checkoutButton;
}
```

### 5.2 Page Objects Admin

```java
// src/test/java/com/ticketing/pages/admin/AdminLoginPage.java
public class AdminLoginPage {
    @FindBy(id = "email")
    private WebElement emailInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;
    
    @FindBy(css = "[data-error-message]")
    private WebElement errorMessage;
}

// src/test/java/com/ticketing/pages/admin/CreateEventPage.java
public class CreateEventPage {
    @FindBy(id = "name")
    private WebElement nameInput;
    
    @FindBy(id = "description")
    private WebElement descriptionInput;
    
    @FindBy(id = "eventDate")
    private WebElement eventDateInput;
    
    @FindBy(id = "venue")
    private WebElement venueInput;
    
    @FindBy(id = "maxCapacity")
    private WebElement maxCapacityInput;
    
    @FindBy(id = "basePrice")
    private WebElement basePriceInput;
    
    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;
}
```

---

## 6. Mapeo hacia Serenity BDD

### 6.1 Dependencias Maven Sugeridas

```xml
<dependencies>
    <!-- Serenity Core -->
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-core</artifactId>
        <version>4.1.0</version>
    </dependency>
    <!-- Serenity JUnit -->
    <dependency>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-junit</artifactId>
        <version>4.1.0</version>
    </dependency>
    <!-- WebDriver Manager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.7.0</version>
    </dependency>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
    </dependency>
</dependencies>
```

### 6.2 Configuración Sereníty

```properties
# serenity.properties
webdriver.driver=chrome
webdriver.autodownload=true
chrome.switches=--no-sandbox,--disable-dev-shm-usage
serenity.take.screenshots=AFTER_EACH_STEP
serenity.report.encoding=UTF-8
serenity.verbose.steps=true
```

### 6.3 Estructura de Proyecto Recomendada

```
src/
├── main/
│   └── java/
│       └── com/ticketing/
│           ├── pages/
│           │   ├── EventsPage.java
│           │   ├── EventDetailPage.java
│           │   ├── CheckoutPage.java
│           │   └── admin/
│           │       ├── AdminLoginPage.java
│           │       ├── AdminDashboardPage.java
│           │       └── CreateEventPage.java
│           └── utils/
│               └── DriverFactory.java
└── test/
    ├── java/
    │   └── com/ticketing/
    │       └── definitions/
    │           ├── EventsStepDefinitions.java
    │           ├── CheckoutStepDefinitions.java
    │           └── AdminStepDefinitions.java
    └── resources/
        ├── features/
        │   ├── events.feature
        │   ├── checkout.feature
        │   └── admin.feature
        └── serenity.conf
```

### 6.4 Ejemplo de Test

```java
@ExtendWith(SerenityJUnitExtension.class)
public class EventsPOMTest {

    EventsPage eventsPage;
    EventDetailPage eventDetailPage;

    @Test
    public void verify_events_list_displayed() {
        openAt("http://localhost:3000");
        
        eventsPage.verifyPageTitle("Events");
        eventsPage.verifyEventsListNotEmpty();
    }

    @Test
    public void select_seat_and_add_to_cart() {
        openAt("http://localhost:3000/events/123");
        
        eventDetailPage.selectFirstAvailableSeat();
        eventDetailPage.clickAddToCart();
        
        eventDetailPage.verifySeatInCart();
    }
}
```

---

## 7. Consideraciones Adicionales

### 7.1 Wait Strategies
- Usar `等待` explícitos para elementos dinámicos
- Configurar timeouts en `serenity.conf`
- Esperar por visibilidad de elementos antes de interactuar

### 7.2 Manejo de iFrames
- Si hay iframes, usar `switchTo().frame()`

### 7.3 Data Driven Testing
- Usar `@CsvFileSource` para datos externos
- Integrar con Excel o JSON si es necesario
