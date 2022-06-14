package eu.zsokolowski.completablefuturetrap;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Demo {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ExecutorService producerExecutorService;
    private CompletableFuture<Boolean> producerTaskFeature;
    private volatile boolean working;

    public Demo() {
        var threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Producer-%d")
            .build();
        this.producerExecutorService = Executors.newSingleThreadExecutor(threadFactory);
    }


    public void startProcess(TimeBox timeBox) {
        working = true;
        Supplier<Boolean> producerTask = () -> {
            while (isWorking()) {
                try {
                    TimeUnit.SECONDS.sleep(timeBox.getDuration());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    working = false;
                }
            }
            return true;
        };

        producerTaskFeature = CompletableFuture.supplyAsync(producerTask, producerExecutorService);
        LOG.info("Started processing:");
    }

    private boolean isWorking() {
        return working && !Thread.currentThread().isInterrupted();
    }

    public void stop() {
        working = false;
        try {
            if (producerTaskFeature != null) {
                producerTaskFeature.cancel(true);
                LOG.info("Processing stopped gracefully.");
            }
        } catch (CancellationException e) {
            LOG.error("Waiting for processing to stop is cancelled. Called cancel for processing.");
        } finally {
            if (producerTaskFeature != null) {
                LOG.info("Processing has to be ended.");
            }
            stopProducerExecutorService();
        }
    }

    public void stopProducerExecutorService() {
        producerExecutorService.shutdown();
        try {
            final boolean terminationConsumerService = producerExecutorService.awaitTermination(5, TimeUnit.SECONDS);
            LOG.info("Producer executor service was terminated: {}", terminationConsumerService);
        } catch (InterruptedException e) {
            LOG.info("ProduceConsumer was interrupted");
            Thread.currentThread().interrupt();
            producerExecutorService.shutdownNow();
        } finally {
            //TODO commented out for demo purpose
            //producerExecutorService.shutdownNow();
        }
    }
}
