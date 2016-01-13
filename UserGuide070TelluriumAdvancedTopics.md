(A PDF version of the user guide is available [here](http://aost.googlecode.com/files/tellurium-reference-0.7.0.pdf))



# Advanced Topics in Tellurium #

## Data Driven Testing ##

Data Driven Testing is a different way to write tests. For example, separate test data from the test scripts and the test flow is not controlled by the test scripts, but by the input file instead. In the input file, users can specify which tests to run, what are input parameters, and what are expected results. Data driven testing in Tellurium is illustrated in Figure 2-5 with the following system diagram:

**Figure 2-5 Data Driven Testing in Tellurium System Diagram**

http://tellurium-users.googlegroups.com/web/TelluriumDataDrivenSmall.jpg?gda=WePTVk4AAAD5mhXrH3CK0rVx4StVj0LYYQ-a0sZzxEmmZWlHbP2MWzTrErMgjh_s8-a7FfMpsGsNL5bu5vgQy2xsA01CuO9M47Cl1bPl-23V2XOW7kn5sQ&gsc=ODykzgsAAACEc2FtJPGdXe_7CHb1VB6Z

The Tellurium Data Driven Test consists of three main parts:

  1. Data Provider
  1. TelluriumDataDrivenModule
  1. TelluriumDataDrivenTest

### Data Provider ###

The Data Provider is responsible for reading data from input stream and converting data to Java variables.

Tellurium includes the following Data Provider methods:

  1. loadData file\_name, load input data from a file
  1. useData String\_name, load input data from a String in the test script
  1. bind(field\_name), bind a variable to a field in a field set
  1. closeData, close the input data stream and report the test results
  1. cacheVariable(name, variable), put variable into cache
  1. getCachedVariable(name, variable), get variable from cache where the file\_name includes the file path. For example:

`loadData "src/test/example/test/ddt/GoogleBookListCodeHostInput.txt"`

Tellurium supports pipe format and CSV format input file.

### loadData ###

To change the file reader for different formats, change the following settings in the configuration file `TelluriumConfig.groovy:`

```
datadriven{
  dataprovider{
    //specify which data reader you like the data provider to use
    //the valid options include "PipeFileReader", "CSVFileReader" at this point
    reader = "PipeFileReader"
  }
}
```

### useData ###

Tellurium's useData is designed to specify test data in the test scripts directly. It loads input from a String. The String is usually defined in Groovy style using triple quota, for example:

```
protected String data = """
  google_search | true | 865-692-6000 | tellurium
  google_search | false| 865-123-4444 | tellurium selenium test
  google_search | true | 755-452-4444 | tellurium groovy
  google_search | false| 666-784-1233 | tellurium user group
  google_search | true | 865-123-5555 | tellurium data driven
  """
  ...

  useData data
```

### bind ###

`bind` is the command used to bind a variable to an input Field Set field at runtime. `FieldSet` is the format of a line of data. For example:

`def row = bind("GCHLabel.row")`

is used to bind the row variable to the "row" field in the FieldSet "GCHLabel". Tellurium does not explicitly differentiate input parameters from the expected results in the input data. To bind variables to the input data then use any of them as the expected results for result comparison.

### cacheVariable and getCachedVariable ###

cacheVariable and getCachedVariable are used to pass intermediate variables among tests.

  * cacheVariable is used to put a variable into a cache
  * getCachedVariable is used to get back the variable

For example:

```
int headernum = getTableHeaderNum()
cacheVariable("headernum", headernum)

...

int headernum = getCachedVariable("headernum")
...
```

### closeData ###

When testing is completed, use "closeData" to close the input data stream. In the meantime, the result reporter outputs the test results in the format specified in the configuration file.

For example: the XML file as shown in the TelluriumConfig.groovy file:

```
test{
  result{
    //specify what result reporter used for the test result
    //valid options include "SimpleResultReporter", "XMLResultReporter", 
    //and "StreamXMLResultReporter"
    reporter = "XMLResultReporter"
   
    //the output of the result
    //valid options include "Console", "File" at this point
    //if the option is "File", you need to specify the file name, 
    //other wise it will use the default
    //file name "TestResults.output"
    output = "Console"

    //test result output file name
    filename = "TestResult.output"
  }
}
```

### TelluriumDataDrivenModule ###

`TelluriumDataDrivenModule` is used to define modules, where users can define UI Modules, FieldSets, and tests as shown in the following Figure 2-6 sequence diagram. Users should extend this class to define their own test modules.

**Figure 2-6 TelluriumDataDrivenModule Sequence Diagram**

http://tellurium-users.googlegroups.com/web/TelluriumDDTModule.png?gda=oIfzLkgAAAD5mhXrH3CK0rVx4StVj0LY6r7Fxo4RaVZ2InRIkvRUPW9wwFNWzBcwQWJJR7cmP5glzhb83kORdLwM2moY-MeuGjVgdwNi-BwrUzBGT2hOzg&gsc=x3TBYwsAAAAeXMPG6HH-B1VXA1h0gdTp

`TelluriumDataDrivenModule` provides one method "defineModule" for users to implement. Since it extends the DslContext class, users define UI modules as in regular Tellurium UI Modules. For example:

```
ui.Table(uid: "labels_table", clocator: [:], group: "true"){
   TextBox(uid: "{row: 1, column: 1} as Label", clocator: [tag: "div", 
           text: "Example project labels:"])
   Table(uid: "{row: 2, column: 1}", clocator: [header: "/div[@id=\"popular\"]"]){
        UrlLink(uid: "{row: all, column: all}", locator: "/a")
   }
}
```

### FieldSet ###

FieldSet defines the format of one line of input data. FieldSet consists of fields such as columns, in the input data. There is a special field "test", where users can specify what tests this line of data applies to. For example:


```
fs.FieldSet(name: "GCHStatus", description: "Google Code Hosting input") {
    Test(value: "getGCHStatus")
    Field(name: "label")
    Field(name: "rowNum", type: "int")
    Field(name: "columNum", type: "int")
}  
```

FieldSet defines the input data format for testing Google code hosting web page.

**Note:** The Test field must be the first column of the input data.

The default name of the test field is "test" and does not need to be specified. If the value attribute of the test field is not specified, it implies this same format. For example, FieldSet is used for different tests.

A regular field includes the following attributes:

```
class Field {
        //Field name
        private String name

        //Field type, default is String
        private String type = "String"

        //optional description of the Field
        private String description

        //If the value can be null, default is true
        private boolean nullable = true

        //optional null value if the value is null or not specified
        private String nullValue

        //If the length is not specified, it is -1
        private int length = -1

        //optional String pattern for the value
        //if specified, use it for String validation
        private String pattern
   } 
```

Tellurium can automatically handle Java primitive types.

### typeHandler ###

Another flexibility Tellurium provides is allowing users to define their own custom type handlers to deal with more complicated data types by using "typeHandler". For example:

```
//define custom data type and its type handler

typeHandler "phoneNumber", "org.tellurium.test.PhoneNumberTypeHandler"

//define file data format
fs.FieldSet(name: "fs4googlesearch", description: "example field set for google search"){
    Field(name: "regularSearch", type: "boolean", 
          description: "whether we should use regular search or use I'm feeling lucky")
    Field(name: "phoneNumber", type: "phoneNumber", description: "Phone number")
    Field(name: "input", description: "input variable")
}
```

The above script defines a custom type "PhoneNumber" and the Tellurium automatically calls this type handler to convert the input data to the "PhoneNumber" Java type.

### Define Test ###

The "defineTest" method is used to define a test in the TelluriumDataDrivenModule. For example, the following script defines the "clickGCHLabel" test:

```
defineTest("clickGCHLabel"){
    def row = bind("GCHLabel.row")
    def column = bind("GCHLabel.column")

    openUrl("http://code.google.com/hosting/")
    click  "labels_table[2][1].[${row}][${column}]"

    waitForPageToLoad 30000
}
```

**Note:** The bind command binds variables row, column to the fields "row" and "column" in the FieldSet "GCHLabel".

### compareResult ###

Tellurium also provides the command "compareResult" for users to compare the actual result with the expected result. For example, the following script compares the expected label, row number, and column number with the acutal ones at runtime:

```
defineTest("getGCHStatus"){
    def expectedLabel = bind("GCHStatus.label")
    def expectedRowNum = bind("GCHStatus.rowNum")
    def expectedColumnNum = bind("GCHStatus.columNum")

    openUrl("http://code.google.com/hosting/")
    def label = getText("labels_table[1][1]")
    def rownum = getTableMaxRowNum("labels_table[2][1]")
    def columnum = getTableMaxColumnNum("labels_table[2][1]")
    
    compareResult(expectedLabel, label)
    compareResult(expectedRowNum, rownum) 
    compareResult(expectedColumnNum, columnum)
    pause 1000
}
```

Sometimes users may require custom "compareResult" to handle more complicated situations. For example, when users compare two lists, users can override the default "compareResult" behaviour by specifying custom code in the closure:

```
compareResult(list1, list2){
    assertTrue(list1.size() == list2.size())
    for(int i=0; i<list1.size();i++){
        //put your custom comparison code here
    }
}
```

### checkResult ###

If users want to check a variable in the test, the "checkResult" method is used coming with a closure where users define the actual assertions inside:

```
checkResult(issueTypeLabel) {
    assertTrue(issueTypeLabel != null)
}
```

Like "compareResult", "checkResult" captures all assertion errors. The test resumes even when the assertions fail. The result is reported in the output.

### Log Message ###

In addition, the "logMessage" is used by users to log any messages in the output.

```
logMessage "Found ${actual.size()} ${issueTypeLabel} for owner " + issueOwner 
```

### Tellurium Data Driven Test ###

TelluriumDataDrivenTest is the class users should extend to run the actual data driven testing. It is more like a data driven testing engine. There is only one method, "testDataDriven", which users implement. The sequence diagram for the testing process is shown in Figure 2-7:

**Figure 2-7 TelluriumDataDrivenTest System Diagram**

http://tellurium-users.googlegroups.com/web/TelluriumDDTTestSequence.png?gda=Fgy8N04AAAD5mhXrH3CK0rVx4StVj0LY6r7Fxo4RaVZ2InRIkvRUPYEAgewWsw-pfk-JI3kLTQ9omPhdHiHZ5EjvmEnpg6SE47Cl1bPl-23V2XOW7kn5sQ&gsc=x3TBYwsAAAAeXMPG6HH-B1VXA1h0gdTp

Complete the following steps to use TelluriumDataDrivenTest:

  1. Use "includeModule" to load defined Modules
  1. Use "loadData" or "useData" to load input data stream
  1. Use "stepToEnd" to read the input data line by line and pick up the specified test and run it, until reaches the end of the data stream
  1. Use "closeData" to close the data stream and output the test results

What the "includeModule" does is to merge in all Ui modules, FieldSets, and tests defined in that module file to the global registry.

"stepToEnd" looks at each input line, first find the test name and pass in all input parameters to it, and then run the test. The whole process is illustrated in the following example:

```
class GoogleBookListCodeHostTest extends TelluriumDataDrivenTest{

    public void testDataDriven() {
        includeModule  example.google.GoogleBookListModule.class
        includeModule  example.google.GoogleCodeHostingModule.class
        //load file
        loadData "src/test/example/test/ddt/GoogleBookListCodeHostInput.txt"

        //read each line and run the test script until the end of the file
        stepToEnd()

        //close file
        closeData()
   }
}
```

The input data for this example are as follows:

```
##TEST should be always be the first column

##Data for test "checkBookList"
##TEST | CATEGORY | SIZE
checkBookList|Fiction|8
checkBookList|Fiction|3

##Data for test "getGCHStatus"
##TEST | LABEL | Row Number | Column Number
getGCHStatus |Example project labels:| 3 | 6
getGCHStatus |Example project| 3 | 6

##Data for test "clickGCHLabel"
##TEST | row | column
clickGCHLabel | 1 | 1
clickGCHLabel | 2 | 2
clickGCHLabel | 3 | 3
```

**Note:** The line starting with "##" is the comment line and the empty line is ignored.

If users want to control the testing execution flow by themselves, Tellurium also provides this capability even though its use is **_not recommended_**.

Tellurium provides two additional commands, "step" and "stepOver".

  * "step" is used to read one line of input data and run it.
  * "stepOver" is used to skip one line of input data.

In this meanwhile, Tellurium also allows the user to specify additional test scripts using closure. For example:

```
step{
    //bind variables
    boolean regularSearch = bind("regularSearch")
    def phoneNumber = bind("fs4googlesearch.phoneNumber")
    String input = bind("input")
    openUrl "http://www.google.com"
    type "google_start_page.searchbox", input
    pause 500
    click "google_start_page.googlesearch"
    waitForPageToLoad 30000
}
```

This usually implies that the input data format is unique or the test script knows about what format the current input data are using.

## Selenium Grid Support ##

Selenium Grid transparently distributes tests on multiple machines so that the tests are run in parallel. Recently support for the Selenium Grid has been added to Tellurium. Now Tellurium tests can be run against different browsers using Selenium Grid. Tellurium core is updated to support Selenium Grid sessions.

For example, assume 3 machines are set up to run Tellurium tests on the Selenium Grid. All the steps can be completed on the userâs local box. To do this locally, remove the machine names with `localhost`. Each machine in this set up has a defined role as described below:

  1. **dev1.tellurium.com** Tellurium test development machine.
  1. **hub.tellurium.com** Selenium Grid hub machine that drives the tests.
  1. **rc.tellurium.com** Multiple Selenium RC server running and registered to the Selenium Grid HUB.

The actual test execution is completed on this machine. Register as many Selenium RC servers as required. However, be realistic about the hardware specification.

Download the Selenium Grid from the following URL and extract the contents of the folder on each of these machines.

Tellurium uses Selenium Grid 1.0.3, the current released version. [http://selenium-grid.seleniumhq.org/download.html](http://selenium-grid.seleniumhq.org/download.html). Figure 2-8 shows an illustration of the environment.

**Figure 2-8 Selenium Grid Support Environment**

http://tellurium-users.googlegroups.com/web/TelluriumGridSetup.png?gda=9fgieUgAAAD5mhXrH3CK0rVx4StVj0LYYQ-a0sZzxEmmZWlHbP2MW6uQS0SyLBzYwfM7_kvx7qklzhb83kORdLwM2moY-MeuGjVgdwNi-BwrUzBGT2hOzg&gsc=ODykzgsAAACEc2FtJPGdXe_7CHb1VB6Z

### Selenium Grid Support Test Procedure ###

1. Launch the Selenium Grid Hub on the hub machine. Open up a terminal on the HUB machine `hub.tellurium.com` and go to the download directory of the Selenium Grid.

```
> cd /Tools/selenium-grid-1.0.3
> ant launch-hub
```

**Result:** The Selenium HUB is launched on the machine with different browsers.

2. Navigate to the following URL location to ensure that the HUB is working properly:
[http://hub.tellurium.com:4444/console](http://hub.tellurium.com:4444/console)

3. View the web page with 3 distinct columns:
  1. a Configured Environments
  1. Available Remote Controls
  1. Active Remote Controls

4. Have a list of browsers configured by default to run the tests while the list for Available Remote Controls and Active Remote Controls is empty.

5. Launch the Selenium RC servers and register them with the selenium HUB. Open up a terminal on rc.tellurium.com and go to the selenium grid download directory.

```
> cd /Tools/selenium-grid-1.0.3
> ant -Dport=5555 -Dhost=rc.tellurium.com -DhubURL=http://hub.tellurium.com:4444 \
      -Denvironment="Firefox on Windows" launch-remote-control
```

**Result:** The command starts a Selenium RC server on this machine.

6. Register the Selenium RC server with the Selenium Grid hub machine as specified by the hubURL.

**Note:** To register another Selenium RC server on this machine for internet explorer repeat the step on a different port.

```
> cd /Tools/selenium-grid-1.0.3
> ant -Dport=5556 -Dhost=rc.tellurium.com -DhubURL=http://hub.tellurium.com:4444  -Denvironment="IE on Windows" launch-remote-control
```

  1. _port_ the remote control is listening to. Must be unique on the machine the remote control runs from.
  1. _hostname_ Hostname or IP address of the machine the remote control runs on. Must be visible from the Hub machine.
  1. _hub url_ Which hub the remote control should register/unregister to. As the hub is running on hostname hub.tellurium.com, the URL is [http://hub.tellurium.com:4444](http://hub.tellurium.com:4444/)

7. Point your browser to the Hub console Once you are successful in replicating a setup similar to the one described above, ([http://hub.tellurium.com:4444/console](http://hub.tellurium.com:4444/console)).

8. Verify that all the remote controls registered correctly. Available remote controls list should be updated and have the 2 selenium servers available to run the tests.

9. Run the Tellurium tests against different browsers once the Selenium Hub and the Selenium RC servers on the Grid environment have started.

10. Go to the Tellurium test development machine, the **dev1.tellurium.com**.

11. Open up the TelluriumConfig.groovy.

12. Change the values of the Selenium server and port to ensure the Tellurium requests for the new sessions from the Selenium HUB are received.

13. Verify that the Selenium HUB points to Tellurium tests run on rc.tellurium.com based on the browser of choice.

14. Change the values for the following properties:

  1. _runInternally_: ensures that the Selenium Server on the local machine is not launched.
  1. _serverHost_: the selenium grid hub machine that has the information about the available selenium rc servers.
  1. _port_: port that Selenium HUB is running on. By default, this port is 4444. This can be changed in the grid\_configuraton.yml file if this port is not available on your HUB machine.
  1. _browser_: the browser that comes under the configured environments list on the selenium HUB machine. These values can be changed to a userâs choice in the grid\_configuration.yml file.

```
tellurium{

    //embedded selenium server configuration
    embeddedserver {

        //port number
        port = "4444"

        //whether to use multiple windows
        useMultiWindows = false

        //whether to run the embedded selenium server. 
        //If false, you need to manually set up a selenium server
        runInternally = false

        //profile location
        profile = ""

        //user-extension.js file
        userExtension = "target/classes/extension/user-extensions.js"
    }

    //event handler
    eventhandler{

        //whether we should check if the UI element is presented
        checkElement = false

        //wether we add additional events like "mouse over"
        extraEvent = true
    }

    //data accessor
    accessor{
        //whether we should check if the UI element is presented
        checkElement = true
    }

    //the configuration for the connector that connects the selenium client 
    //to the selenium server
    connector{
        //selenium server host
        //please change the host if you run the Selenium server remotely
        serverHost = "hub.tellurium.com"

        //server port number the client needs to connect
        port = "4444"

        //base URL
        baseUrl = "http://localhost:8080"

        //Browser setting, valid options are
        //  *firefox [absolute path]
        //  *iexplore [absolute path]
        //  *chrome
        //  *iehta
        browser = "Firefox on Windows"

        //user's class to hold custom selenium methods associated with user-extensions.js
        //should in full class name, for instance, "com.mycom.CustomSelenium"
        customClass = "org.tellurium.test.MyCommand"
    }
```

15. The set up is now complete.

16. Run the tests as usual using either the Maven command or the IDE. Notice that the tests are running on rc.tellurium.com and the list for Active Remote Controls is also updated on the hub URL ([http://hub.tellurium.com:4444/console](http://hub.tellurium.com:4444/console)) during the test execution.

## Mock Http Server ##

This feature only exists in Tellurium Core 0.7.0 SNAPSHOT. The MockHttpServer is an embedded http server leveraging the Java 6 http server and it is very convenient method of testing HTML sources directly without running a web server.

Tellurium defines two classes:

  1. MockHttpHandler
  1. MockHttpServer

### Mock Http Handler Class ###

The MockHttpHandler class processes the http request:

```
public class MockHttpHandler implements HttpHandler {

  private Map<String, String> contents = new HashMap<String, String>();

  private String contentType = "text/html";

  public void handle(HttpExchange exchange) {
     ......
  }
}
```

The MockHttpHandler method is handle (HttpExchange exchange) and its actions are:

  * Reads the request URI
  * Finds the corresponding response HTML source from the hash map contents
  * Sends the response back to the http client

By default, the response is treated as an HTML source. The user can change this by using the following setter:

`public void setContentType(String contentType)`

MockHttpHandler includes two methods to add URI and its HTML source to the hash map contents:

  1. public void registerBody(String url, String body)
  1. public void registerHtml(String url, String html)

The MockHttpHandler comes with a default HTML template as follows:

```
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Mock HTTP Server</title>
    </head>

    <body>
        BODY_HTML_SOURCE   
    </body>
</html>
```

If `registerBody(String url, String body` is used, the MockHttpHandler uses the above HTML template to wrap the HTML body. Overwrite the default HTML template by calling `registerHtml(String url, String html)` directly, which uses the whole HTML source provided in the variable ''html''.

Usually, the MockHttpHandler is encapsulated by the MockHttpServer and the user does not need to work on it directly.

The MockHttpServer includes an embedded http server, a http handler, and a http port:

```
public class MockHttpServer {

  //default port
  private int port = 8080;

  private HttpServer server = null;
  private MockHttpHandler handler;

  public MockHttpServer() {
    this.handler = new MockHttpHandler();
    this.server = HttpServer.create();
  }

  public MockHttpServer(int port) {
    this.handler = new MockHttpHandler();
    this.port = port;
    this.server = HttpServer.create();
  }

  public MockHttpServer(int port, HttpHandler handler) {
    this.port = port;
    this.handler = handler;
    this.server = HttpServer.create();
  }

  ......
}
```

### Mock Http Server ###

The MockHttpServer provides three different constructors so the user can overwrite the default values. The MockHttpServer encapsulates the MockHttpHander by providing the following methods:

  1. public void setContentType(String contentType)
  1. public void registerHtmlBody(String url, String body)
  1. public void registerHtml(String url, String html)

The user can stop and start the server with the following methods:

  1. public void start()
  1. public void stop()

Use a modified version of a HTML source provided by one Tellurium user as an example and create the UI module Groovy class as follows:

```
public class ListModule extends DslContext {

  public static String LIST_BODY = """
<div class="thumbnails">
    <ul>
        <li class="thumbnail">
            <img alt="Image 1"
                 src="/images_root/image_pictures/01.jpg"/>
        </li>
        <li class="thumbnail">
            <img alt="Image 2"
                 src="/images_root/image_pictures/02.jpg"/>
        </li>
        <li class="thumbnail">
            <img alt="Image 3"
                 src="/images_root/image_pictures/03.jpg"/>
        </li>
        <li class="thumbnail">
        </li>
        <li class="thumbnail active">
            <img alt="Image 4"
                 src="/images_root/image_pictures/04.jpg"/>
        </li>
        <li class="thumbnail potd">
            <div class="potd-icon png-fix"/>
            <img alt="Image 5"
                 src="/images_root/image_pictures/05.jpg"/>
        </li>
    </ul>
</div>    
  """

  public void defineUi() {
    ui.Container(uid: "rotator", clocator: [tag: "div", class: "thumbnails"]) {
      List(uid: "tnails", clocator: [tag: "ul"], separator: "li") {
        UrlLink(uid: "{all}", clocator: [:])
      }
    }
  }
}
```

The reason the HTML source in a Groovy file is included is that the """ quote in Groovy is very easy to present complicated HTML source as a String variable. In Java, the user must concatenate each line of the HTML Source to make it a String variable.

The `defineUi()` defines the UI module for the given HTML source. The major part of the UI module is a List, which uses UI templates to represent a list of links. Tellurium makes it easy and concise to use UI templates to represent UI elements.
Based on the ListModule UI module, define a Tellurium JUnit test case as follows:

```
public class ListTestCase  extends TelluriumJavaTestCase {
    private static MockHttpServer server;

    @BeforeClass
    public static void setUp(){
        server = new MockHttpServer(8080);
        server.registerHtmlBody("/list.html", ListModule.LIST_BODY);
        server.start();
    }

    @Test
    public void testGetSeparatorAttribute(){
        ListModule lm = new ListModule();
        lm.defineUi();

        connectUrl("http://localhost:8080/list.html");

        attr = (String)lm.getParentAttribute("rotator.tnails[6]", "class");
        assertEquals("thumbnail potd", attr);
    }
    

    @AfterClass
    public static void tearDown(){
        server.stop();    
    }
}
```

## Generate Html Source From UI Modules ##

Very often, some Tellurium users asked us to help them to track problems in their Tellurium test code. Due to some company policy, they cannot provide us the HTML source directly, but the UI module instead. Without the HTML source, there is no way for us to debug their test code because we do not have access to their web applications.

However, if we can do reverse engineering to generate the HTML source from the given UI module, we can use the [mock http server](http://code.google.com/p/aost/wiki/TelluriumMockHttpServer) to test the generated HTML Source without the need to access their web applications.

Driven by this motivation, we provided the following new method in `DslContext` for users to generate HTML source from UI modules:

```
  public String generateHtml(String uid)
```

The `generateHtml(uid)` method is really helpful if you want to help other people to track the problem in their Tellurium test code but you have not access to their web applications and HTML sources. Once the HTML source is generated, you can use the [mock http server](http://code.google.com/p/aost/wiki/TelluriumMockHttpServer) to test the generated HTML Source].

### Implementation ###

The key is to generate the HTML source for each individual UI object from the composite locator, denoted by `clocator`. As a result, we added two methods to the `CompositeLocator` class:

```
class CompositeLocator {
    String header
    String tag
    String text
    String trailer
    Map<String, String> attributes = [:]

    public String generateHtml(boolean closeTag){ 
      ......
    }

    public String generateCloseTag(){
      ......
    }
```

where `generateHtml(boolean closeTag)` returns the generated HTML source from the composite locator and the boolean variable _closeTag_ indicates whether to generate the closing tag for the HTML source. For Container type UI objects, most likely, you will not generate the closing tag directly, but use the other method `generateCloseTag()` to generate the closing tag separately so that we can include its child elements in between.

Then on the base class UiObject, we add the `generateHtml()` method as follows,

```
abstract class UiObject implements Cloneable{
    String uid
    String namespace = null
  
    def locator

    //reference back to its parent
    def Container parent

    public String generateHtml(){
      if(this.locator != null){
        return getIndent() + this.locator.generateHtml(true) + "\n";
      }
      
      return "\n";
    }

    public String getIndent(){
      if(parent != null){
          return parent.getIndent() + "    ";
      }else{
        return "";
      }
    }
}
```

To make pretty print, we add a `getIndent()` method in the UiObject to get the indentation for the current UI object.

Once we added the `generateHtml()` method, all the concrete UI objects such as Button, InputBox, and UrlLink inherit this method to generate HTML source. However, for a Contain type, the implementation is different because we need to include its child UI objects in the HTML source. As a result, we overwrite the `generateHtml()` method in the UiObject.

```
class Container extends UiObject {
    def components = [:]

    @Override
    public String generateHtml(){
      StringBuffer sb = new StringBuffer(64);
      String indent = getIndent();

      if(this.components.size() > 0){
        if(this.locator != null)
          sb.append(indent + this.locator.generateHtml(false)).append("\n");
        this.components.each {String uid, UiObject obj ->
          sb.append(obj.generateHtml());
        }
        if(this.locator != null)
          sb.append(indent + this.locator.generateCloseTag()).append("\n");
      }else{
        if(this.locator != null){
          sb.append(this.locator.generateHtml(true)).append("\n")
        }
      }

      return sb.toString();
    }
}
```

UI templates in Tellurium objects such as List and Table make things more complicated. The basic idea is to elaborate all UI templates and key is to get the appropriate List size and Table size. We use an algorithm to determine the sizes and we don't want to go over the details here.

Finally, we add the `generateHtml(String uid)` method to the `DslContext` class

```
  public String generateHtml(String uid){
    WorkflowContext context = WorkflowContext.getContextByEnvironment(this.exploreJQuerySelector, this.exploreSelectorCache)
    def obj = walkToWithException(context, uid)
    return obj.generateHtml()
  }
```

Another method `generateHtml()` is used to generate the HTML source for all UI modules defined in a UI module class file.

```
  public String generateHtml(){
    StringBuffer sb = new StringBuffer(128)
    ui.registry.each {String key, UiObject val ->
      sb.append(val.generateHtml())
    }

    return sb.toString()
  }
```

### Usage ###

#### Container ####

We used the following UI module

```
    ui.Form(uid: "accountEdit", clocator: [tag: "form", id: "editPage", method: "post"]) {
        InputBox(uid: "accountName", clocator: [tag: "input", type: "text", name: "acc2", id: "acc2"])
        InputBox(uid: "accountSite", clocator: [tag: "input", type: "text", name: "acc23", id: "acc23"])
        InputBox(uid: "accountRevenue", clocator: [tag: "input", type: "text", name: "acc8", id: "acc8"])
        TextBox(uid: "heading", clocator: [tag: "h2", text: "*Account Edit "])
        SubmitButton(uid: "save", clocator: [tag: "input", class: "btn", type: "submit", title: "Save", name: "save"])
    }
```

Call the `generateHtml()` method

```
    generateHtml("accountEdit");
```

and it generates the HTML source as follows,

```
<form id="editPage" method="post">
    <input type="text" name="acc2" id="acc2"/>
    <input type="text" name="acc23" id="acc23"/>
    <input type="text" name="acc8" id="acc8"/>
    <h2>Account Edit </h2>
    <input class="btn" type="submit" title="Save" name="save"/>
</form>
```

#### List ####

We have the following UI module with a List, which defines a set of URL links using UI template,

```
    ui.Container(uid: "subnav", clocator: [tag: "ul", id: "subnav"]) {
        Container(uid: "CoreLinks", clocator: [tag: "li", id: "core_links"]) {
          List(uid: "links", clocator: [tag: "ul"], separator: "li") {
            UrlLink(uid: "{all}", clocator: [:])
          }
        }
        UrlLink(uid: "subscribe", clocator: [tag: "li", id: "subscribe"])
    }
```

The generated HTML source is

```
<ul id="subnav">
    <li id="core_links">
        <ul>
          <li>
            <a/>
          </li>
        </ul>
    </li>
    <li id="subscribe"/>
</ul>
```

#### Table ####

Table is a frequently used UI object with UI templates, the Tellurium Issue web page has a data grid to show the issues and it can described using the following UI module:

```
    ui.Table(uid: "issueResult", clocator: [id: "resultstable", class: "results"], group: "true") {
        TextBox(uid: "{header: 1}", clocator: [:])
        UrlLink(uid: "{header: 2} as ID", clocator: [text: "*ID"])
        UrlLink(uid: "{header: 3} as Type", clocator: [text: "*Type"])
        UrlLink(uid: "{header: 4} as Status", clocator: [text: "*Status"])
        UrlLink(uid: "{header: 5} as Priority", clocator: [text: "*Priority"])
        UrlLink(uid: "{header: 6} as Milestone", clocator: [text: "*Milestone"])
        UrlLink(uid: "{header: 7} as Owner", clocator: [text: "*Owner"])
        UrlLink(uid: "{header: 9} as Summary", clocator: [text: "*Summary + Labels"])
        UrlLink(uid: "{header: 10} as Extra", clocator: [text: "*..."])

        //define table elements
        //for the border column
        TextBox(uid: "{row: all, column: 1}", clocator: [:])
        TextBox(uid: "{row: all, column: 8}", clocator: [:])
        TextBox(uid: "{row: all, column: 10}", clocator: [:])
        //For the rest, just UrlLink
        UrlLink(uid: "{row: all, column: all}", clocator: [:])
    }
```

The generated HTML source is as follows

```
<table id="resultstable" class="results">
 <tbody>
  <tr>
   <th>
   

   </th>
   <th>
    <a>ID</a>

   </th>
   <th>
    <a>Type</a>

   </th>
   <th>
    <a>Status</a>

   </th>
   <th>
    <a>Priority</a>

   </th>
   <th>
    <a>Milestone</a>

   </th>
   <th>
    <a>Owner</a>

   </th>
   <th>


   </th>
   <th>
    <a>Summary + Labels</a>

   </th>
   <th>
    <a>...</a>

   </th>
  </tr>
  <tr>
   <td>
   

   </td>
   <td>
    <a/>

   </td>
   <td>
    <a/>

   </td>
   <td>
    <a/>

   </td>
   <td>
    <a/>

   </td>
   <td>
    <a/>

   </td>
   <td>
    <a/>

   </td>
   <td>
   

   </td>
   <td>
    <a/>

   </td>
   <td>
   

   </td>
   <td>
    <a/>

   </td>
  </tr>
 </tbody>
</table>
```

## Use Firebug and JQuery to Trace Problems in Tellurium Tests ##

### Firebug Support ###

To add Firebug support, one way is to install the Firebug plugin to your web browser. You can get Firebug from

https://addons.mozilla.org/en-US/firefox/addon/1843

Then, use the Firefox profile in your Tellurium Tests. For example, you can add the Firefox profile in TelluriumConfig.groovy as follows,

```
tellurium{
    //embedded selenium server configuration
    embeddedserver {
        //port number
        port = "4444"
        //whether to use multiple windows
        useMultiWindows = false
        //whether to run the embedded selenium server. If false, you need to manually set up a selenium server
        runInternally = true
        //profile location
        profile = "/home/jfang/.mozilla/firefox/zlduhghq.test"
        //user-extension.js file
        userExtension = "target/test-classes/extension/user-extensions.js"
    }
```

Or you can use the following command to specify the profile if you run the Selenium server externally,

```
[jfang@Mars ]$ java -jar selenium-server.jar -profilesLocation /home/jfang/.mozilla/firefox/zlduhghq.test
```

But sometimes, Selenium server has trouble to create a new profile from your profile and it might be better to add the Firebug plugin directly to the Selenium server. To do this, you need to following the following steps.

First, unpack the custom Selenium server

```
[jfang@Mars ]$ jar xvf selenium-server.jar
```

You will see all the files and directories listed as follows

```
[jfang@Mars Mars]$ ls -l
-rw-rw-r--. 1 jfang jfang    1677 2009-06-09 12:59 coding-conventions.txt
drwxrwxr-x. 6 jfang jfang    4096 2009-06-17 18:41 core
drwxrwxr-x. 3 jfang jfang    4096 2009-06-17 18:41 customProfileDirCUSTFF
drwxrwxr-x. 3 jfang jfang    4096 2009-08-14 16:58 customProfileDirCUSTFFCHROME
drwxrwxr-x. 3 jfang jfang    4096 2009-06-17 18:41 cybervillains
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 doctool
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 hudsuckr
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 images
-rw-rw-r--. 1 jfang jfang    1933 2009-06-09 12:59 index.html
-rw-rw-r--. 1 jfang jfang     620 2009-06-09 12:59 install-readme.txt
drwxrwxr-x. 3 jfang jfang    4096 2009-06-17 18:41 javax
drwxrwxr-x. 6 jfang jfang    4096 2009-06-17 18:41 jsunit
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 killableprocess
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 konqueror
drwxrwxr-x. 3 jfang jfang    4096 2009-06-17 18:41 META-INF
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 opera
drwxrwxr-x. 6 jfang jfang    4096 2009-06-17 18:41 org
-rw-rw-r--. 1 jfang jfang    2020 2009-06-09 12:59 readyState.xpi
-rw-rw-r--. 1 jfang jfang  129458 2009-06-09 12:59 reference.html
-rw-rw-r--. 1 jfang jfang      55 2009-06-12 15:12 selenium-ant.properties
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 sslSupport
drwxrwxr-x. 2 jfang jfang    4096 2009-06-17 18:41 strands
drwxrwxr-x. 5 jfang jfang    4096 2009-06-17 18:41 tests
drwxrwxr-x. 3 jfang jfang    4096 2009-06-17 18:41 unittest
-rw-rw-r--. 1 jfang jfang     153 2009-06-12 15:14 VERSION.txt
```

Then, copy your Firebug installed in your Firefox profile to the profiles in Selenium Server.

```
[jfang@Mars Mars]$ cp -rf /home/jfang/.mozilla/firefox/zlduhghq.test/extensions/firebug\@software.joehewitt.com customProfileDirCUSTFF/extensions/

[jfang@Mars Mars]$ cp -rf /home/jfang/.mozilla/firefox/zlduhghq.test/extensions/firebug\@software.joehewitt.com customProfileDirCUSTFFCHROME/extensions/
```

After that, re-pack the custom Selenium server

```
jar cmf META-INF/MANIFEST.MF selenium-server.jar *
```

Fortunately, you don't need to repeat the above step any more, we provide a custom Selenium server with Firebug support in our Maven repository. You should access it by using the following Maven dependency,

```
        <dependency>
            <groupId>org.openqa.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>1.0.1-tf</version>
          </dependency>
        </dependencies>
```

Of course, you need to specify our Maven repository in your settings.xml or your pom file.

```
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
```

### Debug JavaScript Using Firebug ###

First, you need to put a breakpoint in your Java/Groovy test code, for example, we put a breakpoint on the UserTestCase class,


http://tellurium-users.googlegroups.com/web/TelluriumUserTestCase.png?gda=aK5n30sAAACvY3VTaWrtpkaxlyj9o09Eze2zIhq6411I_8TGzz5DI-qW4SmLmVbvXBbEmeIsaNidYZYxwa0BE4XjboRU-oYaBkXa90K8pT5MNmkW1w_4BQ&gsc=EAymlwsAAABsf4AHuALmOmkLNHG2feRw

Then, use the "Debug" menu to start your test case. Once the test reaches breakpoint, you can go to the Firefox browser to open Firebug.

http://tellurium-users.googlegroups.com/web/TelluriumOpenFirebug.png?gda=ixZrO0oAAACvY3VTaWrtpkaxlyj9o09EDO6MGplfJGHVhUec0kpXPoiAcPDq6RaxFOV-k14EkLyB_EtY4PNGNRy1KtA3K9bL_e3Wg0GnqfdKOwDqUih1tA&gsc=hGoVDAsAAAB8vRO2zTlOouy3DcrgOZrb

Sometime, the Firebug console is disabled by default, you need to enable it. After that, you can select the JavaScript files including those from Selenium core from the Javascripts menu in Firebug.

http://tellurium-users.googlegroups.com/web/TelluriumFirebugScript.png?gda=4-oX70wAAACvY3VTaWrtpkaxlyj9o09E7NkZ8HCattGn-1iiIrkpLwyVYbbBb4wvSnrgfyj7FfY29mST-NKidxSrAhXCh25v_Vpvmo5s1aABVJRO3P3wLQ&gsc=hGoVDAsAAAB8vRO2zTlOouy3DcrgOZrb

You can set a breakpoint in the JavaScript file and resume the test until it hits the breakpoint in the JavaScript file. You can find more details on how to debug Javascript from [Firebug JavaScript debugging](http://getfirebug.com/js.html).

### Trace Problems Using jQuery ###

The custom Selenium server is bundled with jQuery 1.3.2 when we added support for jQuery selector in Tellurium. We yielded the "$" sign and also renamed _jQuery_ to _teJQuery_ to avoid conflicts with user's jQuery library.

To use jQuery, you need to use the single window module for the custom Selenium server, i.e., change settings in TelluriumConfig.groovy to

```
        //whether to use multiple windows
        useMultiWindows = false
```

If you run the Selenium server externally, you should use the following command to start it in a single window mode,

```
java -jar selenium-server.jar -singlewindow
```

Similarly, you need to set a breakpoint in your Java/Groovy test code so that you can work on the Firefox browser using Firebug when the test suspends.

If you open Firebug and look at the html content, you will see that your web application is actually running inside an IFrame in Selenium server shown as follows,

http://tellurium-users.googlegroups.com/web/TelluriumSeleniumServerIFrame.png?gda=PVoZHlMAAACvY3VTaWrtpkaxlyj9o09EgO6a7zKvb8te6WdvjGDohVyd-tnS0xlr1YxP__WLUaGnQLDSh5D6u11BZBWKcByUMrYifh3RmGHD4v9PaZfDexVi73jmlo822J6Z5KZsXFo&gsc=n_OfJwsAAACQg_IcbRBLWbV5y8FhKpga

To access elements in the IFrame using jQuery, you need to use the following trick

```
teJQuery("#selenium_myiframe").contents().find(YOUR_JQUERY)
```

For example, we use the following jQuery to check if a button is there

```
teJQuery("#selenium_myiframe").contents().find("input.btn")
```

We can also dump out the html source starting from the button's parent,

```

teJQuery("#selenium_myiframe").contents().find("input.btn").parent().html()
```

The output is shown as follows,

http://tellurium-users.googlegroups.com/web/TelluriumJQueryIframe.png?gda=W0CtWEsAAACvY3VTaWrtpkaxlyj9o09Ei2DGeSsGpuBT4mNQYpRc0uTSq-Pi7IIn7mvi-lgcznBE4WwyXqDrOCG6YsJPv3PQBkXa90K8pT5MNmkW1w_4BQ&gsc=n_OfJwsAAACQg_IcbRBLWbV5y8FhKpga

Thanks to Dominic. For multiple window mode, you can use the following way to find an element.

```
teJQuery(selenium.browserbot.getCurrentWindow().document).find("#username")
```

## Custom jQuery selectors and Plugins in Tellurium ##

Tellurium leverages jQuery to add [jQuery selector](http://code.google.com/p/aost/wiki/TelluriumjQuerySelector) as a new locator to improve the test speed in IE and add other new functionalities to Selenium core, for example, fetching bulk data in one method call and [the diagonse utility](http://code.google.com/p/aost/wiki/TelluriumPowerUtilityDiagnose).

With the adoption of jQuery, we also need some custom jQuery selectors and plugins to meet our needs.

To design jQuery custom selectors, we need to understand the jQuery selector syntax:

```
$.expr[':'].selector_name = function(obj, index, meta, stack){
......
}
```

where
  * _obj_: a current DOM element
  * _index_: the current loop index in stack
  * _meta_: meta data about your selector
  * _stack_: stack of all elements to loop

The above function returns true to include current element and returns false to exclude current element. A more detailed explanation could be found from [jQuery Custom Selectors with Parameters](http://jquery-howto.blogspot.com/2009/06/jquery-custom-selectors-with-parameters.html).

To avoid conflicts with user's jQuery library, we yield the "$" symbol and rename jQuery to teJQuery in Tellurium.

### Custom jQuery Selectors ###

#### :te\_text ####

The _:te\_text_ selector is created to select a UI element whose text attribute is a given string. The implementation is simple,

```
teJQuery.extend(teJQuery.expr[':'], {
    te_text: function(a, i, m) {
        return teJQuery.trim(teJQuery(a).text()) === teJQuery.trim(m[3]);
    }
});
```

You may wonder why we use _`m[3]`_ here, the variable _m_ includes the following parameters

  * _`m[0]`_: `te_text(argument)` full selector
  * _`m[1]`_: `te_text` selector name
  * _`m[2]`_: `''` quotes used
  * _`m[3]`_: `argument` parameters

As a result, the selector picks up the elements whose text attribute, obtained by `text()`, is equal to the passed in parameter _`m[3]`_.

#### :group ####

The _:group_ selector is used to implement [the group locating](http://code.google.com/p/aost/wiki/UserGuide#Group_Locating) in Tellurium. For example, we want to select a "div" whose children include one "input", one "img", and one "span" tags. How to express this using jQuery?

One way is to use the following selector,

```
teJQuery.expr[':'].group = function(obj){
      var $this = teJQuery(obj);
      return ($this.find("input").length > 0) && ($this.find("img").length > 0) && ($this.find("span").length > 0);
};
```

That is to say, only a DOM node satisfying all the three conditions, i.e, whose children include "input", "img", and "span", is selected because the AND conditions. Remember, only the node that returns true for the above function is selected.

However, in real world, we may have many conditions and we cannot use this hard-coded style selector and we need to use the custom selector with parameters instead. Here is our implementation,

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

If we use firebug to debug the code by running the following jQuery selector

```
teJQuery("div:group(input, img, span)")
```

We can see the variable _m_ includes the following parameters

  * _`m[0]`_: `group(input, img, span)` full selector
  * _`m[1]`_: `group` selector name
  * _`m[2]`_: `''` quotes used
  * _`m[3]`_: `input, img, span` parameters


#### :styles ####

One user provided us the following UI module,

```
   ui.Container(uid: "Program", clocator: [tag: "div"], group: "true") {
      Div(uid: "label", clocator: [tag: "a", text: "Program"])
      Container(uid: "triggerBox", clocator: [tag: "div"], group: "true") {
        InputBox(uid: "inputBox", clocator: [tag: "input", type: "text", readonly: "true", style: "width: 343px;"], respond: ["click"])
        Image(uid: "trigger", clocator: [tag: "img",  style: "overflow: auto; width: 356px; height: 100px;"], respond: ["click"])
      }
    }
```

Unfortunately, the following generated jQuery selector does not work.

```
 $('div:has(input[type=text][readonly=true][style="width: 343px;"], img[style="overflow: auto; width: 356px;height: 100px;"]) img[style="overflow: auto; width: 356px; height: 100px;"]')
```

We have to use a custom jQuery selector to handle the style attribute as follows,

```
teJQuery.expr[':'].styles = function(obj, index, m){
      var $this = teJQuery(obj);

      var splitted = new Array();
      var fs = m[3].split(/:|;/);
      for(var i=0; i<fs.length; i++){
          var trimed = teJQuery.trim(fs[i]);
          if(trimed.length > 0){
              splitted.push(trimed);
          }
      }

      var result = true;

      var l=0;
      while(l < splitted.length){
         result = result && (teJQuery.trim($this.css(splitted[l])) == splitted[l+1]);
         l=l+2;
      }

      return result;
};
```

The main idea is to split the content of the style attribute into multiple single-css classes, then try to match each css class one by one. This approach may not be the optimal one, but it works.

Then, the new runtime jQuery selector becomes,

```
div:group(a:te_text(Program), div) div:group(input:styles(width: 343px;)[type=text][readonly=true], img:styles(overflow: auto; width: 356px; height: 100px;)) img:styles(overflow: auto; width: 356px; height: 100px;)
```

#### :nextToLast ####

One implemented suggested by Kevin is shown as follows,

```
teJQuery.expr[':'].nextToLast = function(obj, index, m){
    var $this = teJQuery(obj);

    if ($this.index() == $this.siblings().length - 1) {
        return true;
    } else {
        return false;
    }
};
```

and he also suggested [a more efficient implementation](http://www.tentonaxe.com/2010/03/custom-jquery-selectors.html).

```
// this is a selector called nextToLast. its sole purpose is to return the next to last
// element of the array of elements supplied to it.
// the parameters in the function below are as follows;
// obj => the current node being checked
// ind => the index of obj in the array of objects being checked
// prop => the properties passed in with the expression
// node => the array of nodes being checked
teJQuery.expr[':'].nextToLast = function(obj, ind, prop, node){

     // if ind is 2 less than the length of the array of nodes, keep it
     if (ind == node.length-2) {
          return true;
     } else {
          // else, remove the node
          return false;
     }
};
```

### Custom jQuery Plugins ###

#### outerHTML ####

When we worked on [the diagnose utility](http://code.google.com/p/aost/wiki/TelluriumPowerUtilityDiagnose), we were frustrated because we need to get the HTML source of a DOM node, but the `html()` method in jQuery only returns innerHTML. We posted a question to [jQuery group](http://groups.google.com/group/jquery-en) and got the answer,

```
$('<div>').append( $(jQuery_Selector).clone() ).html() 
```

and as suggested by another person, we went further to implement this as a simple jQuery plugin,

```
teJQuery.fn.outerHTML = function() {
    return teJQuery("<div/>").append( teJQuery(this[0]).clone() ).html();
};
```

We made two changes here.

  1. _outerHTML_ is defined as a new property of `jQuery.fn` rather than as a standalone function. This registers the function as a plug-in method.
  1. We use the keyword _this_ as a replacement for the jQuery selector. Within a plug-in method, _this_ refers to the jQuery object that is being acted upon.


## Tellurium Powerful Utility: Diagnose ##

Usually, the main problem that users have in Tellurium is that their UI modules are not defined correctly. As a result, the generated runtime locator is either not unique or cannot be found. Very often, users ask our developers to trace or debug their test code. However, it is a difficult task for our Tellurium developers, too because usually the web application and their full test code are not available to us. It would be more important to provide users some utilities for them to trace/debug their code by themselves instead of relying on our Tellurium developers.

The utility method _diagnose_ is designed for this purpose, which is available in the DslContext class and the method signature is as follows,

```
public void diagnose(String uid)
```

What it actually does is to dump the following information to console,

  1. The number of the matching UI element for the runtime locator corresponding to the _uid_.
  1. The html source for the parent UI object of the UI object _uid_.
  1. The closest matching UI elements in the DOM for the generated locator.
  1. The html source for the entire page.

Most of the above are optional, and thus, Tellurium provides you three more methods for your convenience.

```
public DiagnosisResponse getDiagnosisResult(String uid)
public void diagnose(String uid, DiagnosisOption options)
public DiagnosisResponse getDiagnosisResult(String uid, DiagnosisOption options)
```

where DiagnosisResponse is defined as

```
public class DiagnosisResponse {
  private String uid;

  private int count;

  private ArrayList<String> matches;

  private ArrayList<String> parents;

  private ArrayList<String> closest;

  private String html;
}
```

so that you can process the result programmatically.

DiagnosisOption is used to configure the return result,

```
public class DiagnosisOption {

  boolean retMatch = true;

  boolean retHtml = true;

  boolean retParent = true;

  boolean retClosest = true;
}
```


### Implementation ###

Under the hood, Tellurium core first creates a request for the diagnose call,

```
public class DiagnosisRequest {
  //uid for the UI object
  private String uid;

  //parent UI object's locator
  private String pLocator;

  //UI objects attributes obtaining from the composite locator
  private Map<String, String> attributes;

  //options for the return results
  private boolean retMatch;
  
  private boolean retHtml;

  private boolean retParent;

  private boolean retClosest;
```

The request is then converted into a JSON string so that we can pass the request to Selenium as a custom method,

```
class CustomSelenium extends DefaultSelenium {
    ......

    public String diagnose(String locator, String request){
		String[] arr = [locator, request];
		String st = commandProcessor.doCommand("getDiagnosisResponse", arr);
		return st;
    }
}
```

The custom Selenium server includes [our jQuery selector](http://code.google.com/p/aost/wiki/TelluriumjQuerySelector) support. We add the following new Selenium method,

```
Selenium.prototype.getDiagnosisResponse = function(locator, req){
......
}
```

I wouldn't go over the implementation details for this method and you can read the source code on Tellurium Engine project if you are really interested.

### Usage ###

Assume we have the following Tellurium UI module defined

```
public class ProgramModule extends DslContext {

    public static String HTML_BODY = """
<div id="ext-gen437" class="x-form-item" tabindex="-1">
    <label class="x-form-item-label" style="width: 125px;" for="ext-comp-1043">
        <a class="help-tip-link" onclick="openTip('Program','program');return false;" title="click for more info" href="http://localhost:8080">Program</a>
    </label>

    <div id="x-form-el-ext-comp-1043" class="x-form-element" style="padding-left: 130px;">
        <div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" type="hidden" name="programId" value=""/>
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" type="text" autocomplete="off"
                   size="24" readonly="true" style="width: 343px;"/>
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif"/>
        </div>
    </div>
    <div class="x-form-clear-left"/>
</div>
    """

  public void defineUi() {
    ui.Container(uid: "Program", clocator: [tag: "div"], group: "true") {
      Div(uid: "label", clocator: [tag: "a", text: "Program"])
      Container(uid: "triggerBox", clocator: [tag: "div"], group: "true") {
        InputBox(uid: "inputBox", clocator: [tag: "input", type: "text", readonly: "true"], respond: ["click"])
        Image(uid: "trigger", clocator: [tag: "img", src: "*images/s.gif"], respond: ["click"])
      }
    }
  }
}
```

We create a Tellurium test case using the [MockHttpServer](http://code.google.com/p/aost/wiki/TelluriumMockHttpServer) without running an actual web application.

```
public class ProgramModuleTestCase extends TelluriumJavaTestCase{
    private static MockHttpServer server;

    @BeforeClass
    public static void setUp(){
        server = new MockHttpServer(8080);
        server.registerHtmlBody("/program.html", ProgramModule.HTML_BODY);
        server.start();
    }

    @Test
    public void testGetSeparatorAttribute(){
        ProgramModule pm = new ProgramModule();
        pm.defineUi();
        pm.useJQuerySelector();
        connectUrl("http://localhost:8080/program.html");
        pm.diagnose("Program.triggerBox.trigger");
        pm.click("Program.triggerBox.trigger");
    }

    @AfterClass
    public static void tearDown(){
        server.stop();
    }
}
```

Note that we want to diagnose the Image UI object "Program.triggerBox.trigger",

```
pm.diagnose("Program.triggerBox.trigger");
```

Run the test and you will see the return result as follows,

```
Diagnosis Result for Program.triggerBox.trigger

-------------------------------------------------------

	Matching count: 1

	Match elements: 

	--- Element 1 ---

<img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">


	Parents: 

	--- Parent 1---

<div id="x-form-el-ext-comp-1043" class="x-form-element" style="padding-left: 130px;">
        <div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" name="programId" value="" type="hidden">
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" autocomplete="off" size="24" readonly="true" style="width: 343px;" type="text">
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
        </div>
    </div>

	--- Parent 2---

<div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" name="programId" value="" type="hidden">
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" autocomplete="off" size="24" readonly="true" style="width: 343px;" type="text">
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
        </div>


	Closest: 

	--- closest element 1---

<img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
HTML Source: 

<head>
    <title>Mock HTTP Server</title>
</head>
<body>
  <div id="ext-gen437" class="x-form-item" tabindex="-1">
    <label class="x-form-item-label" style="width: 125px;" for="ext-comp-1043">
        <a class="help-tip-link" onclick="openTip('Program','program');return false;" title="click for more info" href="http://localhost:8080">Program</a>
    </label>

    <div id="x-form-el-ext-comp-1043" class="x-form-element" style="padding-left: 130px;">
        <div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" name="programId" value="" type="hidden">
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" autocomplete="off" size="24" readonly="true" style="width: 343px;" type="text">
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
        </div>
    </div>
    <div class="x-form-clear-left">
    </div>
  </div>
</body>

-------------------------------------------------------
```

This is really the happy path and runtime locator is found and is unique. What if the UI module definition is a bit wrong about the Image object?

```
   Image(uid: "trigger", clocator: [tag: "img", src: "*image/s.gif"], respond:["click"])
```

That is to say, the _src_ attribute is not correct.

Run the same test code and the result is as follows,

```
Diagnosis Result for Program.triggerBox.trigger

-------------------------------------------------------

	Matching count: 0


	Parents: 

	--- Parent 1---

<div id="x-form-el-ext-comp-1043" class="x-form-element" style="padding-left: 130px;">
        <div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" name="programId" value="" type="hidden">
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" autocomplete="off" size="24" readonly="true" style="width: 343px;" type="text">
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
        </div>
    </div>

	--- Parent 2---

<div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" name="programId" value="" type="hidden">
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" autocomplete="off" size="24" readonly="true" style="width: 343px;" type="text">
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
        </div>


	Closest: 

	--- closest element 1---

<img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">

HTML Source: 

<head>
    <title>Mock HTTP Server</title>
</head>
<body>
  <div id="ext-gen437" class="x-form-item" tabindex="-1">
    <label class="x-form-item-label" style="width: 125px;" for="ext-comp-1043">
        <a class="help-tip-link" onclick="openTip('Program','program');return false;" title="click for more info" href="http://localhost:8080">Program</a>
    </label>

    <div id="x-form-el-ext-comp-1043" class="x-form-element" style="padding-left: 130px;">
        <div id="ext-gen438" class="x-form-field-wrap" style="width: 360px;">
            <input id="programId" name="programId" value="" type="hidden">
            <input id="ext-comp-1043" class="x-form-text x-form-field x-combo-noedit" autocomplete="off" size="24" readonly="true" style="width: 343px;" type="text">
            <img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
        </div>
    </div>
    <div class="x-form-clear-left">
    </div>
  </div>
</body>

-------------------------------------------------------
```

You can see that there is no matching elements for the runtime locator. But the good thing is that the diagnose method provides you the closest UI elements it can find from the DOM,

```
	--- closest element 1---

<img id="ext-gen439" class="x-form-trigger x-form-arrow-trigger" src="images/s.gif">
```

By looking at this above lines, we could realize that the _src_ attribute is wrong in our UI module.

Some careful readers may want to ask "why you add a partial matching symbol `*` to the src attribute in the UI module. The reason is that in jQuery, seems the _src_ attribute in an Image has to be a full URL such as http://code.google.com/p/aost/. One workaround is to put the partial matching symbol `*` before the URL.

In some case, the return matching count is larger than 1 and you can figure out how to update your UI module definition by looking at all the return elements and their parents.

## Internationalization support in Tellurium ##

Tellurium now provides support for internationalization of strings and exception messages. Any software system should have support for regional language settings and options to be effective. Internationalization and localization provides this support. Locales define the language and region. Locales can define how region specific data is presented to users. Every locale will have a language code followed by a region code. Ex: fr\_FR represents french language in the region of France. Internationalized strings for each locale is provided through a MessageBundle engineered for a specific locale which is of the format `<MessageBundleName>_<language-code>_<country code>.properties`

### Internationalization support in Tellurium ###

The Internationalization support in Tellurium is provided through the InternationalizationManager class. The default bundle used in Tellurium is the DefaultMessagesBundle.properties. All strings and exception messages used in the tellurium core classes are read in from the DefaultMessageBundle properties file.

In order to configure regional messages, This class has a translate that provides Internationalization support.


For plain strings
```
translate( "<key>") 
```
For Strings with parameters
```
translate("<key>" , { [ item1 , item2 , … , item n]}
```
For double numeric value
```
translate(<doubleValue> , false)
```
For currency data
```
translate(<doubleValue> , true)
```
For Dates
```
translate(<dateValue> , false)
```
For time
```
translate(<timeValue> , true)
```

The `translate(<key>)` method signature internationalizes a simple string. The `translate(<key> , { [ item1 , item2 , … , item n]}` method definition allows parameterization of an internationalized string to allow external strings/arguments as parameter to the string.

The localization can be defined in two ways
# setting the locale on your system preferences / settings. (ex: regional settings in Windows machine). This is the preferred and a better way of setting the locale
# adding an i18n section similar to below

```
i18n{
        locales = "en_EN"
    }
```

### Internationalization extension to user defined tests ###

Internationalization support has been extended to test cases, so any user defined test case can use
```
 geti18nManager() 
```

to utilize the translate function support in their own test code. Internationalized strings can be added to user defined MessageBundles defined in the src/main/resources folder of user defined projects. The general steps to provide internationalization in your project are as follows:

1. Create a user defined MessageBundle.properties, a default locale message bundle, as well as one for each region you want to provide support for in your project, ex: MessageBundle\_fr\_FR.properties will have strings translated into french

2. Add the user defined resource bundle using the geti18nManager function, like so: `getI18nManager().addResourceBundle("MessageBundle")`

3. Now use the `translate` function to internationalize strings

### Simple Example ###

Here is a simple example of code from a GoogleBooksListGroovyTestCase. I assume that user has already defined a MessagesBundle.properties,located at src/main/resources, as follows

MessagesBundle.properties
```
GoogleBooksListGroovyTestCase.SetUpModule=Setting up google book list
GoogleBooksListGroovyTestCase.Category=Category is {0}
GoogleBooksListGroovyTestCase.ConnectSeleniumServer=Connection to selenium server
```

Now defining the same properties file in French

MessageBundle\_fr\_FR.properties
```
GoogleBooksListGroovyTestCase.SetUpModule=Liste de livre de google d'établissement
GoogleBooksListGroovyTestCase.Category=La catégorie est {0}
GoogleBooksListGroovyTestCase.ConnectSeleniumServer=Se relier au serveur de sélénium
```

Here is the definition of a testCase that uses the Internationalization support
```
class SampleGroovyTestCase extends TelluriumGroovyTestCase {

    public void initUi() {
    }

    public void setUp(){
        setUpForClass()
        //adding the local resource bundle, make sure it's not titled "DefaultMessagesBundle" since
        //this will overwrite the default one we use in Tellurium core and cause exceptions
        geti18nManager().addResourceBundle("MessagesBundle")
        
        //geti18nManager() can also be replaced by 
        //manager = new InternationalizationManager(), where manager is a class level variable of type
       //InternationalizationManager

    }

    public void tearDown(){
        tearDownForClass()
    }

    public void testTranslateWithEnglishLocale()
    {
       //translating of strings
       String message = geti18nManager().translate("i18nManager.testString")
       assertEquals("This is a testString in English", message)

       //translation of number data types
       Double amount = new Double(345987.246);
       String translatedValue = geti18nManager().translate(amount, false)
       assertEquals("345,987.246" , translatedValue)

       //translation of currency data types
       amount = new Double(9876543.21);
       translatedValue = geti18nManager().translate(amount, true)
       assertEquals("\$9,876,543.21" , translatedValue)

       //translation of dates - date is 2009, Jan 1
       Date date = new Date(109 , 0 , 1)
       translatedValue = geti18nManager().translate(date, false)
       assertEquals("Jan 1, 2009" , translatedValue)
     }
}
```

## Better Reporting With ReportNG ##

ReportNG is a simple HTML reporting plug-in for the TestNG framework. It is intended as a replacement for the default TestNG HTML report. The default report is comprehensive but is not so easy to understand at-a-glance. ReportNG provides a simple, colour-coded view of the test results. You can find more information about ReportNG on the following URL. [ReportNG](https://reportng.dev.java.net/)

TestNG reference project supports generating reports using ReportNG out of the box. To use the reporting plug-in, set the listeners attribute of the testng element in your suite file. ReportNG provides following TestNG listeners.

```
org.uncommons.reportng.HTMLReporter
org.uncommons.reportng.JUnitXMLReporter
```

We are going to use HTMLReporter in the following example. We have a test suite file as shown below.


```
<!DOCTYPE suite SYSTEM "http://beust.com/testng/testng-1.0.dtd" >
<suite name="Google Code Tests">
   <listeners>
        <listener class-name="org.uncommons.reportng.HTMLReporter"/>
    </listeners>

	<test name="Downloads Page">
        <classes>
            <class name="org.tellurium.test.TelluriumDownloadsPageTestNGTestCase"></class>
        </classes>
	</test>

 	<test name="Project Page">
         <classes>
             <class name="org.tellurium.test.TelluriumProjectPageTestNGTestCase"></class>
         </classes>
	</test>

 	<test name="Wiki Page">
         <classes>
             <class name="org.tellurium.test.TelluriumWikiPageTestNGTestCase"></class>
         </classes>
	</test>
</suite>
```

Now you can run the tests either by Maven, ANT or any IDE using this TestNG suite file.

When the test run is complete, you will get a report as shown below.

http://tellurium-users.googlegroups.com/web/tellurium_reportng.png?gsc=4yz_OwsAAABERwLPC1sClZrHewT3tcR5

You can see the test report is more readable and comprehensive.

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