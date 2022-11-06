package io.flybird.platform.multhd;

import java.util.Arrays;

import io.flybird.platform.logger.Logger;

public class AsyncRunModel {
  private final Thread thread;
  
  private final Logger ph;
  
  public AsyncRunModel(String modelName) {
    this.ph = Logger.createOwnLogger("AsyncRunModel_" + modelName);
    this.thread = new Thread(this::OnRun);
    this.thread.setName(modelName);
    this.thread.setDaemon(true);
    this.ph.info("Internalized Done");
  }
  
  public void Run() {
    this.thread.run();
    this.ph.info("AsyncRunTask Begin");
  }
  
  public void Stop() {
    try {
      this.thread.join();
      this.thread.interrupt();
      this.ph.info("AsyncRunTask Stop");
    } catch (InterruptedException e) {
      throw new RuntimeException(new RuntimeException("AsyncRuntimeModel Error When stop at " + Arrays.toString(e.getStackTrace()) + " is " + e.getLocalizedMessage()));
    } 
  }
  
  public void Dispose() {
    Stop();
    this.ph.info("AsyncRunTask Disposed!");
    System.gc();
  }
  
  public void OnRun() {
    OnDone();
  }
  
  public void OnDone() {
    System.gc();
  }
}
