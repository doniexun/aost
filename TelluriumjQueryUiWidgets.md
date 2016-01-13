

# Introduction #

Tellurium provides support to describe a UI widget or a set of nested UI objects as a [Tellurium widget](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_Widget_Extensions). Tellurium widgets have the advantage that we can use the widget directly in our test code just like a regular tellurium UI object and we do not need to deal with individual UIs in the widget at the link or button level any more. What we need to do is to call the methods defined in the Tellurium widget. Another advantage is that once the widget is defined, we can use it anywhere we want by simply including the compiled jar file.

Widgets in JavaScript frameworks are perfect candidates that can utilize the benefits of Tellurium Widgets because JavaScript widgets are usually reused in many places of a web application. A JavaScript widget is only need to be defined once as a Tellurium widget and then can be reused in all places that the widget is included.  We take jQuery UI widgets, more specifically, the [Date Picker widget](http://jqueryui.com/demos/datepicker/) as an example to demonstrate how to define jQuery UI Tellurium widgets and how to use them directly in your test code.  Widgets in other JavaScript frameworks such as Dojo and ExtJs can also be described as Tellurium widgets in a similar way.

# Prerequisites #

  * [Tellurium 0.8.0-SNAPSHOT](http://maven.kungfuters.org/content/repositories/snapshots)
  * [jQuery UI 1.8](http://jqueryui.com/)

# Implementation #

## Create a Tellurium jQuery UI Widget Project ##

We use [Tellurium Widget Archetype](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_Maven_Archetypes) to create the following project.

```
mvn archetype:create -DgroupId=org.telluriumsource -DartifactId=jqueryui-widgets -DarchetypeArtifactId=tellurium-widget-archetype -DarchetypeGroupId=org.telluriumsource -DarchetypeVersion=0.8.0-SNAPSHOT -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/snapshots
```

After generate the appropriate files, the code structure looks as follows.

```
|-- LICENSE.txt
|-- README
|-- TelluriumConfig.groovy
|-- WebContent
|-- pom.xml
`-- src
    |-- main
    |   |-- groovy
    |   |   `-- org
    |   |       `-- telluriumsource
    |   |           `-- ui
    |   |               `-- widget
    |   |                   `-- jqueryui
    |   |                       |-- Init.groovy
    |   |                       |-- JQueryUiWidget.groovy
    |   |                       |-- builder
    |   |                       |   `-- DatePickerBuilder.groovy
    |   |                       `-- object
    |   |                           `-- DatePicker.groovy
    |   `-- resources
    `-- test
        |-- groovy
        |   `-- org
        |       `-- telluriumsource
        |           |-- module
        |           |   `-- DatePickerDefaultModule.groovy
        |           |-- support
        |           |   `-- JettyFileServer.java
        |           `-- test
        |               `-- DatePickerTestCase.java
        `-- resources
```

The JQueryUiWidget class defines the namespace for jQuery UI widget.

```
abstract class JQueryUiWidget extends Widget {
    public static final String NAMESPACE = "jQuery"
    
}
```

## Tellurium Date Picker Widget ##

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
...
}
```

The Date Picker header is a Container with two URL Links for the "prev" and "next" button and Month and Year are TextBoxes.

The dynamic data Grid of the days of the current month is represented by [the Tellurium StandardTable](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Standard_Table) object, which includes headers from Sunday to Monday and days. Due to the dynamic nature of the days, we use [Tellurium UID Description Language](http://code.google.com/p/aost/wiki/TelluriumUIDDescriptionLanguage) (UDL) to define each day with row and column to be "any" positions. Also, we use ID such as D1 to reference the day.

With the above UI module, we define the following methods for the Date Picker widget.

```
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
```

## Register the Date Picker Widget ##

First, we need to define a builder for the Date Picker widget as follows.

```
class DatePickerBuilder extends UiObjectBuilder{

    public build(Map map, Closure c) {
       //add default parameters so that the builder can use them if not specified
        def df = [:]
        DatePicker datepicker = this.internBuild(new DatePicker(), map, df)
        datepicker.defineWidget()
        datepicker.dsl.locator = datepicker.locator

        return datepicker
    }
}
```

Then, we register the Date Picker object in the Init class.

```
class Init implements WidgetBootstrap{

    public void loadWidget(UiObjectBuilderRegistry uiObjectBuilderRegistry) {
        if(uiObjectBuilderRegistry != null){
          uiObjectBuilderRegistry.registerBuilder(getFullName("DatePicker"), new DatePickerBuilder())
        }
    }

    protected String getFullName(String name){
        return JQueryUiWidget.NAMESPACE + JQueryUiWidget.NAMESPACE_SUFFIX + name
    }
}
```

# Use Tellurium jQuery UI Widgets #

We can compile the Tellurium jQuery UI Widgets project as a jar file and then use it in our Tellurium testing project.

## Load Tellurium jQuery UI Widgets ##

To load Tellurium jQuery UI Widgets, we need to specify the UI widget name in Tellurium configuration file, TelluriumConfig.groovy.

```
    widget{
        module{
            //define your widget modules here, for example Dojo or ExtJs
            included="jqueryui"
        }
    }
```

## jQuery UI Date Picker Example ##

To test the widget, we need a jQuery UI Date Picker example. We download [jQuery UI 1.8](http://jqueryui.com/) and unzip it to the WebContext directory. Then, we can use the Date Picker demos from jQuery UI 1.8.

## Use Jetty as An Embedded Http Server ##

To run the example, we need a http server. We use [Jetty 7](http://jetty.codehaus.org/jetty/) as an embedded http server. We add the following dependency in our POM file.

```
   <dependency>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-server</artifactId>
      <version>7.0.1.v20091125</version>
      <scope>test</scope> 
   </dependency>
```

Then we define the embedded Jetty server as follows.

```
public class JettyFileServer extends Thread {

    private int port;

    private String resourceBase;

    private Server server;

    private boolean isRunning = false;

    public JettyFileServer(int port, String resourceBase) {
        this.port = port;
        this.resourceBase = resourceBase;
    }

    public void start()  {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });

        resource_handler.setResourceBase(resourceBase);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
        try {
            server.start();
            isRunning = true;
        } catch (Exception e) {
            e.printStackTrace();
            isRunning = false;
        }
    }

    public void shutDown() {
        if(server != null){
            try {
                server.stop();
            } catch (Exception e) {

            }
            isRunning = false;
        }
    }
}
```

## Create Tellurium Date Picker Widget Test Case ##

We use the default.html from Date Picker demos as our example.

```
<head>
	<meta charset="UTF-8" />
	<title>jQuery UI Datepicker - Default functionality</title>
	<link type="text/css" href="../../themes/base/jquery.ui.all.css" rel="stylesheet"/>
	<script type="text/javascript" src="../../jquery-1.4.2.js"></script>
	<script type="text/javascript" src="../../ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="../../ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="../../ui/jquery.ui.datepicker.js"></script>
	<link type="text/css" href="../demos.css" rel="stylesheet" />
	<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker();
	});
	</script>
