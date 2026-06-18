package com.saucedemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public final class TestConfig {
    public String baseUrl;
    public String browser;
    public boolean headless;
    public int slowMoMs;
    public double defaultTimeoutMs;
    public double navigationTimeoutMs;
    public boolean recordVideo;
    public boolean traceOnFailure;
    public boolean screenshotOnFailure;
    public int viewportWidth;
    public int viewportHeight;
    public Map<String, UserCredential> users;

    public record UserCredential(String username, String password) {}

    public static TestConfig load() {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.json")) {
            if (input == null) throw new IllegalStateException("config.json was not found.");
            TestConfig config = new ObjectMapper().readValue(input, TestConfig.class);

            String baseUrl = System.getenv("BASE_URL");
            String browser = System.getenv("BROWSER");
            String headless = System.getenv("HEADLESS");

            if (baseUrl != null && !baseUrl.isBlank()) config.baseUrl = baseUrl;
            if (browser != null && !browser.isBlank()) config.browser = browser;
            if (headless != null && !headless.isBlank()) config.headless = Boolean.parseBoolean(headless);

            return config;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load test configuration.", e);
        }
    }

    public UserCredential user(String name) {
        UserCredential credential = users.get(name);
        if (credential == null) throw new IllegalArgumentException("Unknown user profile: " + name);
        return credential;
    }
}
