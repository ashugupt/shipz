package co.shipz.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class ExecutorBeans {
  @Bean(name = "kafkaExecutor")
  public Executor kafkaExecutor() {
    return Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors() * 2,
      new ThreadFactoryWithNamePrefix("kafka")
    );
  }
}
