# language: en
Feature: Lista de Espera
  Como usuario autenticado de la plataforma Ticketing
  Quiero poder registrarme en la lista de espera de secciones agotadas
  Para ser notificado si se liberan asientos

  Background:
    Given otro usuario reserva un asiento en la sección "General" del evento "Concierto Sinfónico"
    And el usuario está autenticado en la plataforma Ticketing

  @smoke @positive @waitlist @alta
  Scenario: CP_HU001_01 — Registro exitoso en lista de espera y visualización de confirmación
    Given accedo a la página de detalles del evento "Concierto Sinfónico"
    And la sección "General" tiene asientos reservados por otro usuario
    And no tengo una suscripción activa previa para esa sección y evento
    When intento interactuar con un asiento reservado de la sección "General"
    Then el sistema debe mostrar una opción visible y clara para "Unirse a la lista de espera" asociada a esa sección y evento
    When selecciono la opción para unirme y confirmo mi registro
    Then el sistema debe registrar mi suscripción en la base de datos asociándola a mi usuario, al evento "Concierto Sinfónico" y a la sección "General"
    And debo visualizar un mensaje de confirmación "¡Te has unido a la lista de espera! Te notificaremos si se liberan asientos."

  @negative @waitlist @alta
  Scenario: CP_HU001_03 — Impedir registro duplicado en la misma lista de espera
    Given accedo a la página de detalles del evento "Concierto Sinfónico"
    And la sección "General" tiene asientos reservados por otro usuario
    And ya me encuentro registrado en la lista de espera para la sección "General" del evento "Concierto Sinfónico"
    When accedo nuevamente a la página de detalles de dicho evento y sección
    Then el sistema no debe mostrar la opción para "Unirse a la lista de espera"
    And debe mostrar un indicador visual con el mensaje "Ya estás en la lista de espera"
    And no se debe crear un registro duplicado en WAITLIST_ENTRIES

  @positive @waitlist @cancelacion @alta
  Scenario: CP_HU003_02 — Proceso de cancelación exitoso y actualización de la interfaz
    Given accedo a la página de detalles del evento "Concierto Sinfónico"
    And ya me encuentro registrado en la lista de espera para la sección "General" del evento "Concierto Sinfónico"
    And he iniciado el proceso de cancelación y el modal de confirmación está visible
    When hago clic en el botón "Confirmar"
    Then el estado de mi suscripción en la base de datos debe cambiar a "cancelled"
    And debo visualizar una notificación toast con el mensaje "Has cancelado tu suscripción a la lista de espera."
    And el banner informativo de suscripción activa debe desaparecer de la página
    And el botón para "Unirme a la lista de espera" debe volver a estar visible y funcional
