package com.saucedemo.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("SauceDemo")
@Feature("Inventory regression")
@Tag("regression")
@Tag("inventory")
public class InventoryRegressionTests extends BaseTest {
    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    @BeforeEach
    void login() {
        loginAsStandard();
    }

    @Test @Story("TC-INV-008")
    void multipleProductsUpdateCartCount() {
        inventoryPage.addProduct(BACKPACK);
        inventoryPage.addProduct(BIKE_LIGHT);
        inventoryPage.assertCartCount(2);
    }

    @Test @Story("TC-INV-009")
    void productCanBeRemovedFromInventory() {
        inventoryPage.addProduct(BACKPACK);
        inventoryPage.removeProduct(BACKPACK);
        inventoryPage.assertCartBadgeAbsent();
    }

    @Test @Story("TC-INV-011")
    void sortNamesAscending() {
        inventoryPage.sort("az");
        List<String> actual = inventoryPage.productNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder());
        assertEquals(expected, actual);
    }

    @Test @Story("TC-INV-012")
    void sortNamesDescending() {
        inventoryPage.sort("za");
        List<String> actual = inventoryPage.productNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Comparator.reverseOrder());
        assertEquals(expected, actual);
    }

    @Test @Story("TC-INV-013")
    void sortPricesAscending() {
        inventoryPage.sort("lohi");
        List<BigDecimal> actual = inventoryPage.productPrices();
        List<BigDecimal> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder());
        assertEquals(expected, actual);
    }

    @Test @Story("TC-INV-014")
    void sortPricesDescending() {
        inventoryPage.sort("hilo");
        List<BigDecimal> actual = inventoryPage.productPrices();
        List<BigDecimal> expected = new ArrayList<>(actual);
        expected.sort(Comparator.reverseOrder());
        assertEquals(expected, actual);
    }

    @Test @Story("TC-INV-015")
    void sortingPreservesCart() {
        inventoryPage.addProduct(BACKPACK);
        inventoryPage.sort("hilo");
        inventoryPage.assertCartCount(1);
    }

    @Test @Story("TC-PDP-001")
    void productDetailsDisplayCorrectProduct() {
        inventoryPage.openProduct(BACKPACK);
        productDetailsPage.assertProduct(BACKPACK);
    }
}
