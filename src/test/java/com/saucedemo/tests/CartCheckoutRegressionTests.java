package com.saucedemo.tests;

import com.saucedemo.pages.CheckoutOverviewPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("SauceDemo")
@Feature("Cart and checkout regression")
@Tag("regression")
@Tag("cart-checkout")
public class CartCheckoutRegressionTests extends BaseTest {
    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    @Test @Story("TC-CART-003")
    void multipleCartItemsAreDisplayed() {
        loginAsStandard();
        addAndOpenCart(BACKPACK, BIKE_LIGHT);
        assertEquals(2, cartPage.itemCount());
    }

    @Test @Story("TC-CART-005")
    void itemCanBeRemovedFromCart() {
        loginAsStandard();
        addAndOpenCart(BACKPACK, BIKE_LIGHT);
        cartPage.remove(BACKPACK);
        assertEquals(1, cartPage.itemCount());
        cartPage.assertContains(BIKE_LIGHT);
    }

    @Test @Story("TC-CART-007")
    void continueShoppingReturnsToInventory() {
        loginAsStandard();
        addAndOpenCart(BACKPACK);
        cartPage.continueShopping();
        inventoryPage.assertLoaded();
        inventoryPage.assertCartCount(1);
    }

    @ParameterizedTest
    @CsvSource({
        "'', Smith, 'SW1A 1AA', First Name is required",
        "John, '', 'SW1A 1AA', Last Name is required",
        "John, Smith, '', Postal Code is required"
    })
    @Story("TC-CHK-003 / TC-CHK-004 / TC-CHK-005")
    void checkoutRequiredFieldsAreValidated(String first, String last, String postal, String expected) {
        loginAsStandard();
        addAndOpenCart(BACKPACK);
        cartPage.checkout();
        checkoutInformationPage.submitRaw(first, last, postal);
        checkoutInformationPage.assertErrorContains(expected);
    }

    @Test @Story("TC-CHK-017")
    void checkoutCanBeCancelled() {
        loginAsStandard();
        addAndOpenCart(BACKPACK);
        cartPage.checkout();
        checkoutInformationPage.cancel();
        cartPage.assertContains(BACKPACK);
    }

    @Test @Story("TC-OVR-001")
    void overviewContainsAllSelectedItems() {
        reachOverview(BACKPACK, BIKE_LIGHT);
        checkoutOverviewPage.assertContains(BACKPACK);
        checkoutOverviewPage.assertContains(BIKE_LIGHT);
    }

    @Test @Story("TC-OVR-005")
    void totalsAreInternallyConsistent() {
        reachOverview(BACKPACK, BIKE_LIGHT);
        BigDecimal subtotal = CheckoutOverviewPage.parseMoney(checkoutOverviewPage.itemTotalText());
        BigDecimal tax = CheckoutOverviewPage.parseMoney(checkoutOverviewPage.taxText());
        BigDecimal total = CheckoutOverviewPage.parseMoney(checkoutOverviewPage.totalText());
        assertEquals(subtotal.add(tax), total);
    }

    @Test @Story("TC-ORD-004")
    void cartIsClearedAfterCompletedOrder() {
        reachOverview(BACKPACK);
        checkoutOverviewPage.finish();
        checkoutCompletePage.backHome();
        inventoryPage.assertCartBadgeAbsent();
    }

    @Test @Story("TC-NAV-005")
    void resetAppStateClearsCart() {
        loginAsStandard();
        inventoryPage.addProduct(BACKPACK);
        inventoryPage.openMenu();
        menu.resetAppState();
        inventoryPage.assertCartBadgeAbsent();
    }

    @Test @Story("TC-NAV-007")
    void protectedPageCannotBeUsedAfterLogout() {
        loginAsStandard();
        inventoryPage.openMenu();
        menu.logout();
        page.navigate(CONFIG.baseUrl + "inventory.html");
        loginPage.assertErrorContains("You can only access '/inventory.html' when you are logged in.");
    }
}
