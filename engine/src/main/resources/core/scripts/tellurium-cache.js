//Ui Module cache data
function UmCacheData(){
    this.uiModule = null;
    this.count = 0;
    this.timestamp = Number(new Date());
};

//Cached Data, use uid as the key to reference it
function CacheData(){
    //jQuery selector associated with the DOM reference, which is a whole selector
    //without optimization so that it is easier to find the reminding selector for its children
    this.selector = null;
    //optimized selector for actual DOM search
    this.optimized = null;
    //jQuery object for DOM reference
    this.reference = null;
    //number of reuse
    this.count = 0;
    //last use time
    this.timestamp = Number(new Date());
};

//cache eviction policies
//simply discard new selector
function DiscardNewPolicy(){
    this.name = "DiscardNewPolicy";
};

DiscardNewPolicy.prototype.applyPolicy = function (cache, key, data){

};

DiscardNewPolicy.prototype.myName = function(){
    return this.name;
};

var discardNewCachePolicy = new DiscardNewPolicy();

function DiscardOldPolicy(){
    this.name = "DiscardOldPolicy";
};

DiscardOldPolicy.prototype.applyPolicy = function (cache, key, data){
    var keys = cache.keySet();
    var toBeRemoved = keys[0];
    var oldest = cache.get(toBeRemoved).timestamp;
    for(var i=1; i<keys.length; i++){
        var akey = keys[i];
        var val = cache.get(akey).timestamp;
        if(val < oldest){
            toBeRemoved = akey;
            oldest = val;
        }
    }

    cache.remove(toBeRemoved);
    cache.put(key, data);
};

DiscardOldPolicy.prototype.myName = function(){
    return this.name;
};

var discardOldCachePolicy = new DiscardOldPolicy();

//remove the least used select in the cache
function DiscardLeastUsedPolicy (){
    this.name = "DiscardLeastUsedPolicy";
};

DiscardLeastUsedPolicy.prototype.applyPolicy = function(cache, key, data){
    var keys = cache.keySet();
    var toBeRemoved = keys[0];
    var leastCount = cache.get(toBeRemoved).count;
    for(var i=1; i<keys.length; i++){
        var akey = keys[i];
        var val = cache.get(akey).count;
        if(val < leastCount){
            toBeRemoved = akey;
            leastCount = val;
        }
    }

    cache.remove(toBeRemoved);
    cache.put(key, data);
};

DiscardLeastUsedPolicy.prototype.myName = function(){
    return this.name;
};

var discardLeastUsedCachePolicy = new DiscardLeastUsedPolicy();

//If found invalid selector, remove it and put the new one in
//otherwise, discard the new one
function DiscardInvalidPolicy(){
    this.name = "DiscardInvalidPolicy";
};

DiscardInvalidPolicy.prototype.applyPolicy = function(cache, key, data){
    var keys = cache.keySet();
    for(var i=0; i<keys.length; i++){
        var akey = keys[i];
        if(!akey.valid){
            cache.remove(akey);
            cache.put(key, data);
            break;            
        }
/*
        var $ref = cache.get(akey).reference;
        var isVisible = false;
        try{
           isVisible = $ref.is(':visible');
        }catch(e){
            isVisible = false;
        }
        if(!isVisible){
            cache.remove(akey);
            cache.put(key, data);
            break;
        }
*/
    }
};

DiscardInvalidPolicy.prototype.myName = function(){
    return this.name;
};

var discardInvalidCachePolicy = new DiscardInvalidPolicy();

function TelluriumCache(){

    //global flag to decide whether to cache jQuery selectors
    this.cacheOption = false;

    //cache for jQuery selectors
    this.sCache = new Hashtable();

    this.maxCacheSize = 50;

    this.cachePolicy = discardOldCachePolicy;

    //Algorithm handler for UI
    this.uiAlg = new UiAlg();
};

TelluriumCache.prototype.cleanCache = function(){
    this.sCache.clear();
};

TelluriumCache.prototype.getCacheSize = function(){
    return this.sCache.size();
};

TelluriumCache.prototype.updateUseCount = function(key, data){
    if(key != null && data != null){
        data.count++;
        this.sCache.put(key, data);
    }
};

TelluriumCache.prototype.addToCache = function(key, val){
    if(this.sCache.size() < this.maxCacheSize){
        this.sCache.put(key, val);
    }else{
        this.cachePolicy.applyPolicy(this.sCache, key, val);
    }
};

//update existing data to the cache
TelluriumCache.prototype.updateToCache = function(key, val){
    val.count++;
    val.timestamp = Number(new Date());
    this.sCache.put(key, val);
};

TelluriumCache.prototype.getCachedData = function(key){

    return this.sCache.get(key);
};

