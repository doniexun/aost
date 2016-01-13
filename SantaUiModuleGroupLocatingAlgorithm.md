

# Introduction #

UI Module is the heart of Tellurium Automated Testing Framework. Even UI Module was introduced at the prototype phase, but there was really no algorithm to locate the UI module as a whole. Up to Tellurium 0.6.0, we still need Tellurium core to generate runtime locators based on the UI module definition and then pass Selenium commands to the Selenium core to locate each individual UI element. This procedure can be illustrated by the following diagram.

http://tellurium-users.googlegroups.com/web/xpathjqsel2.png?gda=ZcLbzkEAAACsdq5EJRKLPm_KNrr_VHB_i4k4E3yBw3ZwuTWAYUCsylo_LL8k1Ivp8OS586drcZpTCT_pCLcFTwcI3Sro5jAzlXFeCn-cdYleF-vtiGpWAA&gsc=4508XgsAAACdKjHXeYuQCiQefauhN3Sg

The Santa algorithm is the missing half of the Tellurium UI module concept. The algorithm can locate the whole UI module at the runtime DOM. After that, you can just pass in UI element's UID to find it in the cached UI module on Tellurium Engine. That is to say, you don't need Tellurium Core to generate the runtime locators any more. For compatibility reason, Tellurium Core still generates runtime locators, but they are not really needed if you turn on UI module group locating and caching by calling

```
   useTelluriumEngine(true);
```

Why is the algorithm named **Santa**. This is because I have completed most of the design and coding work during the Christmas season in 2009. It is like a gift for me from Santa Claus.

# The Santa Algorithm #

## Problem ##

Ui Module Group Locating is to locate all elements defined in a UI module by exploiting the relationship among themselves. The problem is to locate the UI module as a whole, not an individual UI element.

## Basic Flow ##

The UI module group locating basic flow is illustrated in the following diagram.

http://tellurium-users.googlegroups.com/web/EngineGroupLocatingFlow.png?gda=CL1f3U0AAACsdq5EJRKLPm_KNrr_VHB_RXAKJQtsnhpNGAFrVZVazbOp4BK91V4s-7udPy6DfdsU9ZdtcfNy08LkG6vh24c05Tb_vjspK02CR95VRrtmeQ&gsc=Yrh2TwsAAAAVdtSV2dFRYROnJX5TWJ5C

First, the Tellurium Engine API accepts a JSON presentation of the UI module. For example,

```
  var json = [{"obj":{"uid":"Form","locator":{"tag":"form"},"uiType":"Form"},"key":"Form"},
   {"obj":{"uid":"Username","locator":{"tag":"tr"},"uiType":"Container"},"key":"Form.Username"},
   {"obj":{"uid":"Label","locator":{"direct":true,"text":"Username:","tag":"td"},"uiType":"TextBox"},"key":"Form.Username.Label"},
   {"obj":{"uid":"Input","locator":{"tag":"input","attributes":{"name":"j_username","type":"text"}},"uiType":"InputBox"},"key":"Form.Username.Input"},
   {"obj":{"uid":"Password","locator":{"tag":"tr"},"uiType":"Container"},"key":"Form.Password"},
   {"obj":{"uid":"Label","locator":{"direct":true,"text":"Password:","tag":"td"},"uiType":"TextBox"},"key":"Form.Password.Label"},
   {"obj":{"uid":"Input","locator":{"tag":"input","attributes":{"name":"j_password","type":"password"}},"uiType":"InputBox"},"key":"Form.Password.Input"},
   {"obj":{"uid":"Submit","locator":{"tag":"input","attributes":{"name":"submit","value":"Login","type":"submit"}},"uiType":"SubmitButton"},"key":"Form.Submit"}];

```

The UI tree, i.e., UTree, builder in Tellurium Engine builds a UTree based on the JSON input. Then Tellurium Engine calls the Santa algorithm to locate all UI elements in the UI module except the elements that are defined as not **cacheable** by two UI object attributes, i.e., _lazy"_ and _noCacheForChildren_. Dynamic elements can be located by searching from its parent and use a subset of the Santa algorithm, which will not be covered here.

Once an element in a UI module is located, its DOM reference is stored into the UTree and an index is also created for fast access. After the Santa algorithm is finished, the UI module is stored into a cache.

