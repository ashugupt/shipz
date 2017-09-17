package co.shipz.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public Executor getAsyncExecutor() {
    return CompletableExecutors.completable(Executors.newFixedThreadPool(10));
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (ex, method, params) -> logger.error("Uncaught async error", ex);
  }
}
