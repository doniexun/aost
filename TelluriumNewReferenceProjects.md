

# Introduction #

Tellurium 0.7.0 merged the original tellurium-junit-java and tellurium-testng-java two reference projects into a new reference project tellurium-website and add a new reference project ui-examples. The first one is real world tellurium tests using tellurium website as an example and the second one uses html source, mostly contributed by Tellurium users, to demonstrate the usage of different Tellurium UI objects.

# Tellurium Website Project #

The tellurium website project illustrates the following usages of Tellurium:

  * How to create your own UI Objects and wire them into Tellurium core
  * How to create UI module files in Groovy
  * How to create JUnit or TestNG tellurium testing files in Java
  * How to create and run DSL scripts
  * How to create Tellurium Data Driven tests

## Create Custom UI objects ##

Tellurium supports custom UI objects defined by users. For most UI objects, they must extend the "UiObject" class and then define actions or methods they support. For container type UI objects, i.e., which hold other UI objects, they should extends the Container class. Please see Tellurium Table and List objects for more details.

In the tellurium-website project, we defined the custom UI object "SelectMenu" as follows:

```
class SelectMenu extends UiObject{   
    public static final String TAG = "div"

    String header = null

    //map to hold the alias name for the menu item in the format of "alias name" : "menu item"
    Map<String, String> aliasMap

    def click(Closure c){
        c(null)
    }

    def mouseOver(Closure c){
        c(null)
    }

    def mouseOut(Closure c){
        c(null)
    }

    public void addTitle(String header){
        this.header = header
    }
    
    public void addMenuItems(Map<String, String> menuItems){
        aliasMap = menuItems
    }
   
    ......

}
```

For each UI object, you must define the builder so that Tellurium knows how to construct the UI object when it parses the UI modules. For example, we define the builder for the "SelectMenu" object as follows:

```
class SelectMenuBuilder extends UiObjectBuilder{
    static final String ITEMS = "items"
    static final String TITLE = "title"

    public build(Map map, Closure c) {
        def df = [:]
        df.put(TAG, SelectMenu.TAG)
        SelectMenu menu = this.internBuild(new SelectMenu(), map, df)
        Map<String, String> items = map.get(ITEMS)
        if(items != null && items.size() > 0){
           menu.addMenuItems(items) 
        }
        
        menu.addTitle(map.get(TITLE))
        
        return menu
    }
}
```

You may wonder how to hook the custom objects into Tellurium core so that it can recognize the new type. The answer is simple, you just add the UI object name and its builder class name to Tellurium configuration file TelluriumConfig.groovy. Update the following section:

```
    uiobject{
        builder{
           SelectMenu="org.telluriumsource.ui.builder.SelectMenuBuilder"
        }
    }
```

## Create UI modules ##

You should create UI modules in Groovy files, which should extend the DslContext class. In the defineUi method, define your UIs and then define all methods for them. Take the Tellurium Downloads page as an example:

