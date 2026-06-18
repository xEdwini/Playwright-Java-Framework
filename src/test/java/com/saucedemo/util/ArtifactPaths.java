package com.saucedemo.util;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ArtifactPaths {
    public static final Path ROOT = Path.of("target", "artifacts");
    public static final Path SCREENSHOTS = ROOT.resolve("screenshots");
    public static final Path TRACES = ROOT.resolve("traces");
    public static final Path VIDEOS = ROOT.resolve("videos");
    public static final Path LOGS = ROOT.resolve("logs");

    private ArtifactPaths() {}

    public static void ensureCreated() {
        try {
            Files.createDirectories(SCREENSHOTS);
            Files.createDirectories(TRACES);
            Files.createDirectories(VIDEOS);
            Files.createDirectories(LOGS);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create artifact directories.", e);
        }
    }

    public static String safeFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
