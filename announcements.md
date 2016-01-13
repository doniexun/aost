

# Tellurium Announcements #

## Tellurium UI Model Plugin (TrUMP) 0.1.0 is released ##

We are glad to announce that TrUMP 0.1.0 Release is out and please download it from Tellurium download page at

http://code.google.com/p/aost/downloads/list


The release includes two xpi files, one for Firefox 3 and one for firefox 2. Basically they are the same, the firefox 3 one utilized some advanced features provided by Firefox 3 to display UI.

The main features of TrUMP 0.1.0 include

  1. Record user's clicks to automatically generate UI module
  1. Validate the UI module
  1. Customize the UI module
  1. Export the generated UI module to a groovy file
  1. Logging support
  1. Preference support

The known issues or limitations include

  1. Cannot record popup/dialog window
  1. Does not support Frames
  1. Does not support UI template yet. This feature will be provided in later versions.

The detailed steps on how to install and use TrUMP could be found at,

> http://code.google.com/p/aost/wiki/TrUMP


On the download page, we provide video demos on how to use TrUMP to create UI modules and how to write Tellurium tests using the generated UI module. We also provide a presentation about Tellurium there.

If you are interested in how TrUMP is implemented, you can find the following tutorial,

> http://code.google.com/p/aost/wiki/HowTrUMPWorks


It would be really helpful for us to improve TrUMP if you could try it and post your problems and suggestions to Tellurium user group at

> http://groups.google.com/group/tellurium-users


Thanks in advance,

Tellurium team

## Tellurium 0.6.0 RC1 is out ##

Tellurium 0.6.0 RC1 includes the following modules:

  * Tellurium Core 0.6.0 RC1
  * Tellurium JUnit Reference Project 0.6.0 RC1
  * Tellurium TestNG Reference Project 0.6.0 RC1
  * Tellurium JUnit Maven Archetype 1.0 RC1
  * Tellurium TestNG Maven Archetype 1.0 RC1
  * Customized Selenium Server 1.0-te-2

