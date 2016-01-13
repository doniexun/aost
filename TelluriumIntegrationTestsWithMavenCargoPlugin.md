

# Introduction #

Tellurium provides users with a way to create structured integration tests. This article introduced how to use the [Maven Cargo Plugin](http://cargo.codehaus.org/Maven2+plugin) to automatically deploy web application and run Tellurium Integration Tests.

# Example #

We use the [Spring Finance Manager sample application](http://code.google.com/p/spring-finance-manager/) as the example. We upgraded this web application to Spring 3.0 release and all the code examples are available from [Tellurium project](http://aost.googlecode.com/svn/branches/finance-manager).

## Tellurium Code Structure ##

To add Tellurium tests to the Spring Finance Manager sample application, we need to create the following directory to support GMaven.

```
src
└── test
    ├── groovy
    │   └── org
    │       └── telluriumsource
    │           ├── integration
    │           │   └── FinanceManager_FuncTest.java
    │           └── module
    │               ├── AccountModule.groovy
    │               ├── LoginModule.groovy
    │               └── MainMenu.groovy

```

## Maven Update ##

We need to add the following update to the original pom.xml for the Spring Finance Manager sample application.

### Repositories ###

```
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
```

### Dependencies ###

```
	<dependency>
		<groupId>org.testng</groupId>
		<artifactId>com.springsource.org.testng</artifactId>
		<version>5.8.0</version>
		<scope>test</scope>
	</dependency> 

       <dependency>
            <groupId>org.codehaus.gmaven</groupId>
            <artifactId>gmaven-mojo</artifactId>
            <version>${gmaven-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.codehaus.gmaven.runtime</groupId>
                    <artifactId>gmaven-runtime-1.5</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.codehaus.gmaven.runtime</groupId>
            <artifactId>gmaven-runtime-1.6</artifactId>
            <version>${gmaven-version}</version>
        </dependency>

        <dependency>
            <groupId>org.telluriumsource</groupId>
            <artifactId>tellurium-core</artifactId>
            <version>${tellurium-version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium.server</groupId>
            <artifactId>selenium-server</artifactId>
            <version>${selenium-version}-${tellurium-engine-version}</version>
            <!--classifier>standalone</classifier-->
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium.client-drivers</groupId>
            <artifactId>selenium-java-client-driver</artifactId>
            <version>${selenium-version}</version>
            <scope>test</scope>
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
            <version>${groovy-version}</version>
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
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.0.1-FINAL</version>
        </dependency>
```

### Resources ###

```
       <testResource>
            <directory>src/test/groovy</directory>
       </testResource>
```

### Plugins ###

```
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-surefire-plugin</artifactId>
		<version>2.4.3</version>
                <configuration>
                    <junitArtifactName>org.junit:com.springsource.org.junit</junitArtifactName>
                    <testNGArtifactName>org.testng:com.springsource.org.testng</testNGArtifactName>
                    <excludes>
                        <exclude>**/integration/**</exclude>
                    </excludes>

                    <includes>
                        <include>**/*Test.java</include>
                    </includes>
                </configuration>
                <executions>
                    <execution>
                        <phase>integration-test</phase>
                        <goals>
                            <goal>test</goal>
                        </goals>
                        <configuration>
                            <excludes>
                                <exclude>none</exclude>
                            </excludes>
                            <includes>
                                <include>**/integration/*_FuncTest.java</include>
                            </includes>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.codehaus.gmaven</groupId>
                <artifactId>gmaven-plugin</artifactId>
                <version>${gmaven-version}</version>
                <configuration>
                    <providerSelection>1.7</providerSelection>
                    <targetBytecode>${java-version}</targetBytecode>
                </configuration>
                <executions>
                    <execution>
                        <goals>      
                            <goal>generateStubs</goal>
                            <goal>compile</goal>
                            <goal>generateTestStubs</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

### Properties ###

```
        <java-version>1.6</java-version>
        <groovy-version>1.7.0</groovy-version>
        <gmaven-version>1.2</gmaven-version>
        <selenium-version>1.0.1</selenium-version>
        <tellurium-engine-version>te2-SNAPSHOT</tellurium-engine-version>
        <tellurium-version>0.7.0-SNAPSHOT</tellurium-version>
        <javac-debug>true</javac-debug>        
```

## Tellurium Tests ##

### UI Modules ###

Take the Login UI module as an example.

```
package org.telluriumsource.module

import org.telluriumsource.dsl.DslContext

class LoginModule extends DslContext {
  
  public void defineUi() {
    ui.Container(uid: "LoginMenu", clocator: [tag: "ul"]) {
      TextBox(uid: "Security", clocator: [tag: "h2", text: "Security"])
      UrlLink(uid: "Login", clocator: [tag: "a", text: "Login", href: "/FinanceManager/login.jsp"])
      TextBox(uid: "Product", clocator: [tag: "h2", text: "Product"])
      UrlLink(uid: "List", clocator: [tag: "a", text: "List", href: "/FinanceManager/product"])
    }

    ui.Span(uid: "LoginSlide", clocator: [tag: "span", text: "Spring Security Login", class: "dijitTitlePaneTextNode"])

    ui.Form(uid: "Login", clocator: [tag: "form", method: "POST", action: "/FinanceManager/j_spring_security_check", name: "f"]) {
      TextBox(uid: "UserNameLabel", clocator: [tag: "label", text: "Email:"])
      InputBox(uid: "UserName", clocator: [tag: "input", type: "text", name: "j_username", class: "dijitReset", id: "j_username"])
      TextBox(uid: "PasswordLabel", clocator: [tag: "label", text: "Password:"])
      InputBox(uid: "Password", clocator: [tag: "input", type: "password", name: "j_password", class: "dijitReset", id: "j_password"])
      SubmitButton(uid: "Submit", clocator: [tag: "input", type: "submit", value: "Submit", id: "proceed"])
      Button(uid: "Reset", clocator: [tag: "input", type: "reset", value: "Reset", id: "reset"])
    }
  }

  public void login(String username, String password){
    keyType "Login.UserName", username
    keyType "Login.Password", password
    click "Login.Submit"
    waitForPageToLoad 30000
  }

  public void selectLogin(){
    click "LoginMenu.Login"
    waitForPageToLoad 30000
  }

  public void listProduct(){
    click "LoginMenu.List"
    waitForPageToLoad 30000
  }

  public void toggle(){
    click "LoginSlide"
  }
}
```

### Integration Tests ###

We created the integration tests for the login functionality as follows.

```
public class FinanceManager_FuncTest extends TelluriumTestNGTestCase {
    private static LoginModule lnm;
    @BeforeClass
    public static void initUi() {
        lnm = new LoginModule();
        lnm.defineUi();
        connectSeleniumServer();
        useCssSelector(true);
        useTelluriumEngine(true);
        useTrace(true);
    }

    @Test
    public void testLogin(){
        connectUrl("http://localhost:8080/FinanceManager/login.jsp");
        String username = "super@admin.com";
        String password = "admin";
        lnm.login(username, password);
    }

    @AfterClass
    public static void tearDown(){
        showTrace();
    }
}
```

# Cargo Maven Plugin #

The cargo Maven plugin is difficult to configure and we managed to set up cargo Maven plugin to deploy the web application to an installed tomcat instance and to download and create a tomcat instance.

## Use An Installed Tomcat ##

Assume we have a tomcat 6 installed at `/usr/local/tomcat`, the configuration is as follows.

```
            <plugin>
                <groupId>org.codehaus.cargo</groupId>
                <artifactId>cargo-maven2-plugin</artifactId>
                <configuration>
                    <wait>false</wait>
                    <container>
                        <type>installed</type>
                        <containerId>tomcat6x</containerId>
                        <home>/usr/local/tomcat</home>
                    </container>
                    <configuration>
                        <type>existing</type>
                        <home>/usr/local/tomcat</home>
                        <properties>
                            <cargo.servlet.port>8080</cargo.servlet.port>
                            <cargo.logging>high</cargo.logging>
                        </properties>
                    </configuration>                  
                    <deployer>
                        <type>installed</type>
                        <deployables>
                            <deployable>
                               <groupId>net.stsmedia.financemanager</groupId>
                                <artifactId>FinanceManager</artifactId>
                                <type>war</type>
                                <properties>
                                    <context>FinanceManager</context>
                                </properties>
                            </deployable>
                        </deployables>
                    </deployer>
                   </configuration>

               <executions>
                    <execution>
                        <id>start-container</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>deployer-deploy</goal>
                            <goal>start</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>stop-container</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>stop</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

Be aware the line `<context>FinanceManager</context>` is used to override the default context from FinanceManager-0.5 to FinanceManager, otherwise the web application will break.

Seems if the `<deployer>` section is defined, we have to call `<goal>deployer-deploy</goal>` explicitly.

## Download and Install A New Tomcat Instance ##

The configuration looks somehow different.

```
            <plugin>
                <groupId>org.codehaus.cargo</groupId>
                <artifactId>cargo-maven2-plugin</artifactId>
                <configuration>
                    <wait>false</wait>
                   <container>
                        <containerId>tomcat6x</containerId>

                        <zipUrlInstaller>
                            <url>http://www.apache.org/dist/tomcat/tomcat-6/v6.0.24/bin/apache-tomcat-6.0.24.zip</url>
                            <installDir>${project.build.directory}/cargoinstalls</installDir>
                        </zipUrlInstaller>
                        <log>${project.build.directory}/tomcat6x/cargo.log</log>
                    </container>

                    <configuration>
                        <home>${project.build.directory}/tomcat6x/config</home>
                        <properties>
                            <cargo.servlet.port>8080</cargo.servlet.port>
                            <cargo.logging>high</cargo.logging>
                        </properties>
                    </configuration>
                    <deployer>
                        <type>installed</type>
                        <deployables>
                            <deployable>
                                <groupId>net.stsmedia.financemanager</groupId>
                                <artifactId>FinanceManager</artifactId>
                                <type>war</type>
                                <properties>
                                    <context>FinanceManager</context>
                                </properties>
                            </deployable>
                        </deployables>
                    </deployer>
                </configuration>
                <executions>
                    <execution>
                        <id>start-container</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>start</goal>
                            <goal>deploy</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>stop-container</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>stop</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

# Run #

To run the tellurium integration tests, simply run

```
mvn install
```

or

```
mvn integration-test
```

# Resources #

  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Cargo Maven 2 Plugin](http://cargo.codehaus.org/Maven2+plugin)
  * [Maven + Cargo + Tomcat = Auto-deployment](http://dpillay.wordpress.com/2009/04/12/maven-cargo-tomcat-auto-deployment/)
  * [Automated Deployment With Cargo and Maven - a Short Primer](http://java.dzone.com/articles/automated-deployment-cargo-and)
