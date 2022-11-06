package io.flybird.platform.multhd;

import io.flybird.platform.logger.Logger;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AsyncTask extends BukkitRunnable {
  private final Logger logger;
  
  public AsyncTask(String name) {
    this.logger = Logger.createOwnLogger(name);
  }
  
  public void Dispose() {
    cancel();
    System.gc();
  }
  
  public boolean GetMode() {
    return isCancelled();
  }
  
  public void run() {
    OnDone();
    logger.info("Run Task Done");
  }
  
  public abstract void Run();
  
  public void OnDone() {
    System.gc();
  }
}
