

# Introduction #

[UI Object ID](http://code.google.com/p/aost/wiki/UserGuide070TelluriumBasics#UiID_Attribute) (UID) is used to identify and describe a UI object in Tellurium Automated Testing Framework. For example, in the following Google Search Module, the uid attribute is the UID. UID "Input" is the name of the InputBox.

```
   ui.Container(uid: "GoogleSearchModule", clocator: [tag: "td"], group: "true"){
     InputBox(uid: "Input", clocator: [title: "Google Search"])
     SubmitButton(uid: "Search", clocator: [name: "btnG", value: "Google Search"])
     SubmitButton(uid: "ImFeelingLucky", clocator: [value: "I'm Feeling Lucky"])
   }
```

Why do we need a language to describe the name of a UI object in Tellurium, then?
The answer is that UID is not just the name of a UI object, it is also used to describe the dynamic factors in a [Tellurium UI template](http://code.google.com/p/aost/wiki/UserGuide070TelluriumBasics#UI_Templates).

Tellurium UI templates have two purposes:

  1. When there are many identical UI elements, use one template to represent them all.
  1. When there are variable/dynamic sizes of UI elements at runtime, the patterns are known, but not the size.

More specifically, Table, StandardTable, and List are the three Tellurium objects that define UI templates. The Table object is a special case of the StandardTable object.

  1. **Table** and **StandardTable** define two dimensional UI templates.
  1. **List** defines one dimensional UI templates.

As a result, the Tellurium UID Description Language (UDL) is designed to
  1. address the dynamic factors in Tellurium UI templates
  1. increase the flexibility of Tellurium UI templates.

# Tellurium UID Description Language #

Tellurium UID Description Language (UDL) is implemented with [the Antlr 3 parser generator](http://www.antlr.org/). The implementation details can be found [here](http://code.google.com/p/aost/wiki/BuildYourOwnJavaParserWithAntlr3).

We like to focus on the grammars and the use of UDL.

## UDL Grammars ##

The UDL grammars are defined as follows,

```
grammar Udl;

uid	
	: 	baseUid
	|	listUid
	|	tableUid	
	;
	
baseUid 
	:	ID
	;
	
listUid
	:	'{' INDEX '}'
	|	'{' INDEX '}' 'as' ID
	;
		
tableUid 
	:	tableHeaderUid
	|	tableFooterUid 
	|	tableBodyUid
	;
	
tableHeaderUid
	:	'{' 'header' ':' INDEX '}'
	|	'{' 'header' ':' INDEX '}' 'as' ID
	;
	
tableFooterUid
	:       '{' 'footer' ':' INDEX '}'
	|       '{' 'footer' ':' INDEX '}' 'as' ID
	;
	
tableBodyUid
        :	'{' 'row' ':' INDEX ',' 'column' ':' INDEX '}'
        |	'{' 'row' ':' INDEX ',' 'column' ':' INDEX '}' 'as' ID
        |	'{' 'row' '->' ID ',' 'column' ':' INDEX '}'
        |	'{' 'row' '->' ID ',' 'column' ':' INDEX '}' 'as' ID
        |	'{' 'row' ':' INDEX ',' 'column' '->' ID '}'
        |	'{' 'row' ':' INDEX ',' 'column' '->' ID '}' 'as' ID
        |	'{' 'row' '->' ID ',' 'column' '->' ID '}'
        |	'{' 'row' '->' ID ',' 'column' '->' ID '}' 'as' ID
        |       '{' 'tbody' ':' INDEX ',' 'row' ':' INDEX ',' 'column' ':' INDEX '}'
        |       '{' 'tbody' ':' INDEX ',' 'row' ':' INDEX ',' 'column' ':' INDEX '}' 'as' ID
        |	'{' 'tbody' ':' INDEX ',' 'row' '->' ID ',' 'column' ':' INDEX '}'
        |	'{' 'tbody' ':' INDEX ',' 'row' '->' ID ',' 'column' ':' INDEX '}' 'as' ID
        |       '{' 'tbody' ':' INDEX ',' 'row' ':' INDEX ',' 'column' '->' ID '}'
        |       '{' 'tbody' ':' INDEX ',' 'row' ':' INDEX ',' 'column' '->' ID '}' 'as' ID
        |	'{' 'tbody' ':' INDEX ',' 'row' '->' ID ',' 'column' '->' ID '}'
        |	'{' 'tbody' ':' INDEX ',' 'row' '->' ID ',' 'column' '->' ID '}' 'as' ID
        ;              			
							
fragment LETTER : ('a'..'z' | 'A'..'Z') ;
fragment DIGIT : '0'..'9';
INDEX	:	(DIGIT+ |'all' | 'odd' | 'even' | 'any' | 'first' | 'last' );   
ID 	: 	LETTER (LETTER | DIGIT | '_')*;
WS 	: 	(' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
```

The grammars defined the UIDs for the following three categories of [Tellurium UI Objects](http://code.google.com/p/aost/wiki/UserGuide070UIObjects).
  1. Regular UI objects without UI templates such as [Input Box](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Input_Box) and [Container](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Container)
  1. List type UI object, i.e., [List](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#List)
  1. Table type UI objects including [Table](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Table) and [StandardTable](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Standard_Table).

We like to go over the grammars in details.

### ID ###

ID is the name of the UI object. The ID in the UDL starts with a letter and is followed by digits, letters, and "`_`" as follows.


http://tellurium-users.googlegroups.com/web/udl_id.jpg?gda=qMY0wDwAAAA7fMi2EBxrNTLhqoq3FzPrYxkpUpFnzJyAbOi2fmAIqphN994tpHQvlLtY7uJzmPP9Wm-ajmzVoAFUlE7c_fAt&gsc=6cd9zQsAAAAkIbFAyUaYDIw1XZeMnNrq

### Index ###

Index defines the position of the UI object. The index in the UDL can be any of the following values:

  * Number, for example, "5".
  * "first", the first element.
  * "last", the last element.
  * "any", any position, usually the position is dynamic at runtime.
  * "odd", the odd elements.
  * "even", the even elements
  * "all", wild match if not exact matches found.

> http://tellurium-users.googlegroups.com/web/udl_index.jpg?gda=IiTkhD8AAAA7fMi2EBxrNTLhqoq3FzPrmWBudP5qiPQkac1peLSYWg56keJ9cuAKmA_YKbtH6VuccyFKn-rNKC-d1pM_IdV0&gsc=myTuAgsAAABNyAlnqKoVxICsBHUflUOz

### Regular UI Objects ###

For most [Tellurium UI objects](http://code.google.com/p/aost/wiki/UserGuide070UIObjects) without UI templates, the UID is an ID, i.e., a name. They don't need any index description since the name and the UI object is one to one mapping.

http://tellurium-users.googlegroups.com/web/udl_baseuid.jpg?gda=i6sK9EEAAAA7fMi2EBxrNTLhqoq3FzPrmWBudP5qiPQkac1peLSYWuuh6eJkplv9T-qV3sY7HytTCT_pCLcFTwcI3Sro5jAzlXFeCn-cdYleF-vtiGpWAA&gsc=myTuAgsAAABNyAlnqKoVxICsBHUflUOz

For example, the UID of the container is "GoogleSearchModule" and "Search" is the name of one SubmitButton in the previous Google search module.

### List ###

[The List object](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#List) defines an array of UI objects and its UID consists of an index and an optional name.


http://tellurium-users.googlegroups.com/web/udl_list.jpg?gda=lwZHAT4AAAA7fMi2EBxrNTLhqoq3FzPrmWBudP5qiPQkac1peLSYWjrjWAqsK6h2ox84bV_5MMfjsKXVs-X7bdXZc5buSfmx&gsc=myTuAgsAAABNyAlnqKoVxICsBHUflUOz

For example, the following List "A", defines the following Ui Objects:

  * InputBox, position at "1" and name "Input".
  * Selector, position at "2" and name "Select".
  * TextBox, represents the rest objects not at position "1" and "2".

```
   ui.List(uid: "A", clocator: [tag: "table"], separator: "tr") {
      InputBox(uid: "{1} as Input", clocator: [:])
      Selector(uid: "{2} as Select", clocator: [:])
      TextBox(uid: "{all}", clocator: [tag: "div"])
   }
```

We can use `"A[1]"` or `"A.Input"` to reference the InputBox object. `"A[3]"` and `"A[6]"` are mapped to the TextBox object.

### Table ###

As we said, the [Table object](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Table) is a special case of the more general object [StandardTable](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Standard_Table), which includes a header, a footer, and one or multiple body sections.

#### Header ####

Table header is very much similar to the List, but its index starts with the "header" indicator.

http://tellurium-users.googlegroups.com/web/udl_tableheader.jpg?gda=Ido_lkUAAAA7fMi2EBxrNTLhqoq3FzPrmWBudP5qiPQkac1peLSYWlkJCT0fa5iWqwJzQkXxz1JzlqnWZQD3y6jZqCMfSFQ6Gu1iLHeqhw4ZZRj3RjJ_-A&gsc=myTuAgsAAABNyAlnqKoVxICsBHUflUOz

#### Footer ####

The footer is similar to header and its index starts with the "footer" indicator.

http://tellurium-users.googlegroups.com/web/udl_tablefooter.jpg?gda=P8Ka3UUAAAA7fMi2EBxrNTLhqoq3FzPrmWBudP5qiPQkac1peLSYWm264Vj_bu4ncefYGcb6mYpzlqnWZQD3y6jZqCMfSFQ6Gu1iLHeqhw4ZZRj3RjJ_-A&gsc=myTuAgsAAABNyAlnqKoVxICsBHUflUOz

#### Body ####

The body is more complicated since it is represented by the triple `[tbody, row, column]`.

http://tellurium-users.googlegroups.com/web/udl_tablebody.jpg?gda=iMD3okMAAAA7fMi2EBxrNTLhqoq3FzPrmWBudP5qiPQkac1peLSYWuiuCXJQSXdF4sM7I0Qg4aMytiJ-HdGYYcPi_09pl8N7FWLveOaWjzbYnpnkpmxcWg&gsc=myTuAgsAAABNyAlnqKoVxICsBHUflUOz

The tbody can be omitted if we have only one tbody. Thus, we can reduce the above subrules to 8. The subrules look complicated, but they are not. Each row and column can be either an index just like that in the List object or a reference to the ID of a header or a footer. The reference is defined by the "`->`" symbol. In this way, the row or column position can be dynamic and follow a header or a footer UI object.

#### Example ####

The Table grammars are better to be explained by an example. The issue search result UI in the issue page of the Tellurium project can be described as follows.

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

The headers of the issue search results can be dragged to different columns and thus, we use "any" to represent the dynamic runtime index. Each header comes with a name so that the body could reference them.

For the body, we have one TextBox for the "Extra" column, i.e., the column where the header "Extra" is at, and all the others are UrlLinks. As a result, we have the following references to the table UI objects:
  * `issueResult.header.ID` refers to the ID header
  * `issueResult[1][ID]` refers to the UI object in the first row and the same column as the header "ID".
  * `issueResult[3][Extra]` refers to the UI object in the third row and the same column as the "Extra" header.

## Routing ##

The routing mechanism maps the runtime UID reference to the appropriate UI template. We will cover the routing mechanism for UI templates in the List and Table objects.

### RTree ###

For a List object, the index could be any of the following:
  * _digits_, such as "1", "3", and "5".
  * _first_, which is converted to "1".
  * _last_, the last element.
  * _any_, any position.
  * _odd_, odd elements.
  * _even_, even elements.
  * _all_, match all and default UI element.

The routing tree for a List object is called a **RTree** as follows.

http://tellurium-users.googlegroups.com/web/udl_rtree.png?gda=AU9R4z8AAAA7fMi2EBxrNTLhqoq3FzPrRTp9-cAEkfvWUJNGZ-Y9si741iQgkgWcrEC79tDnFNaccyFKn-rNKC-d1pM_IdV0&gsc=UJYYUgsAAADkd6yl1NLvBg_VFBAXAGpd

The root is the "all" node and the digits, "any", and "last" are leaf nodes. "odd" and "even" nodes are parents of the digit nodes. The routing algorithm always to match the runtime uid to one of the leaf node, if not found, go up to match its parent node until it reaches the "all" node, which presents a default UI object.

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

By the RTree routing algorithm, the runtime uid mappings are shown as follows.

```
   //Runtime uid         mapped UI object

   //List Referenced by ID
   "Example.Input"  ---> InputBox
   "Example.Select" ---> Selector
   "Example.Submit" ---> SubmitButton
 
   //List Referenced by Index 
   "Example[first]" ---> InputBox
   "Example[1]"     ---> InputBox
   "Example[2]"     ---> TextBox
   "Example[3]"     ---> Button
   "Example[4]"     ---> Selector
   "Example[last]"  ---> SubmitButton
```

### RGraph ###

The Table type of objects usually include one header, one or multiple tbodies, and one footers. That is to say, the Table object is represented by triple `[tbody, row, column]`. Each tuple is represented by a RTree and the three RTrees form a RGraph. The reason it is called a graph is that each tuple is not separated. If multiple nodes in the RGraph form a UI template, we can draw a dash line to connect together. For example, the node "4" in tbody, the "even" node in row, and the "odd" node in the column form a UI template such as

```
   UrlLink(uid: "tbody: 4, row: even, column: odd", clocator: [:])
```

http://tellurium-users.googlegroups.com/web/udl_rgraph.png?gda=yR6UdkAAAAA7fMi2EBxrNTLhqoq3FzPr0b4O3mWwoz9GTCgGtTU-umb7kTUGkceVGqWQIEBe0zxtxVPdW1gYotyj7-X7wDON&gsc=UJYYUgsAAADkd6yl1NLvBg_VFBAXAGpd

In this way, the three RTrees actually form a graph. As a result, the routing problem becomes "how to find a UI template in the RGraph that is the closest one to the runtime UID ?".

To do this, we assign a fitness, i.e., weight, to tbody, row, and column respectively. Usually, we select the weight as follows:

```
   tbody > row > column
```

That is to say, we always try to match the tbody first, then the row and the column.

The routing algorithm can be illustrated by the following code snippet.

```
  UiObject route(String key) {
    //check the ID reference
    UiObject object = this.indices.get(key);

    //this is a index reference
    if(object == null){
      //normalize the index
      String[] parts= key.replaceFirst('_', '').split('_');
      String[] ids = parts;
      if(parts.length < 3){
        ids = ["1", parts].flatten();
      }
      String x = ids[0];
      if("first".equalsIgnoreCase(x)){
        x = "1";
      }
      String y = ids[1];
      if("first".equalsIgnoreCase(y)){
        y = "1";
      }
      String z = ids[2];
      if("first".equalsIgnoreCase(z)){
        z = "1";
      }
      
      //Find match nodes separately for tbody, row, and column
      String[] list = this.generatePath(x);
      Path path = new Path(list);
      RNode nx = this.walkTo(this.t, x, path);
      list = this.generatePath(y);
      path = new Path(list);
      RNode ny = this.walkTo(this.r, y, path);
      list = this.generatePath(z);
      path = new Path(list);
      RNode nz = this.walkTo(this.c, z, path);

      //Use the fitness to select the closest match
      int smallestFitness = 100 * 4;
      RNode xp = nx;
      while (xp != null) {
        RNode yp = ny;
        while (yp != null) {
          RNode zp = nz;
          while(zp != null){
            //internal representation of the index
            String iid = this.getInternalId(xp.getKey(), yp.getKey(), zp.getKey());
            //If they form a UI template 
            if(xp.templates.contains(iid) && yp.templates.contains(iid) && zp.templates.contains(iid)){
              int fitness = (nx.getLevel() - xp.getLevel()) * 100 + (ny.getLevel() - yp.getLevel()) * 10 + (nz.getLevel() - zp.getLevel());
              if(fitness < smallestFitness){
                object = this.templates.get(iid);
                smallestFitness = fitness;
              }
            }
            //walk up the RGraph
            zp = zp.parent;
          }
          //walk up the RGraph
          yp = yp.parent;
         }
        //walk up the RGraph 
        xp = xp.parent;
      }
    }

    //return the closest match
    return object;
  }
```

Take the previous [Tellurium Issue Result UI module](http://code.google.com/p/aost/wiki/TelluriumUIDDescriptionLanguage#Example) as an example, we have the following runtime UID to UI object mapping.

```
   //Runtime UID               mapped UI object
   "issueResult[1][Extra]" --> TextBox
   "issueResult[1][ID]     --> UrlLink
   "issueResult[2[[Type]   --> UrlLink
```

Be aware that the "Extra", "ID", and "Type" are index references to the header columns of the header "Extra", "ID", and "Type" respectively and they will be replaced by the actual column number of the corresponding header at runtime.

# Resources #

  * [Build Your Own Java Parser with Antlr 3](http://code.google.com/p/aost/wiki/BuildYourOwnJavaParserWithAntlr3)
  * [What's New in Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Update)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [TelluriumSource](http://telluriumsource.org)