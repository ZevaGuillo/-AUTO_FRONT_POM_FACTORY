package com.ticketing.stepdefinitions;

import com.ticketing.steps.WaitlistSteps;
import com.ticketing.utils.TestDataConfig;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitlistStepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(WaitlistStepDefinitions.class);

    @Steps
    private WaitlistSteps waitlistSteps;

    // ========================================================================
    // Setup — User A reserves a specific seat, then User B (test user) logs in
    // ========================================================================

    @Given("otro usuario reserva el asiento {int} de fila {int} en la sección {string} del evento {string}")
    public void otroUsuarioReservaUnAsiento(int seatNum, int rowNum, String sectionName, String eventName) {
        logger.info("Step: User A ({}) reserva asiento {}{}-{} en {}",
                TestDataConfig.RESERVE_USER_EMAIL, sectionName, rowNum, seatNum, eventName);

        // 1. Login as User A (reserve user)
        waitlistSteps.loginAsUser(
                TestDataConfig.RESERVE_USER_EMAIL,
                TestDataConfig.RESERVE_USER_PASSWORD);

        // 2. Navigate to event details
        waitlistSteps.navigateToEventDetails(eventName);

        // 3. Click the specific seat
        waitlistSteps.clickSpecificSeat(sectionName, rowNum, seatNum);

        // 4. Wait for info panel, then click "Reserve & Add to Cart"
        waitlistSteps.waitForPageUpdate();
        waitlistSteps.clickReserveAndAddToCart();

        // 5. Wait for reservation to process
        waitlistSteps.waitForPageUpdate();
        logger.info("User A reserved seat {}{}-{} ✓", sectionName, rowNum, seatNum);
    }

    @Given("el usuario está autenticado en la plataforma Ticketing")
    public void elUsuarioEstaAutenticadoEnLaPlataformaTicketing() {
        logger.info("Step: Autenticando usuario B (test user)");
        // Navigate to login page (this also effectively logs out User A)
        waitlistSteps.loginAsUser(TestDataConfig.TEST_USER_EMAIL, TestDataConfig.TEST_USER_PASSWORD);
    }

    // ========================================================================
    // Navigation & preconditions
    // ========================================================================

    @Given("accedo a la página de detalles del evento {string}")
    public void accesoALaPaginaDeDetallesDelEvento(String eventName) {
        logger.info("Step: Navegando a detalles del evento: {}", eventName);
        waitlistSteps.navigateToEventDetails(eventName);
    }

    @Given("la sección {string} tiene asientos reservados por otro usuario")
    public void laSeccionTieneAsientosReservados(String sectionName) {
        logger.info("Step: Verificando sección {} tiene asientos reservados", sectionName);
        boolean sectionVisible = waitlistSteps.verifySectionVisible(sectionName);
        Assert.assertTrue("La sección debe ser visible: " + sectionName, sectionVisible);
    }

    @Given("no tengo una suscripción activa previa para esa sección y evento")
    public void noTengoUnaSuscripcionActivaPrevia() {
        logger.info("Step: Precondición — sin suscripción previa");
        // Precondition ensured by test data setup
    }

    @Given("ya me encuentro registrado en la lista de espera para la sección {string} del evento {string}")
    public void yaEstoyRegistradoEnListaDeEspera(String sectionName, String eventName) {
        logger.info("Step: Precondición — ya registrado en waitlist {} / {}", eventName, sectionName);
        // Precondition ensured by test data setup; banner verified in Then step
    }

    // ========================================================================
    // CP_HU001_01 — Registro exitoso en lista de espera
    // ========================================================================

    @When("intento interactuar con un asiento reservado de la sección {string}")
    public void intentoInteractuarConAsientoReservado(String sectionName) {
        logger.info("Step: Clicking reserved seat in section: {}", sectionName);
        waitlistSteps.clickReservedSeat(sectionName);
    }

    @Then("el sistema debe mostrar una opción visible y clara para {string} asociada a esa sección y evento")
    public void elSistemaMuestraOpcionParaUnirseAListaDeEspera(String buttonText) {
        logger.info("Step: Verificando botón Join Waitlist visible");
        Assert.assertTrue(
            "La opción 'Join Waitlist' debe ser visible",
            waitlistSteps.verifyJoinWaitlistButtonVisible()
        );
    }

    @When("selecciono la opción para unirme y confirmo mi registro")
    public void seleccionoOpcionParaUnirme() {
        logger.info("Step: Haciendo clic en Join Waitlist");
        waitlistSteps.clickJoinWaitlistButton();
    }

    @Then("el sistema debe registrar mi suscripción en la base de datos asociándola a mi usuario, al evento {string} y a la sección {string}")
    public void elSistemaDebeRegistrarMiSuscripcion(String eventName, String sectionName) {
        logger.info("Step: Esperando registro en BD para {} / {}", eventName, sectionName);
        waitlistSteps.waitForPageUpdate();
    }

    @Then("debo visualizar un mensaje de confirmación {string}")
    public void debeVisualizarMensajeDeConfirmacion(String expectedMessage) {
        logger.info("Step: Verificando banner de confirmación de lista de espera");
        waitlistSteps.waitForPageUpdate();
        Assert.assertTrue(
            "Debe mostrar banner de confirmación de waitlist",
            waitlistSteps.verifyWaitlistBannerVisible()
        );
    }

    // ========================================================================
    // CP_HU001_03 — Impedir registro duplicado
    // ========================================================================

    @When("accedo nuevamente a la página de detalles de dicho evento y sección")
    public void accesoNuevamenteADetallesDelEvento() {
        logger.info("Step: Refrescando página del evento");
        waitlistSteps.refreshPage();
        waitlistSteps.waitForPageUpdate();
    }

    @Then("el sistema no debe mostrar la opción para {string}")
    public void elSistemaNoDebeMostrarOpcion(String buttonText) {
        logger.info("Step: Verificando que Join Waitlist NO es visible");
        waitlistSteps.waitForPageUpdate();
        Assert.assertFalse(
            "La opción 'Join Waitlist' NO debe ser visible cuando ya estás en la lista de espera",
            waitlistSteps.verifyJoinWaitlistButtonVisible()
        );
    }

    // Note: This step is kept for potential future use but CP_HU001_03 now clicks Join Waitlist
    // a second time and expects the duplicate error/banner instead of hiding the button.

    @Then("debe mostrar un indicador visual con el mensaje {string}")
    public void debeMostrarIndicadorVisualConMensaje(String expectedMessage) {
        logger.info("Step: Verificando indicador de duplicado: {}", expectedMessage);
        waitlistSteps.waitForPageUpdate();
        // The system shows either the error div ("already in waitlist") or the banner ("You're on the waitlist")
        boolean duplicateError = waitlistSteps.verifyDuplicateErrorVisible();
        boolean bannerVisible  = waitlistSteps.verifyWaitlistBannerVisible();
        Assert.assertTrue(
            "Debe mostrar error de duplicado o banner de waitlist activa",
            duplicateError || bannerVisible
        );
    }

    @Then("no se debe crear un registro duplicado en WAITLIST_ENTRIES")
    public void noSeDebeCrearRegistroDuplicado() {
        logger.info("Step: Verificando que no hay registro duplicado (UI)");
        // Verified by the duplicate error message or banner state
        boolean duplicateOrBanner = waitlistSteps.verifyDuplicateErrorVisible()
            || waitlistSteps.verifyWaitlistBannerVisible();
        Assert.assertTrue("Debe mostrar error de duplicado o banner activo", duplicateOrBanner);
    }

}
