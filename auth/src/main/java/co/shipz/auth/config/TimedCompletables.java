package co.shipz.auth.config;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

class TimedCompletables {
  static Executor timed(ExecutorService executorService, Duration duration) {
    return new CompletableExecutors.TimeoutExecutorService(executorService, duration);
  }
}
