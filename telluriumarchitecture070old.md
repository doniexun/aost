# Tellurium Architecture #

The Tellurium framework architecture is shown in Figure 4-1:

**Figure 4-1 Tellurium Framework Architecture**

http://tellurium-users.googlegroups.com/web/TelluriumUMLSystemDiagram.png?gda=0H5Erk8AAAD5mhXrH3CK0rVx4StVj0LYqZdbCnRI6ajcTiPCMsvamYLLytm0aso8Q_xG6LhygcwA_EMlVsbw_MCr_P40NSWJnHMhSp_qzSgvndaTPyHVdA&gsc=cq8RLwsAAADwIKTw30t0VbWQq6vz0Jcq

The DSL parser consists of the DSL Object Parser, Object Builders, and the Object Registry.

Using Groovy builder pattern, UI objects are defined expressively and in a nested fashion. The DSL object parser parses the DSL object definition recursively and uses object builders to build the objects on the fly. An object builder registry is designed to hold all predefined UI object builders in the Tellurium framework, and the DSL object parser looks at the builder registry to find the appropriate builders.

Since the registry is a hash map, you can override a builder with a new one using the same UI name. Users can also add their customer builders into the builder registry. The DSL object definition always comes first with a container type object. An object registry (a hash map) is used to store all top level UI Objects. As a result, for each DSL object definition, the top object IDs must be unique in the DslContext. The object registry is used by the framework to search for objects by their IDs and fetch objects for different actions.

The Object Locator Mapping (OLM) is the core of the Tellurium framework and it includes UI ID mapping, XPath builder, jQuery selector builder, and Group Locating.

The UI ID supports nested objects. For example, "menu.wiki" stands for a URL Link "wiki" inside a container called "menu".

The UI ID also supports one-dimensional and two-dimensional indices for tables and lists. For example, `"main.table[2][3]"` stands for the UI object of the 2nd row and the 3rd column of a table inside the container "main".

XPath builder builds the XPath from the composite locator. For example, a set of attributes. Starting with version 0.6.0, Tellurium supports jQuery selectors to address the problem of poor performance of XPath in Internet Explorer.

jQuery selector builders are used to automatically generate jQuery selectors instead of XPath with the following advantages:

  * Provides faster performance in IE
  * Leverages the power of jQuery to retrieve bulk data from the web by testing with one method call
  * Provides new features using jQuery attribute selectors

The Group Locating Concept (GLC) exploits the group information inside a collection of UI objects to assist in finding the locator of the UI objects collection.

The Eventhandler handles all events such as "click", "type", "select", etc.

The Data Accessor fetches data or UI status from the DOM. The dispatcher delegates all calls it receives from the Eventhandler and the data accessor attached to the connector is also connected to the Tellurium engine.

The dispatcher is designed to decouple the rest of the Tellurium framework from the base test driving engine so that it can be switched to a different test driving engine by simply changing the dispatcher logic.

## How Tellurium Works ##

Basically, there are two parts for the Tellurium framework. The first part defines UI objects and the second part works on the UI objects such as firing events and obtaining data or status from the DOM.

The defineUI operation is shown in Figure 4-2:

**Figure 4-2 defineUI Operation Sequence Diagram**

http://tellurium-users.googlegroups.com/web/TelluriumDefineUiSequenceDiagram.png?gda=aw23nVYAAAD5mhXrH3CK0rVx4StVj0LY6r7Fxo4RaVZ2InRIkvRUPVa5eLn5D8aMzeq5BmQm6Iqs27ZxjBtfjzl3w_ujfDpqd64s9EZTLP9aL_4jXQez_BPhGuxsWDLdLep2NLleRSE&gsc=x3TBYwsAAAAeXMPG6HH-B1VXA1h0gdTp

When the Test code calls defineUI(), the DslContext calls the Dsl Object Parser to parse the UI definition. The Parser looks at each node and calls the appropriate builders to build UI objects. The top level object is stored in the UI Object registry so that the UI object is searched for by uid.

The processing of actions such as clicking on an UI object is illustrated in Figure 4-3:

**Figure 4-3 Action Processing Sequence Diagram**

http://tellurium-users.googlegroups.com/web/TelluriumClickSequenceDiagram.png?gda=DDIgt1MAAAD5mhXrH3CK0rVx4StVj0LY6r7Fxo4RaVZ2InRIkvRUPeD1j29FUgzDhsVQNMlCcuXsGSF53sOd2QxWBQw1Q0uxMrYifh3RmGHD4v9PaZfDexVi73jmlo822J6Z5KZsXFo&gsc=x3TBYwsAAAAeXMPG6HH-B1VXA1h0gdTp

The action processing includes the following two parts:

  1. The DslContext creates a WorkflowContext so that meta data such as the relative locator inside it is passed.
  1. Then, start looking at the UI object registry by calling the walkTo(uid) method.

**Note:** Remember, the UI object registry holds all the top level UI objects.

If the top level UI object is found, the user can recursively call the `walkTo(uid)` method on the next UI object until the UI Object matching the uid is found.

During the `walkTo` method calls, the user starts to aggregate relative XPaths or jQuery selector into the reference locator. Then it is passed on to the next UI object. In this way, the runtime locator is built.

If the UI Object is found, the action is called.

For example, "click" on the UI object and the call is passed on to the EventHandler. Additional JavaScript events may be fired before and/or after the click action as shown in Figure 4-2.

The Action and JavaScript events are passed all the way down from the dispatcher and connector to the Tellurium Engine, which is embedded in the Selenium server at the current stage.