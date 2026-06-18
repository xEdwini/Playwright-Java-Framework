package com.saucedemo.tests;

import com.saucedemo.config.TestConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("SauceDemo")
@Feature("Login regression")
@Tag("regression")
@Tag("login")
public class LoginRegressionTests extends BaseTest {

    @Test @Story("TC-LOGIN-003")
    void invalidUsernameIsRejected() {
        loginPage.open(CONFIG.baseUrl);
        loginPage.login("invalid_user", "secret_sauce");
        loginPage.assertErrorContains("Username and password do not match");
    }

    @Test @Story("TC-LOGIN-004")
    void invalidPasswordIsRejected() {
        loginPage.open(CONFIG.baseUrl);
        loginPage.login("standard_user", "invalid_password");
        loginPage.assertErrorContains("Username and password do not match");
    }

    @ParameterizedTest(name = "{2}")
    @CsvSource({
        "'', '', Username is required",
        "standard_user, '', Password is required"
    })
    @Story("TC-LOGIN-006 / TC-LOGIN-008")
    void requiredFieldValidation(String username, String password, String expected) {
        loginPage.open(CONFIG.baseUrl);
        loginPage.login(username, password);
        loginPage.assertErrorContains(expected);
    }

    @Test @Story("TC-LOGIN-009")
    void lockedOutUserIsRejected() {
        TestConfig.UserCredential user = CONFIG.user("locked");
        loginPage.open(CONFIG.baseUrl);
        loginPage.login(user.username(), user.password());
        loginPage.assertErrorContains("locked out");
    }

    @Test @Story("TC-LOGIN-010")
    void passwordIsMasked() {
        loginPage.open(CONFIG.baseUrl);
        loginPage.assertPasswordMasked();
    }

    @ParameterizedTest
    @ValueSource(strings = {"' OR 1=1 --", "<script>alert(1)</script>", "!@#$%^&*"})
    @Story("TC-LOGIN-016 / TC-LOGIN-017 / TC-LOGIN-018")
    void maliciousOrSpecialInputDoesNotAuthenticate(String input) {
        loginPage.open(CONFIG.baseUrl);
        loginPage.login(input, input);
        loginPage.assertErrorContains("Username and password do not match");
    }

    @Test @Story("TC-LOGIN-022")
    void inventoryCannotBeAccessedWithoutAuthentication() {
        page.navigate(CONFIG.baseUrl + "inventory.html");
        loginPage.assertErrorContains("You can only access '/inventory.html' when you are logged in.");
    }
}
