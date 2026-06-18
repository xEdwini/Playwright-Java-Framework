package com.saucedemo.tests;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public final class FailureWatcher implements TestWatcher {
    private static final ThreadLocal<Boolean> FAILED = ThreadLocal.withInitial(() -> false);

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        FAILED.set(true);
    }

    static boolean failed() {
        return FAILED.get();
    }

    static void reset() {
        FAILED.remove();
    }
}
