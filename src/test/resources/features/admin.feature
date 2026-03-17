# language: en
Feature: Admin Management
  As an administrator of the ticketing system
  I want to manage events and system settings
  So that I can provide a good experience for customers

  Background:
    Given the ticketing system admin panel is available

  @smoke @positive @login
  Scenario: Login de administrador exitoso
    Given el usuario navega a la página de login de admin
    When ingresa credenciales válidas de administrador
    Then debe autenticarse correctamente
    And debe redirigir al dashboard de admin
    And debe mostrar el panel de navegación de admin

  @positive @events
  Scenario: Crear nuevo evento como administrador
    Given el administrador está autenticado
    And navega a la página de creación de eventos
    When completa el formulario con datos válidos del evento
    | name          | description      | eventDate  | venue           | maxCapacity | basePrice |
    | Test Concert  | Test Description | 2026-12-25 | Madison Square  | 1000        | 50        |
    Then el evento debe crearse exitosamente
    And debe mostrar confirmación de creación
    And debe aparecer en la lista de eventos

  @security @authorization
  Scenario: Acceso no autorizado a páginas de admin
    Given un usuario no está autenticado como administrador
    When intenta acceder directamente al dashboard de admin
    Then debe ser redirigido a la página de login
    And no debe tener acceso a funciones administrativas
    And debe requerir autenticación válida