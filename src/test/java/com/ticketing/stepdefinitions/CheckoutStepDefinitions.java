package com.ticketing.stepdefinitions;

import com.ticketing.steps.CheckoutSteps;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;

/**
 * Step Definitions for Checkout related scenarios
 * Maps Gherkin steps to CheckoutSteps methods
 * Refactored to include only essential steps for main checkout scenarios
 */
public class CheckoutStepDefinitions {

    @Steps
    private CheckoutSteps checkoutSteps;

    // Background steps
    @Given("the user has items in their cart")
    public void theUserHasItemsInTheirCart() {
        checkoutSteps.setupUserWithTicketInCart();
    }

    // Scenario 1: "Checkout exitoso con carrito válido"
    @Given("el usuario tiene items en el carrito")
    public void elUsuarioTieneItemsEnElCarrito() {
        checkoutSteps.setupUserWithTicketInCart();
    }

    @When("completa el checkout con información válida")
    public void completaElCheckoutConInformacionValida() {
        checkoutSteps.navigateToCheckout();
        checkoutSteps.fillPersonalInfo("Juan", "Pérez", "juan.perez@email.com", "+51987654321");
        checkoutSteps.fillPaymentInfo("4532123456789012", "Juan Pérez", "12/26", "123");
        checkoutSteps.clickCompletePurchase();
    }

    @Then("el sistema debe procesar el pago correctamente")
    public void elSistemaDebeProcenarElPagoCorrectamente() {
        boolean paymentProcessed = checkoutSteps.verifyPaymentProcessed();
        Assert.assertTrue("El sistema debe procesar el pago correctamente", paymentProcessed);
    }

    @Then("debe mostrar confirmación de la compra")
    public void debeMostrarConfirmacionDeLaCompra() {
        boolean confirmationVisible = checkoutSteps.verifyConfirmationPageVisible();
        Assert.assertTrue("Debe mostrar confirmación de la compra", confirmationVisible);
    }

    @Then("debe mostrar el número de confirmación")
    public void debeMostrarElNumeroDeConfirmacion() {
        String orderNumber = checkoutSteps.getOrderNumber();
        Assert.assertNotNull("El número de confirmación no debe ser null", orderNumber);
        Assert.assertFalse("El número de confirmación no debe estar vacío", orderNumber.isEmpty());
    }

    // Scenario 2: "Pago con tarjeta inválida"
    @Given("está en la página de checkout")
    public void estaEnLaPaginaDeCheckout() {
        checkoutSteps.navigateToCheckout();
        boolean checkoutPageLoaded = checkoutSteps.verifyCheckoutPageLoaded();
        Assert.assertTrue("Debe estar en la página de checkout", checkoutPageLoaded);
    }

    @When("intenta pagar con tarjeta inválida")
    public void intentaPagarConTarjetaInvalida() {
        checkoutSteps.fillPersonalInfo("Test", "User", "test@email.com", "+51999888777");
        checkoutSteps.fillPaymentInfo("0000000000000000", "Invalid User", "01/20", "000");
        checkoutSteps.clickCompletePurchase();
    }

    @Then("debe mostrar error de tarjeta inválida")
    public void debeMostrarErrorDeTarjetaInvalida() {
        boolean cardErrorVisible = checkoutSteps.verifyCardValidationErrorVisible();
        Assert.assertTrue("Debe mostrar error de tarjeta inválida", cardErrorVisible);
    }

    @Then("no debe procesar el pago")
    public void noDebeProcenarElPago() {
        boolean paymentNotProcessed = !checkoutSteps.verifyPaymentProcessed();
        Assert.assertTrue("No debe procesar el pago con tarjeta inválida", paymentNotProcessed);
    }

    @Then("debe permanecer en la página de checkout")
    public void debePermaneceEnLaPaginaDeCheckout() {
        boolean stillOnCheckout = checkoutSteps.verifyCheckoutPageLoaded();
        Assert.assertTrue("Debe permanecer en la página de checkout", stillOnCheckout);
    }

    // Scenario 3: "Checkout con carrito vacío"
    @Given("el usuario está en la página de checkout")
    public void elUsuarioEstaEnLaPaginaDeCheckout() {
        checkoutSteps.navigateToCheckout();
        boolean checkoutPageLoaded = checkoutSteps.verifyCheckoutPageLoaded();
        Assert.assertTrue("Debe estar en la página de checkout", checkoutPageLoaded);
    }

    @Given("el carrito está vacío")
    public void elCarritoEstaVacio() {
        // Simulate empty cart condition
        Assert.assertTrue("Cart should be empty for this scenario", checkoutSteps.verifyCartEmpty());
    }

    @When("intenta hacer checkout")
    public void intentaHacerCheckout() {
        checkoutSteps.clickProceedToCheckout();
    }

    @Then("debe mostrar mensaje de carrito vacío")
    public void debeMostrarMensajeDeCarritoVacio() {
        String emptyCartMessage = checkoutSteps.getEmptyCartMessage();
        Assert.assertNotNull("Debe mostrar mensaje de carrito vacío", emptyCartMessage);
        Assert.assertFalse("El mensaje no debe estar vacío", emptyCartMessage.isEmpty());
    }

    @Then("no debe permitir procesar el pago")
    public void noDebePermitirProcenarElPago() {
        boolean checkoutButtonEnabled = checkoutSteps.verifyCheckoutButtonEnabled();
        Assert.assertFalse("No debe permitir procesar el pago con carrito vacío", checkoutButtonEnabled);
    }

    @Then("debe sugerir agregar items al carrito")
    public void debeSugerirAgregarItemsAlCarrito() {
        String emptyCartMessage = checkoutSteps.getEmptyCartMessage();
        Assert.assertTrue("Debe sugerir agregar items al carrito", 
                         emptyCartMessage.toLowerCase().contains("agregar") || 
                         emptyCartMessage.toLowerCase().contains("añadir") ||
                         emptyCartMessage.toLowerCase().contains("add"));
    }
}