package com.saucedemo.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ExtentReportManager {

    private static final Path REPORT_DIR = Paths.get("target", "artifacts", "extent");
    private static final ExtentReports EXTENT = create();
    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    private static ExtentReports create() {
        try {
            Files.createDirectories(REPORT_DIR);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create Extent report directory", e);
        }
        ExtentSparkReporter spark = new ExtentSparkReporter(
                REPORT_DIR.resolve("PlaywrightReport.html").toString());
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework", "Playwright Java + JUnit 5");
        return extent;
    }

    public static void start(String name) {
        CURRENT.set(EXTENT.createTest(name));
    }

    public static void pass(String message) {
        CURRENT.get().log(Status.PASS, message);
    }

    public static void fail(String message) {
        CURRENT.get().log(Status.FAIL, message);
    }

    public static void attachScreenshot(Path path) {
        try {
            CURRENT.get().addScreenCaptureFromPath(relativeToReport(path));
        } catch (Exception e) {
            CURRENT.get().log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
        }
    }

    public static void attachVideo(Path path) {
        try {
            String src = relativeToReport(path);
            Markup videoMarkup = () -> "<video width=\"480\" controls><source src=\"" + src
                    + "\" type=\"video/webm\"></video>";
            CURRENT.get().info(videoMarkup);
        } catch (Exception e) {
            CURRENT.get().log(Status.WARNING, "Could not attach video: " + e.getMessage());
        }
    }

    /**
     * addScreenCaptureFromPath (and our hand-rolled video tag) both render an
     * HTML src attribute, which the browser resolves relative to the report
     * HTML file's own folder -- not Maven's working directory. So absolute
     * paths need converting to a path relative to REPORT_DIR first.
     */
    private static String relativeToReport(Path path) {
        return REPORT_DIR.toAbsolutePath()
                .relativize(path.toAbsolutePath())
                .toString()
                .replace('\\', '/');
    }

    public static synchronized void flush() {
        EXTENT.flush();
    }
}