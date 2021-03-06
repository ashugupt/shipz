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
        Executors.newFixedThreadPool(
          Runtime.getRuntime().availableProcessors() * 2 + 2,
          new ThreadFactoryWithNamePrefix("jdbi")
        ),
        Duration.ofSeconds(2)
      );
  }

  @Bean(name = "servletAsyncExecutor")
  public Executor servletTimeoutExecutor() {
    return TimedCompletables
      .timed(
        Executors.newFixedThreadPool(
          Runtime.getRuntime().availableProcessors() * 2 + 2,
          new ThreadFactoryWithNamePrefix("servlet")
        ),
        Duration.ofSeconds(2)
      );
  }
}