## Data Structures ##

The UI module at Tellurium Engine is defined as follows.

```
function UiModule(){
    //top level UI object
    this.root = null;

    //whether the UI module is valid
    this.valid = false;

    //hold a hash table of the uid to UI objects for fast access
    this.map = new Hashtable();

    //index for uid - dom reference for fast access
    this.indices = new Hashtable();

    //If the UI Module is relaxed, i.e., use closest match
    this.relaxed = false;

    //number of matched snapshots
    this.matches = 0;

    //scaled score (0-100) for percentage of match
    this.score = 0;

    //ID Prefix tree, i.e., Trie, for the look Id operation in group locating
    this.idTrie = new Trie();

    //Snapshot Tree, i.e., STree
    this.stree = null;
}
```

From above, you can see the UI module has two indices for fast access. One is UID to UI object mapping and the other one is the UID to DOM reference mapping.

An ID prefix tree, i.e., Trie, is built from UI module JSON presentation if the UI module includes elements with an ID attribute. The Trie is used by the Santa _lookID_ operation. A more detailed Trie build process can be found on the wiki [The UI Module Generating Algorithm in Trump](http://code.google.com/p/aost/wiki/UIModuleGeneratingAlgorithm#A_Trie_Based_Dictionary)

The scaled score is used by the _relax_ operation for partial matching, i.e., closest match, and the score stands for how close the UI module matches the runtime DOM. 100 is a perfect match and zero is no found. This is very powerful to create robust Tellurium test code. That is to say, the Santa algorithm is adapt to changes on the web page under testing to some degree.

## Locate ##

Assume we have UI module as shown in the following graph.

http://tellurium-users.googlegroups.com/web/SantaTeUiModule.png?gda=efAxLkUAAACsdq5EJRKLPm_KNrr_VHB_bIUfD7-F6G2KdUD5D4MxCHMSn2U_NL2M92cFu9ULQMZzlqnWZQD3y6jZqCMfSFQ6Gu1iLHeqhw4ZZRj3RjJ_-A&gsc=-csxfQsAAABJvX1AkrZakeAe14rL-ExC

The group locating procedure is basically a breadth first search algorithm. That is to say, it starts from the root node of the UTree and then its children, its grandchildren, ..., until all node in the UTree has been searched. Santa marks color for already searched node in the UTree and you can see the color changes during the search procedure.

### Main Flow ###

The main flow of group locating can be self-explained by the following greatly simplified code snippet.

```
UiAlg.prototype.santa = function(uimodule, rootdom){
    //start from the root element in the UI module
    if(!uimodule.root.lazy){
        //object Queue
        this.oqueue.push(uimodule.root);

        var ust = new UiSnapshot();
        //Snapshot Queue
        this.squeue.push(ust);
    }
    while(this.oqueue.size() > 0){
        var uiobj = this.oqueue.pop();
        uiobj.locate(this);
   }

   //bind snapshot to the UI Module
   this.bindToUiModule(uimodule, snapshot);

   //unmark marked UID during the locating procedure
   this.unmark();     
```

Where the locate procedure is defined as follows.

```
UiAlg.prototype.locate = function(uiobj, snapshot){
    //get full UID
    var uid = uiobj.fullUid();
    var clocator = uiobj.locator;

    //get parent's DOM reference
    var pref = snapshot.getUi(puid);
    
    //Build CSS selector from UI object's attributes
    var csel = this.buildSelector(clocator);
    //Starting from its parent, search for the UI element
    var $found = teJQuery(pref).find(csel);

    //if multiple matches, need to narrow down
    if($found.size() > 1){
        if(uiobj.noCacheForChildren){
            //dynamic elements, use bestEffort operation
            $found = this.bestEffort(uiobj, $found);
 
        }else{
            //first try lookId operation
            $found = this.lookId(uiobj, $found);
            if($found.size() > 1){
                //then try lookAhead operation
                $found = this.lookAhead(uiobj, $found);
            }
        }
    }

   ...
   if($found.size() == 0){
        if(this.allowRelax){
            //use the relax operation
            var result = this.relax(clocator, pref);
        }  
   }
};

```

### Branch and Trim ###

Santa is basically a branch and trim search procedure on the runtime DOM. Assume at some point, the Santa algorithm has located UI elements A, B, and C. A snapshot has been generated as shown in the following diagram.

http://tellurium-users.googlegroups.com/web/SantaLocate1.png?gda=YaAO4EIAAACsdq5EJRKLPm_KNrr_VHB_bIUfD7-F6G2KdUD5D4MxCDC-P9WsSQjHe215MPg7qVZV4u3aa4iAIyYQIqbG9naPgh6o8ccLBvP6Chud5KMzIQ&gsc=-csxfQsAAABJvX1AkrZakeAe14rL-ExC

When Santa locates UI element D, it finds two matches. Santa branches the snapshot tree and create two separate ones with each hold a different D node.

http://tellurium-users.googlegroups.com/web/SantaLocate2.png?gda=mouCvUIAAACsdq5EJRKLPm_KNrr_VHB_bIUfD7-F6G2KdUD5D4MxCBn8D5nQVokhIuqb621TkD1V4u3aa4iAIyYQIqbG9naPgh6o8ccLBvP6Chud5KMzIQ&gsc=-csxfQsAAABJvX1AkrZakeAe14rL-ExC

After couple steps, Santa locates the UI element G, it removes one of the snapshot trees because it cannot find G from the removed snapshot. Hence, only one snapshot tree is left.

http://tellurium-users.googlegroups.com/web/SantaLocatingTrim.png?gda=47DSH0cAAACsdq5EJRKLPm_KNrr_VHB_Vlnhy29Pl04MMzeNE9O6BpUJa53osfU1upinOmkFwyIVeY4b49xGcMK802iZZ8SFeV4duv6pDMGhhhZdjQlNAw&gsc=Y395mgsAAABmTw12zWjEGVzs38bDXQRc

Of course, the actual locating procedure is much more complicated than what described here. But this should be able to give you some idea on how the branch and trim procedure works.

### Multiple-Match Reduction Mechanisms ###

As you can see from the above procedure, it would be time-consuming if Santa branches too frequently and creates too many snapshot trees because Santa needs to exploit every possible snapshot. As a result, Santa introduced the following multiple-match reduction mechanisms to reduce the number of snapshot trees it needs to search on.

#### Mark ####

When Santa locates a node at the DOM, it marks it with its UID.

```
        $found.eq(0).data("uid", uid);
```

In this way, Santa will skip this DOM node when it tries to locate other UI elements in the UI module.

When Santa finishes the group locating procedure, it unmarks all the uids from the DOM nodes.

#### Look Ahead ####

Look Ahead means to look at not only the current UI element but also its children when Santa locates it. For example, when Santa locates the node D, it also looks at its children G and H. This could decrease snapshot trees at the early search stage and thus reduce the UI module locating time.

http://tellurium-users.googlegroups.com/web/SantaLocateLookChildren.png?gda=giCJ100AAACsdq5EJRKLPm_KNrr_VHB_DQbB4dkECwACM3mZO-ksNZbYqNF9gYK0tqjCPQpPzwk78QNvYVH-EzLyPyoQaO3f5Tb_vjspK02CR95VRrtmeQ&gsc=L_MyaAsAAAAFA-mmrSq3wKWi9zbFEGr6

#### Look ID ####

The ID attribute uniquely defines a UI element on a web page and locating a DOM element by its ID is very fast, thus, Tellurium Engine builds an ID prefix tree, i.e., Trie, when it parses the JSON presentation of the UI module. For example, assume the UI module has four elements, A, D, F, and G, with an ID attribute. The Trie looks as follows.

http://tellurium-users.googlegroups.com/web/SantaLookIDTrie.png?gda=H9_7kUUAAACsdq5EJRKLPm_KNrr_VHB_Nen3NTP9dP3oTwzmB8VfKSSWhnpA9T1yOhjOCab04vJzlqnWZQD3y6jZqCMfSFQ6Gu1iLHeqhw4ZZRj3RjJ_-A&gsc=_MkLKwsAAACPGU9cKreSbkxa0SOycyXv

When Santa locates the UI element A, it can use the IDs for element A and D to reduce multiple matches. If Santa locates element D, only the ID of element G is helpful.

#### Best Effort ####

Best effort is similar to the Look Ahead mechanism, but it is for dynamic UI elements defined Tellurium templates. For dynamic elements, Tellurium defines the following two attributes to determine whether it and its children are cacheable.

```
var UiObject = Class.extend({
    init: function(){
        ...
        //should we do lazy locating or not, i.e., wait to the time we actually use this UI object
        //usually this flag is set because the content is dynamic at runtime
        //This flag is correspond to the cacheable attribute in a Tellurium Core UI object
        this.lazy = false;
    }
});

var UiContainer = UiObject.extend({
    init: function(){
        ...
        this.noCacheForChildren = false;
    },

```

For a dynamic UI element defined by a UI template, it may have zero, one, or multiple matches at runtime. Santa defines a **Bonus Point** for dynamic UI elements. The bonus calculation is straightforward as shown in the following _calcBonus_ method, where variable _one_ is the parent DOM reference and _gsel_ is a set of CSS selectors of current node's children defined by [Tellurium UI templates](http://code.google.com/p/aost/wiki/UserGuide070TelluriumBasics#UI_Templates).

```
UiAlg.prototype.calcBonus = function(one, gsel){
    var bonus = 0;
    var $me = teJQuery(one);
    for(var i=0; i<gsel.length; i++){
        if($me.find(gsel[i]).size() > 0){
            bonus++;
        }
    }

    return bonus;
};
```

If the DOM matches more attributes defined by a UI template, the candidate DOM reference usually gets a higher bonus point. Santa chooses the candidate with the highest bonus point into the snapshot tree.

## Relax ##

The relax procedure, i.e., closest match, is to match the UI attribute with the DOM node as closely as possible. A **Match Score** is defined to measure how many attributes match the one on the DOM node. The total score is scaled to 0-100 at the end. The snapshot with the highest match score is selected.

The following simplified code snippet should give you some idea of how the relax procedure works.

```
        //the tag must be matched 
        var jqs = tag;
        //attrs is the attributes defined by a UI template
        var keys = attrs.keySet();

        //number of properties, tag must be included
        var np = 1;
        //number of matched properties
        var nm = 0;

        if (keys != null && keys.length > 0) {
            np = np + keys.length;
            for (var m = 0; m < keys.length; m++) {
                var attr = keys[m];
                //build css selector
                var tsel = this.cssbuilder.buildSelector(attr, attrs.get(attr));
                var $mt = teJQuery(pref).find(jqs + tsel);
                if ($mt.size()> 0) {
                    jqs = jqs + tsel;
                    if(nm == 0){
                        nm = 2;
                    }else{
                        nm++;
                    }
                }
            }
        }

        //calculate match score, scaled to 100 percentage
        var score = 100*nm/np;
```

As shown in the above code, the relax must satisfy one requirement, i.e., the tag name must match the one on the DOM node. Otherwise, the relax result returns as "not found".

# Usage #

For instance, we have the following html snippet to test.

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

Assume the html was changed recently and you still use the following UI module defined some time ago.

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

What will happen without using the Santa algorithm? You tests will be broken because the generated locators will not be correct any more. But if you use the latest Tellurium 0.7.0 snapshot, you will notice that the tests still work if you allow Tellurium to do closest match by calling

```
    useClosestMatch(true);
```

The magic is that the new Tellurium Engine will locate the UI module as a whole. It may have some trouble to find some individual UI elements such as "ProblematicForm.Username.Input", but it has no problem to locate the whole UI module structure in the DOM.

Apart from that, Tellurium 0.7.0 also provides a handy method for you to validate your UI module. For example, if you call

```
    validateUiModule("ProblematicForm");
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

# Want to Contribute ? #

We welcome contributions for Tellurium from various aspects. Right now, we are looking for new members to join our dev team. More details on [How to Contribute](http://code.google.com/p/aost/wiki/HowToContribute).

# Resources #

  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [The UI Module Generating Algorithm in Trump](http://code.google.com/p/aost/wiki/UIModuleGeneratingAlgorithm#A_Trie_Based_Dictionary)
  * [What's new in Tellurium 0.7.0](http://code.google.com/p/aost/wiki/Tellurium070Update)
  * [Tellurium User Guide](http://code.google.com/p/aost/wiki/UserGuide070Introduction)
  * [Tellurium ui-examples Reference Project](http://aost.googlecode.com/files/tellurium-examples-0.7.0-SNAPSHOT.tar.gz)