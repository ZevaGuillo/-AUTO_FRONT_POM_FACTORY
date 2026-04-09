package com.ticketing.stepdefinitions;

import com.ticketing.steps.AdminSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;

import java.util.Map;

import org.junit.Assert;

public class AdminStepDefinitions {

    @Steps
    private AdminSteps adminSteps;

    @Given("the ticketing system admin panel is available")
    public void theTicketingSystemAdminPanelIsAvailable() {
        adminSteps.navigateToAdminLogin();
        boolean loginPageLoaded = adminSteps.verifyLoginFormDisplayed();
        Assert.assertTrue("El panel de admin debe estar disponible", loginPageLoaded);
    }

    @Given("el usuario navega a la página de login de admin")
    public void elUsuarioNavegaALaPaginaDeLoginDeAdmin() {
        adminSteps.navigateToAdminLogin();
    }

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

    @Given("el administrador está autenticado")
    public void elAdministradorEstaAutenticado() {
        adminSteps.completeAdminLoginAndNavigateToDashboard();
    }

    @Given("navega a la página de creación de eventos")
    public void navegaALaPaginaDeCreacionDeEventos() {
        adminSteps.navigateToCreateEvent();
    }

    @When("completa el formulario con datos válidos del evento")
    public void completaElFormularioConDatosValidosDelEvento(DataTable table) {

        Map<String, String> data = table.asMaps().get(0);

        adminSteps.createNewEvent(
            data.get("name"),
            data.get("description"),
            data.get("eventDate"),
            data.get("venue"),
            data.get("maxCapacity"),
            data.get("basePrice")
        );
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
        boolean eventCreated = adminSteps.verifyEventCreationSuccessful();
        Assert.assertTrue("El evento debe aparecer en la lista", eventCreated);
    }

    @Given("un usuario no está autenticado como administrador")
    public void unUsuarioNoEstaAutenticadoComoAdministrador() {
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
