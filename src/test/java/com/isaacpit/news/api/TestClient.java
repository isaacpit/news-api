package com.isaacpit.news.api;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.*;

@Slf4j
@SpringBootApplication
public class TestClient {

    private static final int NUM_THREADS = 5;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(NUM_THREADS);
    private static final WebClient WEB_CLIENT = WebClient.create();
    private static final RateLimiter RATE_LIMITER = createRateLimiter(5000);

    private static ConcurrentHashMap<String, Long> resultMap = new ConcurrentHashMap<>();

//    private static final String KEY_SUCCESS = "success";
//    private static final String KEY_FAILURE = "failure";

    public static void main(String[] args) {
        String host = "http://localhost:8080";

        String searchEndpoint = "%s/news/api/search?query=Bill gates&client=news".formatted(host);

        final int NUM_ITERATIONS = 10000;
        final Duration WARMUP_SLEEP_DURATION = Duration.ofSeconds(2);

        warmupSearchEndpoint(searchEndpoint, WARMUP_SLEEP_DURATION);
        testSearchEndpoint(NUM_ITERATIONS, searchEndpoint);

        EXECUTOR.shutdown();
    }

    /**
     * makes 1 test API call then sleeps to give some time to call API and populate cache
     */
    @SneakyThrows({InterruptedException.class})
    private static void warmupSearchEndpoint(String endpoint, Duration warmupSleepDuration) {
        log.info("calling API once to warm it up...");
        callApi(endpoint, 0);
        log.info("sleeping for {}", warmupSleepDuration);
        Thread.sleep(warmupSleepDuration.toMillis());

    }

    /**
     * performance test the endpoint
     */
    private static void testSearchEndpoint(int numIterations, String searchEndpoint) {

        // using CompletionService as ExecutorService decorator to drain the tasks
        CompletionService<ResultStatus> completionService = new ExecutorCompletionService<>(EXECUTOR);

        // submit the tasks
        long startTimeMillis = System.currentTimeMillis();
        log.info("starting perf test ... i={} endpoint={} ", numIterations, searchEndpoint);
        for (int i = 1; i <= numIterations; ++i) {
            final int requestNumber = i + 1;
            completionService.submit(() -> {
                // rate limit the api call
                RATE_LIMITER.acquirePermission();
                return callApi(searchEndpoint, requestNumber);
            });
        }

        long submitElapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000;

        // wait for tasks to complete
        log.info("Completed submitting tasks in {}", submitElapsedTime);
        try {
            for (int i = 0; i < numIterations; i++) {
                Future<ResultStatus> f = completionService.take();
                ResultStatus resultStatus = f.get();

                // increment success or failure
                resultMap.merge(resultStatus.name(), 1L, Long::sum);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        long executionElapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000;
        log.info("Completed making {} requests in {} with TPS={} resultMap={}",  numIterations, executionElapsedTime, (float) numIterations / executionElapsedTime, resultMap);

    }

    public static ResultStatus callApi(String endpointUrl, int requestNumber) {

        try {
            String response = WEB_CLIENT.get()
                    .uri(endpointUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Response " + requestNumber + ": responseSize=" + response.toString().length());
            return ResultStatus.SUCCESS;
        } catch (Exception ex) {
            log.error("Error calling API: {}", ex.getMessage());
            return ResultStatus.FAILURE;
        }
    }

    public enum ResultStatus {
        SUCCESS,
        FAILURE;
    }

    public static RateLimiter createRateLimiter(int permitsPerSecond) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(permitsPerSecond)
                .timeoutDuration(Duration.ofSeconds(5))
                .build();

        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        return rateLimiterRegistry
                .rateLimiter("perf-test-ratelimiter", config);

    }
}
