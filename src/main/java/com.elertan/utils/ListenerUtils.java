package com.elertan.utils;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListenerUtils {

    public static CompletableFuture<Void> waitUntilReady(WaitUntilReadyContext context) {
        CompletableFuture<Void> ready = new CompletableFuture<>();

        if (context.isReady()) {
            ready.complete(null);
            return ready;
        }

        context.addListener(() -> {
            if (context.isReady() && !ready.isDone()) {
                context.removeListener();
                ready.complete(null);
            }
        });

        if (context.isReady() && !ready.isDone()) {
            context.removeListener();
            ready.complete(null);
        }

        Duration timeout = context.getTimeout();
        if (timeout != null) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(
                () -> {
                    if (!ready.isDone()) {
                        context.removeListener();
                        ready.completeExceptionally(new TimeoutException(
                            "Timeout waiting for context to become ready"));
                    }
                    scheduler.shutdown();
                }, timeout.toMillis(), TimeUnit.MILLISECONDS
            );
        }

        return ready;
    }

    public interface WaitUntilReadyContext {

        boolean isReady();

        void addListener(Runnable notify);

        void removeListener();

        Duration getTimeout();
    }
}
