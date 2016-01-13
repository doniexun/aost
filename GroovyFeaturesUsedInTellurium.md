

## Groovy Features Used in Tellurium ##

In Tellurium, we used many Groovy features. We like to list some of them as follows,

### `BuildSupport` ###

BuildSupport is very powerful for parsing nested definitions, such as XML markup. In Tellurium, the buildsupport is used as UI object definition parser and it is one of the bases for our internel DSL.

```
UiDslParser extends BuilderSupport{
   
      protected void setParent(Object parent, Object child) {
           if(parent instanceof Container){
               parent.add(child)
               child.parent = parent
           }
       }

       protected Object createNode(Object name) {
           def builder = builderRegistry.getBuilder(name)

           if(builder != null){
                def obj =  builder.build(null, null)

                return obj
           }

           return null 
       }

      //should not come here for Our DSL
       protected Object createNode(Object name, Object value) {

           return null
       }

       protected Object createNode(Object name, Map map) {
           def builder = builderRegistry.getBuilder(name)

           if(builder != null){
                def obj =  builder.build(map, null)                return obj
           }  

           return null
       }

       protected Object createNode(Object name, Map map, Object value) {
           def builder = builderRegistry.getBuilder(name)

           if(builder != null){
                def obj =  builder.build(map, (Closure)value)

               return obj
           }  

          return null
       }

       protected void nodeCompleted(Object parent, Object node) {
      //when the node is completed and its parent is null, it means this node is at the top level
          if(parent == null){
               UiObject uo = (UiObject)node
               //only put the top level nodes into the registry
               registry.put(uo.uid, node)
          }

       }

   }

```

### Dynamic Scripting ###

In Groovy, you can define Groovy code as a String and then run them at run time, i.e, code generates code, which makes pure DSL tests possible. Our DslScriptExecutor is a good example and can demonstrate the power of the dynamic scripting.

```
class DslScriptExecutor {

    static void main(String[] args){
       if(args != null && args.length == 1){
            def dsl = new File(args[0]).text
            def script = """
                import aost.dsl.DslScriptEngine

                class DslTest extends DslScriptEngine{
                    def test(){
                        init()
                        ${dsl}
                        shutDown()
                    }
                }

                DslTest instance = new DslTest()
                instance.test()
            """

            new GroovyShell().evaluate(script)

       }else{
           println("Usage: DslScriptExecutor dsl_file")
       }

    }
}
```

### `GroovyInterceptable` ###

GroovyInterceptable can intercept all method calls so that you can do some processing before or after the invocation. Or you can delegate the method calls to another class.

For example, The Tellurium dispatcher will delegate all method calls it received to Selenium Client as follows,

```
class Dispatcher implements GroovyInterceptable{

    private SeleniumClient sc  = new SeleniumClient()
   
    def invokeMethod(String name, args)
      {
         return sc.client.metaClass.invokeMethod(sc.client, name, args)
      }
}
```

### methodMissing ###

In Groovy, you can use "methodMissing" to intercept and delegate undefined method calls. For example, in DslScriptEngine, the "methodMissing" method will catch and delegate all methods to DslAostSeleneseTestCase except the "init", "openUrl", and "shutDown" methods:

```
class DslScriptEngine extends DslContext{

    protected def methodMissing(String name, args) {
         if(name == "init")
            return init()
         if(name == "openUrl")
            return openUrl(args)
         if(name == "shutDown")
            return shutDown()
        
         if(DslAostSeleneseTestCase.metaClass.respondsTo(aost, name, args)){
              return aost.invokeMethod(name, args)
         }

        throw new MissingMethodException(name, DslScriptEngine.class, args)
     }
}
```

### Singleton ###

You can Use Groovy MetaClass to define a singleton, which is the right Groovy way to define a singleton. For example,  we have the EventHandler class

```
class EventHandler{
}
```

Then, we can define its MetaClass as follows,
```
class EventHandlerMetaClass extends MetaClassImpl{

    private final static INSTANCE = new EventHandler()

    EventHandlerMetaClass() { super(EventHandler) }

    def invokeConstructor(Object[] arguments) { return INSTANCE }
}
```

After that, we register the metaClass:
```
def registry = GroovySystem.metaClassRegistry
registry.setMetaClass(EventHandler, new EventHandlerMetaClass())
```

In this way, all
```
Eventhandler handler = new EventHandler()
```

will return the same Instance, i.e., it is a singleton. Our Tellurium framework heavily depends on this pattern so that we do not need to use another framework to wiring different object together.

Groovy 1.7.0 supports the `@Singleton` annotation to make the singleton definition much easier. For example, we define the BundleProcessor class as follows.

```
@Singleton
public class BundleProcessor implements Configurable {
...
}
```

### GString ###

