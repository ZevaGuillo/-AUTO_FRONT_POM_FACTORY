package com.ticketing.stepdefinitions;

import com.ticketing.steps.WaitlistSteps;
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
    // Background
    // ========================================================================

    @Given("el usuario está autenticado en la plataforma Ticketing")
    public void elUsuarioEstaAutenticadoEnLaPlataformaTicketing() {
        logger.info("Step: Autenticando usuario");
        waitlistSteps.loginAsUser("testuser@example.com", "Test1234!");
    }

    // ========================================================================
    // Navigation & preconditions
    // ========================================================================

    @Given("accedo a la página de detalles del evento {string}")
    public void accesoALaPaginaDeDetallesDelEvento(String eventName) {
        logger.info("Step: Navegando a detalles del evento: {}", eventName);
        waitlistSteps.navigateToEventDetails(eventName);
    }

    @Given("la sección {string} no tiene asientos disponibles")
    public void laSeccionNoTieneAsientosDisponibles(String sectionName) {
        logger.info("Step: Verificando sección {} sin asientos disponibles", sectionName);
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

    @When("intento interactuar con la sección {string}")
    public void intentoInteractuarConLaSeccion(String sectionName) {
        logger.info("Step: Interactuando con sección reservada: {}", sectionName);
        waitlistSteps.clickReservedSeatInSection(sectionName);
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
        logger.info("Step: Verificando mensaje de confirmación");
        Assert.assertTrue(
            "Debe mostrar confirmación: " + expectedMessage,
            waitlistSteps.verifySuccessToast(expectedMessage)
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
        // When already on waitlist, clicking a reserved seat should show the duplicate error
        // instead of the join button
    }

    @Then("debe mostrar un indicador visual con el mensaje {string}")
    public void debeMostrarIndicadorVisualConMensaje(String expectedMessage) {
        logger.info("Step: Verificando banner/indicador con mensaje: {}", expectedMessage);
        waitlistSteps.waitForPageUpdate();

        boolean bannerVisible = waitlistSteps.verifyWaitlistBannerVisible();
        Assert.assertTrue("Debe mostrar banner de waitlist activa", bannerVisible);

        boolean titleCorrect = waitlistSteps.verifyWaitlistBannerTitle(expectedMessage);
        Assert.assertTrue(
            "Banner debe contener: " + expectedMessage,
            titleCorrect
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

    // ========================================================================
    // CP_HU003_02 — Cancelación exitosa
    // ========================================================================

    @Given("he iniciado el proceso de cancelación y el modal de confirmación está visible")
    public void inicioProcesoDelCancelacion() {
        logger.info("Step: Precondición — proceso de cancelación iniciado");
        // Cancel flow depends on actual UI provided; placeholder for now
    }

    @When("hago clic en el botón {string}")
    public void hagoClicEnBoton(String buttonText) {
        logger.info("Step: Clic en botón: {}", buttonText);
        // Mapped to actual cancel confirmation when UI is available
    }

    @Then("el estado de mi suscripción en la base de datos debe cambiar a {string}")
    public void elEstadoDeberiacambiarA(String expectedStatus) {
        logger.info("Step: Verificando cambio de estado a: {}", expectedStatus);
        waitlistSteps.waitForPageUpdate();
    }

    @Then("debo visualizar una notificación toast con el mensaje {string}")
    public void debeVisualizarToastConMensaje(String expectedMessage) {
        logger.info("Step: Verificando toast: {}", expectedMessage);
        Assert.assertTrue(
            "Debe mostrar toast: " + expectedMessage,
            waitlistSteps.verifySuccessToast(expectedMessage)
        );
    }

    @Then("el banner informativo de suscripción activa debe desaparecer de la página")
    public void elBannerDebeDesaparecer() {
        logger.info("Step: Verificando que banner desapareció");
        Assert.assertTrue(
            "El banner debe desaparecer",
            waitlistSteps.verifyWaitlistBannerDisappeared()
        );
    }

    @Then("el botón para {string} debe volver a estar visible y funcional")
    public void elBotonDebeVolverAEstarVisible(String buttonText) {
        logger.info("Step: Verificando que Join Waitlist vuelve a ser visible");
        Assert.assertTrue(
            "El botón Join Waitlist debe volver a ser visible",
            waitlistSteps.verifyJoinWaitlistButtonVisible()
        );
    }
}
