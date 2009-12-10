package org.telluriumsource.bundle

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jun 4, 2009
 * 
 */

public class UiModuleState {

  private String id;

  //whether publish to the Engine
  private boolean published = false;

  def UiModuleState(String id, boolean published) {
    this.id = id;
    this.published = published;
  }

  public boolean hasBeenPublished(){
    return this.published;
  }

  public void setState(boolean published){
    this.published = published;
  }
}