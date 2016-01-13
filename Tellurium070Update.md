

# Introduction #

Tellurium 0.7.0 release is out now. Over 250 issues have been closed. The issues include new features, bugs fixings, and other enhancements requested by users. There are some fundamental changes in Tellurium 0.7.0 compared with Tellurium 0.6.0 such as the group locating algorithm, UI module caching, Macro command, jQuery-based new APIs, and i18n support. To make your life easier, We list all the changes here so that you can update your existing Tellurium testing project accordingly.

The architecture of Tellurium 0.7.0 has been changed and the system diagram is shown as follows,

http://tellurium-users.googlegroups.com/web/telluriumnewarchitecture070.png?gda=A9o6hlEAAAA7fMi2EBxrNTLhqoq3FzPrHtAOAsxxNa15yodhsuIGtSj9p0mTDab5g6bwssabHmxXgg0UR0O9X9-Irzu_uB8AUwk_6Qi3BU8HCN0q6OYwM5VxXgp_nHWJXhfr7YhqVgA

The main changes include
  * The bundle tier to support Macro Command
  * New Tellurium API in Engine
  * Santa group locating algorithm
  * UI Module based Caching
  * Tellurium UID description language (UDL)
  * Runtime Environment

The details are covered in the subsequent sections.

# Changes in Tellurium Core #

## Package Name ##

Tellurium Core used the package name "org.tellurium", but we do not actually own the domain "tellurium.org". Our Tellurium team came out with a domain name "telluriumsource.org" and have registered this domain name. As a result, we changed the package name from "org.tellurium" to "org.telluriumsource".

In your code, if you have the following import statement,

```
import org.tellurium.dsl.DslContext
```

please change it to

```
import org.telluriumsource.dsl.DslContext
```

Accordingly, the Maven dependency should be changed as follows,

```
    <groupId>org.telluriumsource</groupId>
    <artifactId>tellurium-core</artifactId>
    <version>0.7.0-SNAPSHOT</version>
```

## Package Structure ##

The original core package structure was a flat one and it has been changed as follows,

```
`-- org
    `-- telluriumsource
        |-- component
        |   |-- bundle
        |   |-- client
        |   |-- connector
        |   |-- custom
        |   |-- data
        |   |-- dispatch
        |   `-- event
        |-- crosscut
        |   |-- i18n
        |   |-- log
        |   `-- trace
        |-- dsl
        |-- entity
        |-- exception
        |-- framework
        |   |-- bootstrap
        |   `-- config
        |-- server
        |-- test
        |   |-- ddt
        |   |   `-- mapping
        |   |       |-- bind
        |   |       |-- builder
        |   |       |-- io
        |   |       |-- mapping
        |   |       |-- type
        |   |       `-- validator
        |   |-- groovy
        |   |-- java
        |   |-- mock
        |   `-- report
        |-- ui
        |   |-- builder
        |   |-- locator
        |   |-- object
        |   |   `-- routing
        |   `-- widget
        `-- util
            `-- grid
```


## CSS Selector ##

