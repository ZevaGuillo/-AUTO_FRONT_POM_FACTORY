# language: en
Feature: Checkout Process
  As a customer of the ticketing system
  I want to complete the purchase of my selected tickets
  So that I can secure my seats for the event

  Background:
    Given the ticketing application is available
    And the user has items in their cart

  @smoke @positive
  Scenario: Checkout exitoso con carrito válido
    Given el usuario tiene items en el carrito
    When completa el checkout con información válida
    Then el sistema debe procesar el pago correctamente
    And debe mostrar confirmación de la compra
    And debe mostrar el número de confirmación

  @negative @payment
  Scenario: Pago con tarjeta inválida
    Given el usuario tiene items en el carrito
    And está en la página de checkout
    When intenta pagar con tarjeta inválida
    | cardNumber       | expiryDate | cvv | cardholderName |
    | 0000000000000000 | 01/20      | 000 | Invalid User   |
    Then debe mostrar error de tarjeta inválida
    And no debe procesar el pago
    And debe permanecer en la página de checkout

  @negative
  Scenario: Checkout con carrito vacío
    Given el usuario está en la página de checkout
    And el carrito está vacío
    When intenta hacer checkout
    Then debe mostrar mensaje de carrito vacío
    And no debe permitir procesar el pago
    And debe sugerir agregar items al carrito