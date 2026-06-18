package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.math.BigDecimal;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class InventoryPage extends BasePage {
    private final Locator inventoryContainer;
    private final Locator items;
    private final Locator cartLink;
    private final Locator cartBadge;
    private final Locator sort;
    private final Locator menuButton;

    public InventoryPage(Page page) {
        super(page);
        inventoryContainer = page.getByTestId("inventory-container");
        items = page.getByTestId("inventory-item");
        cartLink = page.getByTestId("shopping-cart-link");
        cartBadge = page.getByTestId("shopping-cart-badge");
        sort = page.getByTestId("product-sort-container");
        menuButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open Menu"));
    }

    public void assertLoaded() {
        assertThat(inventoryContainer).isVisible();
    }

    public int productCount() {
        return items.count();
    }

    private Locator item(String productName) {
        return items.filter(new Locator.FilterOptions().setHasText(productName));
    }

    public void addProduct(String productName) {
        click(item(productName).getByRole(
            AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add to cart")), "Add " + productName);
    }

    public void removeProduct(String productName) {
        click(item(productName).getByRole(
            AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove")), "Remove " + productName);
    }

    public void assertCartCount(int count) {
        assertThat(cartBadge).hasText(String.valueOf(count));
    }

    public void assertCartBadgeAbsent() {
        assertThat(cartBadge).hasCount(0);
    }

    public void openCart() {
        click(cartLink, "Shopping cart");
    }

    public void openProduct(String productName) {
        click(item(productName).getByRole(
            AriaRole.LINK, new Locator.GetByRoleOptions().setName(productName)), "Product " + productName);
    }

    public void sort(String value) {
        sort.selectOption(value);
    }

    public List<String> productNames() {
        return items.getByTestId("inventory-item-name").allInnerTexts();
    }

    public List<BigDecimal> productPrices() {
        return items.getByTestId("inventory-item-price").allInnerTexts().stream()
            .map(v -> new BigDecimal(v.replace("$", "")))
            .toList();
    }

    public void openMenu() {
        click(menuButton, "Open menu");
    }
}
