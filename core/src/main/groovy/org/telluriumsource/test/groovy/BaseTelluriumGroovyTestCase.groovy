package org.telluriumsource.test.groovy

import org.telluriumsource.config.CustomConfig

import org.telluriumsource.connector.SeleniumConnector
import org.telluriumsource.framework.TelluriumFramework
import org.telluriumsource.framework.CachePolicy
import org.telluriumsource.util.Helper

abstract class BaseTelluriumGroovyTestCase extends GroovyTestCase{

  //custom configuration
//	protected InternationalizationManager i18nManager = new InternationalizationManagerImpl()
    protected CustomConfig customConfig = null;

    protected SeleniumConnector conn;
    protected TelluriumFramework tellurium;

    public abstract SeleniumConnector getConnector();

	public geti18nManager()
	{
		return tellurium.getI18nManager();
	}
    public void openUrl(String url){
        getConnector().connectSeleniumServer()
        getConnector().connectUrl(url)
    }

    public void connectUrl(String url) {
         getConnector().connectUrl(url)
    }

    public void connectSeleniumServer(){
        getConnector().connectSeleniumServer()
    }
    
    public void disconnectSeleniumServer(){
         getConnector().disconnectSeleniumServer()
    }

    public void setCustomConfig(boolean runInternally, int port, String browser,
                                       boolean useMultiWindows, String profileLocation){
        customConfig = new CustomConfig(runInternally, port, browser, useMultiWindows, profileLocation)
    }

    public void setCustomConfig(boolean runInternally, int port, String browser,
                                       boolean useMultiWindows, String profileLocation, String serverHost){
        customConfig = new CustomConfig(runInternally, port, browser, useMultiWindows, profileLocation, serverHost)
    }

    public void setCustomConfig(boolean runInternally, int port, String browser,
                                       boolean useMultiWindows, String profileLocation, String serverHost, String browserOptions){
        customConfig = new CustomConfig(runInternally, port, browser, useMultiWindows, profileLocation, serverHost, browserOptions)
    }

    public void useCssSelector(boolean isUse){
      tellurium.useCssSelector(isUse);
    }

    public void useCache(boolean isUse){
      tellurium.useCache(isUse);
    }

    public void cleanCache(){
      tellurium.cleanCache();
    }

    public boolean isUsingCache(){
      return tellurium.isUsingCache();
    }

    public void setCacheMaxSize(int size){
      tellurium.setCacheMaxSize(size);
    }

    public int getCacheSize(){
      return tellurium.getCacheSize();
    }

    public int getCacheMaxSize(){
      return tellurium.getCacheMaxSize();
    }

    public Map<String, Long> getCacheUsage(){
      return tellurium.getCacheUsage();
    }

    public void useCachePolicy(CachePolicy policy){
      tellurium.useCachePolicy(policy);
    }

    public String getCurrentCachePolicy(){
      return tellurium.getCurrentCachePolicy();
    }

    public void useDefaultXPathLibrary(){
      tellurium.useDefaultXPathLibrary();
    }

    public void useJavascriptXPathLibrary(){
      tellurium.useJavascriptXPathLibrary();
    }

    public void useAjaxsltXPathLibrary() {
      tellurium.useAjaxsltXPathLibrary();
    }

    public void registerNamespace(String prefix, String namespace){
      tellurium.registerNamespace(prefix, namespace);
    }

    public String getNamespace(String prefix) {
      return tellurium.getNamespace(prefix);
    }

    public void pause(int milliseconds) {
      Helper.pause(milliseconds);
    }

    public void useMacroCmd(boolean isUse){
      tellurium.useMacroCmd(isUse);
    }

    public void setMaxMacroCmd(int max){
      tellurium.setMaxMacroCmd(max);
    }

    public int getMaxMacroCmd(){
      return tellurium.getMaxMacroCmd();
    }

    public boolean isUseTelluriumApi(){
      return tellurium.isUseTelluriumApi();
    }

    public void useTelluriumApi(boolean isUse){
      tellurium.useTelluriumApi(isUse) ;
    }
  
    public void useTrace(boolean isUse){
      tellurium.useTrace(isUse);
    }

    public void showTrace() {
      tellurium.showTrace();
    }

    public void setEnvironment(String name, Object value){
      tellurium.setEnvironment(name, value) ;
    }

    public Object getEnvironment(String name){
      return tellurium.getEnvironment(name);
    }

    public void allowNativeXpath(boolean allow){
      tellurium.allowNativeXpath(allow); 
    }
}  