</head>
<body>

<div class="demo">

<p>Date: <input type="text" id="datepicker"></p>

</div><!-- End demo -->
```

The above HTML can be presented by the following Tellurium UI module.

```
class DatePickerDefaultModule extends DslContext {
    public void defineUi() {
       ui.InputBox(uid: "DatePickerInput", clocator: [id: "datepicker"])
       ui.jQuery_DatePicker(uid: "Default", clocator: [tag: "body"])
    }
...
}
```

where "jQuery" is the namespace for Tellurium jQuery UI widgets and we use DatePicker as a regular Tellurium UI object. In the meanwhile, we can use the `onWidget` commands to call methods in the Tellurium Date Picker widgets.

```
  public void prevMonth(){
    onWidget "Default", click, "DatePicker.Header.Prev"
  }

  public void nextMonth(){
    onWidget "Default", click, "DatePicker.Header.Next"
  }

  public void selectDay(int day){
    onWidget "Default", selectDay, day
  }

  public void selectFriday(int week){
    onWidget "Default", selectFriday, week
  }

  public String getMonthYear(){
    onWidget "Default", getMonthYear
  }
 
  public void toggleInput(){
    click "DatePickerInput"
  }

  public String getSelectedDate(){
    getValue "DatePickerInput"
  }
```

The test case is defined as follows.

```
public class DatePickerTestCase extends TelluriumJUnitTestCase {
    private static JettyFileServer server;

    @BeforeClass
    public static void setUp(){
        server = new JettyFileServer(8088, "WebContent");
        server.start();
        connectSeleniumServer();
        useTelluriumEngine(true);
        useEngineLog(true);
   }

    @Test
    public void testDefaultDatePicker(){
        DatePickerDefaultModule datePicker = new DatePickerDefaultModule();
        datePicker.defineUi();
        connectUrl("http://localhost:8088/demos/datepicker/default.html");
        datePicker.toggleInput();
        datePicker.nextMonth();
        datePicker.selectDay(20);
        String date = datePicker.getSelectedDate();
        System.out.println("Selected Date: " + date);

        datePicker.toggleInput();
        datePicker.prevMonth();
        datePicker.selectFriday(2);
        date = datePicker.getSelectedDate();
        System.out.println("Selected Date: " + date);
        date = datePicker.getMonthYear();
        System.out.println("Selected Month and Year: " + date);
    }

    @AfterClass
    public static void tearDown(){
        if(server != null)
            server.shutDown();
    }
}
```

# Summary #

Date Picker is a simple example to demonstrate the definition and use of Tellurium Widgets. We can define more jQuery UI widgets and compile them as a jar file, then we can reuse them by simply including the jar file. The example code is available from [Tellurium jQuery UI Widget project](http://aost.googlecode.com/svn/trunk/extensions/jqueryui-widgets).

Furthermore, widgets in other JavaScript frameworks such as Dojo and ExtJs can also be described as Tellurium widgets in a similar way.

# Resources #

  * [Tellurium Widget Extensions](http://code.google.com/p/aost/wiki/UserGuide070TelluriumSubprojects#Tellurium_Widget_Extensions)
  * [jQuery UI 1.8](http://jqueryui.com/)
  * [Jetty](http://jetty.codehaus.org/jetty/)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Released)
  * [Tellurium Reference Documentation](http://aost.googlecode.com/files/tellurium-reference-0.7.0.pdf)