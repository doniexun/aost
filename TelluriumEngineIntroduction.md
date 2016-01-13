

# Introduction #

Tellurium Engine is an object based test driving engine. It has the following features.

  * UI module group locating
  * UID based API
  * Macro Command
  * UI module caching and reuse

Tellurium Engine is a test driving engine similar to Selenium Core. But why we need a new test driving engine? The reason is that Selenium Core is locator based, i.e, it focuses on individual UI element. But Tellurium is UI module, i.e., widget, based. Tellurium Engine could locate the whole UI module in one attempt, cache it for later reuse. In this way, Tellurium Engine is more efficient to handle UI modules. Thus, we need a new test driving engine.

# Architecture #

Tellurium architecture is shown in the following diagram.

http://tellurium-users.googlegroups.com/web/telluriumnewarchitecture070.png?gda=0Cry3VEAAAA7fMi2EBxrNTLhqoq3FzPraJRXcLj8Noz0FE1yhGyZ4Cj9p0mTDab5g6bwssabHmxXgg0UR0O9X9-Irzu_uB8AUwk_6Qi3BU8HCN0q6OYwM5VxXgp_nHWJXhfr7YhqVgA&gsc=jkTIIgsAAACI9y1tsPXdLT5_3omT2FAm

The workflow of Tellurium Engine is shown in the following sequence diagram.

http://tellurium-users.googlegroups.com/web/TelluriumRunningMode2.png?gda=kwN3wEsAAAA7fMi2EBxrNTLhqoq3FzPro_2kwg16ExMe9wfbYSgXCQdoVt8RgM6Q8hpOVtSHouWqzYvvFLF4HH6kcFg68ZqyBkXa90K8pT5MNmkW1w_4BQ&gsc=jkTIIgsAAACI9y1tsPXdLT5_3omT2FAm

The Tellurium Core will convert the UI module into a JSON representation and pass it to Tellurium Engine for the first time when the UI module is used. The Tellurium Engine uses the Santa algorithm to locate the whole UI module and put it into a cache. For the sequent calls, the cached UI module will be used instead of locating them again.

A normal Tellurium call flow is illustrated by the following diagram.

http://tellurium-users.googlegroups.com/web/EngineCallFlow.png?gda=XLvDt0QAAACUfGPUXLvcuK0uzit5_KjesoSc0t0-ML4CpJcI1zDB__ks16rt3dIrSOBrH6RbcC1V6u9SiETdg0Q2ffAyHU-dzc4BZkLnSFWX59nr5BxGqA&gsc=uHB3QQsAAAA6XNPREUnCgB2jJaR_3yZJ

The API tries to find the UI module from cache, then walks to the UI object by its UID. After that, it calls the method on the UI object, which delegates the call to the command executor under the hood.

# Implementation #

## APIs ##

### Tellurium Object Definition ###

Tellurium object is the main object in Tellurium Engine.

```
function Tellurium(){

    this.currentWindow = null;

    this.currentDocument = null;

    //whether to use Tellurium new jQuery selector based APIs
    this.isUseTeApi = false;

    //Macro command for Tellurium
    this.macroCmd = new MacroCmd();

    this.browserBot = new TelluriumBrowserBot();

    this.dom = null;

    this.cache = new TelluriumUiCache();

    this.uiAlg = new UiAlg();

    this.textWorker = new TextUiWorker();

    this.cssBuilder = new JQueryBuilder();

    this.cmdMap = new Hashtable();

    //UI object name to Javascript object builder mapping
    this.uiBuilderMap = new Hashtable();

    //JQuery Builder
    this.jqbuilder = new JQueryBuilder();

    //identifier generator
    this.idGen = new Identifier(100);

    //log manager for Tellurium
    this.logManager = new LogManager();

    //outlines
    this.outlines = new Outlines();

    this.currentDom = null;

    this.jqExecutor = new JQueryCmdExecutor();
    this.synExecutor = new SynCmdExecutor();
    this.selExecutor = new SeleniumCmdExecutor();
    
    this.cmdExecutor = this.synExecutor;

    //Proxy object
    this.proxyObject = new UiProxyObject();
}
```

### Macro Command ###