```
class TelluriumDownloadsPage extends DslContext{

   public void defineUi() {

       //define UI module of a form include download type selector and download search
       ui.Form(uid: "downloadSearch", clocator: [action: "list", method: "GET"], group: "true") {
           Selector(uid: "downloadType", clocator: [name: "can", id: "can"])
           TextBox(uid: "searchLabel", clocator: [tag: "span", text: "for"])
           InputBox(uid: "searchBox", clocator: [type: "text", name: "q"])
           SubmitButton(uid: "searchButton", clocator: [value: "Search"])
       }

       ui.Table(uid: "downloadResult", clocator: [id: "resultstable", class: "results"], group: "true"){
           //define table header
           //for the border column
           TextBox(uid: "{header: 1}", clocator: [:])
           UrlLink(uid: "{header: 2}", clocator: [text: "*Filename"])
           UrlLink(uid: "{header: 3}", clocator: [text: "*Summary + Labels"])
           UrlLink(uid: "{header: 4}", clocator: [text: "*Uploaded"])
           UrlLink(uid: "{header: 5}", clocator: [text: "*Size"])

           UrlLink(uid: "{header: 6}", clocator: [text: "*DownloadCount"])
           UrlLink(uid: "{header: 7}", clocator: [text: "*..."])

           //define table elements
           //for the border column
           TextBox(uid: "{row: all, column: 1}", clocator: [:])
         
           //the summary + labels column consists of a list of UrlLinks
          List(uid: "{row: all, column: 3}"){
               UrlLink(uid: "{all}", clocator: [:])
           }
           //For the rest, just UrlLink
           UrlLink(uid: "{all}", clocator: [:])
       }

       ui.RadioButton(uid: "test", clocator: [:], respond: "click")
   }

    public String[] getAllDownloadTypes(){
        return  getSelectOptions("downloadSearch.downloadType")
    }

    public String getCurrentDownloadType(){
        return  getSelectedLabel("downloadSearch.downloadType");
    }

    public void selectDownloadType(String type){
        selectByLabel "downloadSearch.downloadType", type
    }

    public void searchDownload(String keyword){
        type "downloadSearch.searchBox", keyword
        click "downloadSearch.searchButton"
        waitForPageToLoad 30000
    }

    public int getTableHeaderNum(){
        enableCache();
        enableTelluriumApi();
        return getTableHeaderColumnNum("downloadResult")
    }

    public List<String> getHeaderNames(){
        enableCache();
        enableTelluriumApi();

        List<String> headernames = new ArrayList<String>()
        int mcolumn = getTableHeaderColumnNum("downloadResult")
        for(int i=1; i<=mcolumn; i++){
            headernames.add(getText("downloadResult.header[${i}]"))
        }

        return headernames
    }

    public List<String> getDownloadFileNames(){

        int mcolumn = getTableMaxRowNum("downloadResult")
        List<String> filenames = new ArrayList<String>()
        for(int i=1; i<=mcolumn; i++){
            filenames.add(getText("downloadResult[${i}][2]").trim())
        }

        return filenames
    }

    public void clickFileNameColumn(int row){
        click "downloadResult[${row}][2]"
        pause 1000
        chooseCancelOnNextConfirmation()
        pause 500
    }

    ...
}
```

## Create Java Test Cases ##

You can create Java Test Cases by extending the TelluriumJUnitTestCase or TelluriumTestNGTestCase class. Nothing special, just like regular JUnit test cases. For instance,

```
public class TelluriumDownloadsPageTestNGTestCase extends TelluriumTestNGTestCase{
    private static TelluriumDownloadsPage downloadPage;

    @BeforeClass
    public static void initUi() {
        downloadPage = new TelluriumDownloadsPage();
        downloadPage.defineUi();
        connectSeleniumServer();
        useCache(true);
    }

    @BeforeMethod
    public void setUpForMethod(){
        connectUrl("http://code.google.com/p/aost/downloads/list");
    }

    @Test
    public void testValidate(){
        downloadPage.validate("downloadResult");    
    }

    @Test
    public void testDownloadTypes(){
        String[] allTypes = downloadPage.getAllDownloadTypes();
        assertNotNull(allTypes);
        assertTrue(allTypes[1].contains("All downloads"));
        assertTrue(allTypes[2].contains("Featured downloads"));
        assertTrue(allTypes[3].contains("Current downloads"));
        assertTrue(allTypes[4].contains("Deprecated downloads"));
    }

    @Test
    public void testDefaultDownloadType(){
        // Set download type with other value
        downloadPage.selectDownloadType(" All downloads");

        // Navigate away from download page
        connectUrl("http://code.google.com/p/aost/downloads/list");
        String defaultType = downloadPage.getCurrentDownloadType();
        assertNotNull(defaultType);
        assertTrue(defaultType.contains("Current downloads"));
    }

    @Test
    public void testSearchByText(){
        // Set download type with other value
        downloadPage.selectDownloadType(" All downloads");
        downloadPage.searchDownload("Tellurium-0.6.0");

        useTelluriumApi(true);
        List<String> list = downloadPage.getDownloadFileNames();
        useTelluriumApi(false);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(Helper.include(list, "tellurium-core.0.6.0.tar.gz"));
    }

    @Test
    public void testSearchByLabel(){
        // Set download type with other value
        downloadPage.selectDownloadType(" All downloads");
        downloadPage.searchDownload("label:Featured");

        useTelluriumApi(true);
        List<String> list = downloadPage.getDownloadFileNames();
        useTelluriumApi(false);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testDownloadFileNames(){
        int mcolumn = downloadPage.getTableHeaderNum();
        assertEquals(7, mcolumn);
        List<String> list = downloadPage.getHeaderNames();
        assertNotNull(list);
        assertEquals(7, list.size());
        assertTrue(Helper.include(list, "Filename"));
        list = downloadPage.getDownloadFileNames();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(Helper.include(list, "tellurium-core.0.6.0.tar.gz"));
    }

    ......

}
```

