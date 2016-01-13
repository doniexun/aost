

# Introduction #

jQuery is one of the key techniques used in Tellurium. Here we like to cover the use of jQuery in Tellurium briefly.

# Use of jQuery in Tellurium #

## CSS Selector locator ##

When Tellurium works as a Selenium Wrap, Tellurium core generates runtime locator from UI module definition. Tellurium uses CSS selector as the default locator and the CSS selector is backed up by jQuery to locate the UI element in the DOM. Take the following Google Search module as an example,

```
    ui.Container(uid: "Google", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "q"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btnG"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnI"])
    }
```

The generated runtime CSS selectors can be dumped out by calling

```
   dump("Google");
```

The results are as follows, where the content after "jquery=" is the CSS selector, for example "table input[title=Google Search][name=q]".

```
Dump locator information for Google
-------------------------------------------------------
Google: jquery=table
Google.Input: jquery=table input[title=Google Search][name=q]
Google.Search: jquery=table input[type=submit][value=Google Search][name=btnG]
Google.ImFeelingLucky: jquery=table input[type=submit][value$=m Feeling Lucky][name=btnI]
-------------------------------------------------------
```

Once the Selenium Core receives APIs with the locator as a parameter, it calls Tellurium jQuery support to locate the UI element in the DOM.

## Test Driving Mechanism ##