Tellurium 0.6.0 is a feature rich release and please check [Whats New in Tellurium 0.6.0](http://code.google.com/p/aost/wiki/WhatsNewInTellurium_0_6_0) for a detailed introduction of them. For RC1, please replace 0.6.0 with 0.6.0.RC1 and 1.0 with 1.0.RC1 for the configurations in the wiki page.

The Tellurium 0.6.0 RC1 Code base is at

http://aost.googlecode.com/svn/branches/tellurium-0.6.0-RC01

Please go to Tellurium Download page

http://code.google.com/p/aost/downloads/list

to download the following artifacts

  * tellurium-core.0.6.0.RC1.jar:  Tellurium Core 0.6.0 RC1 jar file
  * tellurium-core.0.6.0.RC1-with-dependencies.jar: Tellurium Core 0.6.0 RC1 jar with Dependencies, one jar file includes all dependent jars
  * reference-junit-0.6.0.RC1.tar.gz: Tellurium 0.6.0 RC1 JUnit Reference Project
  * reference-testng-0.6.0.RC1.tar.gz: Tellurium 0.6.0 RC1 TestNG Reference Project

If you use ant to build project, you can simply include tellurium-core.0.6.0.RC1-with-dependencies.jar in your build path. You can also run the customized selenium server using the following command,

```
 java -jar tellurium-core-0.6.0.RC1-with-dependencies.jar 
```

and use

```
java -cp tellurium-core-0.6.0.RC1-with-dependencies.jar:. org.tellurium.dsl.DslScriptExecutor dsl_script_file
```

to run DSL scripts.

The best way to get Tellurium 0.6.0 RC1 is to use Tellurium Maven Archetypes. For a JUnit project, use the following Maven command

mvn archetype:create -DgroupId=your\_group\_id -DartifactId=your\_artifact\_id \
-DarchetypeArtifactId=tellurium-junit-archetype \
-DarchetypeGroupId=tellurium -DarchetypeVersion=1.0.RC1-SNAPSHOT \
-DarchetypeRepository=http://maven.kungfuters.org/content/repositories/snapshots

and use

mvn archetype:create -DgroupId=your\_group\_id -DartifactId=your\_artifact\_id \
-DarchetypeArtifactId=tellurium-testng-archetype -DarchetypeGroupId=tellurium \
-DarchetypeVersion=1.0.RC1-SNAPSHOT \
-DarchetypeRepository=http://maven.kungfuters.org/content/repositories/snapshots

to create a TestNG project.

If you want to manually add Tellurium 0.6.0 RC1 to your Maven project, please include the following dependencies:

```
      <dependency>
          <groupId>org.openqa.selenium.grid</groupId>
          <artifactId>selenium-grid-tools</artifactId>
          <version>1.0.2</version>
        </dependency> 

        <dependency>
            <groupId>tellurium</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>0.6.0.RC1-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>1.0-te-2</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.client-drivers</groupId>
            <artifactId>selenium-java-client-driver</artifactId>
            <version>1.0-beta-2</version>
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
            <version>1.6.0</version>
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
```

and the following repositories,

```
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
```

Here is the latest Tellurium Configuration file, [TelluriumConfig.groovy](http://code.google.com/p/aost/wiki/TelluriumSampleConfigurationFile).

Please report any problems back to Tellurium User Group at

http://groups.google.com/group/tellurium-users

Thanks,

Tellurium Team

## Tellurium 0.6.0 is Released ##

Tellurium 0.6.0 includes the following modules:

  * Tellurium Core 0.6.0
  * Tellurium JUnit Reference Project 0.6.0
  * Tellurium TestNG Reference Project 0.6.0
  * Tellurium JUnit Maven Archetype 0.6.0
  * Tellurium TestNG Maven Archetype 0.6.0
  * Customized Selenium Server 1.0.1-te

### New Features ###

Tellurium 0.6.0 is a feature rich release and the biggest move from Tellurium core 0.5.0 to 0.6.0 is the addition of jQuery selector support together with jQuery Selector Cache option, Selenium Grid support, New APIs for bulk data retrival and css styles, and numerous other enhancements. Please check [Whats New in Tellurium 0.6.0](http://code.google.com/p/aost/wiki/WhatsNewInTellurium_0_6_0) for a detailed introduction of them.

We customized selenium-core 1.0.1 and embedded our Engine code into it. The Engine mainly provides jQuery selector support, jQuery selector caching, and a set of new APIs.

### How to Obtain Tellurium 0.6.0 ###

For a Maven user, the best way to obtain Tellurium 0.6.0 is to use Tellurium Maven archetypes, which include tellurium-junit-archetype and tellurium-testng-archetype for Tellurium JUnit test project and Tellurium TestNG test project, respectively.

To create a Tellurium JUnit test project, the the following Maven command,

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id -DarchetypeArtifactId=tellurium-junit-archetype -DarchetypeGroupId=tellurium -DarchetypeVersion=0.6.0 -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/releases
```

and for a Tellurium TestNG project, use

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id -DarchetypeArtifactId=tellurium-testng-archetype -DarchetypeGroupId=tellurium -DarchetypeVersion=0.6.0 -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/releases
```

The skeleton project created by the Tellurium Maven archetype type includes a sample test case, GoogleSearchTestCase. You can simply go to the project directory and run the following command to test it,

```
mvn test
```

If you have already had a test project and want to include Tellurium 0.6.0 in, please add the following section to your POM file,

```
   <repositories>
        <repository>
            <id>caja</id>
            <url>http://google-caja.googlecode.com/svn/maven</url>
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
          <groupId>org.openqa.selenium.grid</groupId>
          <artifactId>selenium-grid-tools</artifactId>
          <version>1.0.2</version>
        </dependency> 

        <dependency>
            <groupId>tellurium</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>0.6.0</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>1.0.1-te</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium.client-drivers</groupId>
            <artifactId>selenium-java-client-driver</artifactId>
            <version>1.0.1</version>
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
            <version>1.6.0</version>
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
    </dependencies>
```

Here is a [sample POM file](http://code.google.com/p/aost/wiki/TelluriumTestProjectMavenSamplePom) for a Tellurium project.

For an Ant user, you should download [Tellurium core 0.6.0 jar file](http://aost.googlecode.com/files/tellurium-core-0.6.0.jar) from [Tellurium project download page](http://code.google.com/p/aost/downloads/list), [the Tellurium dependency file](http://aost.googlecode.com/files/tellurium-0.6.0-dependencies.zip), and [the Tellurium configuration file](http://code.google.com/p/aost/wiki/TelluriumSampleConfigurationFile). Unpack the Tellurium dependency file to your project /lib directory together with the Tellurium core 0.6.0 jar file. Name the Tellurium configuration file as TelluriumConfig.groovy and put it at your project root directory.

For Ant build scripts, please see [the sample Tellurium Ant build scripts](http://code.google.com/p/aost/wiki/TelluriumSampleAntBuildScript)

### Tellurium Reference Projects ###

If you are new to Tellurium, [Tellurium reference projects](http://code.google.com/p/aost/wiki/ReferenceProjectGuide) could be your good starting point. There are two reference projects, i.e., [tellurium-junit-java](http://aost.googlecode.com/files/reference-junit.0.6.0.tar.gz) and [tellurium-testng-java](http://aost.googlecode.com/files/reference-testng.0.6.0.tar.gz), where can be downloaded from [Tellurium project download page](http://code.google.com/p/aost/downloads/list).

Basically, the two sub-projects are the same and the only difference is that tellurium-junit-java uses JUnit 4 and tellurium-testng-java uses TestNG.

The reference projects illustrate the following usages of Tellurium:

  * How to create your own Tellurium testing project using tellurium jar files.
  * How to create your own UI Objects and wire them into Tellurium core
  * How to create UI module files in Groovy
  * How to create JUnit tellurium testing files in Java
  * How to create and run DSL scripts
  * How to create Tellurium Data Driven tests
  * How to configure Tellurium with the configuration file TelluriumConfig.groovy
  * Ant build script
  * Maven POM

### Feedback ###

Please report any problems back to Tellurium User Group at

http://groups.google.com/group/tellurium-users

Thanks,

Tellurium Team

## First Draft of the Tellurium User Guide 0.6.0 is Available Now ##

The first draft of [the Tellurium User Guide 0.6.0](http://code.google.com/p/aost/wiki/UserGuide) is available for you to review now. A [PDF version](http://telluriumdoc.googlecode.com/files/TelluriumUserGuide.Draft.pdf) is also available.

The user guide is about 117 pages and introduces the background, main concepts and features, examples, FAQs, and resources. In one words, everything you need to know about Tellurium.

The table of content is listed as follows,

```
    * Introduction
          o Motivations
          o Why Tellurium is a New Approach for Web Testing
          o How Challenges and Problems are addressed in Tellurium
          o Tellurium Architecture
          o HOW Tellurium Works
    * Concepts and Features
          o UI Object
          o Composite Locator
          o UI Module
          o UiID
          o Group Locating
          o UI Templates
          o Javascript Events
          o Logical Container
          o jQuery Selector
          o jQuery Selector Cache Option
          o "Include" Frequently Used Sets of elements in UI Modules
          o Customize Individual Test settings Using setCustomConfig
          o User Custom Selenium Extensions
          o Data Driven Testing
          o The Dump Method
          o Selenium Grid Support
          o Mock Http Server
          o Testing Support
    * Tellurium APIs
          o DSL Methods
          o Data Access Methods
          o Test Support DSLs
    * Tellurium UI Objects
          o Basic UI Object
          o UI Object Default Attributes
          o Tellurium UI Object List
                + Button
                + Submit Button
                + Check Box
                + Div
                + Image
                + Icon
                + Radio Button
                + Text Box
                + Input Box
                + Url Link
                + Selector
                + Container
                + Form
                + Table
                + Standard Table
                + List
                + Simple Menu
                + Select Menu
                + Frame
                + Window
                + Option
    * Tellurium Subprojects
          o Tellurium Engine
          o Tellurium Core
          o Tellurium Extensions
          o Tellurium UI Module Plugin (Trump)
          o Tellurium Maven Archetypes
          o Tellurium Reference Projects
    * How to Obtain and Use Tellurium
          o Create a Tellurium Project
          o Setup Tellurium Project in IDEs
          o Create a UI Module
          o Create Tellurium Test Cases
    * Examples
          o Custom UI Object
          o UI Module
          o Groovy Test Case
          o Java Test Case
          o Data Driven Testing
          o DSL Test Script
    * Frequently Asked Questions
          o How Long Has Tellurium Been There
          o What are the main differences between Tellurium and Selenium
          o Do I need to know Groovy before I use Tellurium
          o What Unit Test and Functional Test Frameworks Does Tellurium Support
          o Does Tellurium Provide Any Tools to Automatically Generate UI Modules
          o What Build System Does Tellurium use
          o What is the Best Way to Create a Tellurium Project
          o Where can I find API documents for Tellurium
          o Is There a Tellurium Tutorial Available
          o Tellurium Dependencies
          o What is the ui. in UI module
          o How do I add my own UI object to Tellurium
          o How to build Tellurium from source
          o What is the problem with Selenium XPath expressions and why there is need to create UI Module
          o How to write assertions in Tellurium DSL scripts
          o How to run Selenium server remotely in Tellurium
          o Differences among Tellurium Table, List, and Container
          o How do I use a Firefox profile in Tellurium
          o How to Overwrite Tellurium Settings in My Test Class
          o How to reuse a frequently used set of elements
          o How to handle Table with multiple tbody elements
          o How to use the new XPath library in Selenium
          o How to Debug Selenium Core
          o How to use jQuery Selector with weird characters in its ID
          o How to Use Tellurium for XHTML
          o How to Contribute to Tellurium
          o Tellurium Future Directions
    * Tellurium Support
    * Resources
          o Tellurium Project
          o Users' Experiences
          o Interviews
          o Presentations and Videos
          o IDEs
          o Build
          o Related
```

Please provide your comments and suggestions to [Tellurium user group](http://groups.google.com/group/tellurium-users).

Thanks,

Tellurium Team

## The Tellurium User Guide 0.6.0 is Available Now ##

Special thanks to Davlyn Jones, the [Tellurium user guide 0.6.0](http://aost.googlecode.com/files/TelluriumUserGuide.0.6.0.pdf) is officially released now.

### Content Summary ###

Chapter 1 Introduction: Discusses the following:

  * Motivation for creating Tellurium, the Tellurium approach for Web testing
  * Challenges and problems addressed by Tellurium.
  * Tellurium concepts and features: Introduces and describes the following:
    * UI objects, UI Module (heart of Tellurium), UiID (the UI Object)
    * Two types of Composite Locators
    * Group Locating which is an attribute of the Tellurium UI module
    * UI Templates and the use of Table and List
    * Javascript Events using the DOJO JavaScript framework
    * Use of the Logical Container in Tellurium
    * Use of jQuery Selector which improves the test speed in IE
    * Table Methods
    * jQuery Selector Cache Option mechanism in Tellurium
    * Use of the Include syntax in Tellurium
    * Use of setCustomConfig for project level test settings
    * Use of Custom Selenium Extensions with TelluriumConfig.groovy
    * Use of Data Driven Testing in Tellurium
    * Tellurium Dump Method for printing UI objects and descendants runtime locators
    * Tellurium Selenium grid support
    * Tellurium Core Mock Http Server for testing HTML sources directly
    * Tellurium’s three ways to provide testing support
Chapter 2 Getting Started: Provides a description of the three Tellurium methods for creating a Tellurium project. In addition, Tellurium UI Objects are described and discussed including each pre-defined UI Object.

Chapter 3 Tellurium Subprojects: Describes the multiple sub-projects including Reference Projects, Maven Archetypes, TrUMP, Widget Extensions, Core, and Engine projects.

Chapter 4 Tellurium Architecture: Describes the Tellurium architecture and discusses how Tellurium works as a framework for testing.

Chapter 5 Tellurium APIs: Provides tables of various API methods and the action/result of each method when used. The methods discussed are for DSL, Data Access and Test Support DSLs.

Appendices
  * Appendix A Examples: Provides examples of the following Tellurium test cases:
    * Custom UI Object
    * UI Module
    * Groovy Test Case
    * Java Test Case
    * Data Drive Testing
    * DSL Test Script
  * Appendix B FAQs: Provides a list of Tellurium frequently asked questions and answers.
  * Appendix C Tellurium Support
  * Appendix D Resources

As always, your comments and suggestions are welcome and please provide your feedback to [Tellurium User Group](http://groups.google.com/group/tellurium-users).

Thanks,

Tellurium Team

## IRC Channel #tellurium is Available Now ##

To better support our users, we have registered the IRC channel `#tellurium` at [freenode.net](http://freenode.net).

To access the channel, you can use an IRC client such as [Windows clients](http://www.ircreviews.org/clients/platforms-windows.html), [xchat](http://xchat.org/) in Linux, and [ChatZilla for Firefox](https://addons.mozilla.org/en-US/firefox/addon/16). If you are behind a firewall, you can use a web client, for example, [webchat provided by freenode](http://webchat.freenode.net/). You may like to set up your nick name, please consult [the nick name setup guide](http://freenode.net/faq.shtml#nicksetup). If you are new to IRC chat, please read [Basic IRC Commands](http://www.ircbeginner.com/ircinfo/ircc-commands.html), or use the following command to get online help:

```
/help topic
```

We will schedule the time when our Tellurium members are available on the channel so that you can chat with us directly.

Thanks,

Tellurium Team

## Tellurium 0.7.0 RC1 is Available Now ##

Tellurium 0.7.0 RC1 is out now. Over 150 issues have been closed. The issues include new features, bugs fixings, and other enhancements requested by users. There are some fundamental changes in Tellurium 0.7.0 compared with Tellurium 0.6.0 such as the group locating algorithm, UI module caching, Macro command, jQuery-based new APIs, and i18n support.

### New Features ###

  * Santa Algorithm: The **Santa algorithm** is the missing half of the Tellurium UI module concept. The algorithm can locate the whole UI module at the runtime DOM. After that, you can just pass in UI element's UID to find it in the cached UI module on Tellurium Engine. That is to say, you don't need Tellurium Core to generate the runtime locators any more.
  * Macro Command: **Macro Command** is a set of Selenium commands that are bundled together and sent to Selenium Core in one call. This will reduce the round trip latency from Tellurium Core to Engine and thus, improve the speed performance. Another advantage for Macro Command is that Tellurium Engine can reuse the locator because many times the commands in the same bundle act on the same UI element or same sub-tree in the DOM.
  * UI Module Caching: From Tellurium 0.6.0, Tellurium provides the cache capability for CSS selectors so that Tellurium can reuse them without doing re-locating. In 0.7.0, Tellurium moves a step further to cache the whole UI module on the Engine side. Each UI module cache holds a snapshot of the DOM references for the UI elements in the UI module.
  * Tellurium New APIs: Tellurium Engine in 0.7.0 re-implemented a set of Selenium APIs by exploiting jQuery, plus many more new APIs.
  * Trace: Tellurium 0.7.0 provides built-in support for the command execution time including execution time for each command, total run time, and aggregated times for each command.
  * UI Module Live Show: The **show** command is used to show the UI module that you defined on the actual web page. The UI module on the web page is outlined and if a user hives over the UI module, the UIDs of the selected UI element's and its ancestors' are shown as a tooltip.
  * I18n support: Tellurium now provides support for internationalization of strings and exception messages. Internationalized strings for each locale is provided through a MessageBundle for a specific locale which is of the format `<MessageBundleName>_<language-code>_<country code>.properties`.

### Directory Structure ###

The directory structure is listed as follows, i.e., the RC1 release includes Tellurium Core, Engine, tellurium-website, and ui-examples sub-projects. The documents and dependencies are also included.

```

|-- core
|-- dependencies
|   |-- groovy-all-1.7.0.jar
|   |-- json_simple-r1.jar
|   |-- junit-4.7.jar
|   |-- poi-3.0.1-FINAL.jar
|   |-- reportng-0.9.8.jar
|   |-- selenium-java-client-driver-1.0.1.jar
|   |-- selenium-server-1.0.1-te2-RC1.jar
|   |-- stringtree-json-2.0.10.jar
|   |-- tellurium-core-0.7.0-RC1.jar
|   `-- testng-5.8-jdk15.jar
|-- doc
|   |-- Tellurium0.7.0Update.pdf
|   `-- TelluriumUserGuide.0.6.0.pdf
|-- engine
|-- reference-projects
|   |-- tellurium-website
|   `-- ui-examples
```

### How to Obtain Tellurium 0.7.0 RC1 ###

Tellurium 0.7.0 RC1 bundle can be downloaded from http://aost.googlecode.com/files/tellurium-0.7.0-RC1.tar.gz. You can also find Tellurium core and custom selenium server from the following Maven repositories, respectively.

http://maven.kungfuters.org/content/repositories/releases/org/telluriumsource/tellurium-core/0.7.0-RC1/

http://maven.kungfuters.org/content/repositories/thirdparty/org/seleniumhq/selenium/server/selenium-server/1.0.1-te2-RC1/

If you Tellurium Maven archetypes, you can use the following command to create a Tellurium JUnit test project.

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id \
    -DarchetypeArtifactId=tellurium-junit-archetype \
    -DarchetypeGroupId=org.telluriumsource -DarchetypeVersion=0.7.0-RC1 \
    -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/releases 
```

Similarly, use the following Maven command for a Tellurium TestNG project,

```
mvn archetype:create -DgroupId=your_group_id -DartifactId=your_artifact_id \
    -DarchetypeArtifactId=tellurium-testng-archetype \
    -DarchetypeGroupId=org.telluriumsource -DarchetypeVersion=0.7.0-RC1 \
    -DarchetypeRepository=http://maven.kungfuters.org/content/repositories/releases 
```

### Feedback ###

Please report any problems back to Tellurium User Group at

http://groups.google.com/group/tellurium-users

and follow Tellurium on Twitter (http://twitter.com/TelluriumSource) for any the latest update.

Thanks,

Tellurium Team