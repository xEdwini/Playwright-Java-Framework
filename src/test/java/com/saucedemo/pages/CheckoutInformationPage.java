package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.model.CheckoutCustomer;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class CheckoutInformationPage extends BasePage {
    private final Locator firstName;
    private final Locator lastName;
    private final Locator postalCode;
    private final Locator continueButton;
    private final Locator cancelButton;
    private final Locator error;

    public CheckoutInformationPage(Page page) {
        super(page);
        firstName = page.getByTestId("firstName");
        lastName = page.getByTestId("lastName");
        postalCode = page.getByTestId("postalCode");
        continueButton = page.getByTestId("continue");
        cancelButton = page.getByTestId("cancel");
        error = page.getByTestId("error");
    }

    public void continueWith(CheckoutCustomer customer) {
        submitRaw(customer.firstName(), customer.lastName(), customer.postalCode());
    }

    public void submitRaw(String first, String last, String postal) {
        fill(firstName, first, "first name");
        fill(lastName, last, "last name");
        fill(postalCode, postal, "postal code");
        click(continueButton, "Continue checkout");
    }

    public void assertErrorContains(String expected) {
        assertThat(error).containsText(expected);
    }

    public void cancel() {
        click(cancelButton, "Cancel checkout");
    }
}
