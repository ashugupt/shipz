package co.shipz.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class TimedExecutorBeans {
  @Bean(name = "jdbiAsyncExecutor")
  public Executor jdbiTimeoutExecutor() {
    return TimedCompletables
      .timed(
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2),
        Duration.ofSeconds(2)
      );
  }
}
