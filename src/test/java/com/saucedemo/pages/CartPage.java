package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class CartPage extends BasePage {
    private final Locator cartList;
    private final Locator items;
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;

    public CartPage(Page page) {
        super(page);
        cartList = page.getByTestId("cart-list");
        items = page.getByTestId("inventory-item");
        checkoutButton = page.getByTestId("checkout");
        continueShoppingButton = page.getByTestId("continue-shopping");
    }

    public void assertLoaded() {
        assertThat(cartList).isVisible();
    }

    public int itemCount() {
        return items.count();
    }

    public void assertContains(String productName) {
        assertThat(items.filter(new Locator.FilterOptions().setHasText(productName))).hasCount(1);
    }

    public void remove(String productName) {
        Locator item = items.filter(new Locator.FilterOptions().setHasText(productName));
        click(item.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove")),
            "Remove " + productName + " from cart");
    }

    public void checkout() { click(checkoutButton, "Checkout"); }
    public void continueShopping() { click(continueShoppingButton, "Continue shopping"); }
}
