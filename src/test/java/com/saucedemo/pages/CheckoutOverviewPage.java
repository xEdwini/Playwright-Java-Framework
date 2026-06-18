package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class CheckoutOverviewPage extends BasePage {
    private static final Pattern MONEY = Pattern.compile("\\d+\\.\\d{2}");

    private final Locator items;
    private final Locator itemTotal;
    private final Locator tax;
    private final Locator total;
    private final Locator finishButton;
    private final Locator cancelButton;

    public CheckoutOverviewPage(Page page) {
        super(page);
        items = page.getByTestId("inventory-item");
        itemTotal = page.getByTestId("subtotal-label");
        tax = page.getByTestId("tax-label");
        total = page.getByTestId("total-label");
        finishButton = page.getByTestId("finish");
        cancelButton = page.getByTestId("cancel");
    }

    public void assertContains(String productName) {
        assertThat(items.filter(new Locator.FilterOptions().setHasText(productName))).hasCount(1);
    }

    public String itemTotalText() { return itemTotal.innerText(); }
    public String taxText() { return tax.innerText(); }
    public String totalText() { return total.innerText(); }
    public void finish() { click(finishButton, "Finish order"); }
    public void cancel() { click(cancelButton, "Cancel order"); }

    public static BigDecimal parseMoney(String text) {
        Matcher matcher = MONEY.matcher(text);
        if (!matcher.find()) throw new IllegalArgumentException("No monetary amount found in: " + text);
        return new BigDecimal(matcher.group());
    }
}
