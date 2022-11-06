package io.flybird.platform.test;

import java.io.File;
import java.io.FileWriter;

import io.flybird.platform.logger.Logger;

public class ReportBuilder {
  private final Logger logger = Logger.createOwnLogger(this);
  
  private final File outputFile;
  
  private final StringBuilder builder = new StringBuilder();
  
  public ReportBuilder(String path) {
    this.outputFile = new File(path);
    this.logger.info("Output Path -> %s", path);
  }
  
  public String getString() {
    return this.builder.toString();
  }
  
  public String toString() {
    return getString();
  }
  
  public void save() {
    try {
      FileWriter writer = new FileWriter(this.outputFile.getPath(), false);
      writer.write(this.builder.toString());
      writer.flush();
      writer.close();
    } catch (Exception e) {
      this.logger.error(e);
    } 
  }
  
  public String addRequest(String name, String result, boolean isEndTrue, Exception e) {
    this.builder.append("===============\n")
      .append("ClassName/MethodName -> ").append(name).append("\n")
      .append("TestResult -> ").append(result).append("\n")
      .append("IsResultRight? -> ").append(isEndTrue).append("\n");
    if (!isEndTrue) {
      this.builder.append("Exception -> ").append(e.toString()).append("\n");
      for (StackTraceElement stack : e.getStackTrace())
        this.builder.append("Stack Space -> ").append(stack.toString()).append("\n"); 
      this.builder.append("Exception From -> ").append(e.getCause()).append("\n");
    } 
    return this.builder.toString();
  }
}