## Create and Run DSL scripts ##

In Tellurium, you can create test scripts in pure DSL. Take TelluriumPage.dsl as an example,

```
//define Tellurium project menu
ui.Container(uid: "menu", clocator: [tag: "table", id: "mt", trailer: "/tbody/tr/th"], group: "true"){
    //since the actual text is  Project&nbsp;Home, we can use partial match here. Note "*" stands for partial match
    UrlLink(uid: "project_home", clocator: [text: "*Home"])
    UrlLink(uid: "downloads", clocator: [text: "Downloads"])
    UrlLink(uid: "wiki", clocator: [text: "Wiki"])
    UrlLink(uid: "issues", clocator: [text: "Issues"])
    UrlLink(uid: "source", clocator: [text: "Source"])
}

//define the Tellurium project search module, which includes an input box, two search buttons
ui.Form(uid: "search", clocator: [:], group: "true"){
    InputBox(uid: "searchbox", clocator: [name: "q"])
    SubmitButton(uid: "search_project_button", clocator: [value: "Search projects"])
}

openUrl "http://code.google.com/p/aost/"
click "menu.project_home"
waitForPageToLoad 30000
click "menu.downloads"
waitForPageToLoad 30000
click "menu.wiki"
waitForPageToLoad 30000
click "menu.issues"
waitForPageToLoad 30000

openUrl "http://code.google.com/p/aost/"
type "search.searchbox", "Tellurium Selenium groovy"
click "search.search_project_button"
waitForPageToLoad 30000
```

To run the DSL script, you should run the code with Maven

```
mvn test
```

Then, use the rundsl.sh to run the DSL script

```
./rundsl.sh src/test/resources/org/telluriumsource/dsl/TelluriumPage.dsl
```

For Windows, you should use the rundsl.bat script.

In addition, Tellurium provides a new rundsl.groovy script using the Groovy grape feature.

```
import groovy.grape.Grape;

Grape.grab(group:'org.telluriumsource', module:'tellurium-core', version:'0.7.0-SNAPSHOT', classLoader:this.class.classLoader.rootLoader)
Grape.grab(group:'org.stringtree', module:'stringtree-json', version:'2.0.10', classLoader:this.class.classLoader.rootLoader)
Grape.grab(group:'caja', module:'json_simple', version:'r1', classLoader:this.class.classLoader.rootLoader)
Grape.grab(group:'org.seleniumhq.selenium.server', module:'selenium-server', version:'1.0.1-te3-SNAPSHOT', classLoader:this.class.classLoader.rootLoader)
Grape.grab(group:'org.seleniumhq.selenium.client-drivers', module:'selenium-java-client-driver', version:'1.0.1', classLoader:this.class.classLoader.rootLoader)
Grape.grab(group:'org.apache.poi', module:'poi', version:'3.0.1-FINAL', classLoader:this.class.classLoader.rootLoader)
Grape.grab(group:'junit', module:'junit', version:'4.7', classLoader:this.class.classLoader.rootLoader)

import org.telluriumsource.dsl.DslScriptExecutor

@Grapes([
   @Grab(group='org.codehaus.groovy', module='groovy-all', version='1.7.0'),
   @Grab(group='org.seleniumhq.selenium.server', module='selenium-server', version='1.0.1-te3-SNAPSHOT'),
   @Grab(group='org.seleniumhq.selenium.client-drivers', module='selenium-java-client-driver', version='1.0.1'),
   @Grab(group='junit', module='junit', version='4.7'),
   @Grab(group='caja', module='json_simple', version='r1'),
   @Grab(group='org.apache.poi', module='poi', version='3.0.1-FINAL'),
   @Grab(group='org.stringtree', module='stringtree-json', version='2.0.10'),
   @Grab(group='org.telluriumsource', module='tellurium-core', version='0.7.0-SNAPSHOT')
])

def runDsl(String[] args) {
  def cli = new CliBuilder(usage: 'rundsl.groovy -[hf] [scriptname]')
  cli.with {
    h longOpt: 'help', 'Show usage information'
    f longOpt: 'scriptname',   'DSL script name'
  }
  def options = cli.parse(args)
  if (!options) {
    return
  }
  if (options.h) {
    cli.usage()
    return
  }
  if (options.f) {
    def extraArguments = options.arguments()
    if (extraArguments) {
      extraArguments.each {String name ->
        def input = [name].toArray(new String[0])
        DslScriptExecutor.main(input)
      }
    }
  }

}

println "Running DSL test script, press Ctrl+C to stop."

runDsl(args)

```

