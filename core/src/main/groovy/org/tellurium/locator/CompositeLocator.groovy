package org.telluriumsource.locator

import static org.telluriumsource.Const.*
import org.json.simple.JSONObject

/**
 *  The composite locator which is used to automatically generate the relative
 *  xpath by given attributes and parameters.
 *
 *
 *  @author Jian Fang (John.Jian.Fang@gmail.com)
 * 
 */
class CompositeLocator {
    //obtained from UI object and should not be specified in clocator
    String namespace

    public static final String HEADER = "header"
    String header

    public static final String TAG = "tag"
    String tag

    public static final String TEXT = "text"
    String text

    public static final String TRAILER = "trailer"
    String trailer

    public static final String POSITION = "position"
    def position

    public static final String DIRECT = "direct"
    boolean direct = false
    boolean inside = false
    Map<String, String> attributes = [:]

    JSONObject toJSON(){
      JSONObject jso = new JSONObject()
      if(header != null && header.trim().length() > 0)
        jso.put(HEADER, header)
      if(tag != null && tag.trim().length() > 0)
        jso.put(TAG, tag)
      if(trailer != null && trailer.trim().length() > 0)
        jso.put(TRAILER, trailer)
      if(position != null)
        jso.put(POSITION, position)
      if(direct)
        jso.put(DIRECT, direct)

      attributes.each {key, val->
        jso.put(key, val)
      }

      return jso
    }

    public String getTag(){
        return tag
    }

/*    public boolean isInside(){
      return this.inside
    }*/
  
    public boolean isIdIncluded(){
      boolean result = false
      String id = attributes.get(ID)
      if(id != null && id.trim().length() > 0){
        //only consider ID included if there are really id provided
        if(!(id.startsWith(NOT_PREFIX) || id.startsWith(START_PREFIX) || id.startsWith(END_PREFIX) ||  id.startsWith(ANY_PREFIX))){
          result = true
        }
      }

      return result
    }

   //handy method
    public boolean noIdIncluded(){
      return !isIdIncluded()
    }

    public String generateHtml(boolean closeTag){ 
      if(tag == null)
        return "";

      StringBuffer sb = new StringBuffer(64);
      sb.append("<").append(tag);
      if(attributes != null && attributes.size() > 0){
        attributes.each {String key, String val ->
          String aval = val;
          if((val.startsWith(NOT_PREFIX) || val.startsWith(START_PREFIX) || val.startsWith(END_PREFIX) || val.startsWith(ANY_PREFIX))){
            aval = val.substring(1);
          }else if(val.startsWith(CONTAIN_PREFIX)){
            aval = val.substring(2);
          }
          sb.append(" ${key}=\"${aval}\"");
        }
      }
      if(closeTag){
        if (text != null && text.trim().length() > 0) {
          String atext = text;
          if ((text.startsWith(NOT_PREFIX) || text.startsWith(START_PREFIX) || text.startsWith(END_PREFIX) || text.startsWith(ANY_PREFIX))) {
            atext = text.substring(1);
          }else if(text.startsWith(CONTAIN_PREFIX)){
            atext = text.substring(2);
          }
          sb.append(">${atext}</${tag}>")
        }else{
          sb.append("/>")
        }
      }else{
        if (text != null && text.trim().length() > 0) {
          String atext = text;
          if ((text.startsWith(NOT_PREFIX) || text.startsWith(START_PREFIX) || text.startsWith(END_PREFIX) || text.startsWith(ANY_PREFIX))) {
            atext = text.substring(1);
          }else if(text.startsWith(CONTAIN_PREFIX)){
            atext = text.substring(2);
          }
          sb.append(">${atext}")
        }else{
          sb.append(">")
        }
      }

      return sb.toString();
    }

    public String generateCloseTag(){
      if(tag == null)
        return "";

       return "</${tag}>"
    }
}