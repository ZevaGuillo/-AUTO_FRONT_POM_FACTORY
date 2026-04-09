# language: en
Feature: Lista de Espera
  Como usuario autenticado de la plataforma Ticketing
  Quiero poder registrarme en la lista de espera de secciones agotadas
  Para ser notificado si se liberan asientos

  @smoke @positive @waitlist @alta
  Scenario: CP_HU001_01 — Registro exitoso en lista de espera y visualización de confirmación
    Given el usuario está autenticado en la plataforma Ticketing
    And existe un evento "Concierto Sinfónico" con la sección "General" totalmente reservada
    And no tengo una suscripción activa previa para esa sección y evento
    When accedo a la página de detalles del evento "Concierto Sinfónico"
    And hago clic en un asiento reservado de la sección "General"
    Then el sistema debe mostrar una opción visible y clara para "Unirse a la lista de espera" asociada a esa sección y evento
    When selecciono la opción para unirme y confirmo mi registro
    Then el sistema debe registrar mi suscripción en la base de datos asociándola a mi usuario, al evento "Concierto Sinfónico" y a la sección "General"
    And debo visualizar un mensaje de confirmación "¡Te has unido a la lista de espera! Te notificaremos si se liberan asientos."

  @negative @waitlist @alta
  Scenario: CP_HU001_03 — Impedir registro duplicado en la misma lista de espera
    Given el usuario está autenticado en la plataforma Ticketing
    And existe un evento "Concierto Sinfónico" con la sección "General" totalmente reservada
    And no tengo una suscripción activa previa para esa sección y evento
    When accedo a la página de detalles del evento "Concierto Sinfónico"
    And hago clic en un asiento reservado de la sección "General"
    And selecciono la opción para unirme y confirmo mi registro
    Then el botón de unirse a la lista de espera debe mostrar "On Waitlist"
    When accedo nuevamente a la página de detalles de dicho evento y sección
    And hago clic en un asiento reservado de la sección "General"
    Then el botón de unirse a la lista de espera debe mostrar "On Waitlist"
    And no se debe crear un registro duplicado en WAITLIST_ENTRIES