One feedback we got from [the Rich Web Experience 2009](http://code.google.com/p/aost/wiki/TelluriumAtRichWebExperience2009) is that we should use the name "CSS selector" instead of "jQuery selector" because jQuery implements a subset of CSS selectors. As a result, we changed the following two methods in DslContext

```
public void enableJQuerySelector();
public void disableJQuerySelector(); 
```

to

```
public void enableCssSelector();
public void disableCssSelector(); 
```

## Group Locating ##

Up to Tellurium 0.6.0, Tellurium still generates runtime locators such as XPath and CSS selector on the Tellurium Core side, then pass them to Selenium Core, which is basically still to locate one element in the UI module at a time. With the new Engine in Tellurium 0.7.0, the UI module will be located as a whole first, the subsequent calls will reuse the already located UI element in the DOM.

Group Locating has some fundamental impacts on Tellurium and this can be explained by an example.

For instance, you have the following html on the page that you want to test.

```
<H1>FORM Authentication demo</H1>

<div class="box-inner">
    <a href="js/tellurium-test.js">Tellurium Test Cases</a>
    <input name="submit" type="submit" value="Test">
</div>

<form method="POST" action="j_security_check">
    <table border="0" cellspacing="2" cellpadding="1">
        <tr>
            <td>Username:</td>
            <td><input size="12" value="" name="j_username" maxlength="25" type="text"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input size="12" value="" name="j_password" maxlength="25" type="password"></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input name="submit" type="submit" value="Login">
            </td>
        </tr>
    </table>
</form>
```

The correct UI module is shown as follows,

```
    ui.Container(uid: "Form", clocator: [tag: "table"]){
        Container(uid: "Username", clocator: [tag: "tr"]){
            TextBox(uid: "Label", clocator: [tag: "td", text: "Username:", direct: "true"])
            InputBox(uid: "Input", clocator: [tag: "input", type: "text", name: "j_username"])
        }
        Container(uid: "Password", clocator: [tag: "tr"]){
            TextBox(uid: "Label", clocator: [tag: "td", text: "Password:", direct: "true"])
            InputBox(uid: "Input", clocator: [tag: "input", type: "password", name: "j_password"])
        }
        SubmitButton(uid: "Submit", clocator: [tag: "input", type: "submit", value: "Login", name: "submit"])
    }
```

Assume the html was changed recently and you still use the UI module defined some time ago.

```
    ui.Container(uid: "ProblematicForm", clocator: [tag: "table"]){
        Container(uid: "Username", clocator: [tag: "tr"]){
            TextBox(uid: "Label", clocator: [tag: "td", text: "Username:", direct: "true"])
            InputBox(uid: "Input", clocator: [tag: "input", type: "text", name: "j"])
        }
        Container(uid: "Password", clocator: [tag: "tr"]){
            TextBox(uid: "Label", clocator: [tag: "td", text: "Password:", direct: "true"])
            InputBox(uid: "Input", clocator: [tag: "input", type: "password", name: "j"])
        }
        SubmitButton(uid: "Submit", clocator: [tag: "input", type: "submit", value: "logon", name: "submit"])
    }
```

Here are the differences:

```
   InputBox(uid: "Input", clocator: [tag: "input", type: "text", name: "j_username"])
   InputBox(uid: "Input", clocator: [tag: "input", type: "text", name: "j"])
   
   InputBox(uid: "Input", clocator: [tag: "input", type: "password", name: "j_password"])
   InputBox(uid: "Input", clocator: [tag: "input", type: "password", name: "j"])

   SubmitButton(uid: "Submit", clocator: [tag: "input", type: "submit", value: "Login", name: "submit"])
   SubmitButton(uid: "Submit", clocator: [tag: "input", type: "submit", value: "logon", name: "submit"])     
```

What will happen without the new group locating algorithm? You tests will be broken because the generated locators will not be correct any more. But if you use the latest Tellurium 0.7.0 snapshot, you will notice that the tests still work if you allow Tellurium to do closest match by calling

```
    useClosestMatch(true);
```

The magic is that the new Tellurium Engine will locate the UI module as a whole. It may have trouble to find the individual UI element such as "ProblematicForm.Username.Input", but it has no trouble to find the whole UI module structure in the DOM.

Apart from that, Tellurium 0.7.0 also provides a handy method for you to validate your UI module. For example, if you call

```
    validate("ProblematicForm");
```

You will get the detailed validation results including the closest matches.

```
UI Module Validation Result for ProblematicForm

-------------------------------------------------------

	Found Exact Match: false 

	Found Closest Match: true 

	Match Count: 1 

	Match Score: 85.764 


	Closest Match Details: 

	--- Element ProblematicForm.Submit -->

	 Composite Locator: <input name="submit" value="logon" type="submit"/> 

	 Closest Matched Element: <input name="submit" value="Login" type="submit"> 



	--- Element ProblematicForm.Username.Input -->

	 Composite Locator: <input name="j" type="text"/> 

	 Closest Matched Element: <input size="12" value="" name="j_username" maxlength="25" type="text"> 



	--- Element ProblematicForm.Password.Input -->

	 Composite Locator: <input name="j" type="password"/> 

	 Closest Matched Element: <input size="12" value="" name="j_password" maxlength="25" type="password"> 


-------------------------------------------------------

```


## Macro Command ##

Macro Command is a set of Selenium commands that are bundled together and sent to Selenium Core in one call. This will reduce the round trip latency from Tellurium Core to Engine and thus, improve the speed performance. Another advantage for Macro Command is that Tellurium Engine can reuse the locator because many times the commands in the same bundle act on the same UI element or same sub-tree in the DOM.

To implement Macro Command, we added one more tier to Tellurium Core to automatically handle the Macro Command bundling as shown in the following figure.

http://tellurium-developers.googlegroups.com/web/telluriumbundle.gif?gda=vAECeEUAAABfqXGjliAPh6eFpGfZMLEMvv4gl2uJvcOyvlRvyqCHL_Uz5QqadYe3qLzL3P0zO1Ecn8WIbWh5zqeDKtDBmq67Gu1iLHeqhw4ZZRj3RjJ_-A

To use Macro Command, we add the following settings to configuration file TelluriumConfig.groovy:

```
    //the bundling tier
    bundle{
        maxMacroCmd = 5
        useMacroCommand = false
    }
```

and the following methods to DslContext to change the Macro command settings at runtime

```
    public void useMacroCmd(); 
    public void disableMacroCmd();
    public useMaxMacroCmd(int max);
    public int getMaxMacroCmd();
```

If you look at the server log and you will see what happened under the hood as follows.

```
14:57:49.584 INFO - Command request: getBundleResponse[[{"uid":"ProblematicForm.Username.Input","args":["jquery=table tr input[type=text][name=j]","t"],"name":"keyDown","sequ":44},{"uid":"ProblematicForm.Username.Input","args":["jquery=table tr input[type=text][name=j]","t"],"name":"keyPress","sequ":45},{"uid":"ProblematicForm.Username.Input","args":["jquery=table tr input[type=text][name=j]","t"],"name":"keyUp","sequ":46},{"uid":"ProblematicForm.Password.Input","args":["jquery=table tr input[type=password][name=j]","t"],"name":"keyDown","sequ":47},{"uid":"ProblematicForm.Password.Input","args":["jquery=table tr input[type=password][name=j]","t"],"name":"keyPress","sequ":48}], ] on session 9165cd68806a42fdbdef9f87e804a251
14:57:49.617 INFO - Got result: OK,[] on session 9165cd68806a42fdbdef9f87e804a251

```

In the above example, the command bundle includes the following commands:

```
    keyDown "ProblematicForm.Username.Input", "t"
    keyPress "ProblematicForm.Username.Input", "t"
    keyUp "ProblematicForm.Username.Input", "t"
    keyDown "ProblematicForm.Username.Input", "t"
    keyPress "ProblematicForm.Username.Input", "t"
```

and they are combined as a single API call to the Tellurium Engine.

## UI Module Caching ##

From Tellurium 0.6.0, we provides the cache capability for CSS selectors so that we can reuse them without doing re-locating. In 0.7.0, we move a step further to cache the whole UI module on the Engine side. Each UI module cache holds a snapshot of the DOM references for most of the UI elements in the UI module. The exceptions are dynamic web elements defined by Tellurium UI templates. For these dynamic web elements, the Engine will try to get the DOM reference of its parent and then do locating inside this subtree with its parent node as the root, which will improve the locating speed a lot.

On the Tellurium Core side, all UI modules are converted into a JSON object. That is why you can see the Tellurium UI objects must implement the following method,

```
   abstract JSONObject toJSON();
```

For most UI objects, you only need to implement this method simply as follows if we take the UrlLink object as an example,

```
    public JSONObject toJSON() {

      return buildJSON() {jso ->
        jso.put(UI_TYPE, "UrlLink")
      }
    }
```

For more complicated implementation, please refer to the List object.

Tellurium Core automatically push the UI module definition to the Engine by calling the following method.

```
   public void useUiModule(String json);
```

Once the UI module is cached. All locating procedure will be on the DOM sub-tree the UI module represents to reuse the locators and make the locating very fast.

To turn on and off the caching capability, you just simply call the following method in your code.

```
    void useCache(boolean isUse);
```

Or use the following methods to do fine control of the cache.

```
    public void enableCache(); 
    public boolean disableCache();
    public boolean cleanCache();
    public boolean getCacheState();
    public void setCacheMaxSize(int size);
    public int getCacheSize();
    public void useDiscardNewCachePolicy();
    public void useDiscardOldCachePolicy();
    public void useDiscardLeastUsedCachePolicy();
    public void useDiscardInvalidCachePolicy();
    public String getCurrentCachePolicy();
    public Map<String, Long> getCacheUsage();
```

## Tellurium UID Description Language ##

The Tellurium UID Description Language (UDL) is designed to
  1. address the dynamic factors in Tellurium UI templates
  1. increase the flexibility of Tellurium UI templates.

UDL is implemented with [the Antlr 3 parser generator](http://www.antlr.org/). The implementation details can be found [here](http://code.google.com/p/aost/wiki/BuildYourOwnJavaParserWithAntlr3). The grammars and technical details are recovered in [Tellurium UID Description Language](http://code.google.com/p/aost/wiki/TelluriumUIDDescriptionLanguage).

To migrate your UI templates from 0.6.0 to 0.7.0, please make the following changes:
  * Put "{}" around 0.6.0 UI template uids
  * Replace "`*`" with "all"
  * Replace "all" in table with "row: all, column: all"
  * You can add an ID for each UI template object, where the ID starts with a letter and is followed by digits, letters, and "`_`".
  * The implicit tokens defined in UDL such as "header", "footer", "row", "column", and "tbody" are all reserved and they cannot be used as IDs. But UDL is case-sensitive, you can still use "Header", "Footer", "Row", "Column", and "TBody" as IDs.

For example the following TableExampleModule UI module in the ui-examples reference project.

```
class TableExampleModule extends DslContext {

  public void defineUi() {
    ui.StandardTable(uid: "GT", clocator: [id: "xyz"], ht: "tbody"){
      TextBox(uid: "header: all", clocator: [:])
      TextBox(uid: "row: 1, column: 1", clocator: [tag: "div", class: "abc"])
      Container(uid: "row: 1, column: 2"){
        InputBox(uid: "Input", clocator: [tag: "input", class: "123"])
        Container(uid: "Some", clocator: [tag: "div", class: "someclass"]){
          Span(uid: "Span", clocator: [tag: "span", class: "x"])
          UrlLink(uid: "Link", clocator: [:])
        }
      }
    }
  }

  public void work(String input){
    keyType "GT[1][2].Input", input
    click "GT[1][2].Some.Link"
    waitForPageToLoad 30000
  }
}

```

Now, this UI module can be re-defined in a more flexible way with UDL as follows.

```
class TableExampleModule extends DslContext {

  public void defineUi() {
    ui.StandardTable(uid: "GT", clocator: [id: "xyz"], ht: "tbody"){
      TextBox(uid: "{header: first} as One", clocator: [tag: "th", text: "one"], self: true)
      TextBox(uid: "{header: 2} as Two", clocator: [tag: "th", text: "two"], self: true)
      TextBox(uid: "{header: last} as Three", clocator: [tag: "th", text: "three"], self: true)
      TextBox(uid: "{row: 1, column -> One} as A", clocator: [tag: "div", class: "abc"])
      Container(uid: "{row: 1, column -> Two} as B"){
        InputBox(uid: "Input", clocator: [tag: "input", class: "123"])
        Container(uid: "Some", clocator: [tag: "div", class: "someclass"]){
          Span(uid: "Span", clocator: [tag: "span", class: "x"])
          UrlLink(uid: "Link", clocator: [:])
        }
      }
      TextBox(uid: "{row: 1, column -> Three} as Hello", clocator: [tag: "td"], self: true)
    }
  }

  public void work(String input){
    getText "GT.A"
    keyType "GT.B.Input", input
    click "GT.B.Some.Link"
    waitForPageToLoad 30000
  }
}
```

Apart from that, you need to add the UDL dependency to you project.

```
       <dependency>
           <groupId>org.telluriumsource</groupId>
           <artifactId>tellurium-udl</artifactId>
           <version>0.7.0-SNAPSHOT</version>
           <scope>compile</scope>
       </dependency>
```

## Tellurium New APIs ##

Tellurium Engine in 0.7.0 re-implemented a set of Selenium APIs by exploiting jQuery, plus many more new APIs. For example,

```
TelluriumApi.prototype.getCSS = function(locator, cssName) {
    var element = this.cacheAwareLocate(locator);
    var out = [];
    var $e = teJQuery(element);
    $e.each(function() {
        out.push(teJQuery(this).css(cssName));
    });
    return JSON.stringify(out);
};
```

To switch between Tellurium new API and Selenium Core API, you can call the following method.

```
    public void useTelluriumApi(boolean isUse);
```

Or use the following DSL methods from DslContext.

```
   public void enableTelluriumApi();
   public void disableTelluriumApi()
```

Be aware, the New Tellurium APIs are still under development and are not fully tested. Use them at your discretion.


## Trace ##

Tellurium 0.7.0 provides built-in support for the command execution time including
  * Execution time for each command
  * Total run time
  * Aggregated times for each command

For example, you can see the output as follows,

```
TE: Name: getCurrentCachePolicy, start: 1260496075484, duration: 28ms
TE: Name: useDiscardNewCachePolicy, start: 1260496075514, duration: 51ms
TE: Name: getCurrentCachePolicy, start: 1260496075566, duration: 74ms
TE: Name: useDiscardOldCachePolicy, start: 1260496075642, duration: 35ms
TE: Name: getCurrentCachePolicy, start: 1260496075678, duration: 42ms
TE: Start Time: 1260496060373
End Time: 1260496075720
Total Runtime: 15347ms
Name: keyPress, count: 24, total: 1277ms, average: 53ms
Name: getCurrentCachePolicy, count: 5, total: 222ms, average: 44ms
Name: useDiscardOldCachePolicy, count: 1, total: 35ms, average: 35ms
Name: useDiscardInvalidCachePolicy, count: 1, total: 33ms, average: 33ms
Name: enableCache, count: 2, total: 151ms, average: 75ms
Name: click, count: 3, total: 194ms, average: 64ms
Name: isElementPresent, count: 2, total: 100ms, average: 50ms
Name: useDiscardLeastUsedCachePolicy, count: 1, total: 39ms, average: 39ms
Name: type, count: 1, total: 81ms, average: 81ms
Name: typeKey, count: 3, total: 124ms, average: 41ms
```

We added the following settings to TelluriumConfig.groovy

```
    test{
        execution{
            //whether to trace the execution timing
            trace = false
        }      

```

You can use the follow methods in DslContext to turn on or off the trace, and get the trace data.

```

  public void enableTrace();

  public void disableTrace();
  
  public void showTrace();
```


## Methods Accessible in Test Cases ##

There are many Tellurium APIs that used to be available only in DslContext. That is to say, you have to extend DslContext to use them. For example, you often see code in a test case likes this,

```
GoogleSearchModule gsm = new GoogleSearchModule();
gsm.defineUi();
gsm.usejQuerySelector();
gsm.registerNamespace("te", te_ns);
```

Now, many of them, which are not really tied to a specific UI module, are made available in Tellurium test cases. For example, the above code can be changed as follows,

```
GoogleSearchModule gsm = new GoogleSearchModule();
gsm.defineUi();
useCssSelector();
registerNamespace("te", te_ns);
```

New TelluriumGroovyTestCase provides the following list of new APIs for your convenience.

```
    public void useCssSelector(boolean isUse);

    public void useCache(boolean isUse);

    public void cleanCache();

    public boolean isUsingCache();

    public void setCacheMaxSize(int size);

    public int getCacheSize();

    public int getCacheMaxSize();

    public Map<String, Long> getCacheUsage();

    public void useCachePolicy(CachePolicy policy);

    public String getCurrentCachePolicy();

    public void useDefaultXPathLibrary();

    public void useJavascriptXPathLibrary();

    public void useAjaxsltXPathLibrary();

    public void registerNamespace(String prefix, String namespace);

    public String getNamespace(String prefix);

    public void pause(int milliseconds);

    public void useMacroCmd(boolean isUse);

    public void setMaxMacroCmd(int max);

    public int getMaxMacroCmd();

    public boolean isUseTelluriumApi();

    public void useTelluriumApi(boolean isUse);
  
    public void useTrace(boolean isUse);

    public void showTrace();

    public void setEnvironment(String name, Object value);

    public Object getEnvironment(String name);

    public void allowNativeXpath(boolean allow);

    public void addScript(String scriptContent, String scriptTagId);

    public void removeScript(String scriptTagId);

    public void EngineState getEngineState();

    public void useEngineLog(boolean isUse);

    public void useTelluriumEngine(boolean isUse);

    public void dumpEnvironment();
```

Tellurium Java test cases provide the same APIs and the only difference is that the APIs in Tellurium Java test cases are static.

## Environment ##

We added an Environment class to Tellurium Core so that you can change the runtime environment. The Environment class is defined as follows,

```
public class Environment {
  def envVariables = [:];
  public boolean isUseCssSelector();

  public boolean isUseCache();

  public boolean isUseBundle();

  public boolean isUseScreenshot();

  public boolean isUseTrace();

  public boolean isUseTelluriumApi();

  public boolean isUseEngineLog();

  public void useCssSelector(boolean isUse);

  public void useCache(boolean isUse);

  public void useBundle(boolean isUse);

  public void useScreenshot(boolean isUse);

  public void useTrace(boolean isUse);

  public void useTelluriumApi(boolean isUse);

  public void useEngineLog(boolean isUse);

  public useMaxMacroCmd(int max);

  public int myMaxMacroCmd();

  public String myLocale();
  
  public void setCustomEnvironment(String name, Object value);

  public Object getCustomEnvironment(String name);
}
```

where `setCustomEnvironment` and `getCustomEnvironment` can be used to pass user defined environment variables.

Tellurium provides the following method for users to dump out all the current environment variables.

```
    public void dumpEnvironment();
```

The output is like the following.

```
    Environment Variables:
      useEngineCache: true
      useClosestMatch: false
      useMacroCommand: true
      maxMacroCmd: 5
      useTelluriumApi: true
      locatorWithCache: true
      useCSSSelector: true
      useTrace: true
      logEngine: false
      locale: en_US
```

If any exception is thrown at the dispatcher tier, the environment variable will also be printed out.

## Get UIs by Tag Name ##

As requested by users, Tellurium 0.7.0 added the following two methods.

```
UiByTagResponse getUiByTag(String tag, Map filters);

void removeMarkedUids(String tag);
```

The first method passes in the tag name and the attributes as filters and get back the UI elements associated with the tag. The response object is defined as

```
class UiByTagResponse {
  //tag name
  String tag;

  Map filters

  //temporally assigned IDs
  String[] tids;
}
```

where the tids are assigned by Tellurium Engine. Under the hood, Tellurium core creates one AllPurposeObject for each _tid_ in the _tids_ array and registers it to Core so that users can use the _tid_ as the UID to act on the UI object.

The second method cleans up all the temporally assigned IDs by the Tellurium Engine.

Example:

In UI module JettyLogonModule, we define the following method:

```
  public String[] getInputBox(){
     def attrs = ["type" : "text"];
     UiByTagResponse resp = getUiByTag("input", attrs);
    
     return resp.tids;
  }
```

The test case is as follows

```
    @Test
    public void testGetUiByTag(){
        useEngineLog(true);
        useTelluriumApi(true);
        useCache(true);
        String[] teuids = jlm.getInputBox();
        assertNotNull(teuids);
        for(String teuid: teuids){
            jlm.keyType(teuid, "Tellurium Source");
        }
        jlm.removeMarkedUids("input");
    }
```

## Div ##

The Div object has been changed to be a Container type object. For example, you can define the following UI module.

```
   ui.Div(uid: "div1", clocator: [id: "div1"]) {
      Div(uid: "div2", clocator: [id: "div2"]) {
        List(uid: "list1", clocator: [tag: "ul"], separator: "li"){
          UrlLink(uid: "{all}", clocator: [:])
        }
      } 
   }
```

## Selector ##

For the Selector UI object, the following DSL methods have been implemented in the Tellurium new Engine:

  * `select(String uid, String target)`: Equals to `selectByLabel`.
  * `selectByLabel(String uid, String target)`: Select based on the text attribute.
  * `selectByValue(String uid, String target)`: Select based on the value attribute.
  * `selectByIndex(String uid, int target)`: Select based on index (starting from 1).
  * `selectById(String uid, String target)`: Select based on the ID attribute.
  * `String[] getSelectOptions(String uid)`: Get all option texts.
  * `String[] getSelectValues(String uid)`: Get all option values.
  * `String[] getSelectedLabels(String uid)`: Get selected texts.
  * `String getSelectedLabel(String uid)`: Get selected text, for multiple selections, return the first one.
  * `String[] getSelectedValues(String uid)`: Get selected values.
  * `String getSelectedValue(String uid)`: Get selected value, for multiple selections, return the first one.
  * `addSelectionByLabel(String uid, String target)`: Add selection based on text.
  * `addSelectionByValue(String uid, String target)`: Add selection based on value.
  * `removeSelectionByLabel(String uid, String target)`: Remove selection based on text.
  * `removeSelectionByValue(String uid, String target)`: Remove selection based on value.
  * `removeAllSelections(String uid)`: Remove all selections.

Be aware, the above `target` variable can be a partial matching as follows.

  * `^text`: starts with text.
  * `$text`: ends with text.
  * `*text`: contains text.
  * `!text`: does not contain text.

We can use the following example to demonstrate the use of the new Selector APIs.

HTML snippet:
```
<form method="POST" action="check_phone">
    <select name="Profile/Customer/Telephone/@CountryAccessCode" style="font-size:92%">
        <option value="1" selected=selected>US</option>
        <option value="2" id="uk">UK</option>
        <option value="3">AT</option>
        <option value="4">BE</option>
        <option value="4" id="ca">CA</option>
        <option value="6">CN</option>
        <option value="7">ES</option>
        <option value="8">VG</option>
    </select>
    <input type="text" class="medium paxFerryNameInput" value="" name="Profile/Customer/Telephone/@PhoneNumber"
           maxlength="16" id="phone1" tabindex="26">
    <input name="submit" type="submit" value="Check">
</form>
```

UI Module definition:

```
   ui.Form(uid: "Form", clocator: [method: "POST", action: "check_phone"]){
      Selector(uid: "Country", clocator: [name: "\$CountryAccessCode"])
      InputBox(uid: "Number", clocator: [name: "\$PhoneNumber"])
      SubmitButton(uid: "check", clocator: [value: "Check"])
   }
```

Test Cases:

```
    @Test
    public void testSelect(){
        String[] countries = sm.getSelectOptions("Form.Country");
        for(String country: countries){
            System.out.println("Country: " + country);
        }
        String[] values = sm.getSelectValues("Form.Country");
        for(String value: values){
            System.out.println("Value: " + value);
        }
        sm.selectByLabel("Form.Country", "US");
        String selected = sm.getSelectedLabel("Form.Country");
        assertEquals("US", selected);
        sm.selectByLabel("Form.Country", "$E");
        selected = sm.getSelectedLabel("Form.Country");
        assertEquals("BE", selected);
        sm.selectByLabel("Form.Country", "^E");
        selected = sm.getSelectedLabel("Form.Country");
        assertEquals("ES", selected);
        sm.selectByValue("Form.Country", "8");
        selected = sm.getSelectedLabel("Form.Country");
        assertEquals("VG", selected);
        sm.selectByIndex("Form.Country", 6);
        selected = sm.getSelectedLabel("Form.Country");
        assertEquals("CN", selected);
        sm.selectById("Form.Country", "uk");
        selected = sm.getSelectedLabel("Form.Country");
        assertEquals("UK", selected);
    }
```


## toggle ##

Tellurium 0.7.0 provides a toggle method to animate the UI element on the web page. For example, you can the following commands to show the UI element under testing.

```
    toggle "Form.Username.Input"
    pause 500
    toggle "Form.Username.Input"
```

## UI Module Live Show ##

The show command is used to show the UI module that you defined on the actual web page.

```
    public show(String uid, int delay);
```

where delay is in milliseconds. Be aware that _show_ is available in Tellurium API.

Example:

```
   useCache(true);
   useTelluriumApi(true);
   JettyLogonModule jlm = new JettyLogonModule();       
   jlm.show("Form", 5000);
```

The UI module on the web page will be outlined and if user hives over the UI module,
the UIDs of the selected UI element's and its ancestors' will be shown in a tooltip as shown in the following figure.

http://tellurium-users.googlegroups.com/web/teshowuisnapshotcoloroutline.png?gda=QFLx4lIAAACsdq5EJRKLPm_KNrr_VHB_3bdqdUlgM5p9LxU9B-G7VGI4VaZQIUmR7BTITIeHhJw-4xIeevXbuhZMOIi4CXKoVeLt2muIgCMmECKmxvZ2j4IeqPHHCwbz-gobneSjMyE&gsc=WUr0egsAAADl-yofJNPAI7ohFyS8aj5h

You may be a bit confused by the multiple UID labels. The above UI module is defined as

```
ui.Form(uid: "Form", clocator: [tag: "form"]){
        Container(uid: "Username", clocator: [tag: "tr"]){
            TextBox(uid: "Label", clocator: [tag: "td", text: "Username:", direct: "true"])
            InputBox(uid: "Input", clocator: [tag: "input", type: "text", name: "j_username"])
        }
        Container(uid: "Password", clocator: [tag: "tr"]){
            TextBox(uid: "Label", clocator: [tag: "td", text: "Password:", direct: "true"])
            InputBox(uid: "Input", clocator: [tag: "input", type: "password", name: "j_password"])
        }
        SubmitButton(uid: "Submit", clocator: [tag: "input", type: "submit", value: "Login", name: "submit"])
    }
```

When user hives over the Username Input box, its parent Username and its grandparent Form
are shown as well. We added color coding to the outlines. Different levels in the hierarchy have different colors. How the layout maps to each individual UI element can be illustrated more clearly by the following figure.

http://tellurium-users.googlegroups.com/web/telluriumshowuisnapshot2.png?gda=jciJlU4AAACsdq5EJRKLPm_KNrr_VHB__uAWGGGjpMtQflLHIKHYYIBs4RzukN2p4-NZziI_5n9VyjA3JensFE0ElYzuCQ7x47Cl1bPl-23V2XOW7kn5sQ&gsc=oz20hwsAAAD_weMBWNZir9C-tYu9DiER


In the meanwhile, Tellurium Core also exposes the following two methods for users to start showing UI and clean up UI.

```
   public void startShow(String uid);

   public void endShow(String uid);
```

## getHTMLSource ##

Use `getHTMLSource`, users can get back the runtime html source for a UI module. Tellurium provided two methods for this purpose.

```
public Map getHTMLSourceResponse(String uid);

public void getHTMLSource(String uid);
```

The first method get back the html source as a uid-to-html map and the second one simply print out the html source on the console. Be aware, `getHTMLSource` is only available in Tellurium new APIs.

Example:

```
    @Test
    public void testGetHTMLSource(){
        useEngineLog(true);
        useTelluriumApi(true);
        useCache(true);
        connect("JettyLogon");
        jlm.getHTMLSource("Form");
    }
```

and the output is as follows:

```
Form: 

<form method="POST" action="j_security_check">
    <table border="0" cellpadding="1" cellspacing="2">
        <tbody><tr>
            <td>Username:</td>
            <td><input size="12" value="" name="j_username" maxlength="25" type="text"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input size="12" value="" name="j_password" maxlength="25" type="password"></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input name="submit" value="Login" type="submit">
            </td>
        </tr>
    </tbody></table>
</form>

Form.Username: 

	<tr>
            <td>Username:</td>
            <td><input size="12" value="" name="j_username" maxlength="25" type="text"></td>
        </tr>

Form.Username.Label: 

	<td>Username:</td>

Form.Username.Input: 

	<input size="12" value="" name="j_username" maxlength="25" type="text">

Form.Password: 

	<tr>
            <td>Password:</td>
            <td><input size="12" value="" name="j_password" maxlength="25" type="password"></td>
        </tr>

Form.Password.Label: 

	<td>Password:</td>

Form.Password.Input: 

	<input size="12" value="" name="j_password" maxlength="25" type="password">

Form.Submit: 

	<input name="submit" value="Login" type="submit">
```

## type and keyType ##

type and keyType now accept different types of objects and convert them to a String automatically by calling the toString() method. For example, you can use the following commands:

```
    type "Google.Input", "Tellurium"

    type "Google.Input", 12.15

    type "Google.Input", true
```

## I18N ##

For details, please read [Internationalization Support in Tellurium](http://code.google.com/p/aost/wiki/InternationalizationSupportTellurium)

## Excel File Reader in Data Driven Test ##

Excel file reader has been added to Tellurium Data Driven Test. If you have excel input files, you can specify the reader in the configuration file TelluriumConfig.groovy as follows,

```
    datadriven{
        dataprovider{
            //specify which data reader you like the data provider to use
            //the valid options include "PipeFileReader", "CVSFileReader", and "ExcelFileReader" at this point
            reader = "ExcelFileReader"
        }
    }

```

## Standard Table ##

The Standard Table is used for table with multiple "tbody" sections. The Table layout is as follows.

```
  table
      thead
         tr
           th
           ...
           th
      tbody
         tr
           td
           ...
           td
         ...
      tbody (Could have multiple ones)
         tr
           td
           ...
           td
         ...
      tfoot
         tr
           td
           ...
           td
```

To overwrite the default layout, Tellurium core provides the following attributes in the StandardTable object.
  * _ht_: header tag.
  * _hrt_: header row tag, the default tag is "tr".
  * _hct_: header column tag, the default tag is "th".
  * _bt_: body tag.
  * _brt_: body row tag, the default tag is "tr".
  * _bct_: body column tag, the default tag is "td".
  * _ft_: footer tag.
  * _frt_: footer row tag, the default tag is "tr".
  * _fct_: footer column tag, the default tag is "td".

To overwrite the above tags, simply define them in the object definition. For example,

```
    ui.StandardTable(uid: "table3", clocator: [:], hct: "td"){
      TextBox(uid: "header: all", clocator: [:])
      TextBox(uid: "footer: all", clocator: [:])
    }
```

The header column tag is overwritten to be "td" instead of the default tag "th".

To be more accurate, the footer definition has been changed from "foot" to "footer".

## self attribute ##

Some times, elements inside a Table usually are inside its parent tag, for instance, we have the following HTML source.

```
<div id="table">
   <div>
       <div id="name">
           <div>Data</div>
           <div>
               <img/>
           </div>
       </div>
       <div id="shortname">
           <div>Bezeichnung</div>
           <div>
               <img/>
           </div>
       </div>
       <div id="type">
           <div>Typ</div>
           <div>
               <img/>
           </div>
       </div>
   </div>
   <div id="client-area">
       <div>
           <div>Bildsystem</div>
           <div>Bildsystem</div>
           <div>Bildserver</div>
       </div>
       <div>
           <div>Partner</div>
           <div>Partner</div>
           <div>Bestandssystem</div>
       </div>
       <div>
           <div>MS</div>
           <div>MS</div>
           <div>MS</div>
       </div>
   </div>
</div>
```

where the "Data" element and many others are inside its parent tag "div". To module this, we added a self attribute to the UiObject class and the default is "false".

To describe the above html, we can define the following UI module.

```
    ui.StandardTable(uid: "Table", clocator: [id: "table"], bt: "div", brt: "div", bct: "div"){
      TextBox(uid: "{tbody: 1, row: all, column: 1}", clocator: [tag: "div"], self: "true")
      Image(uid: "{tbody: 1, row: all, column: 2}", clocator: [:])
      TextBox(uid: "{tbody: 2, row: all, column: 1}", clocator: [tag: "div"], self: "true")
      TextBox(uid: "{tbody: 2, row: all, column: 2}", clocator: [tag: "div"], self: "true")
      TextBox(uid: "{tbody: 2, row: all, column: 3}", clocator: [tag: "div"], self: "true")
    }
```

To test the UI module, you can simply call the following api.

```
   getHTMLSource("Table");
```

Be aware that the self can be "true" **ONLY** for UI elements inside a List, a Table, or a StandardTable Object.

## Engine State Offline Update ##

For some reasons, you may need to make Engine state calls before you actually connect to the Engine. For examples, call one of the following methods.

```
   public void enableCache();
   public void disableCache();
   public void useTelluriumApi(boolean isUse);
   public void useClosestMatch(boolean isUse);
```

We added an Engine State tracer in the bundle tier to record all the requests if the Engine is not connected and aggregate them. Once the Engine is connected, Tellurium will automatically send out the Engine state update request.

For example, the following test code works fine in 0.7.0.

```
    @Test
    public void testOfflineEngineStateUpdate(){
        JettyLogonModule jlm = new  JettyLogonModule();
        jlm.defineUi();
        useCssSelector(true);
        useTrace(true);
        //Engine state offline update
        useTelluriumApi(true);
        useCache(true);
        connectSeleniumServer();
        connectUrl("http://localhost:8080/logon.html");
        jlm.logon("test", "test");
        //Back to state online update
        useClosestMatch(true);
        connectUrl("http://localhost:8080/logon.html");
        jlm.plogon("test", "test");
    }
```

## Repeat Object ##

Very often, we need to use the same UI elements for multiple times on the web page. For example, we have the following html.

```
   <div class="segment clearfix">
       <div class="option">
           <ul class="fares">
               <li>
                   <input type="radio">&nbsp;
                   <label>Economy</label>
               </li>
               <li>
                   <input type="radio">&nbsp;
                   <label>Flexible</label>
               </li>
           </ul>
           <div class="details">
               <dl>
                   <dt>Ship:</dt>
                   <dd>A</dd>
                   <dt>Departs</dt>
                   <dd>
                       <em>08:00</em>
                   </dd>
                   <dt>Arrives</dt>
                   <dd>
                       <em>11:45</em>
                   </dd>
               </dl>
           </div>
       </div>
       <div class="option">
           <ul class="fares">
               <li>
                   <input type="radio">&nbsp;
                   <label>Economy</label>
               </li>
               <li>
                   <input type="radio">&nbsp;
                   <label>Flexible</label>
               </li>
           </ul>
           <div class="details">
               <dl>
                   <dt>Ship:</dt>
                   <dd>B</dd>
                   <dt>Departs</dt>
                   <dd>
                       <em>17:30</em>
                   </dd>
                   <dt>Arrives</dt>
                   <dd>
                       <em>21:15</em>
                   </dd>
               </dl>
           </div>
       </div>
   </div>
   <div class="segment clearfix">
       <div class="option">
           <ul class="fares">
               <li>
                   <input type="radio">&nbsp;
                   <label>Economy</label>
               </li>
               <li>
                   <input type="radio">&nbsp;
                   <label>Flexible</label>
               </li>
           </ul>
           <div class="details">
               <div class="photo"><img/></div>
               <dl>
                   <dt>Ship:</dt>
                   <dd>C</dd>
                   <dt>Departs</dt>
                   <dd>
                       <em>02:00</em>
                   </dd>
                   <dt>Arrives</dt>
                   <dd>
                       <em>06:00</em>
                   </dd>
               </dl>
           </div>
       </div>
       <div class="option">
           <ul class="fares">
               <li>
                   <input type="radio">&nbsp;
                   <label>Economy</label>
               </li>
               <li>
                   <input type="radio">&nbsp;
                   <label>Flexible</label>
               </li>
           </ul>
           <div class="details">
               <dl>
                   <dt>Ship:</dt>
                   <dd>D</dd>
                   <dt>Departs</dt>
                   <dd>
                       <em>12:45</em>
                   </dd>
                   <dt>Arrives</dt>
                   <dd>
                       <em>16:30</em>
                   </dd>
               </dl>
           </div>
       </div>
   </div>
</form>
```

You can see the elements `<div class="segment clearfix">` and `<div class="option">` repeated for couple times. We can use the Repeat object for this UI module. The Repeat object inherits the Container object and You can use it just like a Container. The difference is that you should use index to refer to a Repeat object.

For the above html source, we can create the following UI module

```
    ui.Form(uid: "SailingForm", clocator: [name: "selectedSailingsForm"] ){
      Repeat(uid: "Section", clocator: [tag: "div", class: "segment clearfix"]){
        Repeat(uid: "Option", clocator: [tag: "div", class: "option", direct: "true"]){
          List(uid: "Fares", clocator: [tag: "ul", class: "fares", direct: "true"], separator: "li"){

            Container(uid: "all"){
                RadioButton(uid: "radio", clocator: [:], respond: ["click"])
                TextBox(uid: "label", clocator: [tag: "label"])
            }
          }
          Container(uid: "Details", clocator: [tag: "div", class: "details"]){
            Container(uid: "ShipInfo", clocator: [tag: "dl"]){
              TextBox(uid: "ShipLabel", clocator: [tag: "dt", position: "1"])
              TextBox(uid: "Ship", clocator: [tag: "dd", position: "1"])
              TextBox(uid: "DepartureLabel", clocator: [tag: "dt", position: "2"])
              Container(uid: "Departure", clocator: [tag: "dd", position: "2"]){
                TextBox(uid: "Time", clocator: [tag: "em"])
              }
              TextBox(uid: "ArrivalLabel", clocator: [tag: "dt", position: "3"])
              Container(uid: "Arrival", clocator: [tag: "dd", position: "3"]){
                TextBox(uid: "Time", clocator: [tag: "em"])
              }
            }
          }
        }
      }
    }
```

To test the UI module, we can create the following test case.

```
    @Test
    public void testRepeat(){
        connect("JForm");
//        useCssSelector(true);            //uncomment this line if you choose to use css selector
        int num = jlm.getRepeatNum("SailingForm.Section");
        assertEquals(2, num);
        num = jlm.getRepeatNum("SailingForm.Section[1].Option");
        assertEquals(2, num);
        int size = jlm.getListSize("SailingForm.Section[1].Option[1].Fares");
        assertEquals(2, size);
        String ship = jlm.getText("SailingForm.Section[1].Option[1].Details.ShipInfo.Ship");
        assertEquals("A", ship);
        String departureTime = jlm.getText("SailingForm.Section[1].Option[1].Details.ShipInfo.Departure.Time");
        assertEquals("08:00", departureTime);
        String arrivalTime = jlm.getText("SailingForm.Section[1].Option[1].Details.ShipInfo.Arrival.Time");
        assertEquals("11:45", arrivalTime);
    }

```

## All Purpose Object ##

Tellurium 0.7.0 added an all purpose object for internal usage only. The object is defined as

```
class AllPurposeObject extends UiObject {

}
```

This object includes all methods for non-Container type objects.

## Groovy and GMaven ##

Groovy has been upgraded to the latest version 1.7.0.

```
   <dependency>
      <groupId>org.codehaus.groovy</groupId>
      <artifactId>groovy-all</artifactId>
      <version>1.7.0</version>
   </dependency>
```

For GMaven, we use the latest version 1.2 and removed the Maven Antrun plugin.

## JUnit ##

JUnit is upgraded to 4.7 since it provides more features. One such a good feature is [the Rule annotation](http://blog.schauderhaft.de/2009/10/04/junit-rules/).

To be consistent with TestNG test case, the class TelluriumJavaTestCase is deprecated and you should use TelluriumJUnitTestCase instead.

## TestNG ##

TelluriumTestNGTestCase was changed to allow the setup and teardown procedures only work once for all the tests. The magic are the @BeforeTest and @AfterTest annotations. See the following code for more details,

```
public abstract class TelluriumTestNGTestCase extends BaseTelluriumJavaTestCase {

    @BeforeTest(alwaysRun = true)
    public static void setUpForTest() {
        tellurium = TelluriumSupport.addSupport();
        tellurium.start(customConfig);
        connector = (SeleniumConnector) tellurium.getConnector();
    }

    @AfterTest(alwaysRun = true)
    public static void tearDownForTest() {
        if(tellurium != null)
            tellurium.stop();
    }
```

## TelluriumMockJUnitTestCase and TelluriumMockTestNGTestCase ##

TelluriumMockJUnitTestCase and TelluriumMockTestNGTestCase incorporated the Mock Http Server so that you can load up a html web page and test against it with minimal configuration.

One example is as follows,

```
public class JettyLogonJUnitTestCase extends TelluriumMockJUnitTestCase {
    private static JettyLogonModule jlm;

    @BeforeClass
    public static void initUi() {
        registerHtmlBody("JettyLogon");

        jlm = new  JettyLogonModule();
        jlm.defineUi();
        useCssSelector(true);
        useTelluriumApi(true);
        useTrace(true);
        useCache(true);
    }

    @Before
    public void connectToLocal() {
        connect("JettyLogon");
    }

    @Test
    public void testJsonfyUiModule(){
        String json = jlm.jsonify("Form");
        System.out.println(json);
    }

    @AfterClass
    public static void tearDown(){
        showTrace();
    }
}
```

where the html file "JettyLogon" lives in the class path "org/telluriumsource/html". You can change this by the following method:

```
public static void setHtmlClassPath(String path);
```

The implementation is simple as shown as follows.

```
public class TelluriumMockJUnitTestCase extends TelluriumJUnitTestCase {
    protected static MockHttpServer server;

    @BeforeClass
    public static void init(){
        server = new MockHttpServer(8080);
        server.start();
        connectSeleniumServer();
    }

    public static void registerHtml(String testname){
       server.registerHtml("/" + testname + ".html", server.getHtmlFile(testname));
    }

    public static void registerHtmlBody(String testname){
       server.registerHtmlBody("/" + testname + ".html", server.getHtmlFile(testname));
    }

    public static void setHtmlClassPath(String path){
        server.setHtmlClassPath(path);
    }

    public static void connect(String testname){
        connectUrl("http://localhost:8080/" + testname + ".html");
    }

    @AfterClass
    public static void tearDown(){
        server.stop();
    }
}
```

## Tellurium Configuration ##

The configuration parser has been refactored. The configuration file name is stored in the Environment class as follows.

```
@Singleton
public class Environment implements Configurable{

  protected String configFileName = "TelluriumConfig.groovy";

  protected String configString = "";

  public static Environment getEnvironment(){
    return Environment.instance;
  }

  public void useConfigString(String json){
    this.configString = json;
  }

  ...

```

That is to say, you can get the Environment singleton instance and change the file name before Tellurium core is loaded up if you have a good reason to do that. In the meanwhile, we add support to load the configuration from a JSON string, which will be stored in the `configString`} variable in the Environment.

If the `configString` variable is not null or empty, Tellurium core will honor the JSON string and will ignore the configuration file. One way to use the JSON string is illustrated by the following example.

```
public class JettyLogonJUnitTestCase extends TelluriumMockJUnitTestCase {
   private static JettyLogonModule jlm;
   static{
       Environment env = Environment.getEnvironment();
       env.useConfigString(JettyLogonModule.JSON_CONF);
   }

   ...

```

where variable `JSON_CONF` is the JSON configuration string.

```
public class JettyLogonModule extends DslContext {
 public static String JSON_CONF = """{"tellurium":{"test":{"result":{"reporter":"XMLResultReporter","filename":"TestResult.output","output":"Console"},"exception":{"filenamePattern":"Screenshot?.png","captureScreenshot":false},"execution":{"trace":false}},"accessor":{"checkElement":false},"embeddedserver":{"port":"4444","browserSessionReuse":false,"debugMode":false,"ensureCleanSession":false,"interactive":false,"avoidProxy":false,"timeoutInSeconds":30,"runInternally":true,"trustAllSSLCertificates":true,"useMultiWindows":false,"userExtension":"","profile":""},"uiobject":{"builder":{}},"eventhandler":{"checkElement":false,"extraEvent":false},"i18n":{"locale":"en_US"},"connector":{"baseUrl":"http://localhost:8080","port":"4444","browser":"*chrome","customClass":"","serverHost":"localhost","options":""},"bundle":{"maxMacroCmd":5,"useMacroCommand":true},"datadriven":{"dataprovider":{"reader":"PipeFileReader"}},"widget":{"module":{"included":""}}}}""";

...
}
```

If we want a pretty look of the JSON String, we can use [JSON Visualization](http://chris.photobooks.com/json/) to format it as follows.

```
{
    "tellurium": {
        "test": {
            "result": {
                "reporter": "XMLResultReporter",
                "filename": "TestResult.output",
                "output": "Console"
            },
            "exception": {
                "filenamePattern": "Screenshot?.png",
                "captureScreenshot": false
            },
            "execution": {
                "trace": false
            }
        },
        "accessor": {
            "checkElement": false
        },
        "embeddedserver": {
            "port": "4444",
            "browserSessionReuse": false,
            "debugMode": false,
            "ensureCleanSession": false,
            "interactive": false,
            "avoidProxy": false,
            "timeoutInSeconds": 30,
            "runInternally": true,
            "trustAllSSLCertificates": true,
            "useMultiWindows": false,
            "userExtension": "",
            "profile": ""
        },
        "uiobject": {
            "builder": { }
        },
        "eventhandler": {
            "checkElement": false,
            "extraEvent": false
        },
        "i18n": {
            "locale": "en_US"
        },
        "connector": {
            "baseUrl": "http://localhost:8080",
            "port": "4444",
            "browser": "*chrome",
            "customClass": "",
            "serverHost": "localhost",
            "options": ""
        },
        "bundle": {
            "maxMacroCmd": 5,
            "useMacroCommand": true
        },
        "datadriven": {
            "dataprovider": {
                "reader": "PipeFileReader"
            }
        },
        "widget": {
            "module": {
                "included": ""
            }
        }
    }
}

```

If the JSON String is empty or null, Tellurium loads the configuration file TelluriumConfig.groovy first from the project root. If not found, it loads it from the class path. As a result, you can put the TelluriumConfig.groovy file under the resources directory if you use Maven.

The configuration file TelluriumConfig.groovy includes three extra sections. That is to say, the bundle tier,

```
    //the bundling tier
    bundle{
        maxMacroCmd = 5
        useMacroCommand = false
    }
```

the i18n,

```
    i18n{
        //locale = "fr_FR"
        locale = "en_US"
    }
```

and the trace.

```
    test{
        execution{
            //whether to trace the execution timing
            trace = false
        }      
```

The sample configure file for 0.7.0 is available [here](http://code.google.com/p/aost/wiki/TelluriumConfig070).

Tellurium Core 0.7.0 added support for Configuration file check as suggested by our user. That can be easily demonstrated by the following example, if we comment out the following section in TelluriumConfig.groovy

```
      //the bundling tier
//    bundle{
//        maxMacroCmd = 5
//        useMacroCommand = false
//    }
```

What will happen? Tellurium will throw the following exception

```
java.lang.ExceptionInInitializerError
	at java.lang.Class.forName0(Native Method)
	at java.lang.Class.forName(Class.java:169)
	at org.telluriumsource.bootstrap.TelluriumSupport.class$(TelluriumSupport.groovy)
	at org.telluriumsource.bootstrap.TelluriumSupport.$get$$class$org$telluriumsource$framework$TelluriumFrameworkMetaClass(TelluriumSupport.groovy)
	at org.telluriumsource.bootstrap.TelluriumSupport.addSupport(TelluriumSupport.groovy:17)
	at org.telluriumsource.test.java.TelluriumTestNGTestCase.setUpForTest(TelluriumTestNGTestCase.java:22)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	at org.testng.internal.MethodHelper.invokeMethod(MethodHelper.java:607)
	at org.testng.internal.Invoker.invokeConfigurationMethod(Invoker.java:417)
	at org.testng.internal.Invoker.invokeConfigurations(Invoker.java:154)
	at org.testng.internal.Invoker.invokeConfigurations(Invoker.java:88)
	at org.testng.TestRunner.beforeRun(TestRunner.java:510)
	at org.testng.TestRunner.run(TestRunner.java:478)
	at org.testng.SuiteRunner.runTest(SuiteRunner.java:332)
	at org.testng.SuiteRunner.runSequentially(SuiteRunner.java:327)
	at org.testng.SuiteRunner.privateRun(SuiteRunner.java:299)
	at org.testng.SuiteRunner.run(SuiteRunner.java:204)
	at org.testng.TestNG.createAndRunSuiteRunners(TestNG.java:867)
	at org.testng.TestNG.runSuitesLocally(TestNG.java:832)
	at org.testng.TestNG.run(TestNG.java:748)
	at org.testng.remote.RemoteTestNG.run(RemoteTestNG.java:73)
	at org.testng.remote.RemoteTestNG.main(RemoteTestNG.java:124)
Caused by: org.telluriumsource.exception.ConfigNotFoundException: Cannot find Tellurium Configuration tellurium.bundle.maxMacroCmd, please check http://code.google.com/p/aost/wiki/TelluriumConfig070 for updated TelluriumConfig.groovy, or report to Tellurium user group at http://groups.google.com/group/tellurium-users
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
	at org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:77)
	at org.codehaus.groovy.runtime.callsite.ConstructorSite$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:107)
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:52)
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:192)
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:200)
	at org.telluriumsource.config.TelluriumConfigurator.checkConfig(TelluriumConfigurator.groovy:41)

```




## Run DSL Script ##

Tellurium 0.7.0 provides a rundsl.groovy script for users to run DSL test script. The rundsl.groovy uses [Groovy Grape](http://groovy.codehaus.org/Grape) to automatically download all dependencies and then run DSL script.

First, you need to configure Grape. Put the following grapeConfig.xml file into your home/.groovy/.

```
<ivysettings>
  <settings defaultResolver="downloadGrapes"/>
  <property
    name="local-maven2-pattern"
    value="${user.home}/.m2/repository/[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).[ext]"
    override="false" />
  <resolvers>
    <chain name="downloadGrapes">
      <filesystem name="cachedGrapes">
        <ivy pattern="${user.home}/.groovy/grapes/[organisation]/[module]/ivy-[revision].xml"/>
        <artifact pattern="${user.home}/.groovy/grapes/[organisation]/[module]/[type]s/[artifact]-[revision].[ext]"/>
      </filesystem>
      <filesystem name="local-maven-2" m2compatible="true" local="true">
        <ivy pattern="${local-maven2-pattern}"/>
        <artifact pattern="${local-maven2-pattern}"/>
      </filesystem>
      <!-- todo add 'endorsed groovy extensions' resolver here -->
      <ibiblio name="kungfuters.3rdparty" root="http://maven.kungfuters.org/content/repositories/thirdparty/" m2compatible="true"/>
      <ibiblio name="codehaus" root="http://repository.codehaus.org/" m2compatible="true"/>
      <ibiblio name="ibiblio" m2compatible="true"/>
      <ibiblio name="java.net2" root="http://download.java.net/maven/2/" m2compatible="true"/>
      <ibiblio name="openqa" root="http://archiva.openqa.org/repository/releases/" m2compatible="true"/>
      <ibiblio name="kungfuters.snapshot" root="http://maven.kungfuters.org/content/repositories/snapshots/" m2compatible="true"/>
      <ibiblio name="kungfuters.release" root="http://maven.kungfuters.org/content/repositories/releases/" m2compatible="true"/>
    </chain>
  </resolvers>
</ivysettings>
```

Then run

```
groovy rundsl.groovy -f DSL_script_name
```

If you are behind a firewall
```
groovy -Dhttp.proxyHost=proxy_host -Dhttp.proxyPort=proxy_port rundsl.groovy -f DSL_script_name
```

If you defined custom UI objects in your project, you should first build the jar artifact, and install it to your local Maven repo. Then update the rundsl.groovy file to add the new dependency. For example, in the tellurium-website reference project, we defined custom UI objects, we added the following two lines into the rundsl.groovy script.

```
Grape.grab(group:'org.telluriumsource', module:'tellurium-website', version:'0.7.0-SNAPSHOT', classLoader:this.class.classLoader.rootLoader)

...

   @Grab(group='org.telluriumsource', module='tellurium-website', version='0.7.0-SNAPSHOT')
```

## useTelluriumEngine ##

To make it convenient for users, Tellurium provides a `useTelluriumEngine` command as follows,

```
    void useTelluriumEngine(boolean isUse){
        useCache(isUse);
        useMacroCmd(isUse);
        useTelluriumApi(isUse);
    }   
```

As you can see, this command actually consists of three commands. In DslContext, Tellurium also provides two handy DSL style methods.

```
   void enableTelluriumEngine();

   void disableTelluriumEngine();
```

## Selenium RC ##

Selenium RC is 1.0.1 in Tellurium 0.7.0. Please use the following Maven dependencies.

```
        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>${selenium-server-version}</version>
            <!--classifier>standalone</classifier-->
            <exclusions>
                <exclusion>
                    <groupId>ant</groupId>
                    <artifactId>ant</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.client-drivers</groupId>
            <artifactId>selenium-java-client-driver</artifactId>
            <version>${selenium-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.codehaus.gmaven.runtime</groupId>
                    <artifactId>gmaven-runtime-default</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.seleniumhq.selenium.core</groupId>
                    <artifactId>selenium-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.seleniumhq.selenium.server</groupId>
                    <artifactId>selenium-server</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

where the properties are defined as

```
    <properties>
        ...
        <selenium-version>1.0.1</selenium-version>
        <selenium-server-version>1.0.1-te3-SNAPSHOT</selenium-server-version>
    </properties>
```


## Misc ##

The default locator in Tellurium Engine is CSS selector by default. That is to say, we set

```
    exploitCssSelector = true
```

in the `Environment` class and you can use

```
    disableCssSelector()
```

or

```
    useCssSelector(false)
```

to switch back to xpath if you like to.

Exposed the following Selenium APIs to DslContext.

  * `void addScript(String scriptContent, String scriptTagId)`
  * `void removeScript(String scriptTagId)`
  * `void captureEntirePageScreenshot(String filename, String kwargs)`
  * `String captureScreenshotToString()`
  * `String captureEntirePageScreenshotToString(String kwargs)`
  * `String retrieveLastRemoteControlLogs()`

The following methods in DslContext have been renamed

  * `String jsonify(String uid)` is renamed to `String toString(String uid)`
  * `String generateHtml(String uid)` is renamed to `String toHTML(String uid)`

# Changes in Engine #

Tellurium 0.7.0 include a new Engine embedded in Selenium Core. The main functionalities of the Tellurium Engine include
  * CSS Selector support based on jQuery
  * New APIs based on jQuery
  * UI module group locating
  * UI module Caching

## Code Structure ##

The following are Javascript files in the Engine project:

```
[jfang@Mars engine]$ tree src/main/resources/core/scripts/
src/main/resources/core/scripts/
|-- firebuglite
|   |-- errorIcon.png
|   |-- firebug-lite.css
|   |-- firebug-lite.js
|   |-- firebug.gif
|   |-- firebug_logo.png
|   |-- infoIcon.png
|   |-- progress.gif
|   |-- spacer.gif
|   |-- tree_close.gif
|   |-- tree_open.gif
|   `-- warningIcon.png
|-- htmlutils.js
|-- jquery-1.4.2.js
|-- jquery-cookies-2.1.0.js
|-- jquery-simpletip-1.3.1.js
|-- json2.js
|-- log4js.js
|-- selenium-api.js
|-- selenium-browserbot.js
|-- selenium-browserdetect.js
|-- selenium-commandhandlers.js
|-- selenium-executionloop.js
|-- selenium-logging.js
|-- selenium-remoterunner.js
|-- selenium-testrunner.js
|-- selenium-version.js
|-- tellurium-api.js
|-- tellurium-cache.js
|-- tellurium-extensions.js
|-- tellurium-logging.js
|-- tellurium-selector.js
|-- tellurium-udl.js
|-- tellurium-uialg.js
|-- tellurium-uibasic.js
|-- tellurium-uiextra.js
|-- tellurium-uimodule.js
|-- tellurium-uiobj.js
|-- tellurium-uisnapshot.js
|-- tellurium.js
|-- tooltip
|   `-- simpletip.css
|-- user-extensions.js
|-- utils.js
`-- xmlextras.js
```

where
  * jquery-1.4.2.js: jQuery is updated to the latest version 1.4.2.
  * jquery-cookies-2.1.0.js: jQuery Cookies Plugin to support more cookie related operation
  * tellurium.js: Entry point for Tellurium Engine code and it defined the `Tellurium` function.
  * tellurium-selector.js: CSS selector builder
  * tellurium-udl.js: Tellurium UDL processing
  * tellurium-uialg.js: Tellurium UI algorithm
  * tellurium-uibasic.js Tellurium UI basic
  * tellurium-uiextra.js Tellurium extra UI functionalities
  * tellurium-uimodule.js Tellurium UI module definition on Engine side
  * tellurium-uiobj.js Tellurium UI object
  * tellurium-uisnapshot Tellurium UI snapshot
  * tellurium-cache.js: Tellurium Engine caching for UI modules and locators
  * telurium-extension.js: Extra Tellurium APIs for Selenium
  * tellurium-api.js: New Tellurium APIs based on jQuery
  * utils.js: Utility functions

## Locate Strategies ##

Moved the CSS selector locate strategy and the cache aware locate strategy from Core to the Engine. This is done by adding the following lines to the method `BrowserBot.prototype._registerAllLocatorFunctions` in the selenium-browserbot.js file:

```
    this.locationStrategies['jquery'] = function(locator, inDocument, inWindow) {
        return tellurium.locateElementByCSSSelector(locator, inDocument, inWindow);
    };

    //used internally by Tellurium Engine
    this.locationStrategies['uimcal'] = function(locator, inDocument, inWindow) {
        return tellurium.locateElementWithCacheAware(locator, inDocument, inWindow);
    };
```


## CSS Selector Support ##

Actually, [the CSS selector support](http://code.google.com/p/aost/wiki/TelluriumjQuerySelector) was added in Tellurium 0.6.0 and we changed the name "jQuery Selector" to "CSS Selector" as suggested by Dylan.

## New APIs ##

Right now, new Tellurium jQuery-based APIs include

```
TelluriumApi.prototype.blur = function(locator);

TelluriumApi.prototype.click = function(locator);

TelluriumApi.prototype.clickAt = function(locator, coordString);

TelluriumApi.prototype.doubleClick = function(locator);

TelluriumApi.prototype.fireEvent = function(locator, event);

TelluriumApi.prototype.focus = function(locator);

TelluriumApi.prototype.typeKey = function(locator, key);

TelluriumApi.prototype.keyDown = function(locator, key);

TelluriumApi.prototype.keyPress = function(locator, key);

TelluriumApi.prototype.keyUp = function(locator, key);

TelluriumApi.prototype.mouseOver = function(locator);

TelluriumApi.prototype.mouseDown = function(locator);

TelluriumApi.prototype.mouseDownRight = function(locator);

TelluriumApi.prototype.mouseEnter = function(locator);

TelluriumApi.prototype.mouseLeave = function(locator);

TelluriumApi.prototype.mouseOut = function(locator);

TelluriumApi.prototype.submit = function(locator);

TelluriumApi.prototype.check = function(locator);

TelluriumApi.prototype.uncheck = function(locator);

TelluriumApi.prototype.isElementPresent = function(locator);

TelluriumApi.prototype.getAttribute = function(locator, attributeName);

TelluriumApi.prototype.waitForPageToLoad = function(timeout);

TelluriumApi.prototype.type = function(locator, val);

TelluriumApi.prototype.select = function(locator, optionLocator);

TelluriumApi.prototype.addSelection = function(locator, optionLocator);

TelluriumApi.prototype.removeSelection = function(locator, optionLocator);

TelluriumApi.prototype.removeAllSelections = function(locator);

TelluriumApi.prototype.open = function(url);

TelluriumApi.prototype.getText = function(locator);

TelluriumApi.prototype.isChecked = function(locator);

TelluriumApi.prototype.isVisible = function(locator);

TelluriumApi.prototype.isEditable = function(locator) ;

TelluriumApi.prototype.getXpathCount = function(xpath);

TelluriumApi.prototype.getAllText = function(locator);

TelluriumApi.prototype.getCssSelectorCount = function(locator);

TelluriumApi.prototype.getCSS = function(locator, cssName);

TelluriumApi.prototype.isDisabled = function(locator);

TelluriumApi.prototype.getListSize = function(locator, separators);


TelluriumApi.prototype.getCacheState = function();

TelluriumApi.prototype.enableCache = function();

TelluriumApi.prototype.disableCache = function();

TelluriumApi.prototype.cleanCache = function();

TelluriumApi.prototype.setCacheMaxSize = function(size);

TelluriumApi.prototype.getCacheSize = function();

TelluriumApi.prototype.getCacheMaxSize = function();

TelluriumApi.prototype.getCacheUsage = function();

TelluriumApi.prototype.addNamespace = function(prefix, namespace);

TelluriumApi.prototype.getNamespace = function(prefix);

TelluriumApi.prototype.useDiscardNewPolicy = function();

TelluriumApi.prototype.useDiscardOldPolicy = function();

TelluriumApi.prototype.useDiscardLeastUsedPolicy = function();

TelluriumApi.prototype.useDiscardInvalidPolicy = function();

TelluriumApi.prototype.getCachePolicyName = function();

TelluriumApi.prototype.useUiModule = function(json);

TelluriumApi.prototype.isUiModuleCached = function(id);

TelluriumApi.prototype.getEngineState = function();

TelluriumApi.prototype.useEngineLog = function(isUse);
```

As you can see, most of the new APIs have the same signature as the Selenium ones so that your test code is agnostic to which test driving engine that you use. You can always switch between the Tellurium Engine and Selenium Engine by the following API at Tellurium core.

```
public void useTelluriumApi(boolean isUse);
```

## jQuery ##

The jQuery in Engine has been upgraded from 1.3.2 to 1.4.2.

## Engine Logging ##

Tellurium Engine uses [Firebug Lite](http://getfirebug.com/lite.html) to add debug information to the console. By default the Firebug Lite is off and you will only see a Firebug icon on the bottom right as shown in the following figure.

http://tellurium-users.googlegroups.com/web/telluriumEngineFirebugLiteOff.png?gda=g8Q-1lMAAACsdq5EJRKLPm_KNrr_VHB_DVXQX3coVrzCE0wbgZNX0sYl7qkjkLygZeJEraCq1Nizt3T_4awU0H2oK-9nYgtIMrYifh3RmGHD4v9PaZfDexVi73jmlo822J6Z5KZsXFo&gsc=jh3WkgsAAABHX8Z8u5jajkir99LhxBO3

If you click on the icon and the Firebug Lite console will appear and the log information will be shown on the console as follows.

http://tellurium-users.googlegroups.com/web/telluriumEngineFirebugLiteOn.png?gda=jO0GwlIAAACsdq5EJRKLPm_KNrr_VHB_DVXQX3coVrzCE0wbgZNX0sYl7qkjkLygZeJEraCq1Njg8bv9nMK6yOBSE4EEIlmEVeLt2muIgCMmECKmxvZ2j4IeqPHHCwbz-gobneSjMyE&gsc=jh3WkgsAAABHX8Z8u5jajkir99LhxBO3

To turn on the debug, you should either click on the "Tellurium Log" button or call the following method from your test case.

```
    void useEngineLog(boolean isUse);
```

## Add JavaScript Error Stack to Selenium Errors ##

We utilized [the JavaScript Stack Trace project](http://github.com/emwendelin/javascript-stacktrace) to refactor Selenium Errors in Selenium Core so that the JavaScript Error Stack will be passed back Tellurium Core.

The implementation is in htmlutils.js in Selenium Core as follows.

```
function SeleniumError(message) {
    if(tellurium.logManager.isUseLog){
        var jstack = printStackTrace();
        if(jstack != null && typeof(jstack) != 'undefined'){
            message = message + "\nJavaScript Error Stack: \n" + jstack.join('\n\n');
        }
    }
    var error = new Error(message);
    if (typeof(arguments.caller) != 'undefined') { // IE, not ECMA
        var result = '';
        for (var a = arguments.caller; a != null; a = a.caller) {
            result += '> ' + a.callee.toString() + '\n';
            if (a.caller == a) {
                result += '*';
                break;
            }
        }
        error.stack = result;
    }
    error.isSeleniumError = true;
    fbError("Selenium Error: "+ message, error);
    return error;
}
```

where `printStackTrace()` is the stacktrace project API. Be aware that the
JavaScript error stack will only be passed back to Tellurium Core if the Engine
log is enabled by calling.

```
   useEngineLog(true);
```

Example output:

```
com.thoughtworks.selenium.SeleniumException: ERROR: Element uimcal={"rid":"search.search_project_button","locator":"jquery=form:group(input[name=q], input[value=Search projects][type=submit], input[value=Search the Web][type=submit]) input[value=Search projects][type=submit]"} not found
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

SeleniumError("Element uimcal={\"rid\":\"search.search_project_button\",\"locator\":\"jquery=form:group(input[name=q], input[value=Search projects][type=submit], input[value=Search the Web][type=submit]) input[value=Search projects][type=submit]\"} not found")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806

{anonymous}("uimcal={\"rid\":\"search.search_project_button\",\"locator\":\"jquery=form:group(input[name=q], input[value=Search projects][type=submit], input[value=Search the Web][type=submit]) input[value=Search projects][type=submit]\"}")@http://localhost:4444/selenium-server/core/scripts/selenium-browserbot.js:1341

{anonymous}("uimcal={\"rid\":\"search.search_project_button\",\"locator\":\"jquery=form:group(input[name=q], input[value=Search projects][type=submit], input[value=Search the Web][type=submit]) input[value=Search projects][type=submit]\"}")@http://localhost:4444/selenium-server/core/scripts/selenium-api.js:227

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:922

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:876

{anonymous}("[{\"uid\":\"search.search_project_button\",\"args\":[\"jquery=form:group(input[name=q], input[value=Search projects][type=submit], input[value=Search the Web][type=submit]) input[value=Search projects][type=submit]\"],\"name\":\"click\",\"sequ\":7}]","")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338

{anonymous}("[{\"uid\":\"search.search_project_button\",\"args\":[\"jquery=form:group(input[name=q], input[value=Search projects][type=submit], input[value=Search the Web][type=submit]) input[value=Search projects][type=submit]\"],\"name\":\"click\",\"sequ\":7}]","")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112

{anonymous}(-3)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78

{anonymous}(-3)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60
	at com.thoughtworks.selenium.HttpCommandProcessor.throwAssertionFailureExceptionOrError(HttpCommandProcessor.java:97)
	at com.thoughtworks.selenium.HttpCommandProcessor.doCommand(HttpCommandProcessor.java:91)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)

```

# Changes in Maven Build #

Our Maven repository is moved and the Maven Repositories are changed as well.

```
        <repository>
            <id>kungfuters-public-releases-repo</id>
            <name>Kungfuters.org Public Releases Repository</name>
            <url>http://maven.kungfuters.org/content/repositories/releases</url>
        </repository>
        <snapshotRepository>
            <id>kungfuters-public-snapshots-repo</id>
            <name>Kungfuters.org Public Snapshot Repository</name>
            <url>http://maven.kungfuters.org/content/repositories/snapshots</url>
        </snapshotRepository>
```

telluriumsource.org becomes our official domain name and the web site is under construction.

```
        <site>
            <id>tellurium-site</id>
            <name>Tellurium Site</name>
            <url>scp://telluriumsource.org/var/www/telluriumsource.org/public/maven</url>
        </site>
```

After the domain name change, you need to use the following Tellurium dependencies for your project

```
        <dependency>
            <groupId>org.telluriumsource</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>0.7.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>1.0.1-te3-SNAPSHOT</version>
        </dependency>
```

For Maven archetypes, tellurium-junit-archetype becomes the following,

```
    <groupId>org.telluriumsource</groupId>
    <artifactId>tellurium-junit-archetype</artifactId>
    <version>0.7.0-SNAPSHOT</version>
```

Similarly, the tellurium-testng-archetype has been changed to

```
    <groupId>org.telluriumsource</groupId>
    <artifactId>tellurium-testng-archetype</artifactId>
    <version>0.7.0-SNAPSHOT</version>
```

To create a Tellurium project based on Tellurium 0.7.0 SNAPSHOT, you should use the Maven archetype 0.7.0-SNAPSHOT. To create a JUnit project, use the following Maven command:

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id -DarchetypeArtifactId=tellurium-junit-archetype -DarchetypeGroupId=org.telluriumsource -DarchetypeVersion=0.7.0-SNAPSHOT -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/snapshots
```

Similarly, to create a TestNG project, use the following command:

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id -DarchetypeArtifactId=tellurium-testng-archetype -DarchetypeGroupId=org.telluriumsource -DarchetypeVersion=0.7.0-SNAPSHOT -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/snapshots
```

To create a Tellurium UI widget project, we can use Tellurium Widget archetype as follows.

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id -DarchetypeArtifactId=tellurium-widget-archetype -DarchetypeGroupId=org.telluriumsource -DarchetypeVersion=0.7.0-SNAPSHOT -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/snapshots
```

# Reference Project #

The old two reference projects, i.e., tellurium-junit-java and tellurium-testng-java reference projects have been updated and merged into one reference project, tellurium-website. The code structure is shown as follows,

```
.
|-- HowTO
|-- LICENSE.txt
|-- README
|-- TelluriumConfig.groovy
|-- pom.xml
|-- rundsl.bat
|-- rundsl.sh
|-- src
|   |-- main
|   |   |-- groovy
|   |   |   `-- org
|   |   |       `-- telluriumsource
|   |   |           `-- ui
|   |   |               |-- builder
|   |   |               |   `-- SelectMenuBuilder.groovy
|   |   |               `-- object
|   |   |                   `-- SelectMenu.groovy
|   |   `-- resources
|   `-- test
|       |-- groovy
|       |   `-- org
|       |       `-- telluriumsource
|       |           |-- ddt
|       |           |   |-- TelluriumIssuesDataDrivenTest.groovy
|       |           |   `-- TelluriumIssuesModule.groovy
|       |           |-- module
|       |           |   |-- TelluriumDownloadsPage.groovy
|       |           |   |-- TelluriumIssuesPage.groovy
|       |           |   |-- TelluriumProjectPage.groovy
|       |           |   `-- TelluriumWikiPage.groovy
|       |           `-- test
|       |               |-- TelluriumDownloadsPageJUnitTestCase.java
|       |               |-- TelluriumDownloadsPageTestNGTestCase.java
|       |               |-- TelluriumIssuesPageJUnitTestCase.java
|       |               |-- TelluriumIssuesPageTestNGTestCase.java
|       |               |-- TelluriumProjectPageJUnitTestCase.java
|       |               |-- TelluriumProjectPageTestNGTestCase.java
|       |               |-- TelluriumWikiPageJUnitTestCase.java
|       |               `-- TelluriumWikiPageTestNGTestCase.java
|       `-- resources
|           `-- org
|               `-- telluriumsource
|                   |-- data
|                   |   `-- TelluriumIssuesInput.txt
|                   |-- dsl
|                   |   `-- TelluriumPage.dsl
|                   `-- test-suites
|                       `-- reportng.xml

```

# Examples #

In Tellurium Core, we include test cases for Google search UI.

## Google Search UI Module ##

```
package org.telluriumsource.module

import org.telluriumsource.dsl.DslContext

public class GoogleSearchModule extends DslContext {

  public void defineUi() {
      ui.Image(uid: "Logo", clocator: [tag: "img", src: "*.gif"])

    ui.Container(uid: "Google", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "q"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btnG"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnI"])
    }
  }

  public void doGoogleSearch(String input) {
    keyType "Google.Input", input
    pause 500
    click "Google.Search"
    waitForPageToLoad 30000
  }

  public void doImFeelingLucky(String input) {
    type "Google.Input", input
    pause 500
    click "Google.ImFeelingLucky"
    waitForPageToLoad 30000
  }

  //Test jQuery selector for attributes
  public String getLogoAlt(){
    return getImageAlt("Logo")
  }

  boolean isInputDisabled() {
    return isDisabled("Google.Input")
  }

  public void doTypeRepeated(String input){
    customUiCall "Google.Input", typeRepeated, input
    pause 500
    click "Google.Search"
    waitForPageToLoad 30000
  }
}
```

## TestNG Test Case ##

```
package org.telluriumsource.java;

import org.telluriumsource.test.java.TelluriumTestNGTestCase;
import org.telluriumsource.module.GoogleSearchModule;
import org.telluriumsource.framework.CachePolicy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import static org.testng.Assert.*;

public class GoogleSearchTestNGTestCase extends TelluriumTestNGTestCase {
    private static GoogleSearchModule gsm;
    private static String te_ns = "http://telluriumsource.org/ns";

    @BeforeClass
    public static void initUi() {
        gsm = new GoogleSearchModule();
        gsm.defineUi();
        connectSeleniumServer();
        useCssSelector(true);
        useTelluriumApi(true);
        useTrace(true);
//        useCache(true);
    }

    @BeforeMethod
    public void connectToGoogle() {

        connectUrl("http://www.google.com");
    }

    @Test
    public void testGoogleSearch() {
        gsm.doGoogleSearch("tellurium . ( Groovy ) Test");
    }

    @Test
    public void testGoogleSearchFeelingLucky() {
        gsm.doImFeelingLucky("tellurium automated Testing");
    }

    @Test
    public void testLogo(){
        String alt = gsm.getLogoAlt();
        assertNotNull(alt);
        assertEquals("Google", alt);
    }

    @Test
    public void testIsDisabled(){
        useCssSelector(true);
        boolean result = gsm.isInputDisabled();
        assertFalse(result);
        useCssSelector(false);
        result = gsm.isInputDisabled();
        assertFalse(result);
    }

    @Test
    public void testUseSelectorCache(){
        useCache(true);
        boolean result = gsm.getCacheState();
        assertTrue(result);

        useCache(false);
        result = gsm.getCacheState();
        assertFalse(result);
    }

    @Test
    public void testTypeRepeated(){
        gsm.doTypeRepeated("tellurium jQuery");
    }

    @Test
    public void testRegisterNamespace(){
        registerNamespace("te", te_ns);
        String ns = getNamespace("te");
        assertNotNull(ns);
        assertEquals(te_ns, ns);
        ns = getNamespace("x");
        assertNotNull(ns);
        assertEquals("http://www.w3.org/1999/xhtml", ns);
        ns = getNamespace("mathml");
        assertNotNull(ns);
        assertEquals("http://www.w3.org/1998/Math/MathML", ns);
    }

    @Test
    public void testCachePolicy(){
        useCssSelector(true);
        useCache(true);
        String policy = getCurrentCachePolicy();
        assertEquals("DiscardOldPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_LEAST_USED);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardLeastUsedPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_INVALID);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardInvalidPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_NEW);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardNewPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_OLD);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardOldPolicy", policy);
    }

    @AfterClass
    public static void tearDown(){
        showTrace();
    }
}

```

# Where to Find ? #

Where to find the latest 0.7.0 snapshots?

They are in our Maven repo.

  * [Tellurium Core 0.7.0 Snapshot](http://maven.kungfuters.org/content/repositories/snapshots/org/telluriumsource/tellurium-core/0.7.0-SNAPSHOT/)
  * [Custom Selenium Server with Tellurium Engine](http://maven.kungfuters.org/content/repositories/snapshots/org/seleniumhq/selenium/server/selenium-server/1.0.1-te3-SNAPSHOT/)

If you use Maven, you need the following dependencies

```
        <dependency>
            <groupId>org.telluriumsource</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>0.7.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>1.0.1-te3-SNAPSHOT</version>
        </dependency>
```

Here is a sample POM file.

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.telluriumsource</groupId>
    <artifactId>tellurium-website</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>Tellurium Reference Project - Tellurium Website</name>

    <repositories>
        <repository>
            <id>caja</id>
            <url>http://google-caja.googlecode.com/svn/maven</url>
        </repository>
        <repository>
            <id>kungfuters-public-snapshots-repo</id>
            <name>Kungfuters.org Public Snapshot Repository</name>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <url>http://maven.kungfuters.org/content/repositories/snapshots</url>
        </repository>
        <repository>
            <id>kungfuters-public-releases-repo</id>
            <name>Kungfuters.org Public Releases Repository</name>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <url>http://maven.kungfuters.org/content/repositories/releases</url>
        </repository>
        <repository>
            <id>kungfuters-thirdparty-releases-repo</id>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <url>http://maven.kungfuters.org/content/repositories/thirdparty</url>
        </repository>
        <repository>
            <id>openqa-release-repo</id>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <url>http://archiva.openqa.org/repository/releases</url>
        </repository>
    </repositories>

        
    <dependencies>
        <dependency>
            <groupId>org.codehaus.gmaven</groupId>
            <artifactId>gmaven-mojo</artifactId>
            <version>${gmaven-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.codehaus.gmaven.runtime</groupId>
                    <artifactId>gmaven-runtime-1.5</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        
        <dependency>
            <groupId>org.codehaus.gmaven.runtime</groupId>
            <artifactId>gmaven-runtime-1.6</artifactId>
            <version>${gmaven-version}</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.7</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.telluriumsource</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>${tellurium-version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>${selenium-version}-te3-SNAPSHOT</version>
            <!--classifier>standalone</classifier-->
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.client-drivers</groupId>
            <artifactId>selenium-java-client-driver</artifactId>
            <version>${selenium-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.codehaus.groovy.maven.runtime</groupId>
                    <artifactId>gmaven-runtime-default</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.seleniumhq.selenium.core</groupId>
                    <artifactId>selenium-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.seleniumhq.selenium.server</groupId>
                    <artifactId>selenium-server</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-all</artifactId>
            <version>${groovy-version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>caja</groupId>
            <artifactId>json_simple</artifactId>
            <version>r1</version>
        </dependency>
        <dependency>
            <groupId>org.stringtree</groupId>
            <artifactId>stringtree-json</artifactId>
            <version>2.0.10</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.0.1-FINAL</version>
        </dependency>
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>5.8</version>
            <classifier>jdk15</classifier>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.uncommons</groupId>
            <artifactId>reportng</artifactId>
            <version>0.9.8</version>
        </dependency>
        <dependency>
            <groupId>velocity</groupId>
            <artifactId>velocity-dep</artifactId>
            <version>1.4</version>
        </dependency>
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>src/main/groovy</directory>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
        <testResources>
            <testResource>
                <directory>src/test/groovy</directory>
            </testResource>
            <testResource>
                <directory>src/test/resources</directory>
            </testResource>
        </testResources>

        <pluginManagement>
            <plugins>
                <plugin>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>2.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.4.3</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-report-plugin</artifactId>
                    <version>2.4.3</version>
                </plugin>
                <plugin>
                    <artifactId>maven-jar-plugin</artifactId>
                    <version>2.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-source-plugin</artifactId>
                    <version>2.0.4</version>
                </plugin>
                <plugin>
                    <artifactId>maven-javadoc-plugin</artifactId>
                    <version>2.4</version>
                </plugin>
                <plugin>
                    <artifactId>maven-site-plugin</artifactId>
                    <version>2.0-beta-7</version>
                </plugin>
                <plugin>
                    <groupId>org.codehaus.gmaven</groupId>
                    <artifactId>gmaven-plugin</artifactId>
                    <version>${gmaven-version}</version>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-jxr-plugin</artifactId>
                    <version>2.1</version>
                </plugin>                
            </plugins>
        </pluginManagement>
        
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java-version}</source>
                    <target>${java-version}</target>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <includes>
                        <include>**/*_UT.java</include>
                        <include>**/*TestCase.java</include>
                    </includes>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-jar-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>test-jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <artifactId>maven-source-plugin</artifactId>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>jar</goal>
                            <goal>test-jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <artifactId>maven-javadoc-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.codehaus.gmaven</groupId>
                <artifactId>gmaven-plugin</artifactId>
                <configuration>
                    <providerSelection>1.7</providerSelection>
                    <targetBytecode>${java-version}</targetBytecode>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <!-- The generateStubs goals are not yet working for enums and generics -->
                            <goal>generateStubs</goal>
                            <goal>compile</goal>
                            <goal>generateTestStubs</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    <reporting>
        <plugins>
            <plugin>
                <artifactId>maven-surefire-report-plugin</artifactId>
                <reportSets>
                    <reportSet>
                        <reports>
                            <report>report-only</report>
                        </reports>
                    </reportSet>
                </reportSets>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jxr-plugin</artifactId>
            </plugin>
        </plugins>
    </reporting>
    <properties>
        <java-version>1.6</java-version>
        <groovy-version>1.7.0</groovy-version>
        <gmaven-version>1.2</gmaven-version>
        <selenium-version>1.0.1</selenium-version>
        <tellurium-version>0.7.0-SNAPSHOT</tellurium-version>
        <javac-debug>true</javac-debug>
    </properties>

</project>

```

# What's Next ? #

  * Tellurium Widget revisit
  * Trump IDE upgrade to match 0.7.0

# Want to Contribute ? #

We welcome contributions for Tellurium from various aspects. More details on [How to Contribute](http://code.google.com/p/aost/wiki/HowToContribute).

# Resources #

  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium at Rich Web Experience 2009 by Jian Fang and Vivek Mongolu](http://www.slideshare.net/John.Jian.Fang/tellurium-at-rich-web-experience2009-2806967)
  * [Tellurium 0.6.0 User Guide](http://www.slideshare.net/John.Jian.Fang/tellurium-060-user-guide)
  * [Tellurium video tutorial by Vivek Mongolu](http://vimeo.com/8601173)
  * [Tellurium - A New Approach For Web Testing](http://www.slideshare.net/John.Jian.Fang/telluriumanewapproachforwebtesting)
  * [10 Minutes to Tellurium](http://vimeo.com/8600410)
  * [Tellurium 0.7.0 Demo Project](http://aost.googlecode.com/files/santa-algorithm-demo.tar.gz)