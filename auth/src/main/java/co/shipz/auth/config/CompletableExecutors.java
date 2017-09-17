package co.shipz.auth.config;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

final class CompletableExecutors {
  static CompletableExecutorService completable(ExecutorService delegate) {
    return new DelegatingCompletableExecutorService(delegate);
  }

  static class DelegatingCompletableExecutorService extends DelegatingExecutorService implements CompletableExecutorService {

    DelegatingCompletableExecutorService(ExecutorService threads) {
      super(threads);
    }

    @Override
    @Nonnull
    public <T> CompletableFuture<T> submit(@Nonnull Callable<T> task) {
      final CompletableFuture<T> cf = new CompletableFuture<>();
      delegate.submit(() -> {
        try {
          cf.complete(task.call());
        } catch (CancellationException e) {
          cf.cancel(true);
        } catch (Exception e) {
          cf.completeExceptionally(e);
        }
      });
      return cf;
    }

    @Override
    @Nonnull
    public <T> CompletableFuture<T> submit(@Nonnull Runnable task, T result) {
      return submit(Executors.callable(task, result));
    }

    @Override
    @Nonnull
    public CompletableFuture<?> submit(@Nonnull Runnable task) {
      return submit(Executors.callable(task));
    }
  }

  public interface CompletableExecutorService extends ExecutorService {
    @Override
    @Nonnull
    <T> CompletableFuture<T> submit(@Nonnull Callable<T> task);

    @Override
    @Nonnull
    <T> CompletableFuture<T> submit(@Nonnull Runnable task, T result);

    @Override
    @Nonnull
    CompletableFuture<?> submit(@Nonnull Runnable task);
  }

  static class TimeoutExecutorService extends CompletableExecutors.DelegatingCompletableExecutorService {
    private final Duration timeout;
    private final ScheduledExecutorService schedulerExecutor;

    TimeoutExecutorService(ExecutorService delegate, Duration timeout) {
      super(delegate);
      this.timeout = timeout;
      schedulerExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    @Override
    @Nonnull
    public <T> CompletableFuture<T> submit(@Nonnull Callable<T> task) {
      CompletableFuture<T> cf = new CompletableFuture<>();
      Future<?> future = delegate.submit(() -> {
        try {
          cf.complete(task.call());
        } catch (CancellationException e) {
          cf.cancel(true);
        } catch (Throwable ex) {
          cf.completeExceptionally(ex);
        }
      });

      schedulerExecutor.schedule(() -> {
        if (!cf.isDone()) {
          cf.completeExceptionally(new TimeoutException("Timeout after " + timeout));
          future.cancel(true);
        }
      }, timeout.toMillis(), TimeUnit.MILLISECONDS);
      return cf;
    }
  }

  static class DelegatingExecutorService implements ExecutorService {
    ExecutorService delegate;

    DelegatingExecutorService(ExecutorService executorService) {
      this.delegate = executorService;
    }

    @Override
    public void shutdown() {
      delegate.shutdown();
    }

    @Override
    @Nonnull
    public List<Runnable> shutdownNow() {
      return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
      return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
      return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, @Nonnull TimeUnit unit) throws InterruptedException {
      return delegate.awaitTermination(timeout, unit);
    }

    @Override
    @Nonnull
    public <T> Future<T> submit(@Nonnull Callable<T> task) {
      return delegate.submit(task);
    }

    @Override
    @Nonnull
    public <T> Future<T> submit(@Nonnull Runnable task, T result) {
      return delegate.submit(task, result);
    }

    @Override
    @Nonnull
    public Future<?> submit(@Nonnull Runnable task) {
      return delegate.submit(task);
    }

    @Override
    @Nonnull
    public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks) throws InterruptedException {
      return delegate.invokeAll(tasks);
    }

    @Override
    @Nonnull
    public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks, long timeout, @Nonnull TimeUnit unit)
      throws InterruptedException {

      return delegate.invokeAll(tasks, timeout, unit);
    }

    @Override
    @Nonnull
    public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {

      return delegate.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks, long timeout, @Nonnull TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {

      return delegate.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(@Nonnull Runnable command) {
      delegate.execute(command);
    }
  }
}
