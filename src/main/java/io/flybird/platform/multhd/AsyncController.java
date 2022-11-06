package io.flybird.platform.multhd;

import java.util.ArrayList;
import java.util.List;

import io.flybird.platform.logger.Logger;
import io.flybird.platform.logger.SharedLoggers;

public class AsyncController {
  private final Thread testforthd;
  
  private final Logger logger = SharedLoggers.getShardLoggers("Platform");
  
  private final List<IAsyncTask> tasks = new ArrayList<>();
  
  private boolean isInvoking;
  
  public void addTask(IAsyncTask task) {
    this.tasks.add(task);
    task.init();
  }
  
  public void startListening() {
    this.isInvoking = true;
    this.testforthd.start();
  }
  
  public void end() {
    this.isInvoking = false;
    this.testforthd.interrupt();
    this.tasks.clear();
    System.gc();
  }
  
  public AsyncController() {
    this.isInvoking = false;
    this.testforthd = new Thread(this::onTest);
    this.testforthd.setDaemon(true);
    this.testforthd.setName("TestForThread");
  }
  
  private void onTest() {
    while (this.isInvoking) {
      if (this.tasks.size() > 0) {
        ArrayList<IAsyncTask> toDoList = new ArrayList<>(this.tasks);
        for (IAsyncTask task : toDoList) {
          try {
            boolean result = task.onAction();
            if (!result) {
              task.error();
              continue;
            } 
            task.done();
          } catch (Exception e) {
            this.logger.error("Error In Invoking Task : %s", e.getLocalizedMessage());
            task.error();
          } 
        } 
        this.tasks.removeAll(toDoList);
        System.gc();
      } 
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } 
    } 
  }
}
