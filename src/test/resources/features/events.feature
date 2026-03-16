# language: en
Feature: Events Management
  As a user of the ticketing system
  I want to view and interact with events
  So that I can find and select events I want to attend

  Background:
    Given the ticketing application is available

  @smoke @positive
  Scenario: Visualizar lista de eventos
    Given el usuario navega a la página de inicio
    When la página carga correctamente
    Then debe mostrar la lista de eventos disponibles
    And cada evento debe mostrar nombre, descripción, fecha y venue
    And la navegación principal debe estar visible

  @smoke @positive
  Scenario: Ver detalle de evento con seatmap
    Given el usuario está en la página de eventos
    When hace clic en un evento
    Then debe navegar a la página de detalle del evento
    And debe mostrar el mapa de asientos
    And debe mostrar los detalles del evento correctamente
    And debe mostrar la leyenda de asientos

  @positive @seat-selection
  Scenario: Seleccionar asiento disponible
    Given el usuario está en la página de detalle de un evento
    And hay asientos disponibles para seleccionar
    When hace clic en un asiento disponible
    Then el asiento debe cambiar a estado "selected"
    And debe mostrar el precio del asiento
    And el botón "Add to Cart" debe estar habilitado