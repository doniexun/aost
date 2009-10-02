package org.tellurium.dsl

import org.tellurium.dsl.BaseDslContext
import org.tellurium.dsl.WorkflowContext
import org.tellurium.exception.NotWidgetObjectException
import org.tellurium.widget.Widget

abstract class DslContext extends BaseDslContext {

    //later on, may need to refactor it to use resource file so that we can show message for different localities
    protected static final String XML_DOCUMENT_SCRIPT = """var doc = window.document;
        var xml = null;
        if(doc instanceof XMLDocument){
           xml = new XMLSerializer().serializeToString(doc);
        }else{
           xml = getText(doc.body);
        }
        xml; """

//    def defUi = ui.&Container

    //Must implement this method to define UI
//    remove this constraint so that DSL script does not need to define this method
//    public abstract void defineUi()

    def getWidget(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        def obj = walkToWithException(context, uid)
        if (!(obj instanceof Widget)) {
            println i18nManager.translate("DslContext.UIObject" , {uid})

            throw new NotWidgetObjectException(i18nManager.translate("DslContext.UIObject" , {uid}))
        }

        //add reference xpath for the widget
        Widget widget = (Widget)obj
        widget.updateParentRef(context.getReferenceLocator())

        return obj
    }

    def onWidget(String uid, String method, Object[] args) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        def obj = walkToWithException(context, uid)
        if (!(obj instanceof Widget)) {
            println i18nManager.translate("DslContext.UIObject" , {uid})

//            throw new RuntimeException(i18nManager.translate("DslContext.UIObject" , {uid}))
            throw new NotWidgetObjectException(i18nManager.translate("DslContext.UIObject" , {uid}))
        } else {
            if (obj.metaClass.respondsTo(obj, method, args)) {

                //add reference xpath for the widget
                Widget widget = (Widget)obj
                widget.updateParentRef(context.getReferenceLocator())

                return obj.invokeMethod(method, args)
            } else {

                throw new MissingMethodException(method, obj.metaClass.class, args)
            }
        }
    }

    protected String locatorMapping(WorkflowContext context, loc) {
      return locatorMappingWithOption(context, loc, null)
    }

    protected String locatorMappingWithOption(WorkflowContext context, loc, optLoc) {
        //get ui object's locator
        String locator = locatorProcessor.locate(context, loc)

        //get the reference locator all the way to the ui object
        if (context.getReferenceLocator() != null){
//            locator = context.getReferenceLocator() + locator
            context.appendReferenceLocator(locator)
            locator = context.getReferenceLocator() 
        }

        if(optLoc != null)
          locator = locator + optLoc

        if(context.isUseJQuerySelector()){
//          locator = optimizer.optimize(JQUERY_SELECTOR + locator.trim())
            locator = postProcessSelector(context, locator.trim())
        } else {
          //make sure the xpath starts with "//"
//          if (locator != null && (!locator.startsWith("//")) && (!locator.startsWith(JQUERY_SELECTOR))) {
            if (locator != null && (!locator.startsWith("//"))){
              locator = "/" + locator
          }
        }
      
        return locator
    }

    def selectFrame(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.selectFrame() {String loc ->
            eventHandler.selectFrame(loc)
        }
    }

    def selectFrameByIndex(int index) {
        eventHandler.selectFrame("index=${index}")
    }

    def selectParentFrameFrom(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.selectParentFrame() {String loc ->
            eventHandler.selectFrame(loc)
        }
    }

    def selectTopFrameFrom(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.selectTopFrame() {String loc ->
            eventHandler.selectFrame(loc)
        }
    }

    boolean getWhetherThisFrameMatchFrameExpression(String uid, String target) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.getWhetherThisFrameMatchFrameExpression(target) {String loc, String tgt ->
            accessor.getWhetherThisFrameMatchFrameExpression(loc, tgt)
        }
    }

    void waitForFrameToLoad(String uid, int timeout) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.waitForFrameToLoad(timeout) {String loc, int tmo ->
            accessor.waitForFrameToLoad(loc, Integer.toString(tmo))
        }
    }

    def openWindow(String uid, String url) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.openWindow(url) {String loc, String aurl ->
            eventHandler.openWindow(aurl, loc)
        }
    }

    def selectWindow(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.selectWindow() {String loc ->
            eventHandler.selectWindow(loc)
        }
    }

    def closeWindow(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.closeWindow() {String loc ->
            eventHandler.closeWindow(loc)
        }
    }

    def selectMainWindow() {
        eventHandler.selectWindow(null)
    }

    def selectParentWindow() {
        eventHandler.selectWindow(".")
    }

    def waitForPopUp(String uid, int timeout) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.waitForPopUp(timeout) {String loc ->
            accessor.waitForPopUp(loc, Integer.toString(timeout))
        }
    }

    boolean getWhetherThisWindowMatchWindowExpression(String uid, String target) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        walkToWithException(context, uid)?.getWhetherThisWindowMatchWindowExpression(target) {String loc ->
            accessor.getWhetherThisWindowMatchWindowExpression(loc, target)
        }
    }

    def windowFocus() {
        eventHandler.windowFocus()
    }

    def windowMaximize() {
        eventHandler.windowMaximize()
    }

    String getBodyText() {
        return accessor.getBodyText()
    }

    boolean isTextPresent(String pattern) {
        return accessor.isTextPresent(pattern)
    }

    String getHtmlSource() {
        return accessor.getHtmlSource()
    }

    String getExpression(String expression) {
        return accessor.getExpression(expression)
    }

    String getCookie() {
        return accessor.getCookie()
    }

    void runScript(String script) {
        accessor.runScript(script)
    }

    void captureScreenshot(String filename) {
        accessor.captureScreenshot(filename)
    }

    void chooseCancelOnNextConfirmation() {
        eventHandler.chooseCancelOnNextConfirmation()
    }

    void chooseOkOnNextConfirmation() {
        eventHandler.chooseOkOnNextConfirmation()
    }

    void answerOnNextPrompt(String answer) {
        eventHandler.answerOnNextPrompt(answer)
    }

    void goBack() {
        eventHandler.goBack()
    }

    void refresh() {
        eventHandler.refresh()
    }

    boolean isAlertPresent() {
        return accessor.isAlertPresent()
    }

    boolean isPromptPresent() {
        return accessor.isPromptPresent()
    }

    boolean isConfirmationPresent() {
        return accessor.isConfirmationPresent()
    }

    String getAlert() {
        return accessor.getAlert()
    }

    String getConfirmation() {
        return accessor.getConfirmation()
    }

    String getPrompt() {
        return accessor.getPrompt()
    }

    String getLocation() {
        return accessor.getLocation()
    }

    String getTitle() {
        return accessor.getTitle()
    }

    String[] getAllButtons() {
        return accessor.getAllButtons()
    }

    String[] getAllLinks() {
        return accessor.getAllLinks()
    }

    String[] getAllFields() {
        return accessor.getAllFields()
    }

    String[] getAllWindowIds() {
        return accessor.getAllWindowIds()
    }

    String[] getAllWindowNames() {
        return accessor.getAllWindowNames()
    }

    String[] getAllWindowTitles() {
        return accessor.getAllWindowTitles()
    }

    void waitForPageToLoad(int timeout) {
      accessor.waitForPageToLoad(Integer.toString(timeout))
    }

    public String getXMLDocument(){

      String xml =  getEval(XML_DOCUMENT_SCRIPT)

      return xml
    }

    //let the missing property return the a string of the properity, this is useful for the onWidget method
    //so that we can pass in widget method directly, instead of passing in the method name as a String
    def propertyMissing(String name) {
        println i18nManager.translate("BaseDslContext.PropertyIsMissing" , {name})
    }
}