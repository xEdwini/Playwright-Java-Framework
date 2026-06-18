package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {
    protected final Page page;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void click(Locator locator, String description) {
        log.info("Click: {}", description);
        locator.click();
    }

    protected void fill(Locator locator, String value, String description) {
        log.info("Fill: {} = {}", description, "password".equalsIgnoreCase(description) ? "***" : value);
        locator.fill(value);
    }
}
