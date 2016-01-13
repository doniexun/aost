# Introduction #

Have you ever thought of seeing the actual UI on the web page under testing? Tellurium 0.7.0 adds a cool feature to show this visual effect.

Tellurium provides a _show_ command to display the UI module that you defined on the actual web page.

```
    public show(String uid, int delay);
```

where _uid_ is the UI module name and _delay_ is in milliseconds. In the meanwhile, Tellurium Core exposes the following two methods for users to start showing UI and clean up UI manually.

```
   public void startShow(String uid);

   public void endShow(String uid);
```


# How it Works ? #

Under the hood, Tellurium Engine does the following things.

## Build a Snapshot Tree ##

The Snapshot Tree, or STree in short, is different from the UI module. The UI module defines how the UI looks like. Even [the UI module group locating algorithm - Santa](http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm) only locates cachable UI elements and leaves out the dynamic elements. The snapshot tree, however, needs to include every UI elements inside the UI module. For example, the following Google Book List UI module is very simple.

```
    ui.Container(uid: "GoogleBooksList", clocator: [tag: "table", id: "hp_table"]) {
      List(uid: "subcategory", clocator: [tag: "td", class: "sidebar"], separator: "div") {
        Container(uid: "all") {
          TextBox(uid: "title", clocator: [tag: "div", class: "sub_cat_title"])
          List(uid: "links", separator: "p") {
            UrlLink(uid: "all", clocator: [:])
          }
        }
      }
    }
```

But its STree may include many book categories and books.

A snapshot tree includes the following different types of nodes.

### SNode ###

SNode can present a non-container type UI object, i.e., UI element without any child.

```
var UiSNode = Class.extend({
    init: function() {

        //parent's rid
        this.pid = null;

        //rid, runtime id
        this.rid = null;

        //point to its parent in the UI SNAPSHOT tree
        this.parent = null;

        //UI object, which is defined in the UI module, reference
        this.objRef = null;

        //DOM reference
        this.domRef = null;
    },
...
}
```

### CNode ###

CNode is a container type node and it has children.

```

var UiCNode = UiSNode.extend({
    init: function(){
        this._super();
        //children nodes, regular UI Nodes
        this.components = new Hashtable();
    },

...
}
```

### TNode ###

The TNode stands for a table with headers, footers, and one or multiple bodies.
```
var UiTNode = UiSNode.extend({
    init: function(){
        this._super();

        //header nodes
        this.headers = new Hashtable();

        //footer nodes
        this.footers = new Hashtable();

        //body nodes
        this.components = new Hashtable();
    },
...
```

Finally, the Snapshot tree is defined as

```
function UiSTree(){
    //the root node
    this.root = null;

    //the reference point to the UI module that the UI Snapshot tree is derived
    this.uimRef = null;
}
```

The STree build process is quite complicated. The basic idea is to use the cached DOM references in a cached UI module and use a subset of the Santa algorithm to locate dynamic UI elements.

## STree Visitors ##

The STree defines a traverse method, so that we can pass in different visitors to work on each individual node in the tree for different purpose.

```
UiSTree.prototype.traverse = function(context, visitor){
    if(this.root != null){
        this.root.traverse(context, visitor);
    }
};

var UiSNode = Class.extend({
...
    traverse: function(context, visitor){
        visitor.visit(context, this);
    },
...
}

```

The STree Visitor class is defined as

```
var STreeVisitor = Class.extend({
    init: function(){

    },

    visit: function(context, snode){

    }
});
```

Tellurium Engine also defines a Visitor Chain to pass in multiple visitors.

```
var STreeChainVisitor = Class.extend({
    init: function(){
        this.chain = new Array();
    },

    removeAll: function(){
        this.chain = new Array();
    },

    addVisitor: function(visitor){
        this.chain.push(visitor);
    },

    size: function(){
        return this.chain.length;
    },

    visit: function(context, snode){
        for(var i=0; i<this.chain.length; i++){
            this.chain[i].visit(context, snode);
        }
    }
});
```

For the show UI method, the following two Visitors are implemented.

### Outline Visitor ###

The outline visitor is used to mark the UI elements inside a UI module to differentiate the UI elements in the UI module from other UI elements.

