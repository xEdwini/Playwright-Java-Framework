package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class CheckoutCompletePage extends BasePage {
    private final Locator completeHeader;
    private final Locator backHome;

    public CheckoutCompletePage(Page page) {
        super(page);
        completeHeader = page.getByTestId("complete-header");
        backHome = page.getByTestId("back-to-products");
    }

    public void assertOrderComplete() {
        assertThat(completeHeader).hasText("Thank you for your order!");
    }

    public void backHome() {
        click(backHome, "Back home");
    }
}
