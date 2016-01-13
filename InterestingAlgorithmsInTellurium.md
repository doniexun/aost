

# Introduction #

Algorithm is the spirit of computing. Tellurium is an automated web testing framework and it brings up many interesting algorithm design problems. Here we like to list a few of them to shed light on Tellurium internal so that you can get some ideas of what Tellurium really does and what it tries to achieve.

# Selected Algorithm Design Problems #

## UI Module Generating Algorithm in Trump ##

The [Tellurium UI module plugin](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_UI_Module_Plugin_(TrUMP)) (Trump) is a Firefox plugin and it generates UI modules automated from a set of web elements that user clicks on.

For example, we have the following HTML snippet,

```
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

If we click and select the following web elements,

```
<form method="POST" action="j_security_check">
<td>Username:</td>
<td><input size="12" value="" name="j_username" maxlength="25" type="text"></td>
<td>Password:</td>
<td><input size="12" value="" name="j_password" maxlength="25" type="password"></td>
<input name="submit" type="submit" value="Login">
```

Trump generates the following UI module.

```
ui.Form(uid: "root", clocator: [action: "j_security_check"]){
	Container(uid: "T4t", clocator: [tag: "tr"]){
		TextBox(uid: "td0", clocator: [tag: "td", text: "Username:", direct: "true"])
		InputBox(uid: "input1", clocator: [tag: "input", direct: "true", type: "text", name: "j_username"])
	}
	Container(uid: "T4t2", clocator: [tag: "tr"]){
		TextBox(uid: "td2", clocator: [tag: "td", text: "Password:", direct: "true"])
		InputBox(uid: "input3", clocator: [tag: "input", direct: "true", type: "password", name: "j_password"])
	}
	SubmitButton(uid: "input4", clocator: [tag: "input", type: "submit", value: "Login", name: "submit"])
}
```

How the UI module is generated? Tellurium used a Prie-based [UI module generating algorithm](http://code.google.com/p/aost/wiki/UIModuleGeneratingAlgorithm). We are also working on a new algorithm to support multiple UI module generation and frames/iframes.

## Smart Name Suggestion in Trump ##

The names, i.e., the UID, trump suggested as shown in above section are ugly somehow. Can you design an algorithm that generates better names such as the following?

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

One naive method is to see if the web element comes with an ID or name attribute, if it does, then use the attribute as the name. But for a more practical algorithm, it should be able to suggest a good name based on the context and web element's attributes.

## UI Template Aggregation Algorithm ##

[Tellurium UI templates](http://code.google.com/p/aost/wiki/UserGuide070TelluriumBasics#UI_Templates) are used to represent many identical UI elements and dynamic data grid.

For example, the following HTML source

```
  <ul class="a">
    <li>
      <A HREF="site?tcid=a" class="b">
        AA
      </A>
    </li>
    <li>
      <A HREF="site?tcid=b" class="b">
        BB
      </A>
    </li>
    <li>
      <A HREF="site?;tcid=c" class="b">
        CC
      </A>
    </li>
    <li>
      <A HREF="site?tcid=d" class="b">
        DD
      </A>
    </li>
    <li>
      <A HREF="site?tcid=e" class="b">
        EE
      </A>
    </li>
    <li>
      <A HREF="site?tcid=f" class="b">
        FF
      </A>
    </li>
  </ul>
```

can be represented by a List very concisely as follows.

```
ui.List(uid: "list", clocator: [tag: "ul", class: "a"], separator:"li")
{
    UrlLink(uid: "{all}", clocator: [class: "b"])
}
```

Trump generates UI module based the actual web elements user selects, but how to aggregate the UI elements into UI templates, i.e., many UrlLinks into a List object in the above example, is really challenging.

Consider more general case, the [Tellurium issue result](http://code.google.com/p/aost/issues/list) is a dynamic data grid. Could you create an algorithm to automatically
generate a UI module similar to the following for it?

```
    ui.Table(uid: "issueResult", clocator: [id: "resultstable", class: "results"], group: "true") {
      //Define the header elements
      UrlLink(uid: "{header: any} as ID", clocator: [text: "*ID"])
      UrlLink(uid: "{header: any} as Type", clocator: [text: "*Type"])
      UrlLink(uid: "{header: any} as Status", clocator: [text: "*Status"])
      UrlLink(uid: "{header: any} as Priority", clocator: [text: "*Priority"])
      UrlLink(uid: "{header: any} as Milestone", clocator: [text: "*Milestone"])
      UrlLink(uid: "{header: any} as Owner", clocator: [text: "*Owner"])
      UrlLink(uid: "{header: any} as Summary", clocator: [text: "*Summary + Labels"])
      UrlLink(uid: "{header: any} as Extra", clocator: [text: "*..."])

      //Define table body elements
      //Column "Extra" are TextBoxs
      TextBox(uid: "{row: all, column -> Extra}", clocator: [:])
      //For the rest, they are UrlLinks
      UrlLink(uid: "{row: all, column: all}", clocator: [:])
    }
```

## Object Attribute Optimize Algorithm ##

When Trump generates UI module, it selects web element's attributes based on a predefined while list and a black list, which is not very flexible. How to design an algorithm to automatically select appropriate attributes so that the number of attributes are not too redundant and not too few?

## HTML Source Skeletonize Algorithm ##

HTML is very verbose and it is very difficult to generate UI module from a given HTML source directly. How to skeletonize the HTML source to automatically filter out unnecessary HTML tags is another interesting algorithm design problem.

## UI Module Group Locating Algorithm ##

When Tellurium works as a wrap for Selenium, Tellurium Core generates runtime locators for UI modules and uses the locators to locate each individual elements just like what Selenium does. For example, we can dump the runtime locators for the UI module as shown in the previous section by calling

```
   dump("Form");
