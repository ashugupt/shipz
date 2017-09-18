package co.shipz.auth.config;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryWithNamePrefix implements ThreadFactory {
  private static final AtomicInteger poolNumber = new AtomicInteger(1);
  private final ThreadGroup group;
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  private final String namePrefix;

  ThreadFactoryWithNamePrefix(String prefix) {
    SecurityManager s = System.getSecurityManager();
    group = (s != null) ? s.getThreadGroup()
      : Thread.currentThread().getThreadGroup();
    namePrefix = prefix + "-"
      + poolNumber.getAndIncrement()
      + "-thread-";
  }

  @Override
  public Thread newThread(@Nonnull Runnable r) {
    Thread t = new Thread(group, r,
      namePrefix + threadNumber.getAndIncrement(),
      0);
    if (t.isDaemon()) {
      t.setDaemon(false);
    }
    if (t.getPriority() != Thread.NORM_PRIORITY) {
      t.setPriority(Thread.NORM_PRIORITY);
    }
    return t;
  }
}
