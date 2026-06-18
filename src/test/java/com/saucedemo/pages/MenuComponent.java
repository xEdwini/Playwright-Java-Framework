package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class MenuComponent extends BasePage {
    private final Locator menu;
    private final Locator allItems;
    private final Locator reset;
    private final Locator logout;
    private final Locator close;

    public MenuComponent(Page page) {
        super(page);
        menu = page.locator(".bm-menu");
        allItems = page.getByTestId("inventory-sidebar-link");
        reset = page.getByTestId("reset-sidebar-link");
        logout = page.getByTestId("logout-sidebar-link");
        close = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close Menu"));
    }

    public void assertOpen() { assertThat(menu).isVisible(); }
    public void resetAppState() { click(reset, "Reset app state"); }
    public void logout() { click(logout, "Logout"); }
    public void allItems() { click(allItems, "All items"); }
    public void close() { click(close, "Close menu"); }
}
