

# Introduction #

UI Module represents a group of nested UI elements or a UI widget and it is the heart of Tellurium Automated Testing Framework (Tellurium). You may not be aware that Tellurium 0.7.0 provides a set of UI module level APIs. Here we go over them one by one.


# UI Module APIs #

## Example ##

First of all, we like to use Google Search module as an example so that everyone can run the test code in this article.

For Google Search Module, we define the UI module class as

```
package org.telluriumsource.module

import org.telluriumsource.dsl.DslContext

public class GoogleSearchModule extends DslContext {

  public void defineUi() {
    ui.Container(uid: "Google", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "q"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btnG"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnI"])
    }

    ui.Container(uid: "ProblematicGoogle", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "p"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btns"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnf"])
    }    
  }

  public void doProblematicGoogleSearch(String input) {
    keyType "ProblematicGoogle.Input", input
    pause 500
    click "ProblematicGoogle.Search"
    waitForPageToLoad 30000
  }
}
```

where UI Module "Google" is a correct UI Module definition for Google Search and "ProblematicGoogle" is a not-so-correct UI module to demonstrate the power of UI module partial matching in Tellurium 0.7.0.

## dump ##

The dump method prints out the generated runtime locators by Tellurium core.

### API ###

```
   void dump(String uid);
```
where _uid_ is the UI module name, i.e., the root element UID of a UI module.

### Test case ###

```
    @Test
    public void testDump(){
        useCssSelector(false);
        gsm.dump("Google");
        useCssSelector(true);
        gsm.dump("Google");
    }
```

Note that here we ask Tellurium core to generate xpath and CSS selector locators, respectively.

### Results ###

```
Dump locator information for Google
-------------------------------------------------------
Google: //descendant-or-self::table
Google.Input: //descendant-or-self::table/descendant-or-self::input[@title="Google Search" and @name="q"]
Google.Search: //descendant-or-self::table/descendant-or-self::input[@type="submit" and @value="Google Search" and @name="btnG"]
Google.ImFeelingLucky: //descendant-or-self::table/descendant-or-self::input[@type="submit" and @value="I'm Feeling Lucky" and @name="btnI"]
-------------------------------------------------------

Dump locator information for Google
-------------------------------------------------------
Google: jquery=table
Google.Input: jquery=table input[title=Google Search][name=q]
Google.Search: jquery=table input[type=submit][value=Google Search][name=btnG]
Google.ImFeelingLucky: jquery=table input[type=submit][value$=m Feeling Lucky][name=btnI]
-------------------------------------------------------
```

## toString ##

The _toString_ method converts a UI module to a JSON presentation. Tellurium Core actually provides two methods for your convenience.

### API ###

```
   public String toString(String uid);
   public JSONArray toJSONArray(String uid);
```

The _toString_ method calls the _toJSONArray_ method first under the hood and then prints out the JSON array as a string.

### Test case ###

```
    @Test
    public void testToString(){
        String json = gsm.toString("Google");
        System.out.println(json);
    }
```

### Results ###

```
   [{"obj":{"uid":"Google","locator":{"tag":"table"},"uiType":"Container"},"key":"Google"},{"obj":{"uid":"Input","locator":{"tag":"input","attributes":{"title":"Google Search","name":"q"}},"uiType":"InputBox"},"key":"Google.Input"},{"obj":{"uid":"Search","locator":{"tag":"input","attributes":{"name":"btnG","value":"Google Search","type":"submit"}},"uiType":"SubmitButton"},"key":"Google.Search"},{"obj":{"uid":"ImFeelingLucky","locator":{"tag":"input","attributes":{"name":"btnI","value":"I'm Feeling Lucky","type":"submit"}},"uiType":"SubmitButton"},"key":"Google.ImFeelingLucky"}]

```

## toHTML ##