```

and the output is as follows.

```
-------------------------------------------------------
Form: jquery=form
Form.Password: jquery=form tr
Form.Password.Input: jquery=form tr input[type=password][name=j_password]
Form.Password.Label: jquery=form tr > td:te_text(Password:)
Form.Username: jquery=form tr
Form.Username.Input: jquery=form tr input[type=text][name=j_username]
Form.Username.Label: jquery=form tr > td:te_text(Username:)
Form.Submit: jquery=form input[type=submit][value=Login][name=submit]
-------------------------------------------------------
```

This is really not very efficient because for each UI element, we have to locate it for each call. What if we have the following test code:

```
  public void logon(String username, String password){
    keyType "Form.Username.Input", username
    keyType "Form.Password.Input", password
    click "Form.Submit"
    waitForPageToLoad 30000
  }
```

The keyType command simulates each key stroke including KeyUp, KeyDown, and KeyPress. As a result, we will have many calls for the input such as username or password. For example, if we call

```
  logon("test", "test");
```

we can see the command sequence in Tellurium Marco command as follows.

```
Command request: getBundleResponse[[{"uid":"Form.Username.Input","args":["jquery=form tr input[type=text][name=j_username]","e"],"name":"keyPress","sequ":9},{"uid":"Form.Username.Input","args":["jquery=form tr input[type=text][name=j_username]","e"],"name":"keyUp","sequ":10},{"uid":"Form.Username.Input","args":["jquery=form tr input[type=text][name=j_username]","s"],"name":"keyDown","sequ":11},{"uid":"Form.Username.Input","args":["jquery=form tr input[type=text][name=j_username]","s"],"name":"keyPress","sequ":12},{"uid":"Form.Username.Input","args":["jquery=form tr input[type=text][name=j_username]","s"],"name":"keyUp","sequ":13}], ] 

...
```

A more efficient and more elegant way is to locate the whole UI module in one shot and cache the UI mode. Then we can use the cached UI elements for all subsequent calls to remove the element locating overhead for each call. This is exact what Tellurium new Engine mode does. We are used to locate a single UI element by its XPath or CSS selector, but how to locate all the UI elements defined in the UI module simultaneously?

Let us consider another scenario, the following UI module was defined some time ago.

```
    ui.Form(uid: "Form", clocator: [tag: "form"]){
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

We changed our HTML recently to the one we listed in the first section. What will happen if we still rely on individual locator such as XPath and CSS selector? The test will break because the XPath or CSS selector is no longer valid. But how about a fuzzy matching idea, I know I have one form that include two InputBox and one Submit button, can we still be able to find them with the old UI module? Here comes the partial matching idea from Tellurium, we can define a matching score to be 0-100 with 100 as the exact match. In this way, test code robust can be improved a lot and you don't have to worry about your test code because some developer changed some attributes of the Web.

Question is how to achieve these? Tellurium 0.7.0 introduced a group locating algorithm called [Santa algorithm](http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm) to locate the UI module as a whole and support UI module partial matching.

## UDL Routing Algorithm ##

[Tellurium UID Description Language](http://code.google.com/p/aost/wiki/TelluriumUIDDescriptionLanguage) (UDL) was designed to address the dynamic factors in [Tellurium UI templates](http://code.google.com/p/aost/wiki/UserGuide070TelluriumBasics#UI_Templates) and to increase the flexibility of Tellurium UI templates.

With UDL, we can define more flexible UI templates with the following indices:
  * Number, for example, "5".
  * "first", the first element.
  * "last", the last element.
  * "any", any position, usually the position is dynamic at runtime.
  * "odd", the odd elements.
  * "even", the even elements
  * "all", wild match if not exact matches found.

Flexibility does not come without a cost. How to route the uid to a specific UI template
programmatically is very challenging.

For example, we have the following List defined:

```
   ui.List(uid: "Example", clocator: [tag:"div"], separator: "p"){
      InputBox(uid: "{first} as Input", clocator: [:])
      Button(uid: "{odd}", clocator: [:])
      Selector(uid: "{4} as Select", clocator: [:])
      SubmitButton(uid: "{last} as Submit", clocator: [:])
      TextBox(uid: "{all}", clocator: [:])    
   }
```

What UI object the following uids actually refer to then?

```
   "Example.Input"  
   "Example.Select" 
   "Example[first]" 
   "Example[1]"    
   "Example[2]"    
   "Example[3]"    
   "Example[4]"   
   "Example[last]"  
```

The UI templates in the Table object can be more complicated because a Table object can be two or three dimensions.

The routing algorithm in Tellurium UDL is called [RTree and RGraph](http://code.google.com/p/aost/wiki/TelluriumUIDDescriptionLanguage#Routing).

# Summary #

If you are interested in the algorithm design problems listed here and want to contribute some ideas to Tellurium, please feel free to contact us directly or post your ideas to [Tellurium User Group](http://groups.google.com/group/tellurium-users). Thanks in advance.

# Resources #

  * [Tellurium Project Home](http://code.google.com/p/aost/)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Tellurium Developer Group](http://groups.google.com/group/tellurium-developers)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [TelluriumSource](http://telluriumsource.org)
  * [Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Released)
  * [Tellurium Reference Documentation](http://aost.googlecode.com/files/tellurium-reference-0.7.0.pdf)