package co.shipz.auth.config;

import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TimedExecutorBeans {
  @Bean(name = "jdbi")
  public Executor timeoutExecutor() {
    return TimedCompletables
      .timed(
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2),
        Duration.ofSeconds(2)
      );
  }
}