Then run

```
groovy rundsl.groovy -f DSL_script_name
```

If you are behind a firewall
```
groovy -Dhttp.proxyHost=proxy_host -Dhttp.proxyPort=proxy_port rundsl.groovy -f DSL_script_name
```

## Data Driven Testing ##

We use Tellurium Issue page as the data driven testing example, we define tests to search issues assigned to a Tellurium team member and use the input file to define which team members we want the result for.

We first define a TelluriumIssuesModule class that extends TelluriumDataDrivenModule class and includes a method "defineModule". In the "defineModule" method, we define UI modules,  input data format, and different tests. The UI modules are the same as defined before for the Tellurium issue page.

The input data format is defined as

```
fs.FieldSet(name: "OpenIssuesPage") {
   Test(value: "OpenTelluriumIssuesPage")
}

fs.FieldSet(name: "IssueForOwner", description: "Data format for test SearchIssueForOwner") {
   Test(value: "SearchIssueForOwner")
   Field(name: "issueType", description: "Issue Type")
   Field(name: "owner", description: "Owner")
}
```

Here we have two different input data formats. The "Test" field defines the test name and the "Field" field define the input data name and description. For example, the input data for the test "SearchIssueForOwner" have two input parameters "issueType" and "owner".

The tests are defined use "defineTest". One of the test "SearchIssueForOwner" is defined as follows,

```
        defineTest("SearchIssueForOwner") {
            String issueType = bind("IssueForOwner.issueType")
            String issueOwner = bind("IssueForOwner.owner")
            int headernum = getCachedVariable("headernum")
            int expectedHeaderNum = getTableHeaderNum()
            compareResult(expectedHeaderNum, headernum)
            
            List<String> headernames = getCachedVariable("headernames")
            String[] issueTypes = getCachedVariable("issuetypes")
            String issueTypeLabel = getIssueTypeLabel(issueTypes, issueType)
            checkResult(issueTypeLabel) {
                assertTrue(issueTypeLabel != null)
            }
            //select issue type
            if (issueTypeLabel != null) {
                selectIssueType(issueTypeLabel)
            }
            //search for all owners
            if ("all".equalsIgnoreCase(issueOwner.trim())) {
                searchForAllIssues()
            } else {
                searchIssue("owner:" + issueOwner)
            }
            ......
        }

    }

```

As you can see, we use "bind" to tie the variable to input data field. For example, the variable "issueType" is bound to "IssueForOwner.issueType", i.e., field "issueType" of the  input Fieldset "IssueForOwner". "getCachedVariable" is used to get variables passed from previous tests and "compareResult" is used to compare the actual result with the expected result.

The input file format looks like

```
OpenTelluriumIssuesPage
## Test Name | Issue Type | Owner
SearchIssueForOwner | Open | all
SearchIssueForOwner | All | matt.senter
SearchIssueForOwner | Open | John.Jian.Fang
SearchIssueForOwner | All |  vivekmongolu
SearchIssueForOwner | All |  haroonzone
```

The actual test class is very simple

```
class TelluriumIssuesDataDrivenTest extends TelluriumDataDrivenTest{


    public void testDataDriven() {

        includeModule org.telluriumsource.ddt.TelluriumIssuesModule.class

        //load file
        loadData "src/test/resources/org/telluriumsource/data/TelluriumIssuesInput.txt"

        useCache(true);
        useClosestMatch(true);
      
        //read each line and run the test script until the end of the file
        stepToEnd()

        //close file
        closeData()

    }
}
```

We first define which Data Driven Module we want to load and then read input data file. After that, we read the input data file line by line and execute the appropriate tests defined in the input file. Finally, close the data file.

