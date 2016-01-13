

# Introduction #

For the past three months, I have worked on [the new Tellurium Engine project](http://code.google.com/p/aost/wiki/TelluriumEngineProject) heavily. A lot of new ideas, for example, [the Santa algorithm](http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm) and [UI module visual effect](http://code.google.com/p/aost/wiki/TelluriumUiModuleVisualEffect), became a reality. Debugging Tellurium Engine is a critical task for me everyday. I cannot image I could have finished all the implementations without a good debug skill.

Tellurium Engine is similar to Selenium core to drive browser events to simulate users who physically work on the web application. If you are familiar with Selenium core, you may know that it is very difficult to debug Selenium core. As a result, I took a different approach to do debugging and like to share my experience with you here, especially for tellurium developers.

# Basic Idea #

The basic idea is to write a Javascript test to call Tellurium Engine and embed the test code in the header of the html page that I want to test. Then I can set a breakpoint in the JavaScript code using Firebug. After refresh the web page, I can run the test code, which will stop at the breakpoint. One obstacle is that some parts of Tellurium Engine need to call Selenium core, I work around this problem by mocking Selenium Core.

# Prerequisites #

To use Firebug, I need to have Firebug installed to either a Firebug profile or the customized Selenium server. How to add Firebug support is described in details on the wiki page [Use Firebug and JQuery to Trace Problems in Tellurium Tests](http://code.google.com/p/aost/wiki/TelluriumJQueryFirebug).

# Details #

## Jetty ##

First, I installed a Jetty server and use the Jetty server to load up the web page that I want to run tellurium Engine tests on.

## Example ##

Take the following html source as an example,

```
H1>FORM Authentication demo</H1>

<div class="box-inner">
    <a href="js/tellurium-test.js">Tellurium Test Cases</a>
    <input name="submit" type="submit" value="Test" onclick="teTestCase.testSuite();">
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

The UI module for the above html source is defined as

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

## Create Test Page ##

First, I added the following headers to the above html source.

```
<head>
  <title>Tellurium Test Page</title>
          <script src="js/selenium-mock.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/jquery-1.4.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/json2.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/utils.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-logging.js"></script> 
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-api.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-selector.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-uibasic.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-uiobj.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-uimodule.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-uisnapshot.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js"> </script>
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium-uiextra.js"> </script>    
          <script src="http://localhost:4444/selenium-server/core/scripts/tellurium.js"> </script>
          <script src="js/tellurium-test.js"> </script>
          <script type="text/javascript">
            teJQuery(document).ready(function(){
                   var testcase = new TelluriumTestCase();
                   testcase.testLogonUiModule();      
            });
          </script>
</head>
```

Where the tellurium-test.js is the Engine test script. The above header loads up the Tellurium Engine code when the web page is served by Jetty.

## Engine Test Script ##

I defined a test class as follows,

```
function TelluriumTestCase(){

};

TelluriumTestCase.prototype.testLogonUiModule = function(){
  var json = [{"obj":{"uid":"Form","locator":{"tag":"form"},"uiType":"Form"},"key":"Form"},
   {"obj":{"uid":"Username","locator":{"tag":"tr"},"uiType":"Container"},"key":"Form.Username"},
   {"obj":{"uid":"Label","locator":{"direct":true,"text":"Username:","tag":"td"},"uiType":"TextBox"},"key":"Form.Username.Label"},
   {"obj":{"uid":"Input","locator":{"tag":"input","attributes":{"name":"j_username","type":"text"}},"uiType":"InputBox"},"key":"Form.Username.Input"},
   {"obj":{"uid":"Password","locator":{"tag":"tr"},"uiType":"Container"},"key":"Form.Password"},
   {"obj":{"uid":"Label","locator":{"direct":true,"text":"Password:","tag":"td"},"uiType":"TextBox"},"key":"Form.Password.Label"},
   {"obj":{"uid":"Input","locator":{"tag":"input","attributes":{"name":"j_password","type":"password"}},"uiType":"InputBox"},"key":"Form.Password.Input"},
   {"obj":{"uid":"Submit","locator":{"tag":"input","attributes":{"name":"submit","value":"Login","type":"submit"}},"uiType":"SubmitButton"},"key":"Form.Submit"}];
    tellurium.logManager.isUseLog = true;
    var uim = new UiModule();
    uim.parseUiModule(json);
    var alg = new UiAlg();
    var dom = teJQuery("html");
    alg.santa(uim, dom);
    tellurium.cache.cacheOption = true;
    tellurium.cache.addToCache("Form", uim);
    var context = new WorkflowContext();
    context.alg = alg;
    var uiid = new Uiid();
    var uinput = uim.walkTo(context, uiid.convertToUiid("Form.Username.Input"));
    var pinput = uim.walkTo(context, uiid.convertToUiid("Form.Password.Input"));
    var smt = uim.walkTo(context, uiid.convertToUiid("Form.Submit"));
    tellurium.teApi.getHTMLSource("Form");
    var attrs = [{"val":"text","key":"type"}];
    var teuids = tellurium.teApi.getUiByTag("input", attrs);
    fbLog("result ", teuids);
};

```

The variable json is the JSON presentation of the UI module. You can obtain the JSON string of a UI module by calling the following method in DslContext.

```
    public String toString(String uid); 
```

The following test case first runs the Santa algorithm to do group locating and then call _walkTo_ to find the DOM reference for the UI element. After that, I test the two Tellurium APIs, _getHTMLSource_ and _getUiByTag_.

As I said, Tellurium Engine needs to call Selenium Core code somewhere. I need to mock up Selenium core as follows.

```
function Selenium(){
    this.browserbot = new BrowserBot();
};

function BrowserBot(){

};

BrowserBot.prototype.findElement = function(locator){
    if(locator.startsWith("jquery=")){
        return teJQuery(locator.substring(7));
    }

    return null;
};

function SeleniumError(message) {
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
    return error;
}

var selenium = new Selenium();
```

## Logging ##

Firebug provides very powerful console logging capability, where you can inspect the JavaScript object. Tellurium Engine provides the following wrapper for Firebug console logging.

```
function fbLog(msg, obj){
    if (typeof(console) != "undefined") {
        console.log(msg, obj);
    }
}

function fbInfo(msg, obj){
    if (typeof(console) != "undefined") {
        console.info(msg, obj);
    }
}

function fbDebug(msg, obj){
    if (typeof(console) != "undefined") {
        console.debug(msg, obj);
    }
}

function fbWarn(msg, obj){
    if (typeof(console) != "undefined") {
        console.warn(msg, obj);
    }
}

function fbError(msg, obj){
    if (typeof(console) != "undefined") {
        console.trace();
        console.error(msg, obj);
    }
}

function fbTrace(){
    if (typeof(console) != "undefined") {
        console.trace();
    }
}

function fbAssert(expr, obj){
    if (typeof(console) != "undefined") {
        console.assert(expr, obj);
    }
}

function fbDir(obj){
    if (typeof(console) != "undefined") {
        console.dir(obj);
    }
}
```

For browsers other than Firefox, Tellurium provides [the Firebug Lite](http://code.google.com/p/aost/wiki/Tellurium070Update#Engine_Logging) in the custom selenium server so that you can still use the above logging wrapper.

## Run the Test ##

To run the above test page, first, I copy the above page to `JETTY_HOME/webapps/test/` directory. Copy the test script and Selenium mock script to `JETTY_HOME/webapps/test/js/` directory. Then start the Jetty server and open the Firefox browser to point to `http://localhost:8080/testpagename.html`. I can see all JavaScript code including Tellurium Engine code and the test script. Set a breakpoint somewhere and refresh the page, Firebug will stop at breakpoint and then I can start to debug the Engine.

# Resource #

  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [the Santa algorithm](http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm)
  * [UI module visual effect](http://code.google.com/p/aost/wiki/TelluriumUiModuleVisualEffect)
  * [Use Firebug and JQuery to Trace Problems in Tellurium Tests](http://code.google.com/p/aost/wiki/TelluriumJQueryFirebug)