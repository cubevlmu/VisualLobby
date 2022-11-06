package io.flybird.platform.multhd;

public interface IAsyncTask {
  void init();
  
  boolean onAction();
  
  void done();
  
  void error();
}
