

# Tellurium Core #

## Bug Report ##

Tellurium Core added a `bugReport()` method in DslContext, most likely you won't call it directly. Whenever an exception is thrown, Tellurium core will automatically generate a bug report including information about UI module, html source, UI module validation, Exception, Selenium RC log, and Environment variables.

To support this new functionality, the following parameter has been added to TelluriumConfig.groovy:

```
        exception{

            //Whether to generate bug report
            bugReport = true
        }
```

You can programmatically turn on and off the bug report feature by calling the following method in your test class.

```
   public void generateBugReport(boolean isUse);
```


On example bug report result is shown as follows.

```
Please cut and paste the following bug report to Tellurium user group (http://groups.google.com/group/tellurium-users)

---------------------------- Bug Report --------------------------------

-----------------------------------
UI Module Form: 

[{"obj":{"uid":"Form","locator":{"tag":"form","attributes":{"action":"check_phone","method":"POST"}},"uiType":"Form","metaData":{"id":"Form","type":"UiObject"}},"key":"Form"},{"obj":{"uid":"check","locator":{"tag":"input","attributes":{"value":"Check","type":"submit"}},"uiType":"SubmitButton","metaData":{"id":"check","type":"UiObject"}},"key":"Form.check"},{"obj":{"uid":"Number","locator":{"tag":"input","attributes":{"name":"$PhoneNumber"}},"uiType":"InputBox","metaData":{"id":"Number","type":"UiObject"}},"key":"Form.Number"},{"obj":{"uid":"Country","locator":{"tag":"select","attributes":{"name":"$CountryCode"}},"uiType":"Selector","metaData":{"id":"Country","type":"UiObject"}},"key":"Form.Country"}]
-----------------------------------


-----------------------------------
HTML Source: 

<head>


    <title></title>
</head><body>
<form method="POST" action="check_phone">
    <select name="Profile/Customer/Telephone/@CountryAccessCode" style="font-size: 92%;">
        <option value="1" selected="selected">US</option>
        <option value="2" id="uk">UK</option>
        <option value="3">AT</option>
        <option value="4">BE</option>
        <option value="4" id="ca">CA</option>
        <option value="6">CN</option>
        <option value="7">ES</option>
        <option value="8">VG</option>
    </select>
    <input class="medium paxFerryNameInput" value="" name="Profile/Customer/Telephone/@PhoneNumber" maxlength="16" id="phone1" tabindex="26" type="text">
    <input name="submit" value="Check" type="submit">
</form>
</body>
-----------------------------------


-----------------------------------
Validate UI ModuleForm: 

UI Module Validation Result for Form

-------------------------------------------------------

	Found Exact Match: false 

	Found Closest Match: true 

	Match Count: 1 

	Match Score: 87.5 


	Closest Match Details: 

	--- Element Form.Country -->

	 Composite Locator: <select name="CountryCode"/> 

	 Closest Matched Element: <select name="Profile/Customer/Telephone/@CountryAccessCode" style="font-size: 92%;">
        <option value="1" selected="selected">US</option>
        <option value="2" id="uk">UK</option>
        <option value="3">AT</option>
        <option value="4">BE</option>
        <option value="4" id="ca">CA</option>
        <option value="6">CN</option>
        <option value="7">ES</option>
        <option value="8">VG</option>
    </select> 




-------------------------------------------------------

-----------------------------------


-----------------------------------
Environment: 

    Environment Variables:
      configFileName: TelluriumConfig.groovy
      configString: 
      useEngineCache: true
      useClosestMatch: false
      useMacroCommand: true
      maxMacroCmd: 5
      useTelluriumApi: true
      locatorWithCache: true
      useCSSSelector: true
      useTrace: false
      logEngine: true
      locale: en_US
      
-----------------------------------


-----------------------------------
Last Error: 

ERROR: Cannot locate UI module Form
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

SeleniumError("Cannot locate UI module Form")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806

{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952

{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375

{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887

{anonymous}("[{\"uid\":\"Form\",\"args\":[[{\"obj\":{\"uid\":\"Form\",\"locator\":{\"tag\":\"form\",\"attributes\":{\"action\":\"check_phone\",\"method\":\"POST\"}},\"uiType\":\"Form\",\"metaData\":{\"id\":\"Form\",\"type\":\"UiObject\"}},\"key\":\"Form\"},{\"obj\":{\"uid\":\"check\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"value\":\"Check\",\"type\":\"submit\"}},\"uiType\":\"SubmitButton\",\"metaData\":{\"id\":\"check\",\"type\":\"UiObject\"}},\"key\":\"Form.check\"},{\"obj\":{\"uid\":\"Number\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"name\":\"$PhoneNumber\"}},\"uiType\":\"InputBox\",\"metaData\":{\"id\":\"Number\",\"type\":\"UiObject\"}},\"key\":\"Form.Number\"},{\"obj\":{\"uid\":\"Country\",\"locator\":{\"tag\":\"select\",\"attributes\":{\"name\":\"$CountryCode\"}},\"uiType\":\"Selector\",\"metaData\":{\"id\":\"Country\",\"type\":\"UiObject\"}},\"key\":\"Form.Country\"}]],\"name\":\"getUseUiModule\",\"sequ\":0},{\"uid\":\"Form.Country\",\"args\":[\"jquery=form[method=POST] select[name$=CountryCode]\"],\"name\":\"getSelectOptions\",\"sequ\":6}]","")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338

{anonymous}("[{\"uid\":\"Form\",\"args\":[[{\"obj\":{\"uid\":\"Form\",\"locator\":{\"tag\":\"form\",\"attributes\":{\"action\":\"check_phone\",\"method\":\"POST\"}},\"uiType\":\"Form\",\"metaData\":{\"id\":\"Form\",\"type\":\"UiObject\"}},\"key\":\"Form\"},{\"obj\":{\"uid\":\"check\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"value\":\"Check\",\"type\":\"submit\"}},\"uiType\":\"SubmitButton\",\"metaData\":{\"id\":\"check\",\"type\":\"UiObject\"}},\"key\":\"Form.check\"},{\"obj\":{\"uid\":\"Number\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"name\":\"$PhoneNumber\"}},\"uiType\":\"InputBox\",\"metaData\":{\"id\":\"Number\",\"type\":\"UiObject\"}},\"key\":\"Form.Number\"},{\"obj\":{\"uid\":\"Country\",\"locator\":{\"tag\":\"select\",\"attributes\":{\"name\":\"$CountryCode\"}},\"uiType\":\"Selector\",\"metaData\":{\"id\":\"Country\",\"type\":\"UiObject\"}},\"key\":\"Form.Country\"}]],\"name\":\"getUseUiModule\",\"sequ\":0},{\"uid\":\"Form.Country\",\"args\":[\"jquery=form[method=POST] select[name$=CountryCode]\"],\"name\":\"getSelectOptions\",\"sequ\":6}]","")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

{anonymous}("Cannot locate UI module Form\nJavaScript Error Stack: \n{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589\n\nprintStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574\n\nSeleniumError(\"Cannot locate UI module Form\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806\n\n{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60")@http://localhost:4444/selenium-server/core/scripts/selenium-remoterunner.js:276

{anonymous}([object Error])@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:132

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:81

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589,printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574,{anonymous}("Cannot locate UI module Form\nJavaScript Error Stack: \n{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589\n\nprintStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574\n\nSeleniumError(\"Cannot locate UI module Form\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806\n\n{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60")@http://localhost:4444/selenium-server/core/scripts/selenium-remoterunner.js:276,{anonymous}([object Error])@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:132,{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:81,{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60
com.thoughtworks.selenium.HttpCommandProcessor.throwAssertionFailureExceptionOrError(HttpCommandProcessor.java:97)
		com.thoughtworks.selenium.HttpCommandProcessor.doCommand(HttpCommandProcessor.java:91)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrapNoCoerce.invoke(PojoMetaMethodSite.java:229)
		org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite.call(PojoMetaMethodSite.java:52)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:129)
		org.telluriumsource.component.connector.CustomSelenium.getBundleResponse(CustomSelenium.groovy:257)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:88)
		groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:233)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1058)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:886)
		groovy.lang.DelegatingMetaClass.invokeMethod(DelegatingMetaClass.java:149)
		groovy.lang.MetaObjectProtocol$invokeMethod.call(Unknown Source)
		org.telluriumsource.component.dispatch.Dispatcher.methodMissing(Dispatcher.groovy:60)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:88)
		groovy.lang.MetaClassImpl.invokeMissingMethod(MetaClassImpl.java:813)
		groovy.lang.MetaClassImpl.invokePropertyOrMissing(MetaClassImpl.java:1107)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1060)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:886)
		org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:39)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:129)
		org.telluriumsource.component.bundle.BundleProcessor.passBundledCommand(BundleProcessor.groovy:323)
		org.telluriumsource.component.bundle.BundleProcessor$passBundledCommand.callCurrent(Unknown Source)
		org.telluriumsource.component.bundle.BundleProcessor.process(BundleProcessor.groovy:415)
		org.telluriumsource.component.bundle.BundleProcessor$process.callCurrent(Unknown Source)
		org.telluriumsource.component.bundle.BundleProcessor.methodMissing(BundleProcessor.groovy:428)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:88)
		groovy.lang.MetaClassImpl.invokeMissingMethod(MetaClassImpl.java:813)
		groovy.lang.MetaClassImpl.invokePropertyOrMissing(MetaClassImpl.java:1107)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1060)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:886)
		org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:39)
		org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:40)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:117)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:129)
		org.telluriumsource.component.data.Accessor.getSelectOptions(Accessor.groovy:135)
		org.telluriumsource.component.data.Accessor$getSelectOptions.call(Unknown Source)
		org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:40)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:117)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:129)
		org.telluriumsource.dsl.BaseDslContext$_getSelectOptions_closure22.doCall(BaseDslContext.groovy:363)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:88)
		groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:233)
		org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:272)
		groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:886)
		org.codehaus.groovy.runtime.callsite.PogoMetaClassSite.call(PogoMetaClassSite.java:39)
		org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:40)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:117)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
		org.telluriumsource.ui.object.Selector.getSelectOptions(Selector.groovy:60)
		org.telluriumsource.ui.object.Selector$getSelectOptions.call(Unknown Source)
		org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:40)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:117)
		org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:125)
		org.telluriumsource.dsl.BaseDslContext.getSelectOptions(BaseDslContext.groovy:361)
		org.telluriumsource.ft.SpecialJunitTestCase.testSelect(SpecialJunitTestCase.java:65)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:44)
		org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15)
		org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:41)
		org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:20)
		org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:28)
		org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:76)
		org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
		org.junit.runners.ParentRunner$3.run(ParentRunner.java:193)
		org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:52)
		org.junit.runners.ParentRunner.runChildren(ParentRunner.java:191)
		org.junit.runners.ParentRunner.access$000(ParentRunner.java:42)
		org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:184)
		org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:28)
		org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:31)
		org.junit.runners.ParentRunner.run(ParentRunner.java:236)
		org.junit.runners.Suite.runChild(Suite.java:128)
		org.junit.runners.Suite.runChild(Suite.java:24)
		org.junit.runners.ParentRunner$3.run(ParentRunner.java:193)
		org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:52)
		org.junit.runners.ParentRunner.runChildren(ParentRunner.java:191)
		org.junit.runners.ParentRunner.access$000(ParentRunner.java:42)
		org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:184)
		org.junit.runners.ParentRunner.run(ParentRunner.java:236)
		org.junit.runner.JUnitCore.run(JUnitCore.java:157)
		com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:94)
		com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:192)
		com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:64)
		sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
		sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
		java.lang.reflect.Method.invoke(Method.java:597)
		com.intellij.rt.execution.application.AppMain.main(AppMain.java:110)
		
-----------------------------------


-----------------------------------
System log: 

14:06:58.794 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"Form","args":[[{"obj":{"uid":"Form","locator":{"tag":"form","attributes":{"action":"check_phone","method":"POST"}},"uiType":"Form","metaData":{"id":"Form","type":"UiObject"}},"key":"Form"},{"obj":{"uid":"check","locator":{"tag":"input","attributes":{"value":"Check","type":"submit"}},"uiType":"SubmitButton","metaData":{"id":"check","type":"UiObject"}},"key":"Form.check"},{"obj":{"uid":"Number","locator":{"tag":"input","attributes":{"name":"$PhoneNumber"}},"uiType":"InputBox","metaData":{"id":"Number","type":"UiObject"}},"key":"Form.Number"},{"obj":{"uid":"Country","locator":{"tag":"select","attributes":{"name":"$CountryCode"}},"uiType":"Selector","metaData":{"id":"Country","type":"UiObject"}},"key":"Form.Country"}]],"name":"getUseUiModule","sequ":0},{"uid":"Form.Country","args":["jquery=form[method=POST] select[name$=CountryCode]"],"name":"getSelectOptions","sequ":6}], ] on session dd53d08fdd2f4eeda6d273b238b5cf71

14:06:59.028 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: ERROR: Cannot locate UI module Form
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

SeleniumError("Cannot locate UI module Form")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806

{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952

{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375

{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887

{anonymous}("[{\"uid\":\"Form\",\"args\":[[{\"obj\":{\"uid\":\"Form\",\"locator\":{\"tag\":\"form\",\"attributes\":{\"action\":\"check_phone\",\"method\":\"POST\"}},\"uiType\":\"Form\",\"metaData\":{\"id\":\"Form\",\"type\":\"UiObject\"}},\"key\":\"Form\"},{\"obj\":{\"uid\":\"check\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"value\":\"Check\",\"type\":\"submit\"}},\"uiType\":\"SubmitButton\",\"metaData\":{\"id\":\"check\",\"type\":\"UiObject\"}},\"key\":\"Form.check\"},{\"obj\":{\"uid\":\"Number\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"name\":\"$PhoneNumber\"}},\"uiType\":\"InputBox\",\"metaData\":{\"id\":\"Number\",\"type\":\"UiObject\"}},\"key\":\"Form.Number\"},{\"obj\":{\"uid\":\"Country\",\"locator\":{\"tag\":\"select\",\"attributes\":{\"name\":\"$CountryCode\"}},\"uiType\":\"Selector\",\"metaData\":{\"id\":\"Country\",\"type\":\"UiObject\"}},\"key\":\"Form.Country\"}]],\"name\":\"getUseUiModule\",\"sequ\":0},{\"uid\":\"Form.Country\",\"args\":[\"jquery=form[method=POST] select[name$=CountryCode]\"],\"name\":\"getSelectOptions\",\"sequ\":6}]","")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338

{anonymous}("[{\"uid\":\"Form\",\"args\":[[{\"obj\":{\"uid\":\"Form\",\"locator\":{\"tag\":\"form\",\"attributes\":{\"action\":\"check_phone\",\"method\":\"POST\"}},\"uiType\":\"Form\",\"metaData\":{\"id\":\"Form\",\"type\":\"UiObject\"}},\"key\":\"Form\"},{\"obj\":{\"uid\":\"check\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"value\":\"Check\",\"type\":\"submit\"}},\"uiType\":\"SubmitButton\",\"metaData\":{\"id\":\"check\",\"type\":\"UiObject\"}},\"key\":\"Form.check\"},{\"obj\":{\"uid\":\"Number\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"name\":\"$PhoneNumber\"}},\"uiType\":\"InputBox\",\"metaData\":{\"id\":\"Number\",\"type\":\"UiObject\"}},\"key\":\"Form.Number\"},{\"obj\":{\"uid\":\"Country\",\"locator\":{\"tag\":\"select\",\"attributes\":{\"name\":\"$CountryCode\"}},\"uiType\":\"Selector\",\"metaData\":{\"id\":\"Country\",\"type\":\"UiObject\"}},\"key\":\"Form.Country\"}]],\"name\":\"getUseUiModule\",\"sequ\":0},{\"uid\":\"Form.Country\",\"args\":[\"jquery=form[method=POST] select[name$=CountryCode]\"],\"name\":\"getSelectOptions\",\"sequ\":6}]","")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112

{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78

{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

{anonymous}("Cannot locate UI module Form\nJavaScript Error Stack: \n{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589\n\nprintStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574\n\nSeleniumError(\"Cannot locate UI module Form\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806\n\n{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112\n\n{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78\n\n{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60")@http://localhost:4444/selenium-server/core/scripts/selenium-remoterunner.js:276

{anonymous}([object Error])@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:132

{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:81

{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589,printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574,{anonymous}("Cannot locate UI module Form\nJavaScript Error Stack: \n{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589\n\nprintStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574\n\nSeleniumError(\"Cannot locate UI module Form\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806\n\n{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112\n\n{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78\n\n{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60")@http://localhost:4444/selenium-server/core/scripts/selenium-remoterunner.js:276,{anonymous}([object Error])@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:132,{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:81,{anonymous}(0)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60 on session dd53d08fdd2f4eeda6d273b238b5cf71

14:06:59.258 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: cleanCache[, ] on session dd53d08fdd2f4eeda6d273b238b5cf71

14:06:59.381 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK on session dd53d08fdd2f4eeda6d273b238b5cf71

14:06:59.386 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: testComplete[, ] on session dd53d08fdd2f4eeda6d273b238b5cf71

14:06:59.386 INFO [18] org.openqa.selenium.server.browserlaunchers.FirefoxChromeLauncher - Killing Firefox...

14:06:59.441 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK on session dd53d08fdd2f4eeda6d273b238b5cf71

14:07:46.189 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getNewBrowserSession[*chrome, http://localhost:8080, ] on session null

14:07:46.190 INFO [18] org.openqa.selenium.server.BrowserSessionFactory - creating new remote session

14:07:46.196 INFO [18] org.openqa.selenium.server.BrowserSessionFactory - Allocated session 24bcb7b4451a480bb1d59fe4fa233c2b for http://localhost:8080, launching...

14:07:46.358 INFO [18] org.openqa.selenium.server.browserlaunchers.FirefoxChromeLauncher - Preparing Firefox profile...

14:07:52.819 INFO [18] org.openqa.selenium.server.browserlaunchers.FirefoxChromeLauncher - Launching Firefox...

14:07:58.299 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,24bcb7b4451a480bb1d59fe4fa233c2b on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.004 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"","args":[],"name":"enableCache","sequ":2}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.015 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,[] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.046 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"","args":[true],"name":"useTeApi","sequ":3}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.057 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,[] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.062 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"","args":[true],"name":"useTeApi","sequ":4}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.079 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,[] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.086 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"","args":[true],"name":"useEngineLog","sequ":5}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.123 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,[] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.138 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: open[http://localhost:8080/Special.html, ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.275 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.523 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"Form","args":[[{"obj":{"uid":"Form","locator":{"tag":"form","attributes":{"action":"check_phone","method":"POST"}},"uiType":"Form","metaData":{"id":"Form","type":"UiObject"}},"key":"Form"},{"obj":{"uid":"check","locator":{"tag":"input","attributes":{"value":"Check","type":"submit"}},"uiType":"SubmitButton","metaData":{"id":"check","type":"UiObject"}},"key":"Form.check"},{"obj":{"uid":"Number","locator":{"tag":"input","attributes":{"name":"$PhoneNumber"}},"uiType":"InputBox","metaData":{"id":"Number","type":"UiObject"}},"key":"Form.Number"},{"obj":{"uid":"Country","locator":{"tag":"select","attributes":{"name":"$CountryCode"}},"uiType":"Selector","metaData":{"id":"Country","type":"UiObject"}},"key":"Form.Country"}]],"name":"getUseUiModule","sequ":0},{"uid":"Form.Country","args":["jquery=form[method=POST] select[name$=CountryCode]"],"name":"getSelectOptions","sequ":6}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.757 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: ERROR: Cannot locate UI module Form
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

SeleniumError("Cannot locate UI module Form")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806

{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952

{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375

{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887

{anonymous}("[{\"uid\":\"Form\",\"args\":[[{\"obj\":{\"uid\":\"Form\",\"locator\":{\"tag\":\"form\",\"attributes\":{\"action\":\"check_phone\",\"method\":\"POST\"}},\"uiType\":\"Form\",\"metaData\":{\"id\":\"Form\",\"type\":\"UiObject\"}},\"key\":\"Form\"},{\"obj\":{\"uid\":\"check\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"value\":\"Check\",\"type\":\"submit\"}},\"uiType\":\"SubmitButton\",\"metaData\":{\"id\":\"check\",\"type\":\"UiObject\"}},\"key\":\"Form.check\"},{\"obj\":{\"uid\":\"Number\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"name\":\"$PhoneNumber\"}},\"uiType\":\"InputBox\",\"metaData\":{\"id\":\"Number\",\"type\":\"UiObject\"}},\"key\":\"Form.Number\"},{\"obj\":{\"uid\":\"Country\",\"locator\":{\"tag\":\"select\",\"attributes\":{\"name\":\"$CountryCode\"}},\"uiType\":\"Selector\",\"metaData\":{\"id\":\"Country\",\"type\":\"UiObject\"}},\"key\":\"Form.Country\"}]],\"name\":\"getUseUiModule\",\"sequ\":0},{\"uid\":\"Form.Country\",\"args\":[\"jquery=form[method=POST] select[name$=CountryCode]\"],\"name\":\"getSelectOptions\",\"sequ\":6}]","")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338

{anonymous}("[{\"uid\":\"Form\",\"args\":[[{\"obj\":{\"uid\":\"Form\",\"locator\":{\"tag\":\"form\",\"attributes\":{\"action\":\"check_phone\",\"method\":\"POST\"}},\"uiType\":\"Form\",\"metaData\":{\"id\":\"Form\",\"type\":\"UiObject\"}},\"key\":\"Form\"},{\"obj\":{\"uid\":\"check\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"value\":\"Check\",\"type\":\"submit\"}},\"uiType\":\"SubmitButton\",\"metaData\":{\"id\":\"check\",\"type\":\"UiObject\"}},\"key\":\"Form.check\"},{\"obj\":{\"uid\":\"Number\",\"locator\":{\"tag\":\"input\",\"attributes\":{\"name\":\"$PhoneNumber\"}},\"uiType\":\"InputBox\",\"metaData\":{\"id\":\"Number\",\"type\":\"UiObject\"}},\"key\":\"Form.Number\"},{\"obj\":{\"uid\":\"Country\",\"locator\":{\"tag\":\"select\",\"attributes\":{\"name\":\"$CountryCode\"}},\"uiType\":\"Selector\",\"metaData\":{\"id\":\"Country\",\"type\":\"UiObject\"}},\"key\":\"Form.Country\"}]],\"name\":\"getUseUiModule\",\"sequ\":0},{\"uid\":\"Form.Country\",\"args\":[\"jquery=form[method=POST] select[name$=CountryCode]\"],\"name\":\"getSelectOptions\",\"sequ\":6}]","")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60

{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330

{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60
JavaScript Error Stack: 
{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589

printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574

{anonymous}("Cannot locate UI module Form\nJavaScript Error Stack: \n{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589\n\nprintStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574\n\nSeleniumError(\"Cannot locate UI module Form\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806\n\n{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60")@http://localhost:4444/selenium-server/core/scripts/selenium-remoterunner.js:276

{anonymous}([object Error])@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:132

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:81

{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589,printStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574,{anonymous}("Cannot locate UI module Form\nJavaScript Error Stack: \n{anonymous}(null)@http://localhost:4444/selenium-server/core/scripts/utils.js:589\n\nprintStackTrace()@http://localhost:4444/selenium-server/core/scripts/utils.js:574\n\nSeleniumError(\"Cannot locate UI module Form\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:806\n\n{anonymous}([object Object],null)@http://localhost:4444/selenium-server/core/scripts/tellurium-uialg.js:952\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-cache.js:375\n\n{anonymous}([object Array])@http://localhost:4444/selenium-server/core/scripts/tellurium-api.js:603\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/tellurium.js:958\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/tellurium.js:887\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/tellurium-extensions.js:338\n\n{anonymous}(\"[{\\\"uid\\\":\\\"Form\\\",\\\"args\\\":[[{\\\"obj\\\":{\\\"uid\\\":\\\"Form\\\",\\\"locator\\\":{\\\"tag\\\":\\\"form\\\",\\\"attributes\\\":{\\\"action\\\":\\\"check_phone\\\",\\\"method\\\":\\\"POST\\\"}},\\\"uiType\\\":\\\"Form\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Form\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"check\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"value\\\":\\\"Check\\\",\\\"type\\\":\\\"submit\\\"}},\\\"uiType\\\":\\\"SubmitButton\\\",\\\"metaData\\\":{\\\"id\\\":\\\"check\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.check\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Number\\\",\\\"locator\\\":{\\\"tag\\\":\\\"input\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$PhoneNumber\\\"}},\\\"uiType\\\":\\\"InputBox\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Number\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Number\\\"},{\\\"obj\\\":{\\\"uid\\\":\\\"Country\\\",\\\"locator\\\":{\\\"tag\\\":\\\"select\\\",\\\"attributes\\\":{\\\"name\\\":\\\"$CountryCode\\\"}},\\\"uiType\\\":\\\"Selector\\\",\\\"metaData\\\":{\\\"id\\\":\\\"Country\\\",\\\"type\\\":\\\"UiObject\\\"}},\\\"key\\\":\\\"Form.Country\\\"}]],\\\"name\\\":\\\"getUseUiModule\\\",\\\"sequ\\\":0},{\\\"uid\\\":\\\"Form.Country\\\",\\\"args\\\":[\\\"jquery=form[method=POST] select[name$=CountryCode]\\\"],\\\"name\\\":\\\"getSelectOptions\\\",\\\"sequ\\\":6}]\",\"\")@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60\n\n{anonymous}([object Object],[object Object])@http://localhost:4444/selenium-server/core/scripts/selenium-commandhandlers.js:330\n\n{anonymous}()@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:112\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:78\n\n{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60")@http://localhost:4444/selenium-server/core/scripts/selenium-remoterunner.js:276,{anonymous}([object Error])@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:132,{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/selenium-executionloop.js:81,{anonymous}(2)@http://localhost:4444/selenium-server/core/scripts/htmlutils.js:60 on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.921 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"","args":[],"name":"getHtmlSource","sequ":7}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:07:59.956 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,[{"sequ":7,"name":"getHtmlSource","returnType":"STRING","returnResult":"<head>\u000a\u000a\u000a    <title></title>\u000a</head><body>\u000a<form method=\"POST\" action=\"check_phone\">\u000a    <select name=\"Profile/Customer/Telephone/@CountryAccessCode\" style=\"font-size: 92%;\">\u000a        <option value=\"1\" selected=\"selected\">US</option>\u000a        <option value=\"2\" id=\"uk\">UK</option>\u000a        <option value=\"3\">AT</option>\u000a        <option value=\"4\">BE</option>\u000a        <option value=\"4\" id=\"ca\">CA</option>\u000a        <option value=\"6\">CN</option>\u000a        <option value=\"7\">ES</option>\u000a        <option value=\"8\">VG</option>\u000a    </select>\u000a    <input class=\"medium paxFerryNameInput\" value=\"\" name=\"Profile/Customer/Telephone/@PhoneNumber\" maxlength=\"16\" id=\"phone1\" tabindex=\"26\" type=\"text\">\u000a    <input name=\"submit\" value=\"Check\" type=\"submit\">\u000a</form>\u000a</body>"}] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:08:00.024 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: getBundleResponse[[{"uid":"Form","args":[[{"obj":{"uid":"Form","locator":{"tag":"form","attributes":{"action":"check_phone","method":"POST"}},"uiType":"Form","metaData":{"id":"Form","type":"UiObject"}},"key":"Form"},{"obj":{"uid":"check","locator":{"tag":"input","attributes":{"value":"Check","type":"submit"}},"uiType":"SubmitButton","metaData":{"id":"check","type":"UiObject"}},"key":"Form.check"},{"obj":{"uid":"Number","locator":{"tag":"input","attributes":{"name":"$PhoneNumber"}},"uiType":"InputBox","metaData":{"id":"Number","type":"UiObject"}},"key":"Form.Number"},{"obj":{"uid":"Country","locator":{"tag":"select","attributes":{"name":"$CountryCode"}},"uiType":"Selector","metaData":{"id":"Country","type":"UiObject"}},"key":"Form.Country"}]],"name":"getValidateUiModule","sequ":8}], ] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:08:00.243 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Got result: OK,[{"sequ":8,"name":"getValidateUiModule","returnType":"STRING","returnResult":{"id":"Form","found":false,"relaxed":true,"matches":1,"score":87.5,"relaxDetails":[{"uid":"Form.Country","locator":{"tag":"select","attributes":{"name":"$CountryCode"}},"html":"<select name=\"Profile/Customer/Telephone/@CountryAccessCode\" style=\"font-size: 92%;\">\u000a        <option value=\"1\" selected=\"selected\">US</option>\u000a        <option value=\"2\" id=\"uk\">UK</option>\u000a        <option value=\"3\">AT</option>\u000a        <option value=\"4\">BE</option>\u000a        <option value=\"4\" id=\"ca\">CA</option>\u000a        <option value=\"6\">CN</option>\u000a        <option value=\"7\">ES</option>\u000a        <option value=\"8\">VG</option>\u000a    </select>"}]}}] on session 24bcb7b4451a480bb1d59fe4fa233c2b

14:08:00.457 INFO [18] org.openqa.selenium.server.SeleniumDriverResourceHandler - Command request: retrieveLastRemoteControlLogs[, ] on session 24bcb7b4451a480bb1d59fe4fa233c2b


-----------------------------------

----------------------------    End     --------------------------------

```

## Misc ##

  * `getValue(String uid)` is added to UI base object UiObject so that all UI objects can call this method.
  * `mouseDown{String uid)` is added to UI base object UiObject so that all UI object ca call this method.
  * `mouseUp{String uid)` is added to UI base object UiObject so that all UI object ca call this method.

# Engine #

# Trump #