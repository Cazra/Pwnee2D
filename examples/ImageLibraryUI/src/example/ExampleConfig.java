package example;

import pwnee.fileio.*;
import java.io.Serializable;
import java.util.HashMap;

/** A saveable/loadable HashMap of persistent variables used by this UI. */
public class ExampleConfig implements Serializable {
  public static String path = "imgLibConfig.dat";
  
  public HashMap<String, String> vars = new HashMap<String,String>();
  
  public ExampleConfig() {
    
  }
  
  public static ExampleConfig load() {
    ObjectFileIO ofio = new ObjectFileIO();
    Object obj = ofio.loadObject(ExampleConfig.path);
    if(obj != null && obj instanceof ExampleConfig)
      return (ExampleConfig) obj;
    else {
      ExampleConfig config = new ExampleConfig();
      config.setDefaults();
      return config;
    }
  }
  
  
  
  public String get(String key) {
    return vars.get(key);
  }
  
  public void put(String key, String value) {
    vars.put(key, value);
  }
  
  
  public void save() {
    ObjectFileIO ofio = new ObjectFileIO();
    ofio.saveObject(this, ExampleConfig.path);
  }
  
  
  public void setDefaults() {
    vars.put("lastOpen", ".");
  }
  
}
