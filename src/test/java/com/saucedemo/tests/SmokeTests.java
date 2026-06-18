package com.saucedemo.tests;

import com.saucedemo.model.CheckoutCustomer;
import com.saucedemo.pages.CheckoutOverviewPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("SauceDemo")
@Feature("Smoke")
@Tag("smoke")
public class SmokeTests extends BaseTest {
    private static final String BACKPACK = "Sauce Labs Backpack";

    @Test @Story("TC-LOGIN-001")
    void loginPageLoads() {
        loginPage.open(CONFIG.baseUrl);
        loginPage.assertLoaded();
    }

    @Test @Story("TC-LOGIN-002")
    void standardUserCanLogin() {
        loginAsStandard();
    }

    @Test @Story("TC-INV-001")
    void inventoryPageLoads() {
        loginAsStandard();
        assertEquals(6, inventoryPage.productCount());
    }

    @Test @Story("TC-INV-007")
    void productCanBeAddedToCart() {
        loginAsStandard();
        inventoryPage.addProduct(BACKPACK);
        inventoryPage.assertCartCount(1);
    }

    @Test @Story("TC-CART-002")
    void cartDisplaysCorrectProduct() {
        loginAsStandard();
        addAndOpenCart(BACKPACK);
        cartPage.assertContains(BACKPACK);
    }

    @Test @Story("TC-CHK-001")
    void validCheckoutInformationIsAccepted() {
        loginAsStandard();
        addAndOpenCart(BACKPACK);
        cartPage.checkout();
        checkoutInformationPage.continueWith(CheckoutCustomer.validUkCustomer());
        checkoutOverviewPage.assertContains(BACKPACK);
    }

    @Test @Story("TC-OVR-003")
    void itemSubtotalIsCorrect() {
        reachOverview(BACKPACK);
        assertEquals(new BigDecimal("29.99"),
            CheckoutOverviewPage.parseMoney(checkoutOverviewPage.itemTotalText()));
    }

    @Test @Story("TC-OVR-005")
    void finalTotalEqualsSubtotalPlusTax() {
        reachOverview(BACKPACK);
        BigDecimal subtotal = CheckoutOverviewPage.parseMoney(checkoutOverviewPage.itemTotalText());
        BigDecimal tax = CheckoutOverviewPage.parseMoney(checkoutOverviewPage.taxText());
        BigDecimal total = CheckoutOverviewPage.parseMoney(checkoutOverviewPage.totalText());
        assertEquals(subtotal.add(tax), total);
    }

    @Test @Story("TC-OVR-010")
    void orderCanBeCompleted() {
        reachOverview(BACKPACK);
        checkoutOverviewPage.finish();
        checkoutCompletePage.assertOrderComplete();
    }

    @Test @Story("TC-NAV-006")
    void userCanLogout() {
        loginAsStandard();
        inventoryPage.openMenu();
        menu.logout();
        loginPage.assertLoaded();
    }
}
