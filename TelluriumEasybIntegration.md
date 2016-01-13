

# Tellurium & easyb Integration #

Tellurium is now integrated with [the easyb framework](http://www.easyb.org/). It is now very easy to write a Tellurium tests using easyb story/specification approach.

easyb is a behavior driven development framework for the Java platform. By using a specification based Domain Specific Language, easyb aims to enable executable, yet readable documentation.

You can find more information about easyb on the following url http://www.easyb.org

There are different ways to [run easyb stories](http://www.easyb.org/running.html). We will be using maven to run Tellurium tests as easyb story. You need to define the following dependencies in your pom.xml to work with easyb and Tellurium.

```
    <dependencies>
        <dependency>
            <groupId>org.telluriumsource</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>0.8.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.easyb</groupId>
                <artifactId>maven-easyb-plugin</artifactId>
                <version>0.9.7</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <easybTestDirectory>${basedir}/src/test/groovy/org/telluriumsource/easyb/story</easybTestDirectory>
                    <storyReport>${project.build.directory}/easyb-stories.txt</storyReport>
                    <xmlReport>${project.build.directory}/easyb-report.xml</xmlReport>
                </configuration>
            </plugin>
        </plugins>
    </build>

```

You can use any of the Java IDEs to write an easyb story. You might want to change the configuration in the pom.xml above to match to your environment.

There are plug-ins available for IntelliJ and Eclipse. Following is the code for GoogleSearchTest.story.

```
import org.telluriumsource.test.java.TelluriumEasybTestCase
import easyb.module.GoogleSearchModule

scenario "user searches for tellurium on google.com", {

before "start tellurium"
  tellurium = new TelluriumEasybTestCase();
  tellurium.start();
  googleSearch = new GoogleSearchModule();
  googleSearch.defineUi();
  tellurium.connectSeleniumServer();

given "google.com is up",{
  tellurium.connectUrl("http://www.google.com");
}

when "user searches for tellurium",{
  googleSearch.doGoogleSearch("tellurium test");
}

then "title should start with tellurium",{
  googleSearch.getTitle().shouldStartWith "tellurium";
}

after "stop tellurium"
    tellurium.stop();
}

```

You would have notice that we are making use of _GoogleSearchModule_ class which is standard way of defining UI modules in Tellurium and we have also exposed methods to run Tellurium within an easyb story in _TelluriumEasybTestCase_ class.

This is all you need to do in order to define an easyb story and run this using Tellurium.

You can use the maven test goal to run the easyb stories.
```
mvn test
```

Once the test is finished, the output will be generated in your project build directory as  _easyb-stories.txt_ and _easyb-report.xml_

Similarly you can write an easyb specification and use Tellurium to drive the tests for that specification. We are very excited about this as this is just the beginning to drive Tellurium towards Behaviour Driven Development.

You can find the sample project on Tellurium code repository under the following location.

```
tellurium/trunk/extensions/tellurium-easyb
```

As always, in case of any questions please feel free to post it to the Tellurium User Group.

Thanks
Tellurium Team

# Resources #

  * [Tellurium Project Home](http://code.google.com/p/aost/)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Easyb](http://www.easyb.org/)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Released)
  * [What's New in Tellurium](http://code.google.com/p/aost/wiki/WhatsNewInTellurium080) 0.8.0.