Multiple API calls could be bundled into one API call, i.e., `getBundleResponse(bundle)`. This bundle is called [Macro Command](http://code.google.com/p/aost/wiki/UserGuide070TelluriumAdvancedTopics#Macro_Command).

For most Tellurium APIs, the entry point is the following extended Selenium command.

```
Selenium.prototype.getBundleResponse = function(bundle){
    tellurium.parseMacroCmd(bundle);
    return tellurium.dispatchMacroCmd();
};
```

The following method goes over each command in the bundle, dispatch to Tellurium Engine or Selenium core, and construct the response object for the bundle.

```
Tellurium.prototype.dispatchMacroCmd = function(){
    var response = new BundleResponse();

    while (this.macroCmd.size() > 0) {
        var cmd = this.macroCmd.first();
        if(cmd.name == "getUseUiModule"){
            //do UI module locating
            var result = this.useUiModule(cmd.args[0]);
            response.addResponse(cmd.sequ, cmd.name, ReturnType.OBJECT, result);
        }else{
            //for other commands
             if ((!this.isUseTeApi) || this.isApiMissing(cmd.name)) {
                this.delegateToSelenium(response, cmd);
            }else{
               this.delegateToTellurium(response, cmd);
            }
        }
    }

    return response.toJSon();
};
```

### UID Based APIs ###

Tellurium Engine API is [UID](http://code.google.com/p/aost/wiki/UserGuide070TelluriumBasics#UiID_Attribute) based, not locator base. For example, the type method is defined as.

```
  Tellurium.prototype.type = function(uid, val){
    ......
  }
```

That is to say, you can call the api like

```
  type("GoogleSearchModule.Input", "Tellurium test")
```

The API execution flow is as follows, i.e., it tries to find the UI object from the UID, and then check if the UI object has the method defined. If it does, then call the method, otherwise, throw an exception.

```
Tellurium.prototype.execCommand = function(cmd, uid, param){
    var context = new WorkflowContext();
    context.alg = this.uiAlg;
    var uiid = new Uiid();
    uiid.convertToUiid(uid);

    var first = uiid.peek();
    var uim = this.cache.get(first);
    if(uim != null){
        var obj = uim.walkTo(context, uiid);
        if(obj != null){
            if(obj.respondsTo(cmd)){
                var params = [context];
                if(param != null && param != undefined){
                    params.push(param);
                }
                return obj[cmd].apply(obj, params);
            }else{
                  throw new TelluriumError(ErrorCodes.INVALID_CALL_ON_UI_OBJ, "UI Object " + uid  + " of type "
                        + obj.uiType + " and tag " + obj.tag + " does not have method " + cmd);
            }
        }else{
            throw new TelluriumError(ErrorCodes.UI_OBJ_NOT_FOUND, "Cannot find UI object " + uid);
        }
    }else{
        var proxyObject = this.proxyObject.walkTo(context, uid);
        if(proxyObject != null){
            if(proxyObject.respondsTo(cmd)){
                var params = [context];
                if(param != null && param != undefined){
                    params.push(param);
                }

                return proxyObject[cmd].apply(proxyObject, params);
            }else{
                throw new TelluriumError(ErrorCodes.INVALID_CALL_ON_UI_OBJ, "Proxy UI Object " + uid + " does not have method " + cmd);
            }    
        }else{
            throw new TelluriumError(ErrorCodes.UI_MODULE_IS_NULL, "Cannot find UI Module " + first);
        }
    }
};
```

The proxy object is used for general purpose objects that are internally used by the API call getUiByTag().

## Tellurium UI Objects ##

### UI Object Definition ###

[Tellurium UI objects](http://code.google.com/p/aost/wiki/UserGuide070UIObjects#Tellurium_UI_Objects) are build blocks for Tellurium UI module. The basic UI object is defined as

```
//base UI object
var UiObject = Class.extend({
    
    init: function() {
        //reference ID during UI module recording and generating process
        this.refId = null;

        //UI object identification
        this.uid = null;

        //meta data
        this.metaData = null;

        //its parent UI object
        this.parent = null;

        //namespace, useful for XML, XHTML, XForms
        this.namespace = null;

        this.locator = null;
        
        //optional attributes
        this.optionalAttributes = null;

        //event this object should be respond to
        this.events = null;

        //should we do lazy locating or not, i.e., wait to the time we actually use this UI object
        //usually this flag is set because the content is dynamic at runtime
        this.lazy = false;

        //If it is contained in its parent or not
        this.self = false;

        this.uiType = null;

        //Tellurium Core generated locator for this UI Object
        this.generated = null;

        //dom reference
        this.domRef = null;

        //UI Module reference, which UI module this UI object belongs to
        this.uim = null;

        //the node associated with this UiObject
        this.node = null;
        
    },

    fullUid: function() {
        if (this.parent != null) {
            return this.parent.fullUid() + "." + this.uid;
        }

        return this.uid;
    },

    respondsTo: function(methodName) {
        return this[methodName] != undefined; 
    },

    traverse: function(context, visitor){
        visitor.visit(context, this);
    },

    around: function(context, visitor){
        visitor.before(context, this);
        visitor.after(context, this);
    },

    walkTo: function(context, uiid) {
       .......
    },

......
}
```

In addition, the basic UI object includes a list of methods existing in all UI objects.

```
    focus: function(context){
        var element = context.domRef;
        tellurium.cmdExecutor.focus(element);
    },

    click: function(context){
        var element = context.domRef;
        tellurium.cmdExecutor.click(element);
    },

    mouseOver: function(context){
        var element = context.domRef;
        tellurium.cmdExecutor.mouseOver(element);
    },

    getValue: function(context) {
        var element = context.domRef;
        return tellurium.cmdExecutor.getValue(element);
    },

    getAttribute: function(context, attribute){
        var element = context.domRef;
        return tellurium.cmdExecutor.getAttribute(element, attribute);
    },

    getText: function(context){
        var element = context.domRef;
        return tellurium.cmdExecutor.getText(element);
    },

    isVisible: function(context){
        var element = teJQuery(context.domRef);
        return tellurium.cmdExecutor.isVisible(element);
    },
    
......

```

where the command executor cmdExecutor will be discussed in the next section.

All Tellurium UI objects inherit from the basic object, for example, The InputBox object has extra methods such as type() and typeKey().

```
var UiInputBox = UiObject.extend({
    init: function(){
        this._super();
        this.uiType = 'InputBox';
        this.tag = "input";
    },

    type: function(context, val){
        var element = context.domRef;
        tellurium.cmdExecutor.type(element, val);
    },

    typeKey: function(context, key){
        var element = context.domRef;
        tellurium.cmdExecutor.typeKey(element, key);
    },
......
});

```

The List and Table objects are used to represent UI templates in Tellurium. For example, the List object is defined as.

```

var UiList = UiContainer.extend({
    init: function(){
        this._super();
        this.uiType = 'List';
        this.noCacheForChildren = true;
        this.separator = null;
        this.defaultUi = new UiTextBox();
        this.rTree= new RTree();
    },

    getListSize: function(context){
        ......
    },

    ......
```

The flag noCacheForChildren is used to indicate that the List holds UI templates and the Engine will not try to cache its child nodes. The rTree is used to for [Tellurium UID Description Language](http://code.google.com/p/aost/wiki/TelluriumUIDDescriptionLanguage) to find the actual UID reference.

UI objects are build from UI object builders. The basic UI object builder is defined as.

```

var UiObjectBuilder = Class.extend({
    build: function(){
        return new UiObject();
    },

    buildFrom: function(attributes, respond){
        var obj = this.build();
        obj.tag = attributes.get(CONSTANTS.TAG);
        obj.respond = respond;
        obj.locator = new CompositeLocator();
        obj.locator.buildLocator(attributes);

        return obj;
    }
});

```

All UI object builders extend the basic builder, for example.

```
var UiInputBoxBuilder = UiObjectBuilder.extend({
    build : function(){
        return new UiInputBox();
    }
});
```

### Command Executor ###

The command executor is the actual code to act on JavaScript events. The basic executor is defined as.

```
var CmdExecutor = Class.extend({

    init: function() {
        this.ctrl = false;
        this.shift = false;
        this.alt = false;
        this.meta = false;
    }
});

```

Tellurium Engine defined three command executors. The first one is jQuery command executor, i.e, use jQuery to fire all JavaScript events and get DOM attributes and states.

```

var JQueryCmdExecutor = CmdExecutor.extend({

    init: function() {
        this._super();
    },


    fireEvent: function(element, event){
        teJQuery(element).trigger(event);
    },

    blur: function(element){
        teJQuery(element).blur();
    },
    getText: function(element){
        return teJQuery(element).text();
    },

    isVisible: function(element){
        var isHiddenCSS = element.css("visibility") == "hidden" ? true : false;
        var isHidden = element.is(":hidden");

        if (isHidden) {
            return false;
        } else if (isHiddenCSS) {
            return false;
        } else {
            return true;
        }
    },

    ......

}
```

Syn command executor is used to solve the problem that jQuery click event does not work properly in some web application.

```
var SynCmdExecutor = JQueryCmdExecutor.extend({

    init: function() {
        this._super();
    },

    click: function(element){
        Syn.click(element);
    },

    ......

});

```

The last one is the Selenium command executor, i.e, dispatch command to Selenium core for actions only (not do locating).

```

var SeleniumCmdExecutor = SynCmdExecutor.extend({

    init: function() {
        this._super();
    },

    click: function(element) {
        tellurium.setCurrentDom(element);
        selenium.doClick("tedom=current");
    },

    ......
}
```

To use different command executors, simply set the command executor in the Tellurium object. For example, we can select the Syn command executor in the following way.

```
function Tellurium(){
    ......
    this.cmdExecutor = this.synExecutor;
}
```

## UI Module ##

### UI Module Definition ###

UI module is a nested UI object and it is defined as follows.

```
function UiModule(){
    
    this.id = null;

    //the document object this UI module lives on
    this.doc = null;

    //top level UI object
    this.root = null;

    this.valid = false;

    //hold a hash table of the uid to UI objects for fast access
    this.map = new Hashtable();

    //index for uid - dom reference for fast access
    this.indices = null;

    //If the UI Module is relaxed, i.e., use closest match
    this.relaxed = false;

    //the relax details including the UIDs and their corresponding html source
    this.relaxDetails = null;

    //number of matched snapshots
    this.matches = 0;

    //scaled score (0-100) for percentage of match
    this.score = 0;

    //ID Prefix tree, i.e., Trie, for the lookForId operation in group locating
    this.idTrie = new Trie();

    //Cache hit, i.e., direct get dom reference from the cache
    this.cacheHit = 0;

    //Cache miss, i.e., have to use walkTo to locate elements
    this.cacheMiss = 0;

    //the latest time stamp for the cache access
    this.timestamp = null;

    //UI module dump visitor
    this.dumpVisitor = new UiDumpVisitor();

    //Snapshot Tree, i.e., STree
    this.stree = null;

    this.valid = false;
}

```

where the root member holds the tree to represent the UI module and most methods on UI module are based on the tree. For example.

```

UiModule.prototype.visit = function(visitor){
    if(this.root != null){
        var context = new WorkflowContext();
        this.root.traverse(context, visitor);
    }
};

UiModule.prototype.around = function(visitor){
    if(this.root != null){
        var context = new WorkflowContext();
        this.root.around(context, visitor);
    }
};

```

We can use the visitor design pattern to work on the tree. For example, the following visitor is used to convert the UI module into a string.

```

var AroundChainVisitor = Class.extend({
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

    before: function(context, snode){
        for(var i=0; i<this.chain.length; i++){
            this.chain[i].before(context, snode);
        }
    },

    after: function(context, snode){
        for(var i=0; i<this.chain.length; i++){
            this.chain[i].after(context, snode);
        }
    }
});

var StringifyVisitor = AroundVisitor.extend({
    init: function(){
        this.out = new Array();
    },

    before: function(context, node){
        var level = node.checkLevel();
        var str = node.strUiObject(level);
        this.out.push(str);
    },

    after: function(context, node){
        if(node.hasChildren()){
            var level = node.checkLevel();
            var str = node.strUiObjectFooter(level);
            this.out.push(str);
        }
    }
});

```

UI module object also includes method to parse UI module from a JSON representation.

```
UiModule.prototype.parseUiModule = function(ulst){
    var klst = new Array();

    for(var i=0; i<ulst.length; i++){
        this.map.put(ulst[i].key, this.buildFromJSON(ulst[i].obj));
        klst.push(ulst[i].key);
    }

    this.buildTree(klst);
};

```

Another important method on the UI module object is validate, which is used to check if the UI module could be found from the runtime dom.

```
UiModule.prototype.validate = function(alg, doc){
    if (doc)
        alg.validate(this, doc);
    else
        alg.validate(this, this.doc);

    var found = false;
    if (this.score == 100 || (!this.relaxed)){
        found = true;
    }
    
   ......
};
```

### UI Module Cache ###

[UI modules are cached for reuse in Tellurium](http://code.google.com/p/aost/wiki/UserGuide070TelluriumAdvancedTopics#UI_Module_Caching). The cache object is defined as

```

function TelluriumUiCache(){

    //cache for UI modules
    this.cache = new Hashtable();

    this.maxCacheSize = 50;

    this.cachePolicy;

    //Cache hit, i.e., direct get dom reference from the cache
    this.cacheHit = 0;

    //Cache miss, i.e., have to use walkTo to locate elements
    this.cacheMiss = 0;

}
```

The cached data includes the data itself, a time stamp, and a counter.

```
function CacheData(){
    this.data = null;
    this.timestamp = null;
    this.count = 0;
}
```

Tellurium Engine provides the following cache polices to automatically handle cache.

```
var CachePolicy = Class.extend({
    init: function(){
        this.name = "";
    },

    myName: function(){
        return this.name;
    },

    applyPolicy: function (cache, key, data){

    }
});

var DiscardNewPolicy = CachePolicy.extend({
    init: function(){
        this.name = "DiscardNewPolicy";
    }
});

var DiscardOldPolicy = CachePolicy.extend({
    init: function() {
        this.name = "DiscardOldPolicy";
    },
    ......
};

var DiscardLeastUsedPolicy = CachePolicy.extend({
    init: function() {
        this.name = "DiscardLeastUsedPolicy";
    },

    ......
};

var DiscardInvalidPolicy = CachePolicy.extend({
    init: function() {
        this.name = "DiscardOldPolicy";
    },

    ......
}
```

### UI Module Algorithms ###

The UiAlg object defines the algorithms used to handle UI modules and UI objects.

```
//algorithms to handle UI modules and UI Objects
function UiAlg(){
    //current root DOM element
    this.dom = null;

    //whether allow to use closest matching element if no one matches
    this.allowRelax = false;

    //FIFO queue to hold UI snapshots
    this.squeue = new FifoQueue();

    //FIFO queue to hold UI objects in the UI module
    this.oqueue = new FifoQueue();

    //jQuery builder to build CSS selectors
    this.cssbuilder = new JQueryBuilder();

}
```

The most important algorithm is [the Santa algorithm](http://code.google.com/p/aost/wiki/SantaUiModuleGroupLocatingAlgorithm) to locate the whole UI module.

```
UiAlg.prototype.santa = function(uimodule, rootdom){

    this.currentColor = this.colors.GRAY;
    //start from the root element in the UI module
    if(!uimodule.root.lazy){
        this.oqueue.push(uimodule.root);
        var ust = new UiSnapshot();
        ust.color = this.colors.GRAY;
        this.squeue.push(ust);
    }else{
        return false;
    }
    while(this.oqueue.size() > 0){
        var uiobj = this.oqueue.pop();

        uiobj.locate(this);
    }
    if(this.squeue.size() == 0){
        throw new SeleniumError("Cannot locate UI module " +  uimodule.root.uid);
    }

    //if allow closest match
    if (this.allowRelax) {
        uimodule.matches = this.squeue.size();
        //use match score to select the best match
        var snapshot = this.squeue.pop();
        var maxscore = snapshot.getScaledScore();
        while (this.squeue.length > 0) {
            var nsnapshot = this.squeue.pop();
            var nscore = nsnapshot.getScaledScore();
            if (nscore > maxscore) {
                snapshot = nsnapshot;
                maxscore = nscore;
            }
        }

        this.bindToUiModule(uimodule, snapshot);
        this.unmark();
    } else {
        //for exact match, cannot have multiple matches
        if (this.squeue.size() > 1) {
            throw new SeleniumError("Found " + this.squeue.size() + " matches for UI module " + uimodule.root.uid);
        }
        //found only one snapshot, happy path
        var osnapshot = this.squeue.pop();
        this.bindToUiModule(uimodule, osnapshot);
        this.unmark();
        uimodule.matches = 1;
    }

    uimodule.valid = true;
    return true;
};
```

## Customize ##

By default, Tellurium Engine registered the following UI objects,

```
Tellurium.prototype.registerDefaultUiBuilders = function(){
    this.uiBuilderMap.put("Button", new UiButtonBuilder());
    this.uiBuilderMap.put("CheckBox", new UiCheckBoxBuilder());
    this.uiBuilderMap.put("Div", new UiDivBuilder());
    this.uiBuilderMap.put("Icon", new UiIconBuilder());
    this.uiBuilderMap.put("Image", new UiImageBuilder());
    this.uiBuilderMap.put("InputBox", new UiInputBoxBuilder());
    this.uiBuilderMap.put("RadioButton", new UiRadioButtonBuilder());
    this.uiBuilderMap.put("Selector", new UiSelectorBuilder());
    this.uiBuilderMap.put("Span", new UiSpanBuilder());
    this.uiBuilderMap.put("SubmitButton", new UiSubmitButtonBuilder());
    this.uiBuilderMap.put("TextBox", new UiTextBoxBuilder());
    this.uiBuilderMap.put("UrlLink", new UiUrlLinkBuilder());
    this.uiBuilderMap.put("Container", new UiContainerBuilder());
    this.uiBuilderMap.put("Frame", new UiFrameBuilder());
    this.uiBuilderMap.put("Form", new UiFormBuilder());
    this.uiBuilderMap.put("List", new UiListBuilder());
    this.uiBuilderMap.put("Table", new UiTableBuilder());
    this.uiBuilderMap.put("StandardTable", new UiStandardTableBuilder());
    this.uiBuilderMap.put("Window", new UiWindowBuilder());
    this.uiBuilderMap.put("Repeat", new UiRepeatBuilder());
    this.uiBuilderMap.put("UiAllPurposeObject", new UiAllPurposeObjectBuilder());
};
```

and the following APIs.

```
Tellurium.prototype.registerCommands = function(){
    this.registerCommand("useTeApi", CommandType.NoUid, ReturnType.VOID, this.useTeApi);
    this.registerCommand("isUseLog", CommandType.NoUid, ReturnType.BOOLEAN, this.isUseLog);
    this.registerCommand("open", CommandType.NoUid, ReturnType.VOID, this.open);
    this.registerCommand("toggle", CommandType.HasUid, ReturnType.VOID, this.toggle);
    this.registerCommand("blur", CommandType.HasUid, ReturnType.VOID, this.blur);
    this.registerCommand("click", CommandType.HasUid, ReturnType.VOID, this.click);

    ......
}
```

Tellurium provides the following two methods for you to register your own UI objects and APIs.

```
//expose this so that users can hook in their own custom UI objects or even overwrite the default UI objects
Tellurium.prototype.registerUiBuilder = function(name, builder){
    this.uiBuilderMap.put(name, builder);
};

Tellurium.prototype.registerCommand = function(name, type, returnType, handler){
    var cmd = new TelluriumCommand(name, type, returnType, handler);
    this.cmdMap.put(name, cmd);
};
```

# Summary #

Tellurium Engine is a novel and young project and it has been used by many users. [Tellurium IDE](http://code.google.com/p/aost/wiki/TelluriumIde080RC2) uses Tellurium Engine to drive tests. But we need more JavaScript experts to join our team to improve the JavaScript event handling, i.e., the Command Executors. If you are interested to help, please join [Tellurium developer group](http://groups.google.com/group/tellurium-developers) and send emails to telluriumsource at gmail dot com.

# Resources #

  * [Tellurium User Group](http://groups.google.com/group/tellurium-users)
  * [Tellurium on Twitter](http://twitter.com/TelluriumSource)
  * [Tellurium IDE 0.8.0 RC2](http://code.google.com/p/aost/wiki/TelluriumIde080RC2) is released
  * Haroon published an article on _Test Experience_ magazine: [Tellurium Automated Testing Framework](http://aost.googlecode.com/files/testingexperience12_12_10_Article.pdf)
  * [InfoQ Article: Introducing the Tellurium Automated Testing Framework](http://www.infoq.com/articles/tellurium_intro)
  * [Tellurium IDE 0.8.0 RC1 tutorial video](http://www.youtube.com/watch?v=yVIBY8QzWzE/)
  * [Tellurium IDE 0.8.0 RC1: Tellurium test script generation](http://code.google.com/p/aost/wiki/TelluriumIde080RC1)