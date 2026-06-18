package com.saucedemo.tests;

import com.microsoft.playwright.*;
import com.saucedemo.config.TestConfig;
import com.saucedemo.model.CheckoutCustomer;
import com.saucedemo.pages.*;
import com.saucedemo.util.ArtifactPaths;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(FailureWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public abstract class BaseTest {
    protected static final TestConfig CONFIG = TestConfig.load();
    protected static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected ProductDetailsPage productDetailsPage;
    protected CartPage cartPage;
    protected CheckoutInformationPage checkoutInformationPage;
    protected CheckoutOverviewPage checkoutOverviewPage;
    protected CheckoutCompletePage checkoutCompletePage;
    protected MenuComponent menu;

    private boolean traceStarted;

    @BeforeEach
    void setUp() {
        ArtifactPaths.ensureCreated();
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = browserType().launch(new BrowserType.LaunchOptions()
            .setHeadless(CONFIG.headless)
            .setSlowMo((double) CONFIG.slowMoMs));

        Browser.NewContextOptions options = new Browser.NewContextOptions()
            .setViewportSize(CONFIG.viewportWidth, CONFIG.viewportHeight)
            .setIgnoreHTTPSErrors(false);

        if (CONFIG.recordVideo) {
            options.setRecordVideoDir(ArtifactPaths.VIDEOS);
        }

        context = browser.newContext(options);
        page = context.newPage();
        page.setDefaultTimeout(CONFIG.defaultTimeoutMs);
        page.setDefaultNavigationTimeout(CONFIG.navigationTimeoutMs);

        page.onConsoleMessage(msg -> LOG.info("Browser console [{}]: {}", msg.type(), msg.text()));
        page.onPageError(error -> LOG.error("Browser page error: {}", error));

        if (CONFIG.traceOnFailure) {
            context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
            traceStarted = true;
        }

        loginPage = new LoginPage(page);
        inventoryPage = new InventoryPage(page);
        productDetailsPage = new ProductDetailsPage(page);
        cartPage = new CartPage(page);
        checkoutInformationPage = new CheckoutInformationPage(page);
        checkoutOverviewPage = new CheckoutOverviewPage(page);
        checkoutCompletePage = new CheckoutCompletePage(page);
        menu = new MenuComponent(page);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        boolean failed = FailureWatcher.failed();
        String name = ArtifactPaths.safeFileName(testInfo.getDisplayName());
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

        try {
            if (failed && CONFIG.screenshotOnFailure) {
                Path screenshot = ArtifactPaths.SCREENSHOTS.resolve(name + "-" + stamp + ".png");
                byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setPath(screenshot).setFullPage(true));
                Allure.addAttachment("Failure screenshot", "image/png", new ByteArrayInputStream(bytes), ".png");
            }

            if (traceStarted) {
                if (failed) {
                    Path trace = ArtifactPaths.TRACES.resolve(name + "-" + stamp + ".zip");
                    context.tracing().stop(new Tracing.StopOptions().setPath(trace));
                } else {
                    context.tracing().stop();
                }
            }
        } catch (Exception e) {
            LOG.warn("Unable to collect failure artifacts.", e);
        } finally {
            if (context != null) context.close();
            if (browser != null) browser.close();
            if (playwright != null) playwright.close();
            FailureWatcher.reset();
        }
    }

    private BrowserType browserType() {
        return switch (CONFIG.browser.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            case "chromium" -> playwright.chromium();
            default -> throw new IllegalArgumentException("Unsupported browser: " + CONFIG.browser);
        };
    }

    protected void loginAs(String profile) {
        TestConfig.UserCredential user = CONFIG.user(profile);
        loginPage.open(CONFIG.baseUrl);
        loginPage.login(user.username(), user.password());
        inventoryPage.assertLoaded();
    }

    protected void loginAsStandard() {
        loginAs("standard");
    }

    protected void addAndOpenCart(String... products) {
        for (String product : products) inventoryPage.addProduct(product);
        inventoryPage.openCart();
        cartPage.assertLoaded();
    }

    protected void reachOverview(String... products) {
        loginAsStandard();
        addAndOpenCart(products);
        cartPage.checkout();
        checkoutInformationPage.continueWith(CheckoutCustomer.validUkCustomer());
    }
}
