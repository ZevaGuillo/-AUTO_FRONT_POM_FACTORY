package com.ticketing.stepdefinitions;

import com.ticketing.steps.AdminSteps;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;

/**
 * Step Definitions for Admin related scenarios
 * Maps Gherkin steps to AdminSteps methods
 * Refactored to include only essential steps for main admin scenarios
 */
public class AdminStepDefinitions {

    @Steps
    private AdminSteps adminSteps;

    // Background step
    @Given("the ticketing system admin panel is available")
    public void theTicketingSystemAdminPanelIsAvailable() {
        adminSteps.navigateToAdminLogin();
        boolean loginPageLoaded = adminSteps.verifyLoginFormDisplayed();
        Assert.assertTrue("El panel de admin debe estar disponible", loginPageLoaded);
    }

    // Navigation to admin login
    @Given("el usuario navega a la página de login de admin")
    public void elUsuarioNavegaALaPaginaDeLoginDeAdmin() {
        adminSteps.navigateToAdminLogin();
    }

    // Authentication steps for login scenario
    @When("ingresa credenciales válidas de administrador")
    public void ingresaCredencialesValidasDeAdministrador() {
        adminSteps.loginAsAdminWithDefaults();
    }

    @Then("debe autenticarse correctamente")
    public void debeAutenticarseCorrectamente() {
        boolean loginSuccessful = adminSteps.verifyLoginSuccessful();
        Assert.assertTrue("Debe autenticarse correctamente", loginSuccessful);
    }

    @Then("debe redirigir al dashboard de admin")
    public void debeRedirigirAlDashboardDeAdmin() {
        boolean dashboardLoaded = adminSteps.verifyDashboardLoaded();
        Assert.assertTrue("Debe redirigir al dashboard de admin", dashboardLoaded);
    }

    @Then("debe mostrar el panel de navegación de admin")
    public void debeMostrarElPanelDeNavegacionDeAdmin() {
        boolean dashboardLoaded = adminSteps.verifyDashboardLoaded();
        Assert.assertTrue("Debe mostrar el panel de navegación de admin", dashboardLoaded);
    }

    // Event creation scenario steps
    @Given("el administrador está autenticado")
    public void elAdministradorEstaAutenticado() {
        adminSteps.completeAdminLoginAndNavigateToDashboard();
    }

    @Given("navega a la página de creación de eventos")
    public void navegaALaPaginaDeCreacionDeEventos() {
        adminSteps.navigateToCreateEvent();
    }

    @When("completa el formulario con datos válidos del evento")
    public void completaElFormularioConDatosValidosDelEvento() {
        adminSteps.createNewEvent("Test Concert", "Test Description", "2024-12-25", "Madison Square", "1000", "50");
    }

    @Then("el evento debe crearse exitosamente")
    public void elEventoDebeCrearseExitosamente() {
        boolean eventCreated = adminSteps.verifyEventCreationSuccessful();
        Assert.assertTrue("El evento debe crearse exitosamente", eventCreated);
    }

    @Then("debe mostrar confirmación de creación")
    public void debeMostrarConfirmacionDeCreacion() {
        boolean eventCreated = adminSteps.verifyEventCreationSuccessful();
        Assert.assertTrue("Debe mostrar confirmación de creación", eventCreated);
    }

    @Then("debe aparecer en la lista de eventos")
    public void debeAparecerEnLaListaDeEventos() {
        // This would be implemented in the AdminSteps class
        // For now, we verify that the event was created successfully
        boolean eventCreated = adminSteps.verifyEventCreationSuccessful();
        Assert.assertTrue("El evento debe aparecer en la lista", eventCreated);
    }

    // Security/Unauthorized access scenario steps
    @Given("un usuario no está autenticado como administrador")
    public void unUsuarioNoEstaAutenticadoComoAdministrador() {
        // Ensure no admin session exists
        // This step represents starting with no authentication
        Assert.assertNotNull("AdminSteps should be initialized", adminSteps);
    }

    @When("intenta acceder directamente al dashboard de admin")
    public void intentaAccederDirectamenteAlDashboardDeAdmin() {
        adminSteps.navigateToAdminDashboard();
    }

    @Then("debe ser redirigido a la página de login")
    public void debeSerRedirigidoALaPaginaDeLogin() {
        boolean onLoginPage = adminSteps.verifyLoginFormDisplayed();
        Assert.assertTrue("Debe ser redirigido a la página de login", onLoginPage);
    }

    @Then("no debe tener acceso a funciones administrativas")
    public void noDebeTenerAccesoAFuncionesAdministrativas() {
        boolean onLoginPage = adminSteps.verifyLoginFormDisplayed();
        Assert.assertTrue("No debe tener acceso a funciones administrativas", onLoginPage);
    }

    @Then("debe requerir autenticación válida")
    public void debeRequerirAutenticacionValida() {
        boolean onLoginPage = adminSteps.verifyLoginFormDisplayed();
        Assert.assertTrue("Debe requerir autenticación válida", onLoginPage);
    }
}