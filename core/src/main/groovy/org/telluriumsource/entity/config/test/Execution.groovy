package org.telluriumsource.entity.config.test

import org.json.simple.JSONObject

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Apr 7, 2010
 * 
 * 
 */
class Execution {
  //whether to trace the execution timing
  public static String TRACE = "trace";
  boolean trace = false;

  def Execution() {
  }

  def Execution(Map map) {
    this.trace = map.get(TRACE);
  }

  public JSONObject toJSON(){
    JSONObject obj = new JSONObject();
    obj.put(TRACE, this.trace);
    
    return obj;
  }
}