TelluriumCache.prototype.getCachedUiElement = function(uid){

    var uiid = new Uiid();
    uiid.convertToUiid(uid);
    if(uiid.size() > 0){
        var first = uiid.peek();
        var uim = this.sCache.get(first);
        if(uim != null){
            var context = new WorkflowContext();
            var obj = uim.walkTo(context, uiid);
            if(obj != null){
                this.sCache.updateToCache(first, uim);
            }

            return obj;
        }
    }

    return null;
};

TelluriumCache.prototype.useUiModule = function(json){
    var uim = new UiModule();
    fbLog("Input JSON for UI Module: ", json);
    uim.parseUiModule(json);
//    uim.prelocate();
    this.uiAlg.santa(uim, null);
    //set the UI Module to be valid after it is located
    uim.valid = true;
    fbLog("Ui Module after Group Locating: ", uim);
    var id = uim.getId();
    var cached = this.getCachedData(id);
    if(cached == null){
        this.addToCache(id, uim);
    }else{
        this.updateToCache(id, uim);
    }
};

TelluriumCache.prototype.isUiModuleCached = function(id){

    return this.sCache.get(id) != null;
};

//Cache Selectors

TelluriumCache.prototype.getCachedSelector = function(key){

    return this.sCache.get(key);
};

TelluriumCache.prototype.addSelectorToCache = function(key, data){
    if(this.sCache.size() < this.maxCacheSize){
        this.sCache.put(key, data);
    }else{
        this.cachePolicy.applyPolicy(this.sCache, key, data);
    }
};

//update existing selector to the cache
TelluriumCache.prototype.updateSelectorToCache = function(key, data){
    data.count++;
    data.timestamp = Number(new Date());
    this.sCache.put(key, data);
};

TelluriumCache.prototype.validateCache = function($reference){
    //This may impose some problem if the DOM element becomes invisable instead of being removed
    try{
        return $reference.is(':visible');
//        return ($reference.eq(0).parent().length > 0);
//        return jQuery($reference).is(':visible');
//        return jQuery($reference).parents('html').length > 0;
//        return $reference.parents('html').length > 0;
//        return true;
    }catch(e){
        //seems for IE, it throws exception
//        jslogger.error("Validate exception " + e.message);
        return false;
    }
};

TelluriumCache.prototype.checkSelectorFromCache = function(key){
    var $found = null;

    var cached = this.getCachedSelector(key);
    if (cached != null) {
        $found = cached.reference;
        //validate the DOM reference
        if (!this.validateCache($found)) {
            $found = null;
            this.sCache.remove(key);
        }else{
            this.updateSelectorToCache(key, cached);
        }
    }

    return $found;
};

TelluriumCache.prototype.checkAncestorSelector = function(akey){
    var cached = this.getCachedSelector(akey);

    if (cached != null) {
        //check if the ancestor's DOM reference is still valid
        if (!this.validateCache(cached)) {
            //if not valid, try to select it using jQuery
            var $newsel = teJQuery(cached.optimized);
            if ($newsel.length > 0) {
                cached.reference = $newsel;
                cached.count = 0;
                this.updateSelectorToCache(akey, cached);
            } else {
                //remove invalid selector
                this.sCache.remove(akey);
                cached = null;
            }
        }else{
            this.updateSelectorToCache(akey, cached);
        }
    }

    return cached;
};

TelluriumCache.prototype.findFromAncestor = function(ancestor, sel){
    var asel = ancestor.selector;
    var $found = null;

    if(sel.length > asel.length){
        var start = sel.substring(0, asel.length);
        if(start == asel){
            var leftover = trimString(sel.substring(asel.length));
            $found = ancestor.reference.find(leftover);
        }
    }

    return $found;
};

TelluriumCache.prototype.setCacheState = function(flag){
    this.cacheOption = flag;
};

TelluriumCache.prototype.getCacheUsage = function(){
    var out = [];
    var keys = this.sCache.keySet();
    for(var i=0; i< keys.length; i++){
        var key = keys[i];
        var val = this.sCache.get(key);
        var usage = {};
        usage[key] = val.count;
        out.push(usage);
    }

    return JSON.stringify(out);
};

TelluriumCache.prototype.useDiscardNewPolicy = function(){
    this.cachePolicy = discardNewCachePolicy;
};

TelluriumCache.prototype.useDiscardOldPolicy = function(){
    this.cachePolicy = discardOldCachePolicy;
};

TelluriumCache.prototype.useDiscardLeastUsedPolicy = function(){
    this.cachePolicy = discardLeastUsedCachePolicy;
};

TelluriumCache.prototype.useDiscardInvalidPolicy = function(){
    this.cachePolicy = discardInvalidCachePolicy;
};

TelluriumCache.prototype.getCachePolicyName = function(){
    return this.cachePolicy.name;
};
