package com.ticketing.stepdefinitions;

import com.ticketing.steps.EventsSteps;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;

/**
 * Step Definitions for Events related scenarios
 * Maps Gherkin steps to EventsSteps methods
 * Refactored to include only essential steps for main scenarios
 */
public class EventsStepDefinitions {

    @Steps
    private EventsSteps eventsSteps;

    // Background step
    @Given("the ticketing application is available")
    public void theTicketingApplicationIsAvailable() {
        // Application availability is assumed - this could check server status
        Assert.assertNotNull("Events steps should be initialized", eventsSteps);
    }

    // Navigation steps
    @Given("el usuario navega a la página de inicio")
    public void elUsuarioNavegaALaPaginaDeInicio() {
        eventsSteps.navigateToHome();
    }

    @Given("el usuario está en la página de eventos")
    public void elUsuarioEstaEnLaPaginaDeEventos() {
        eventsSteps.navigateToEvents();
    }

    // Page loading and verification steps
    @When("la página carga correctamente")
    public void laPaginaCargaCorrectamente() {
        boolean pageLoaded = eventsSteps.verifyPageLoaded();
        Assert.assertTrue("La página debería cargar correctamente", pageLoaded);
    }

    @Then("debe mostrar la lista de eventos disponibles")
    public void debeMostrarLaListaDeEventosDisponibles() {
        boolean eventsDisplayed = eventsSteps.verifyEventsDisplayed();
        Assert.assertTrue("Debe mostrar la lista de eventos", eventsDisplayed);
    }

    @Then("cada evento debe mostrar nombre, descripción, fecha y venue")
    public void cadaEventoDebeMostrarNombreDescripcionFechaYVenue() {
        boolean allEventsComplete = eventsSteps.verifyAllEventsComplete();
        Assert.assertTrue("Todos los eventos deben tener información completa", allEventsComplete);
    }

    @Then("la navegación principal debe estar visible")
    public void laNavegacionPrincipalDebeEstarVisible() {
        boolean navbarVisible = eventsSteps.verifyNavbarVisible();
        Assert.assertTrue("La navegación debe estar visible", navbarVisible);
    }

    // Event interaction steps
    @When("hace clic en un evento")
    public void haceClicEnUnEvento() {
        eventsSteps.clickFirstEvent();
    }

    // Event detail page verification
    @Then("debe navegar a la página de detalle del evento")
    public void debeNavegarALaPaginaDeDetalleDelEvento() {
        boolean detailPageLoaded = eventsSteps.verifyEventDetailPageLoaded();
        Assert.assertTrue("Debe navegar a la página de detalle", detailPageLoaded);
    }

    @Then("debe mostrar el mapa de asientos")
    public void debeMostrarElMapaDeAsientos() {
        boolean seatmapDisplayed = eventsSteps.verifySeatmapDisplayed();
        Assert.assertTrue("Debe mostrar el mapa de asientos", seatmapDisplayed);
    }

    @Then("debe mostrar los detalles del evento correctamente")
    public void debeMostrarLosDetallesDelEventoCorrectamente() {
        boolean hasRequiredDetails = eventsSteps.verifyEventHasRequiredDetails();
        Assert.assertTrue("Debe mostrar todos los detalles del evento", hasRequiredDetails);
    }

    @Then("debe mostrar la leyenda de asientos")
    public void debeMostrarLaLeyendaDeAsientos() {
        boolean legendDisplayed = eventsSteps.verifySeatLegendDisplayed();
        Assert.assertTrue("Debe mostrar la leyenda de asientos", legendDisplayed);
    }

    // Seat availability and interaction steps
    @Given("el usuario está en la página de detalle de un evento")
    public void elUsuarioEstaEnLaPaginaDeDetalleDeUnEvento() {
        eventsSteps.navigateToEvents();
        eventsSteps.clickFirstEvent();
        boolean detailPageLoaded = eventsSteps.verifyEventDetailPageLoaded();
        Assert.assertTrue("Debe estar en la página de detalle del evento", detailPageLoaded);
    }

    // Seat selection scenarios
    @Given("hay asientos disponibles para seleccionar")
    public void hayAsientosDisponiblesParaSeleccionar() {
        int availableSeats = eventsSteps.getAvailableSeatsCount();
        Assert.assertTrue("Debe haber asientos disponibles", availableSeats > 0);
    }

    @When("hace clic en un asiento disponible")
    public void haceClicEnUnAsientoDisponible() {
        eventsSteps.selectFirstAvailableSeat();
    }

    @Then("el asiento debe cambiar a estado \"selected\"")
    public void elAsientoDebeCambiarAEstadoSelected() {
        int selectedSeats = eventsSteps.getSelectedSeatsCount();
        Assert.assertTrue("Debe haber al menos un asiento seleccionado", selectedSeats > 0);
    }

    @Then("debe mostrar el precio del asiento")
    public void debeMostrarElPrecioDelAsiento() {
        String seatPrice = eventsSteps.getSeatPrice();
        Assert.assertNotNull("El precio del asiento no debe ser null", seatPrice);
        Assert.assertFalse("El precio del asiento no debe estar vacío", seatPrice.isEmpty());
    }

    @Then("el botón \"Add to Cart\" debe estar habilitado")
    public void elBotonAddToCartDebeEstarHabilitado() {
        boolean addToCartEnabled = eventsSteps.verifyAddToCartEnabled();
        Assert.assertTrue("El botón Add to Cart debe estar habilitado", addToCartEnabled);
    }
}