GString can be used to embed variable in a String and the value will be resolved at run time. This makes it very convenient to write XPath template. For example, In the Selector object, we have

```
class Selector extends UiObject {

    def selectByLabel(String target, Closure c){
        c(locator, "label=${target}")
    }

    def selectByValue(String target, Closure c){

        c(locator, "value=${target}")()
    }
}

```

### Optional Type ###

In Groovy you can specify a variable type if you know the type and you want to do Type check at compile time. If you do not care about the type or want the method to be more flexible, you do not need to define the type and just use "def" to define a variable without specifying its type.

For example, in the composite locator class we specified the Type of all variables except the position. Because type of UI component position might be a single type.

```
class CompositeLocator {
    String header
    String tag
    String text
    String trailer
    def position
    Map<String, String> attributes = [:]
}
```

### Closure ###

Closure is also used in Tellurium frequently. For example, in the DslContext class we define the closure as

```
def selectByLabel(String uid, String target){
    WorkflowContext context = WorkflowContext.getDefaultContext()
    ui.walkTo(context, uid)?.selectByLabel(target){ loc, optloc ->
        String locator = locatorMapping(context, loc)
        eventHandler.select(locator, optloc)
    }
}
```

In the Selector object, it will run the closure:

```
class Selector extends UiObject {
    def selectByLabel(String target, Closure c){
        c(locator, "label=${target}")
    }
}
```

i.e, it will pass in UI specific parameters to the closure and run it. What the closure actually does is to create the runtime locator and call the eventhandler to fire the select event to selenium server. Be aware, because the scope of closure, the eventhandler and the WorkflowContext defined in the DslContext class are still available to the closure even the closure is run inside the Selector object. Amazing, right?

### Groovy Syntax ###

In Groovy, the syntax is very expressive, for example, the DslContext class defines a lot of methods such as:

```
def doubleClick(String uid){}
```

and it can be written as

```
doubleClick uid
```

and the above is one of the basic DSLs for Tellurium.

The Map definition is also very expressive and useful. Tellurium locators use map to define xpath or UI component attributes. For example:

```
ui.Container(uid: "google_start_page", clocator: [tag: "td"], group: "true"){
   InputBox(uid: "searchbox", clocator: [title: "Google Search"])
   SubmitButton(uid: "googlesearch", clocator: [name: "btnG", value: "Google Search"])
   SubmitButton(uid: "Imfeelinglucky", clocator: [value: "I'm Feeling Lucky"])
}
```

### Varargs ###

Groovy followed a convention if you wanted a method to have varargs: the last argument should be declared as `Object[]`. For example, in DslContext, we have the onWidget method:

```
def onWidget(String uid, String method, Object[] args){
......
}
```

The onWidget method can be expressed in DSL syntax as follows,

```
onWidget "DOJO_DatePicker", selectYear, 1992
```

### propertyMissing ###

Groovy also supports propertyMissing for dealing with property resolution attempts. For example, in DslContext, we define

```
    def propertyMissing(String name) {
        println "Warning: property ${name} is missing, treat it as a String. "
        name
    }
```

That is to say, for the missing property, we will return it as a String. This is very useful for the "onWidget" method. Without the propertyMissing, we have to use

```
onWidget "DOJO_DatePicker", "selectYear", 1992
```

With the propertyMissing, we can use the method name directly:

```
onWidget "DOJO_DatePicker", selectYear, 1992
```

### Delegate ###

Groovy 1.6 provides a set of Java annotations for meta programming, the `@Delegate` is one of them. With the `@Delegate` transformation, a class field or property can be annotated and become an object to which method calls are delegated. Tellurium widget class used this feature.

```
abstract class Widget extends UiObject {

  @Delegate
  private WidgetDslContext dsl = new WidgetDslContext();

  ...
}
```

That is to say, all DSL calls will be delegated to the WidgetDslContext class.

### Grape ###

Grape is the infrastructure enabling the grab() calls in Groovy, a set of classes leveraging Ivy to allow for a repository driven module system for Groovy. Grape will, at runtime, download as needed and link the named libraries and all dependencies forming a transitive closure when the script is run from existing repositories such as Ibiblio, Codehaus, and java.net.

Tellurium rundsl.groovy used Grape to download dependencies for DSL scripts.

```
import groovy.grape.Grape;

Grape.grab(group:'org.telluriumsource', module:'tellurium-core', version:'0.7.0', classLoader:this.class.classLoader.rootLoader)
...

import org.telluriumsource.dsl.DslScriptExecutor

def runDsl(String[] args) {
  def cli = new CliBuilder(usage: 'rundsl.groovy -[hf] [scriptname]')
  cli.with {
    h longOpt: 'help', 'Show usage information'
    f longOpt: 'scriptname',   'DSL script name'
  }

  ...

}

println "Running DSL test script, press Ctrl+C to stop."

runDsl(args)
```