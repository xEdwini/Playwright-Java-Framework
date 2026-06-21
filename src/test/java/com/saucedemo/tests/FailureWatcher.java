package com.saucedemo.tests;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class FailureWatcher implements AfterTestExecutionCallback {
    private static final ThreadLocal<Boolean> FAILED = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Throwable> CAUSE = new ThreadLocal<>();

    @Override
    public void afterTestExecution(ExtensionContext context) {
        context.getExecutionException().ifPresentOrElse(
                ex -> { FAILED.set(true); CAUSE.set(ex); },
                () -> FAILED.set(false)
        );
    }

    static boolean failed() {
        return FAILED.get();
    }

    static Throwable cause() {
        return CAUSE.get();
    }

    static void reset() {
        FAILED.remove();
        CAUSE.remove();
    }
}