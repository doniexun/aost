package org.tellurium.bundle

import org.tellurium.dispatch.Dispatcher
import org.tellurium.config.Configurable
import org.tellurium.dsl.WorkflowContext
import org.tellurium.locator.MetaCmd
import org.stringtree.json.JSONReader

/**
 * Command Bundle Processor
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: May 19, 2009
 * 
 */

@Singleton
public class CommandBundleProcessor implements Configurable {

  public static final String OK = "ok";

  //sequence number for each command
  private int sequence = 0;

  //maximum number of commands in a bundle
  private int maxBundleCmds = 5;

  private Dispatcher dispatcher = new Dispatcher();

  private CommandBundle bundle = new CommandBundle();

  //whether to use the bundle feature
  private boolean exploitBundle = true;

  private JSONReader reader = new JSONReader();

  public void useBundleFeature(){
    this.exploitBundle = true;
  }

  public void disableBundleFeature(){
    this.exploitBundle = false;
  }

  public int nextSeq(){
    return ++sequence;
  }

  //TODO: how to parse the returning result?
  protected def parseReturnValue(String value){

    if(value.startsWith("OK,")){
      value = value.substring(3);
    } else {
      return null;
    }

    List list = reader.read(value);
    if(list != null && list.size() > 0){
      //only return one result for the time being since call is always sychronized
      //The result type is a map from JSON
//      CmdResponse resp = list.get(0);
      def resp = list.get(0);
      def result = resp.get(CmdResponse.RETURN_RESULT);

      ReturnType type = ReturnType.valueOf(resp.get(CmdResponse.RETURN_TYPE).toUpperCase());

      switch(type){
        case ReturnType.VOID:
          break;
        case ReturnType.BOOLEAN:
          return "true".equals(result);
          break;
        case ReturnType.NUMBER:
//          return Integer.parseInt(result);
          return (int)result;
          break;
        case ReturnType.STRING:
          return result;
          break;
        case ReturnType.ARRAY:
          //TODO: need to really convert the String array to the object array 
          return result;
          break;
      }
    }

    return null;
  }

  def issueCommand(WorkflowContext context, String uid, String name, args){
    CmdRequest cmd = new CmdRequest(nextSeq(), uid, name, args);
    if(bundle.shouldAppend(cmd)){
      bundle.addToBundle(cmd);
      if(bundle.size() >= this.maxBundleCmds){
        //need to issue the command bundle since it reaches the maximum limit
        String json = bundle.extractAllAndConvertToJson();

        String val = dispatcher.issueBundle(json);
        return parseReturnValue(val);
      }

      return OK;
    }else{
      String json = bundle.extractAllAndConvertToJson();
      bundle.addToBundle(cmd);
      
      String val = dispatcher.issueBundle(json);
      return parseReturnValue(val);
    }
  }

  def passThrough(WorkflowContext context, String uid, String name, args){
    //if no command on the bundle, call directly
    if(bundle.size() == 0){
      return dispatcher.metaClass.invokeMethod(dispatcher, name, args);
    }else{
      //there are commands in the bundle, pigback this command with the commands in a bundle and issue it
      CmdRequest cmd = new CmdRequest(nextSeq(), uid, name, args);
      bundle.addToBundle(cmd);
      String json = bundle.extractAllAndConvertToJson();
      
      String val = dispatcher.issueBundle(json);
      return parseReturnValue(val);
    }
  }

  //push remaining commands in the bundle to the Engine before disconnect from it
  def flush(){
    if(bundle.size() > 0){
      String json = bundle.extractAllAndConvertToJson();

      String val = dispatcher.issueBundle(json);
      return parseReturnValue(val);      
    }

    return null;
  }

  protected Object[] removeWorkflowContext(Object[] args){
    List list = new ArrayList();
    for(int i=1; i<args.length; i++){
      list.add(args[i]);
    }

    if(list.size() > 0)
      return list.toArray();

    return null;
  }

  def process(String name, args) {
    WorkflowContext context = args[0]
    String uid = null;
    MetaCmd cmd = context.extraMetaCmd();
    if(cmd != null)
      uid = cmd.uid;
    Object[] params = this.removeWorkflowContext(args);

    if(this.exploitBundle && context.isBundlingable()){
      return issueCommand(context, uid, name, params);
    }

    return passThrough(context, uid, name, params);  
  }

  protected def methodMissing(String name, args) {
    return process(name, args)
  }
}