The test result looks as follows,

```
<TestResults>
  <Total>6</Total>
  <Succeeded>6</Succeeded>
  <Failed>0</Failed>
  <Test name='OpenTelluriumIssuesPage'>
    <Step>1</Step>
    <Passed>true</Passed>
    <Input>
      <test>OpenTelluriumIssuesPage</test>
    </Input>
    <Assertion Value='10' Passed='true' />
    <Status>PROCEEDED</Status>
    <Runtime>2.579049</Runtime>
  </Test>
  <Test name='SearchIssueForOwner'>
    <Step>2</Step>
    <Passed>true</Passed>
    <Input>
      <test>SearchIssueForOwner</test>
      <issueType>Open</issueType>
      <owner>all</owner>
    </Input>
    <Assertion Expected='10' Actual='10' Passed='true' />
    <Assertion Value=' Open Issues' Passed='true' />
    <Status>PROCEEDED</Status>
    <Runtime>4.118923</Runtime>
    <Message>Found 10  Open Issues for owner all</Message>
    <Message>Issue: Better way to wait or pause during testing</Message>
    <Message>Issue: Add support for JQuery selector</Message>
    <Message>Issue: Export Tellurium to Ruby</Message>
    <Message>Issue: Add check Alter function to Tellurium</Message>
    <Message>Issue: Firefox plugin to automatically generate UI module for users</Message>
    <Message>Issue: Create a prototype for container-like Dojo widgets</Message>
    <Message>Issue: Need to create Wiki page to explain how to setup Maven and use Maven to build multiple projects</Message>
    <Message>Issue: Configure IntelliJ to properly load one maven sub-project and not look for an IntelliJ project dependency</Message>
    <Message>Issue: Support nested properties for Tellurium</Message>
    <Message>Issue: update versions for extensioin dojo-widget and TrUMP projects</Message>
  </Test>
  ...
</TestResults>
```

# Tellurium ui-examples Project #

Some of the html sources in the ui-examples project are contributed by tellurium users. Thanks for their contributions. ui-examples project includes the following different types of UI objects.

## Selector ##

HTML source:

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

UI Module:

```
class SelectorExampleModule extends DslContext{

  public void defineUi(){
    ui.Form(uid: "Form", clocator: [method: "POST", action: "check_phone"]){
      Selector(uid: "Country", clocator: [name: "\$CountryAccessCode"])
      InputBox(uid: "Number", clocator: [name: "\$PhoneNumber"])
      SubmitButton(uid: "check", clocator: [value: "Check"])
    }
  }

  public void check(String country, String number){
    select "Form.Country", country
    keyType "Form.Number", number
    click "Form.check"
    waitForPageToLoad 30000
  }
  
}
```

## Container ##

HTML source:

```
<div class="mainMenu">
    <a href="http://localhost:8080/ContainerExample.html">Events</a>
    <a href="http://localhost:8080/ContainerExample.html">Suppliers</a>
    <a href="http://localhost:8080/ContainerExample.html">Venues</a>
    <a href="http://localhost:8080/ContainerExample.html">Booking Report</a>
    <a href="http://localhost:8080/ContainerExample.html">Notifications</a>
    <a href="http://localhost:8080/ContainerExample.html">Help</a>
</div>
```

UI Module:

```
class ContainerExampleModule extends DslContext {

  public void defineUi(){
    
    ui.Container(uid: "mainnav", clocator: [tag: "div", class: "mainMenu"], group: true) {
      UrlLink(uid: "events", clocator: [text: "Events"])
      UrlLink(uid: "suppliers", clocator: [text: "Suppliers"])
      UrlLink(uid: "venues", clocator: [text: "Venues"])
      UrlLink(uid: "bookingReport", clocator: [text: "Booking Report"])
      UrlLink(uid: "notifications", clocator: [text: "Notifications"])
      UrlLink(uid: "help", clocator: [text: "Help"])
    }
  }
}
```

## Form ##

HTML Source:

```
<H1>FORM Authentication demo</H1>

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

UI Module:

```
public class FormExampleModule extends DslContext {

