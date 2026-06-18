package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class ProductDetailsPage extends BasePage {
    private final Locator name;
    private final Locator price;
    private final Locator addButton;
    private final Locator removeButton;
    private final Locator backButton;

    public ProductDetailsPage(Page page) {
        super(page);
        name = page.getByTestId("inventory-item-name");
        price = page.getByTestId("inventory-item-price");
        addButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart"));
        removeButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Remove"));
        backButton = page.getByTestId("back-to-products");
    }

    public void assertProduct(String expectedName) {
        assertThat(name).hasText(expectedName);
    }

    public String priceText() {
        return price.innerText();
    }

    public void addToCart() { click(addButton, "Add product from details"); }
    public void removeFromCart() { click(removeButton, "Remove product from details"); }
    public void backToProducts() { click(backButton, "Back to products"); }
}