The _toHTML_ method converts the UI module to a HTML source by [reverse engineering](http://code.google.com/p/aost/wiki/GenerateHtmlFromUIModule). This API is extremely useful to work with [Tellurium mock http server](http://code.google.com/p/aost/wiki/TelluriumMockHttpServer) if we want to diagnose problems in other people's UI module definitions while we have no access to their html sources.

### API ###

```
   public String toHTML(String uid);
   public String toHTML();
```

The first method generates the HTML source for a UI module and the second one generates the HTML source for all UI modules defined in the current UI module class.

### Test Case ###

```
   @Test
   public void testToHTML(){
       String html = gsm.toHTML("Google");
       System.out.println(html);
   }
```

### Result ###

```
   <table>
      <input title="Google Search" name="q"/>
      <input type="submit" value="Google Search" name="btnG"/>
      <input type="submit" value="I'm Feeling Lucky" name="btnI"/>
   </table>
```

## getHTMLSource ##

The getHTMLSource method returns the runtime HTML source for a UI module.

### API ###

```
   java.util.List getHTMLSourceResponse(String uid);
   void getHTMLSource(String uid);
```

The first method gets back the HTML source as a list of _key_ and _val_ pair and the second one prints out them to console.

```
   class KeyValuePair {
     public static final String KEY = "key";
     String key;

     public static final String VAL = "val";
     String val;
   }
```

### Test Case ###

```
    @Test
    public void testGetHTMLSource(){
        gsm.getHTMLSource("Google");
    }
```

### Result ###

```
TE: Name: getHTMLSource, start: 1266260182744, duration: 67ms
TE: Found exact match for UI Module 'Google': {"id":"Google","relaxDetails":[],"matches":1,"relaxed":false,"score":100.0,"found":true}
Google: 

<table cellpadding="0" cellspacing="0"><tbody><tr valign="top"><td width="25%">&nbsp;</td><td nowrap="nowrap" align="center"><input name="hl" value="en" type="hidden"><input name="source" value="hp" type="hidden"><input autocomplete="off" onblur="google&amp;&amp;google.fade&amp;&amp;google.fade()" maxlength="2048" name="q" size="55" class="lst" title="Google Search" value=""><br><input name="btnG" value="Google Search" class="lsb" onclick="this.checked=1" type="submit"><input name="btnI" value="I'm Feeling Lucky" class="lsb" onclick="this.checked=1" type="submit"></td><td id="sbl" nowrap="nowrap" width="25%" align="left"><font size="-2">&nbsp;&nbsp;<a href="/advanced_search?hl=en">Advanced Search</a><br>&nbsp;&nbsp;<a href="/language_tools?hl=en">Language Tools</a></font></td></tr></tbody></table>

Google.Input: 

<input autocomplete="off" onblur="google&amp;&amp;google.fade&amp;&amp;google.fade()" maxlength="2048" name="q" size="55" class="lst" title="Google Search" value="">

Google.Search: 

<input name="btnG" value="Google Search" class="lsb" onclick="this.checked=1" type="submit">

Google.ImFeelingLucky: 

<input name="btnI" value="I'm Feeling Lucky" class="lsb" onclick="this.checked=1" type="submit">

```

## show ##

The _show_ method outlines the UI module on the web page under testing and shows [some visual effects](http://code.google.com/p/aost/wiki/TelluriumUiModuleVisualEffect) when a user mouses over it.

### API ###

```
   void show(String uid, int delay);
   void startShow(String uid);
   void endShow(String uid);
```

The first method shows the UI module visual effect for some time (delay in milliseconds). The other two methods are used to manually start and end the visual effect.

### Test Case ###

```
    @Test
    public void testShow(){
        gsm.show("Google", 10000);
//        gsm.startShow("Form");
//        gsm.endShow("Form");
    }
```

### Result ###

The visual effect is illustrated in the following graph.

http://tellurium-users.googlegroups.com/web/GoogleSearchShowUi.png?gda=qOZnC0gAAACsdq5EJRKLPm_KNrr_VHB_3eE2qYcIelEaKO4G2gjlKg0yV8_RHJOjV4bPZr7T9lYlzhb83kORdLwM2moY-MeuGjVgdwNi-BwrUzBGT2hOzg&gsc=m1-YYgsAAADW8LgqTyA9jlZ6blL9elJr

## validate ##

The _validate_ method is used to validate if the UI module is correct and returns the mismatches at runtime.

### API ###

```
   UiModuleValidationResponse getUiModuleValidationResult(String uid);
   void validate(String uid)
```

The first method returns the validation result as a UiModuleValidationResponse object,

```
public class UiModuleValidationResponse {
  
    //ID for the UI module
    public static String ID = "id";
    private String id = null;

    //Successfully found or not
    public static String FOUND = "found";
    private boolean found = false;

    //whether this the UI module used closest Match or not
    public static String RELAXED = "relaxed";
    private boolean relaxed = false;

    //match count
    public static String MATCHCOUNT = "matches";
    private int matches = 0;

    //scaled match score (0-100)
    public static String SCORE = "score";
    private float score = 0.0;

    public static String RELAXDETAIL = "relaxDetail";
    //details for the relax, i.e., closest match
    public static String RELAXDETAILS = "relaxDetails";
    private List<RelaxDetail> relaxDetails = null;
```

and the RelaxDetail is defined as follows,

```
public class RelaxDetail {
    //which UID got relaxed, i.e., closest Match
    public static String UID = "uid";
    private String uid = null;

    //the clocator defintion for the UI object corresponding to the UID
    public static String LOCATOR = "locator";
    private CompositeLocator locator = null;

    //The actual html source of the closest match element
    public static String HTML = "html";
    private String html = null;
```

The second one simply prints out the result to console.

### Test Case ###

```
    @Test
    public void testValidate(){
        gsm.validate("Google");
        gsm.validate("ProblematicGoogle");
    }
```

Here we validate the correct UI module "Google" and the not-so-correct UI module "ProblematicGoogle".

### Result ###

```
TE: Name: getValidateUiModule, start: 1266260203639, duration: 68ms

UI Module Validation Result for Google

-------------------------------------------------------

	Found Exact Match: true 

	Found Closest Match: false 

	Match Count: 1 

	Match Score: 100 


-------------------------------------------------------

TE: Name: getValidateUiModule, start: 1266260203774, duration: 41ms

UI Module Validation Result for ProblematicGoogle

-------------------------------------------------------

	Found Exact Match: false 

	Found Closest Match: true 

	Match Count: 1 

	Match Score: 32.292 


	Closest Match Details: 

	--- Element ProblematicGoogle.Input -->

	 Composite Locator: <input title="Google Search" name="p"/> 

	 Closest Matched Element: <input autocomplete="off" onblur="google&amp;&amp;google.fade&amp;&amp;google.fade()" maxlength="2048" name="q" size="55" class="lst" title="Google Search" value=""> 



	--- Element ProblematicGoogle.Search -->

	 Composite Locator: <input name="btns" value="Google Search" type="submit"/> 

	 Closest Matched Element: <input name="btnG" value="Google Search" class="lsb" onclick="this.checked=1" type="submit"> 



	--- Element ProblematicGoogle.ImFeelingLucky -->

	 Composite Locator: <input name="btnf" value="I'm Feeling Lucky" type="submit"/> 

	 Closest Matched Element: <input name="btnI" value="I'm Feeling Lucky" class="lsb" onclick="this.checked=1" type="submit"> 

-------------------------------------------------------

```

As we can see, the correct UI module returns a score as 100 and the problematic UI module returns a score less than 100.

## Closest Match ##

The UI module closest match, i.e., partial match, is extremely important to keep your test code robust to changes to some degree. By partial match, Tellurium uses the [http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm Santa algorithm](.md) to locate the whole UI module by using only partial information inside the UI module.

### API ###

```
   enableClosestMatch();
   disableClosestMatch();
   useClosestMatch(boolean isUse);
```

The first two methods are used in DslContext to enable and disable the closest match feature. The last one is used on Groovy or Java test case for the same purpose. Be aware that this feature only works with UI module caching, i.e., you should call either

```
   useTelluriumEngine(true);
```

or

```
   useCache(true);
```

before use this feature.

### Test Case ###

Here we run our test code based on the problematic UI module "ProblematicGoogle".

```
    @Test
    public void testClosestMatch(){
        useClosestMatch(true);
        gsm.doProblematicGoogleSearch("Tellurium Source");
        useClosestMatch(false);
    }
```

### Result ###

The test case passed successfully. What happens if you disable the closest match feature by calling

```
   useClosestMatch(true);
```

The above test case will break and you will get a red bar in IDE.

## Summary ##

To summarize, we provide the full test code as follows,

```
package org.telluriumsource.ft;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.telluriumsource.module.GoogleSearchModule;
import org.telluriumsource.test.java.TelluriumJUnitTestCase;

public class UiModuleDemoTestCase extends TelluriumJUnitTestCase {
    private static GoogleSearchModule gsm;

    @BeforeClass
    public static void initUi() {
        gsm = new GoogleSearchModule();
        gsm.defineUi();
        connectSeleniumServer();
        useTelluriumEngine(true);
        useTrace(true);
    }

    @Before
    public void connectToGoogle() {

        connectUrl("http://www.google.com/intl/en/");
    }

    @Test
    public void testDump(){
        useCssSelector(false);
        gsm.dump("Google");
        useCssSelector(true);
        gsm.dump("Google");
    }

    @Test
    public void testToString(){
        String json = gsm.toString("Google");
        System.out.println(json);
    }

    @Test
    public void testToHTML(){
        String html = gsm.toHTML("Google");
        System.out.println(html);
    }

    @Test
    public void testGetHTMLSource(){
        gsm.getHTMLSource("Google");
    }

    @Test
    public void testShow(){
        gsm.show("Google", 10000);
//        gsm.startShow("Form");
//        gsm.endShow("Form");
    }

    @Test
    public void testValidate(){
        gsm.validate("Google");
        gsm.validate("ProblematicGoogle");
    }

    @Test
    public void testClosestMatch(){
        useClosestMatch(true);
        gsm.doProblematicGoogleSearch("Tellurium Source");
        useClosestMatch(false);
    }

    @AfterClass
    public static void tearDown(){
        showTrace();
    }
}
```

How to run the above test case? First, you can create a Tellurium 0.7.0 test project by using either [Tellurium Maven archetypes](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_Maven_Archetypes) or one of [the reference projects](http://code.google.com/p/aost/wiki/TelluriumNewReferenceProjects) in [0.7.0 RC1](http://aost.googlecode.com/files/tellurium-0.7.0-RC1.tar.gz). Copy the GoogleSearchModule and the UiModuleDemoTestCase into the project, then run it. Or you can simply check out Tellurium subversion trunk/core project, the above code are in the core project.

# Resources #

  * [Tellurium Project website](http://code.google.com/p/aost/)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Tellurium Automated Testing Framework LinkedIn Group](http://www.linkedin.com/groups?gid=1900807)
  * [Tellurium at Rich Web Experience 2009 by Jian Fang and Vivek Mongolu](http://www.slideshare.net/John.Jian.Fang/tellurium-at-rich-web-experience2009-2806967)
  * [Tellurium 0.6.0 User Guide](http://www.slideshare.net/John.Jian.Fang/tellurium-060-user-guide)
  * [Tellurium video tutorial by Vivek Mongolu](http://vimeo.com/8601173)
  * [Tellurium - A New Approach For Web Testing](http://www.slideshare.net/John.Jian.Fang/telluriumanewapproachforwebtesting)
  * [10 Minutes to Tellurium](http://vimeo.com/8600410)
  * [How to create your own Tellurium testing project with IntelliJ 9.0 Community Edition](http://code.google.com/p/aost/wiki/CustomTelluriumIntelliJProject)