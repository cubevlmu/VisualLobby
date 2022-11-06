package io.flybird.platform.test;

import io.flybird.platform.logger.Logger;
import io.flybird.platform.models.ModelManger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class PlatformTest {
  private final Logger logger = Logger.createOwnLogger(this);
  
  private final ArrayList<Class<?>> allTests;
  
  private final ReportBuilder builder;
  
  public PlatformTest(ModelManger manger) {
    HashMap<Annotation[], Class> clazz = manger.getAllClass();
    this.allTests = sync(clazz);
    this.builder = new ReportBuilder(String.format("%s/Plugins/VisualManger/DebugLogs.log", System.getProperty("user.dir")));
  }
  
  private ArrayList<Class<?>> sync(HashMap<Annotation[], Class> all) {
    ArrayList<Class<?>> resultList = new ArrayList<>();
    for (Annotation[] anno : all.keySet()) {
      if (anno[0] instanceof PluginTest)
        resultList.add(all.get(anno)); 
    } 
    System.gc();
    return resultList;
  }
  
  private HashMap<String, TestMtdInfo> initTestes() throws Exception {
    HashMap<String, TestMtdInfo> mtdList = new HashMap<>();
    for (Object clazz : this.allTests) {
      Object instance = ((Class)clazz).newInstance();
      for (Method mtd : ((Class)clazz).getMethods()) {
        if ((mtd.getAnnotations()).length != 0) {
          Annotation result = mtd.getAnnotations()[0];
          if (result instanceof TestMethod)
            mtdList.put(String.format("%s.%s", ((Class)clazz)

                    .getName(), ((TestMethod)result)
                    .name()), new TestMtdInfo(instance, mtd, new Object[0]));
        } 
      } 
    } 
    return mtdList;
  }
  
  public String getResult() {
    return this.builder.toString();
  }
  
  public String toString() {
    return getResult();
  }
  
  public void release() {
    this.allTests.clear();
    System.gc();
  }
  
  public PlatformTest runTestes() {
    try {
      HashMap<String, TestMtdInfo> result = initTestes();
      for (String item : result.keySet()) {
        this.logger.info("On Plugin Test Running\n at -> %s", item);
        boolean end = true;
        Exception record = null;
        TestMtdInfo obj = result.get(item);
        String printResult = "{NoResult Or Void}";
        try {
          Object object = obj.method.invoke(obj.instance, obj.args);
          if (object != null)
            printResult = object.toString(); 
        } catch (Exception e) {
          this.logger.error("Error Test Method -> %s", e.getLocalizedMessage());
          end = false;
          record = e;
        } 
        String res = this.builder.addRequest(item, printResult, end, record);
        if (res == null)
          throw new NullPointerException("Error When Report"); 
      } 
      result.clear();
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error(e);
    } 
    this.builder.save();
    System.gc();
    return this;
  }

  public record TestMtdInfo(Object instance, Method method, Object[] args) {

  }
}
