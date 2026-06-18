package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class LoginPage extends BasePage {
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator error;

    public LoginPage(Page page) {
        super(page);
        username = page.getByTestId("username");
        password = page.getByTestId("password");
        loginButton = page.getByTestId("login-button");
        error = page.getByTestId("error");
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl);
        assertThat(loginButton).isVisible();
    }

    public void login(String user, String pass) {
        fill(username, user, "username");
        fill(password, pass, "password");
        click(loginButton, "Login button");
    }

    public void assertLoaded() {
        assertThat(loginButton).isVisible();
    }

    public void assertErrorContains(String expected) {
        assertThat(error).containsText(expected);
    }

    public void assertPasswordMasked() {
        assertThat(password).hasAttribute("type", "password");
    }
}