The outline visitor include a worker to outline an element.

```

var UiOutlineVisitor = STreeVisitor.extend({
    
    visit: function(context, snode){
        var elem = snode.domRef;
        teJQuery(elem).data("originalOutline", elem.style.outline);
        elem.style.outline = tellurium.outlines.getDefaultOutline();
    }
});
```

and a cleaner to restore the UI to the original one.

```
var UiOutlineCleaner = STreeVisitor.extend({
    visit: function(context, snode){
        var elem = snode.domRef;
        var $elem = teJQuery(elem);
        var outline = $elem.data("originalOutline");
        elem.style.outline = outline;
        $elem.removeData("originalOutline");
    } 
});

```

### Tooltip Visitor ###

The Tooltip visitor is used to show the full UID of an element in a tooltip fasion. Tellurium exploited [jQuery Simpletip plugin](http://craigsworks.com/projects/simpletip/) to achieve this visual effect. In additional to that, the tooltip visitor also changes the outlines of the selected UI elements.

We need to change Simpletip plugin code a bit. By default Simpletip create a div element and insert this element inside a UI element that you want to show tooltip. But this would not work if the tag of the UI element is "input", thus, we changed this code to use insertAfter as follows.

```
      var tooltip = teJQuery(document.createElement('div'))
                     .addClass(conf.baseClass)
                     .addClass("teengine")
                     .addClass( (conf.fixed) ? conf.fixedClass : '' )
                     .addClass( (conf.persistent) ? conf.persistentClass : '' )
                     .css({'z-index': '2', 'position': 'right', 'padding': '8', 'color': '#303030',
                            'background-color': '#f5f5b5', 'border': '1', 'solid': '#DECA7E',
                            'font-family': 'sans-serif', 'font-size': '8', 'line-height': '12px', 'text-align': 'center'
                          })
                     .html(conf.content)
                     .insertAfter(elem);
```

In additional to that, we added a class "teengine" for the div tag so that it will conflict with users' web content.

Like the outline visitor, the tooltip visitor includes a worker to set up the visual effects,

```
var UiSimpleTipVisitor = STreeVisitor.extend({

    visit: function(context, snode) {
        var elem = snode.domRef;
        var frid = snode.getFullRid();

        var $elem = teJQuery(elem);
        $elem.data("level", snode.getLevel());
        $elem.simpletip({
            // Configuration properties
            onShow: function() {
                var $parent = this.getParent();
                var parent = $parent.get(0);
                var level = $parent.data("level");

                var outline = $parent.data("outline");
                if(outline == undefined || outline == null){
                    $parent.data("outline", parent.style.outline);
                }

                parent.style.outline = tellurium.outlines.getOutline(level);
            },
            onHide: function() {
                var $parent = this.getParent();
                var parent = $parent.get(0);

                parent.style.outline = $parent.data("outline");
            },

            content: convertRidToUid(frid),
            fixed: false
        });
    }
});
```

and a cleaner to remove the visual effects.

```
var UiSimpleTipCleaner = STreeVisitor.extend({
    visit: function(context, snode){
        var elem = snode.domRef;
        var frid = snode.getFullRid();

        var $elem = teJQuery(elem);
        $elem.removeData("outline");
        $elem.removeData("level");
        $elem.find("~ div.teengine.tooltip").remove();
    }
});
```

# Demo #

## Example ##

To show the UI module visual effect, we consider the following html snippet

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

Based on the html source, we can define the UI module as follows,

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

The corresponding test case is as follows.

```
public class FormExampleTestCase extends TelluriumMockTestNGTestCase {
    private static FormExampleModule fem;

    @BeforeClass
    public static void initUi() {
        registerHtmlBody("FormExample");

        fem = new FormExampleModule();
        fem.defineUi();
        useTelluriumEngine(true);
        useTrace(true);        
        useEngineLog(true);
    }

    @Test
    public void testShowUi(){
//        fem.show("Form", 10000);
        fem.startShow("Form");
        fem.endShow("Form");
    }

    @AfterClass
    public static void tearDown(){
        showTrace();
    }
```

## Visual Effects ##

First, you can download the above example by checking out the ui-examples reference project from [subversion](http://aost.googlecode.com/svn/trunk/reference-projects/ui-examples) or download it from

http://aost.googlecode.com/files/tellurium-examples-0.7.0-SNAPSHOT.tar.gz

Then, load the project into an IDE and put a breakpoint at the line

```
fem.startShow("Form");
```

After that, debug the test case `testShowUi()`.

Before the `startShow` command, the UI looks as follows,

http://tellurium-users.googlegroups.com/web/teshowuinoeffect.png?gda=K7wvOUYAAACsdq5EJRKLPm_KNrr_VHB_D4HLj40ELs-zhpN3Bl4yREf8cXQvcxDOBRa54Ef7YmV3riz0RlMs_1ov_iNdB7P8E-Ea7GxYMt0t6nY0uV5FIQ&gsc=oGileAsAAACCUi6IlstSnnhb_xNbAycZ

and after the `startShow` command, the UI module is outlined by the outline visitor.

http://tellurium-users.googlegroups.com/web/teshowuidefault.png?gda=O6D890UAAACsdq5EJRKLPm_KNrr_VHB_D4HLj40ELs-zhpN3Bl4yRN5qC_dOTsTgqGqvK1ZkNDxzlqnWZQD3y6jZqCMfSFQ6Gu1iLHeqhw4ZZRj3RjJ_-A&gsc=oGileAsAAACCUi6IlstSnnhb_xNbAycZ

If you mouse over the UI module, you will see the visual effects as follows.

http://tellurium-users.googlegroups.com/web/teshowuimouseover.png?gda=hwCMV0cAAACsdq5EJRKLPm_KNrr_VHB_D4HLj40ELs-zhpN3Bl4yRLXL3uGDhcmmKEh-Qhrel1EVeY4b49xGcMK802iZZ8SFeV4duv6pDMGhhhZdjQlNAw&gsc=oGileAsAAACCUi6IlstSnnhb_xNbAycZ

You may be a bit confused by the multiple UID labels. For example, When user hives over the Username Input box, its parent "Username" and its grandparent "Form" are shown as well. We added color coding to the outlines. Different levels in the hierarchy are presented by different colors. How the layout maps to each individual UI element in the STree can be illustrated more clearly by the following figure.

http://tellurium-users.googlegroups.com/web/telluriumshowuisnapshot2.png?gda=jciJlU4AAACsdq5EJRKLPm_KNrr_VHB__uAWGGGjpMtQflLHIKHYYIBs4RzukN2p4-NZziI_5n9VyjA3JensFE0ElYzuCQ7x47Cl1bPl-23V2XOW7kn5sQ&gsc=oz20hwsAAAD_weMBWNZir9C-tYu9DiER

Once you call the following command

```
   endShow("Form");
```

The visual effects are removed and the UI is restored to the original one.

## Screen Cast ##

The whole process is shown in [the Tellurium UI Module Visual Demo at Vimeo](http://vimeo.com/9305675). Or from Youtube.

> <a href='http://www.youtube.com/watch?feature=player_embedded&v=J1-NN9VK2WE' target='_blank'><img src='http://img.youtube.com/vi/J1-NN9VK2WE/0.jpg' width='425' height=344 /></a>

# Want to Contribute ? #

We welcome contributions for Tellurium from various aspects. Right now, we are looking for new members to join our dev team. More details on [How to Contribute](http://code.google.com/p/aost/wiki/HowToContribute).

# Resources #

  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [What's New in Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Update)
  * [Tellurium User Guide](http://code.google.com/p/aost/wiki/UserGuide070Introduction?tm=6)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [jQuery Simpletip Plugin](http://craigsworks.com/projects/simpletip/)
  * [the Tellurium UI Module Visual Demo at Vimeo](http://vimeo.com/9305675)
  * [Tellurium ui-examples Reference Project](http://aost.googlecode.com/files/tellurium-examples-0.7.0-SNAPSHOT.tar.gz)
  * [Tellurium at Rich Web Experience 2009 by Jian Fang and Vivek Mongolu](http://www.slideshare.net/John.Jian.Fang/tellurium-at-rich-web-experience2009-2806967)