[Tellurium new Engine](http://code.google.com/p/aost/wiki/telluriumengine) uses jQuery as a test driving mechanism, i.e., automatically simulate end user actions on the browser. For example, the "mouseOver" action can be implemented by the following jQuery Event.

```
TelluriumApi.prototype.mouseOver = function(locator){
    var element = this.cacheAwareLocate(locator);
    teJQuery(element).trigger('mouseover');
};
```

where teJQuery is an alias of jQuery to avoid name collision.

## DOM Manipulation Tool ##

[Tellurium new Engine](http://code.google.com/p/aost/wiki/telluriumengine) utilizes jQuery as a DOM manipulation tool in algorithm and API implementation. For example, the UI module group locating algorithm, i.e., [the Santa algorithm](http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm) uses the jQuery `data()` API in the marking phase.

```
    if($found.size() == 1){
        //found exactly one, happy path
        //temporally assign uid to the found element
        !tellurium.logManager.isUseLog || fbLog("Marking uid " + uid, $found.eq(0));
        $found.eq(0).data("uid", uid);
        snapshot.addUi(uid, $found.get(0));
        //store all the elements with data("uid")
        this.uidset.push($found.eq(0));
        snapshot.setColor(ncolor);
        snapshot.score += 100;
        snapshot.nelem++;
        this.squeue.push(snapshot);
...
```

To fully utilize the power of jQuery, we add some [custom jQuery selectors and plugins](http://code.google.com/p/aost/wiki/CustomJQuerySelectorInTellurium) to meet our needs, for example, the Tellurium _group_ selector is defined as follows.

```
teJQuery.expr[':'].group = function(obj, index, m){
      var $this = teJQuery(obj);

      var splitted = m[3].split(",");
      var result = true;

      for(var i=0; i<splitted.length; i++){
         result = result && ($this.find(splitted[i]).length > 0);
      }

      return result;
};
```

## Trump ##

The [Tellurium UI Module Firefox Plugin](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_UI_Module_Plugin_%28TrUMP%29) (Trump) will be updated soon to support CSS selector locator. jQuery will also be a powerful tool in Trump for UI implementation, algorithm, and test driving.

## jQuery UI Widgets ##

[Tellurium widgets](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_Widget_Extensions) have the advantage that we can use the widget directly in our test code just like a regular tellurium UI object and we do not need to deal with individual UIs in the widget at the link or button level any more. What we need to do is to call the methods defined in the Tellurium widget. Another advantage is that once the widget is defined, we can use it anywhere we want by simply including the compiled jar file. We take the Date Picker widget as an example to demonstrate how to define jQuery UI Tellurium widgets and how to use them directly in your test code.

The [jQuery UI Date Picker widget](http://docs.jquery.com/UI/Datepicker) can be presented as a Tellurium Widget as follows.

```
class DatePicker extends JQueryUiWidget {
  void defineWidget() {

    ui.Container(uid: "DatePicker", clocator: [tag: "div", class: "ui-datepicker ui-widget ui-widget-content"]) {
      Container(uid: "Header", clocator: [tag: "div", class: "ui-datepicker-header"]) {
        UrlLink(uid: "Prev", clocator: [class: "ui-datepicker-prev"])
        UrlLink(uid: "Next", clocator: [class: "ui-datepicker-next"])
        Container(uid: "Title", clocator: [tag: "div", class: "ui-datepicker-title"]) {
          TextBox(uid: "Month", clocator: [tag: "span", class: "ui-datepicker-month"])
          TextBox(uid: "Year", clocator: [tag: "span", class: "ui-datepicker-year"])
        }
      }
      StandardTable(uid: "Calendar", clocator: [class: "ui-datepicker-calendar"]) {
        TextBox(uid: "{header: 1} as Sunday", clocator: [tag: "span", text: "Su"])
        TextBox(uid: "{header: 2} as Monday", clocator: [tag: "span", text: "Mo"])
        TextBox(uid: "{header: 3} as Tuesday", clocator: [tag: "span", text: "Tu"])
        TextBox(uid: "{header: 4} as Wednesday", clocator: [tag: "span", text: "We"])
        TextBox(uid: "{header: 5} as Thursday", clocator: [tag: "span", text: "Th"])
        TextBox(uid: "{header: 6} as Friday", clocator: [tag: "span", text: "Fr"])
        TextBox(uid: "{header: 7} as Sunday", clocator: [tag: "span", text: "Sa"])
        UrlLink(uid: "{row: any, column: any} as D1", clocator: [text: "1"])
        UrlLink(uid: "{row: any, column: any} as D2", clocator: [text: "2"])
        UrlLink(uid: "{row: any, column: any} as D3", clocator: [text: "3"])
        UrlLink(uid: "{row: any, column: any} as D4", clocator: [text: "4"])
        UrlLink(uid: "{row: any, column: any} as D5", clocator: [text: "5"])
        UrlLink(uid: "{row: any, column: any} as D6", clocator: [text: "6"])
        UrlLink(uid: "{row: any, column: any} as D7", clocator: [text: "7"])
        UrlLink(uid: "{row: any, column: any} as D8", clocator: [text: "8"])
        UrlLink(uid: "{row: any, column: any} as D9", clocator: [text: "9"])
        UrlLink(uid: "{row: any, column: any} as D10", clocator: [text: "10"])
        UrlLink(uid: "{row: any, column: any} as D11", clocator: [text: "11"])
        UrlLink(uid: "{row: any, column: any} as D12", clocator: [text: "12"])
        UrlLink(uid: "{row: any, column: any} as D13", clocator: [text: "13"])
        UrlLink(uid: "{row: any, column: any} as D14", clocator: [text: "14"])
        UrlLink(uid: "{row: any, column: any} as D15", clocator: [text: "15"])
        UrlLink(uid: "{row: any, column: any} as D16", clocator: [text: "16"])
        UrlLink(uid: "{row: any, column: any} as D17", clocator: [text: "17"])
        UrlLink(uid: "{row: any, column: any} as D18", clocator: [text: "18"])
        UrlLink(uid: "{row: any, column: any} as D19", clocator: [text: "19"])
        UrlLink(uid: "{row: any, column: any} as D20", clocator: [text: "20"])
        UrlLink(uid: "{row: any, column: any} as D21", clocator: [text: "21"])
        UrlLink(uid: "{row: any, column: any} as D22", clocator: [text: "22"])
        UrlLink(uid: "{row: any, column: any} as D23", clocator: [text: "23"])
        UrlLink(uid: "{row: any, column: any} as D24", clocator: [text: "24"])
        UrlLink(uid: "{row: any, column: any} as D25", clocator: [text: "25"])
        UrlLink(uid: "{row: any, column: any} as D26", clocator: [text: "26"])
        UrlLink(uid: "{row: any, column: any} as D27", clocator: [text: "27"])
        UrlLink(uid: "{row: any, column: any} as D28", clocator: [text: "28"])
        UrlLink(uid: "{row: any, column: any} as D29", clocator: [text: "29"])
        UrlLink(uid: "{row: any, column: any} as D30", clocator: [text: "30"])
        UrlLink(uid: "{row: any, column: any} as D31", clocator: [text: "31"])
        TextBox(uid: "{row: all, column: all}", 
           clocator: [class: "ui-datepicker-other-month ui-datepicker-unselectable ui-state-disabled"], self: "true")
      }
    }
  }

  public String getYear(){
    getText("DatePicker.Header.Title.Year");
  }

  public String getMonth(){
    getText("DatePicker.Header.Title.Month");
  }

  public void prev(){
    click("DatePicker.Header.Prev")
  }

  public void next(){
    click("DatePicker.Header.Next")
  }

  public void selectDay(int day){
    String uid = "DatePicker.Calendar.D" + day;
    mouseOver(uid);
    click(uid);
    mouseOut(uid);
  }

  public void selectFriday(int week){
    String uid = "DatePicker.Calendar[" + week + "][Friday]";
    mouseOver(uid);
    click(uid);
    mouseOut(uid);
  }

  public String getMonthYear(){
    String month = getText("DatePicker.Header.Title.Month");
    String year = getText("DatePicker.Header.Title.Year");

    return "${month} ${year}";
  }
}
```

Once the Tellurium Date Picker widget is defined, we can use it like a regular Tellurium object as follows.

```
class DatePickerDefaultModule extends DslContext {
    public void defineUi() {
       ui.InputBox(uid: "DatePickerInput", clocator: [id: "datepicker"])
       ui.jQuery_DatePicker(uid: "Default", clocator: [tag: "body"])
    }
...
}
```

where jQuery is the name space.

# Summary #

More detailed use of jQuery could be found from the [Tellurium Engine](http://code.google.com/p/aost/wiki/telluriumengine) project. Tellurium team is looking for jQuery developers to join our team to work on the [Engine](http://code.google.com/p/aost/wiki/telluriumengine), [jQuery UI widget](http://code.google.com/p/aost/wiki/TelluriumjQueryUiWidgets), and the [Trump](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_UI_Module_Plugin_%28TrUMP%29) project.

# Resources #

  * [jQuery](http://jquery.com/)
  * [jQuery UI 1.8](http://jqueryui.com/)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Released)
  * [Tellurium Reference Documentation](http://aost.googlecode.com/files/tellurium-reference-0.7.0.pdf)