  public void defineUi() {
    ui.Form(uid: "Form", clocator: [tag: "table"]){
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
  }

  public void logon(String username, String password){
    keyType "Form.Username.Input", username
    keyType "Form.Password.Input", password
    click "Form.Submit"
    waitForPageToLoad 30000
  }
}
```

## List ##

HTML Source:

```
<table id="hp_table" cellspacing="0" cellpadding="0">
  <tbody>
    <tr>
     <td class="sidebar" valign="top">
     <DIV class="sub_cat_section">
        <DIV class="sub_cat_title" style="">Fiction</DIV>
        <P><A href="http://books.google.com/books?q=+subject:%22+Literature+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Literature</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Science+Fiction+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Science fiction</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Fantasy+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Fantasy</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Romance+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Romance</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Fiction+/+Mystery+%26+detective+/+General+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Mystery</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Fairy+tales+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Fairy tales</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Short+Stories+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Short stories</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Poetry+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_fict" style="">Poetry</A></P></DIV>
    <DIV class="sub_cat_section">
        <DIV class="sub_cat_title">Non-fiction</DIV>
        <P><A href="http://books.google.com/books?q=+subject:%22+Philosophy+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Philosophy</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Economics+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Economics</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Political+Science+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Political science</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Linguistics+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Linguistics</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Mathematics+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Mathematics</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Science+/+Physics+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Physics</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Science+/+Chemistry+/+General+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Chemistry</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Science+/+Life+Sciences+/+Biology+/+General+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_nofict">Biology</A>
        </P></DIV>
    <DIV class="sub_cat_section">
        <DIV class="sub_cat_title">Random subjects</DIV>
        <P><A href="http://books.google.com/books?q=+subject:%22+Toys+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Toys</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Solar+houses+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Solar houses</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Stories+in+rhyme+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Stories in rhyme</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Courtship+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Courtship</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Mythology+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Mythology</A>
        </P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Differential+equations+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Differential equations</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Inscriptions,+Latin+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Latin inscriptions</A></P>

        <P><A href="http://books.google.com/books?q=+subject:%22+Nuclear+energy+%22&amp;as_brr=3&amp;rview=1&amp;source=gbs_hplp_subj">Nuclear energy</A></P>
    </DIV>
</td>
</tr>
</tbody>
</table>
```

UI Module:

```
class ListExampleModule extends DslContext {

  public void defineUi() {
    ui.Container(uid: "GoogleBooksList", clocator: [tag: "table", id: "hp_table"]) {
      List(uid: "subcategory", clocator: [tag: "td", class: "sidebar"], separator: "div") {
        Container(uid: "{all}") {
          TextBox(uid: "title", clocator: [tag: "div", class: "sub_cat_title"])
          List(uid: "links", separator: "p") {
            UrlLink(uid: "{all}", clocator: [:])
          }
        }
      }
    }
  }
}
```

## Table ##

HTML Source:

```
<table id="xyz">
    <tbody>
    <tr>
        <th>one</th>
        <th>two</th>
        <th>three</th>
    </tr>
    </tbody>

    <tbody>
    <tr>
        <td>
            <div class="abc">Profile</div>
        </td>
        <td><input class="123"/><br/>

            <div class="someclass">
                <span class="x">Framework</span><br/>
                <a href="http://code.google.com/p/aost">Tellurium</a>
            </div>
        </td>
        <td>
            Hello World!
        </td>
    </tr>

    </tbody>
</table>
```

UI Module:

```
class TableExampleModule extends DslContext {
  public void defineUi() {
    ui.StandardTable(uid: "GT", clocator: [id: "xyz"], ht: "tbody"){
      TextBox(uid: "{header: all}", clocator: [:])
      TextBox(uid: "{row: 1, column: 1}", clocator: [tag: "div", class: "abc"])
      Container(uid: "{row: 1, column: 2}"){
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

## Repeat ##

HTML Source:

```
<form name="selectedSailingsForm">
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

UI Module:

```
class RepeatExampleModule extends DslContext {

  public void defineUi(){

    ui.Form(uid: "SailingForm", clocator: [name: "selectedSailingsForm"] ){
      Repeat(uid: "Section", clocator: [tag: "div", class: "segment clearfix"]){
        Repeat(uid: "Option", clocator: [tag: "div", class: "option", direct: "true"]){
          List(uid: "Fares", clocator: [tag: "ul", class: "fares", direct: "true"], separator: "li"){
            Container(uid: "{all}"){
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
  }
}
```

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