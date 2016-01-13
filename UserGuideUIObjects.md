(A PDF version of the user guide is available [here](http://telluriumdoc.googlecode.com/files/TelluriumUserGuide.Draft.pdf))



# Tellurium UI Objects #

Tellurium provides a set of predefined Ui objects, which users can use directly. Here we describe them one by one in details.

## Basic UI Object ##

The basic UI object is an abstract class and users cannot instantiate it directly. The basic UI Object works as the base class for all Ui objects and it includes the following attributes:

  1. uid: UI object's identifier
  1. namespace: used for XHTML
  1. locator: the locator of the UI object, could be a base locator or a composite locator
  1. respond: the JavaScript events the UI object can respond to. The value is a list.

and the base Ui object also provides the following methods:

  * boolean isElementPresent()
  * boolean isVisible()
  * boolean isDisabled()
  * waitForElementPresent(int timeout), where the time unit is ms.
  * waitForElementPresent(int timeout, int step)
  * String getText()
  * getAttribute(String attribute)

Obviously, all UI Objects will inherit the above attributes and methods. But be aware that you usually do not call these methods directly and you should use DSL syntax instead. For example, use

```
  click "GoogleStartPage.GoogleSearch"
```

In this way, Tellurium will first map the UiID ""GoogleStartPage.GoogleSearch" to the actual UI object and then call the _click_ method on it. If that Ui object does not have the _click_ method defined, you will get an error.

The UML class diagram is shown as follows.

http://tellurium-users.googlegroups.com/web/UiObject.png?gda=0JeoTj4AAACvY3VTaWrtpkaxlyj9o09EK9OqypH-u6ovWjcCAevrnUS0NUtU8tBM-H4ipR_0oWnjsKXVs-X7bdXZc5buSfmx&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q


## UI Object Default Attributes ##

Tellurium UI objects have some default attributes as shown in the following table:

| **Tellurium Object** | **Locator Default Attributes** | **Extra Attributes** | **UI Template** |
|:---------------------|:-------------------------------|:---------------------|:----------------|
| Button               | tag: "input"                   |                      | no              |
| Container            |                                | group                | no              |
| CheckBox             | tag: "input", type: "checkbox" |                      | no              |
| Div                  | tag: "div"                     |                      | no              |
| Form                 | tag: "form"                    | group                | no              |
| Image                | tag: "img"                     |                      | no              |
| InputBox             | tag: "input"                   |                      | no              |
| RadioButton          | tag: "input", type: "radio"    |                      | no              |
| Selector             | tag: "select"                  |                      | no              |
| Span                 | tag: "span"                    |                      | no              |
| SubmitButton         | tag: "input", type: "submit"   |                      | no              |
| UrlLink              | tag: "a"                       |                      | no              |
| List                 |                                | separator            | yes             |
| Table                | tag: "table"                   | group, header        | yes             |
| StandardTable        | tag: "table"                   | group, header, footer | yes             |
| Frame                |                                | group, id, name, title | no              |
| Window               |                                | group, id, name, title | no              |



## Tellurium UI Object List ##

### Button ###

Button represents various Buttons on the web and its default tag is "input". The following methods can be applied to Button:

  * click()
  * doubleClick()
  * clickAt(String coordination)

Example:

```
Button(uid: "searchButton", clocator: [value: "Search", name: "btn"])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Button.png?gda=3KovTTwAAACvY3VTaWrtpkaxlyj9o09ErAuyvr_BhY1ILt3t4UiY02BNhIfMicGpzO7OcOv9IkT9Wm-ajmzVoAFUlE7c_fAt&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Submit Button ###

SubmitButton is a special Button with its type being "submit".

Example:

```
SubmitButton(uid: "search_web_button", clocator: [value: "Search the Web"])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/SubmitButton.png?gda=23ltUEIAAACvY3VTaWrtpkaxlyj9o09Eq2re9z1g46laR7HVj3aMdhQB0YElXntmg3UvY_6wrxpV4u3aa4iAIyYQIqbG9naPgh6o8ccLBvP6Chud5KMzIQ&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Check Box ###

The CheckBox on the web is abstracted as "CheckBox" Ui object. The default tag for CheckBox is "input" and its type is "checkbox". CheckBox comes with the following methods:

  * check()
  * boolean isChecked()
  * uncheck()
  * String getValue()

Example:

```
CheckBox(uid: "autoRenewal", clocator: [dojoattachpoint: 'dap_auto_renew'])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/CheckBox.png?gda=lh-8Zj4AAACvY3VTaWrtpkaxlyj9o09ENlEhIiA-hb2mdKJPpbNwXCHJ3Uy3kY3kE7J2mE4y0vHjsKXVs-X7bdXZc5buSfmx&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Div ###

Div is often used in the Dojo framework and it can represent a lot objects. Obviously, its tag is "div" and it has the following method:

  * click()

Example:


```
Div(uid: "dialog", clocator: [class: 'dojoDialog', id: 'loginDialog'])
```

### Image ###

Image is used to abstract the "img" tag and it comes with the following additional methods:

  * getImageSource()
  * getImageAlt()
  * String getImageTitle()

Example:

```
Image(uid: "dropDownArrow", clocator: [src: 'drop_down_arrow.gif'])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Image.png?gda=0z5QJzsAAACvY3VTaWrtpkaxlyj9o09EujBhvAMotBAe8u4e4cflj34XZ2iAB5nmNWaSiL8s4UwGRdr3QrylPkw2aRbXD_gF&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Icon ###

Icon is similar to the Image object, but user can perform actions on it. As a result, it can have the following additional methods:

  * click()
  * doubleClick()
  * clickAt(String coordination)

Example:

```
Icon(uid: "taskIcon", clocator:[tag: "p", dojoonclick: 'doClick', img: "Show_icon.gif"] )
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Icon.png?gda=PJSddzoAAACvY3VTaWrtpkaxlyj9o09EZcK6fJrP-123puIckMFHhbHJFgsrcyPlBEaANubSDln97daDQaep90o7AOpSKHW0&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q


### Radio Button ###

RadioButton is the abstract object for the Radio Button Ui. As a result, its default tag is "input" and its type is "radio". RadioButton has the following additional methods:

  * check()
  * boolean isChecked()
  * uncheck()
  * String getValue()

Example:

```
RadioButton(uid: "autoRenewal", clocator: [dojoattachpoint: 'dap_auto_renew'])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/RadioButton.png?gda=dZD62kEAAACvY3VTaWrtpkaxlyj9o09EA9G0sOYo7hcFDFCV1utGuehRDoWPgHq-DlEIqfKM7N5TCT_pCLcFTwcI3Sro5jAzlXFeCn-cdYleF-vtiGpWAA&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Text Box ###

TextBox is the abstract Ui object from which you can get back the text, i.e., it comes with the method:

  * String waitForText(int timeout)

Note, TextBox can have various types of tags.

Example:

```
TextBox(uid: "searchLabel", clocator: [tag: "span"])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/TextBox.png?gda=frdGUj0AAACvY3VTaWrtpkaxlyj9o09EZBJxiwBnFeW2bGnkYb-FBqoIxTd_Qry1lDY-jgYSWY3lNv--OykrTYJH3lVGu2Z5&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Input Box ###

InputBox is the Ui where user types in input data. As its name stands, InputBox's default tag is "input". InputBox has the following additional methods:

  * type(String input)
  * keyType(String input), used to simulate keyboard typing
  * typeAndReturn(String input)
  * clearText()
  * boolean isEditable()
  * String getValue()

Example:

```
InputBox(uid: "searchBox", clocator: [name: "q"])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/InputBox.png?gda=mOLQqT4AAACvY3VTaWrtpkaxlyj9o09EOS25eozxTd1-2Kl5EQxVuo83nWIEvwSk2L4t2l-mabLjsKXVs-X7bdXZc5buSfmx&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Url Link ###

UrlLink stands for the web url link, i.e., its tag is "a". UrlLink has the following additional methods:

  * String getLink()
  * click()
  * doubleClick()
  * clickAt(String coordination)

Example:

```
UrlLink(uid: "Grid", clocator: [text: "Grid", direct: "true"])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/UrlLink.png?gda=OmfYUj0AAACvY3VTaWrtpkaxlyj9o09EPKrVkdl5tr4h2nKWPPTg00bJ3MnJmkZ45utMyqVp1HjlNv--OykrTYJH3lVGu2Z5&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Selector ###

Selector represents the Ui with tag "select" and user can select from a set of options. Selector has a lot of methods, such as:

  * selectByLabel(String target)
  * selectByValue(String value)
  * addSelectionByLabel(String target)
  * addSelectionByValue(String value)
  * removeSelectionByLabel(String target)
  * removeSelectionByValue(String value)
  * removeAllSelections()
  * String[.md](.md) getSelectOptions()
  * String[.md](.md) getSelectedLabels()
  * String getSelectedLabel()
  * String[.md](.md) getSelectedValues()
  * String getSelectedValue()
  * String[.md](.md) getSelectedIndexes()
  * String getSelectedIndex()
  * String[.md](.md) getSelectedIds()
  * String getSelectedId()
  * boolean isSomethingSelected()

Example:

```
Selector(uid: "issueType", clocator: [name: "can", id: "can"])
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Selector.png?gda=b632Hj4AAACvY3VTaWrtpkaxlyj9o09EYhkj25wnL3ZjnmNSek4H7Li8ZNp69eplCP3Btz3M8UDjsKXVs-X7bdXZc5buSfmx&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Container ###

Container is an abstract object that can hold a collection of Ui objects. As a result, the  Container has a special attribute "useGroupInfo" and its default value is false. If this attribute is true, the Group Locating is enabled. But make sure all the Ui objects inside the Container are children nodes of the Container in the DOM, otherwise, you should not use the Group Locating capability.

Example:

```
ui.Container(uid: "google_start_page", clocator: [tag: "td"], group: "true"){
    InputBox(uid: "searchbox", clocator: [title: "Google Search"])
    SubmitButton(uid: "googlesearch", clocator: [name: "btnG", value: "Google Search"])
    SubmitButton(uid: "Imfeelinglucky", clocator: [value: "I'm Feeling Lucky"])
}
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Container.png?gda=nR7EcT8AAACvY3VTaWrtpkaxlyj9o09E_HoO-w-KJ5H5AQEAtuACpL8e0j-ZJvcoFEzwQ0yOKlOccyFKn-rNKC-d1pM_IdV0&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Form ###

Form is a type of Container with its tag being "form" and it represents web form. Like Container, it has the capability to use Group Locating and it has a special method:

  * submit()

This method is useful and can be used to submit input data if the form does not have a submit button.

Example:

```
ui.Form(uid: "downloadSearch", clocator: [action: "list", method: "get"], group: "true") {
    Selector(uid: "downloadType", clocator: [name: "can", id: "can"])
    TextBox(uid: "searchLabel", clocator: [tag: "span"])

    InputBox(uid: "searchBox", clocator: [name: "q"])
    SubmitButton(uid: "searchButton", clocator: [value: "Search"])
}
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Form.png?gda=Sy4vpzoAAACvY3VTaWrtpkaxlyj9o09EAysfc1sfERzii7dZMaerX1c5GCXVf1Htqu13xS-QMXn97daDQaep90o7AOpSKHW0&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Table ###

Table is one of the most complicated Ui Object and also the most often used one. Obviously, its tag is "table" and a table could have headers besides rows and columns. Table is a good choice for data grid. Tellurium can handle its header, rows, and columns automatically for users. One important is the Table has different UiID than other Ui objects. For example, if the id of the table is "table1", then its i-th row and j-th column is referred as `"table1[i][j]"` and its m-th header is `"table1.header[m]"`.

Another distinguished feature of Table is that you can define Ui template for its elements. For example, the following example defines different table headers and the template for the first column, the element on the second row and the second column, and the template for all the other elements in other rows and columns.

```
ui.Table(uid: "downloadResult", clocator: [id: "resultstable", class: "results"], 
         group: "true")
{
    //define table header
    //for the border column
    TextBox(uid: "header: 1", clocator: [:])
    UrlLink(uid: "header: 2", clocator: [text: "%%Filename"])
    UrlLink(uid: "header: 3", clocator: [text: "%%Summary + Labels"])
    UrlLink(uid: "header: 4", clocator: [text: "%%Uploaded"])
    UrlLink(uid: "header: 5", clocator: [text: "%%Size"])
    UrlLink(uid: "header: 6", clocator: [text: "%%DownloadCount"])
    UrlLink(uid: "header: 7", clocator: [text: "%%..."])

    //define Ui object for the second row and the second column
    InputBox(uid: "row: 2, colum: 2" clocator: [:])
    //define table elements
    //for the border column
    TextBox(uid: "row: *, column: 1", clocator: [:])
    //For the rest, just UrlLink
    UrlLink(uid: "all", clocator: [:])
}
```

Be aware, the templates inside the Table follow the name convention:

  * For the i-th row, j-th column, the uid should be "row: i, column: j"
  * The wild case for row or column is `"*"`
  * "all" stands for matching all rows and columns

As a result, `"row : *, column : 3"` refers to the 3rd column for all rows. Once the templates are defined for the table, Tellurium uses a special way to find a matching for a Ui element `"table[i][j]"` in the table. i.e., the following rules apply,

  * First, Tellurium tries to find the template defined for the i-th row, j-th column.
  * If not found, Tellurium tries to search for a general template `"row: *, column: j"`, i.e., the template for column j.
  * If not found, Tellurium tries to search for another general template `"row: i, column: *"`, i.e., the template for row i.
  * If not found either, Tellurium tries to find the template matching all rows and columns.
  * If still out of luck, Tellurium will use a TextBox as the default element for this element.

Generally speaking, Tellurium always searches for the special case first, then more general case, and until the all matching case. In this way, user can define very flexible templates for tables.

Table is a type of Container and thus, it can use the Group Locating feature. Table has the following special methods:

  * boolean hasHeader()
  * int getTableHeaderColumnNum()
  * int getTableMaxRowNum()
  * int getTableMaxColumnNum()

From Tellurium 0.6.0, you can also specify the tbody attribute for the Table object and this may be helpful if you have multiple tbody elements inside a single table tab. For example, you can specify tbody as follows,

```
Container(uid: "tables", clocator:[:]){
     Table(uid: "first", clocator: [id: "someId", tbody: [position: "1"]]){
          ......
      }
     Table(uid: "second", clocator: [id: "someId", tbody: [position: "2"]]){
          ......
      }

      ...

} 

```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Table.png?gda=Q7WHKjsAAACvY3VTaWrtpkaxlyj9o09EUIpv859W_EwCa1Q6m6z5KZXQRNjjvS1-2bMcnX_XyTwGRdr3QrylPkw2aRbXD_gF&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Standard Table ###

A StandardTable is a table in the following format

```

table
      thead
         tr
           td
           ...
           td
      tbody
         tr
           td
           ...
           td
         ...
       tbody (multiple tbodies)
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

For a StandardTable, you can specify UI templates for different tbodies. Apart from the methods in Table, it has the following additional methods:

  * int getTableFootColumnNum()
  * int getTableMaxTbodyNum()
  * int getTableMaxRowNumForTbody(int tbody\_index)
  * int getTableMaxColumnNumForTbody(int body\_index)

Example:

```
ui.StandardTable(uid: "table", clocator: [id: "std"]) {
   UrlLink(uid: "header: 2", clocator: [text: "%%Filename"])
   UrlLink(uid: "header: 3", clocator: [text: "%%Uploaded"])
   UrlLink(uid: "header: 4", clocator: [text: "%%Size"])
   TextBox(uid: "header: all", clocator: [:])

   Selector(uid: "tbody: 1, row:1, column: 3", clocator: [name: "can"])
   SubmitButton(uid: "tbody: 1, row:1, column:4", clocator: [value: "Search", name: "btn"])
   InputBox(uid: "tbody: 1, row:2, column:3", clocator: [name: "words"])
   InputBox(uid: "tbody: 2, row:2, column:3", clocator: [name: "without"])
   InputBox(uid: "tbody: 2, row:*, column:1", clocator: [name: "labels"])

   TextBox(uid: "foot: all", clocator: [tag: "td"])
}
```

### List ###

List is also a Container type abstract Ui object and it can be used to represent any list like Ui objects. Very much like Table, users can define Ui templates for List and following rule of "the special case first and then the general case". The index number is  used to specify an element and "all" is used to match all elements in the List. List also uses TextBox as the default Ui if no template could be found. Since List is a Container type, it can use the Group Locating feature.

List has one special attribute "separator", which is used to indicate the tag used to separate different List UI elements. If "separator" is not specified or "", all UI elements must be under the same DOM node, i.e., they are siblings.

Example:

```

List(uid: "subcategory", locator: "", separator: "p"){
    InputBox(uid: "2", clocator: [title: "Google Search"])
    UrlLink(uid: "all", clocator: [:])
}
```

The List includes the following additional methods:

  * int getListSize(id): Gets the item count of a list


UML Class Diagram:

http://tellurium-users.googlegroups.com/web/List.png?gda=01ee8zoAAACvY3VTaWrtpkaxlyj9o09EHGLXJixxWuoZZiLNkS9cOj7XS0Nk1NECX1UixwubQ5H97daDQaep90o7AOpSKHW0&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Simple Menu ###

The SimpleMenu represent a menu without a header and only contains menu items. The default tag is "div" and user should specify the alias name for each menu item. For example,

```
//items is a map in the format of "alias name" : menu_item
ui.SimpleMenu(uid: "IdMenu", clocator:[class: "popup", id: "pop_0"],
    items: ["SortUp":"Sort Up", "SortDown":"Sort Down", "HideColumn":"Hide Column"])
```

The above menu specified the menu item "Sort up", "Sort Down", and "Hiden Column" with their alias names. Users should use the alias name to refer the menu item, for instance, "IdMenu.SortUp".

The SimpleMenu has the following methods:

  * click()
  * mouseOve()
  * mouseOut()

### Select Menu ###

SelectMenu is designed for the selecting column menu on the Tellurium Issues page and it is prototyped to demonstrate how to write Ui object with interaction with the DOM since the Ui elements have different patterns at runtime, hence, it is not a general purpose Ui object. SelectMenu could have a header and its menu item content could keep changing when users select different columns to display.

The SelectMenu on the Tellurium issues page is expressed as follows,

```
ui.SelectMenu(uid: "selectColumnMenu", clocator:[class: "popup",id: "pop__dot"], 
    title: "Show columns:", items: ["ID":"ID", "Type":"Type", "Status":"Status",
    "Priority":"Priority", "Milestone":"Milestone", "Owner":"Owner", 
    "Summary":"Summary", "Stars":"Stars", "Opened":"Opened", "Closed":"Closed",
    "Modified":"Modified", "EditColumn":"Edit Column Spec..." ])
```

Like SimpleMenu, SelectMenu also has the following methods:

  * click()
  * mouseOve()
  * mouseOut()

### Frame ###

Frame is a type of Container and is used to mode Frame or IFrame. It includes the
following attributes:
  * id
  * name
  * title

and the following methods

  * selectParentFrame()
  * selectTopFrame()
  * selectFrame(locator)
  * getWhetherThisFrameMatchFrameExpression(currentFrameString, target)
  * waitForFrameToLoad(frameAddress, timeout)

When you test website with IFrames, you should use multiple window mode, i.e., set the option useMultiWindows to be true in TelluriumConfig.groovy.

Example:

```
ui.Frame(uid: "SubscribeFrame", name: "subscrbe"){
   Form(uid: "LoginForm", clocator: [name: "loginForm"]){
      InputBox(uid: "UserName", clocator: [id: "username", type: "text"])
      InputBox(uid: "Password", clocator: [id: "password", type: "password"])
      Button(uid: "Login", clocator: [type: "image", class: "login"])
      CheckBox(uid: "RememberMe", clocator: [id: "rememberme"])
   }
} 
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Frame.png?gda=r8U0ejsAAACvY3VTaWrtpkaxlyj9o09EjtjD0VCwlgWwf4IYqIRmHEThbDJeoOs4Ibpiwk-_c9AGRdr3QrylPkw2aRbXD_gF&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q


### Window ###

Window is a type of Container and is used to mode Popup Window. It includes the
following attributes:
  * id
  * name
  * title

and the following methods

  * openWindow(String UID, String url)
  * selectWindow(String UID)
  * closeWindow(String UID)
  * boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target)
  * waitForPopup(String UID, int timeout)

Example:

```
ui.Window(uid: "HelpWindow", name: "HelpWindow"){
...
}

openWindow helpUrl, "HelpWindow"
waitForPopUp "HelpWindow", 2000
selectWindow "HelpWindow" 
...
selectMainWindow()
```

UML Class Diagram:

http://tellurium-users.googlegroups.com/web/Window.png?gda=Gu9YEDwAAACvY3VTaWrtpkaxlyj9o09E5Vet3V7T4_qN9X8E0xCb5LaejjoTjElKJray_l5X0DD9Wm-ajmzVoAFUlE7c_fAt&gsc=9wy8aQsAAAAwbhxuc1eUADl_IisDYQ6Q

### Option ###

Option is also designed to be adaptive the dynamic web. Option is a pure abstract object and it holds multiple UIs with each representing a possible UI pattern at runtime. For example, the List/Grid selector on the issue page can described as:

```
//The selector to choose the data grid layout as List or Grid
ui.Option(uid: "layoutSelector"){
    Container(uid: "layoutSelector", clocator: [tag: "div"], group: "true") {
        TextBox(uid: "List", clocator: [tag: "b", text: "List", direct: "true"])
        UrlLink(uid: "Grid", clocator: [text: "Grid", direct: "true"])
    }
    Container(uid: "layoutSelector", clocator: [tag: "div"], group: "true") {
        UrlLink(uid: "List", clocator: [text: "List", direct: "true"])
        TextBox(uid: "Grid", clocator: [tag: "b", text: "Grid", direct: "true"])
    }
}
```

Note, the option's uid must be the same as the next UI objects it represent and in this way, you do not need to include option's uid in the UiID. For example,  you can just use

```
click "layoutSelector.List"
```

instead of

```
click "layoutSelector.layoutSelector.List"
```

The option object will automatically detect which UI pattern you need to use at runtime.