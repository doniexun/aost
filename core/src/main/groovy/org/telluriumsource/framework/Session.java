package org.telluriumsource.framework;

import org.telluriumsource.crosscut.i18n.IResourceBundle;
import org.telluriumsource.dsl.SeleniumWrapper;
import org.telluriumsource.dsl.TelluriumApi;

/**
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Sep 10, 2010
 */
public class Session {

    private String sessionId;

    private RuntimeEnvironment env;

    private BeanFactory beanFactory;

    private Lookup lookup;

    private SeleniumWrapper wrapper;

    private TelluriumApi api;

    private IResourceBundle i18nBundle;

    public IResourceBundle getI18nBundle() {
        return i18nBundle;
    }

    public void setI18nBundle(IResourceBundle i18nBundle) {
        this.i18nBundle = i18nBundle;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public RuntimeEnvironment getEnv() {
        return env;
    }

    public void setEnv(RuntimeEnvironment env) {
        this.env = env;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }

    public SeleniumWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(SeleniumWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public TelluriumApi getApi() {
        return api;
    }

    public void setApi(TelluriumApi api) {
        this.api = api;
    }

    public <T> T getBean(Class<T> clazz){
        return this.beanFactory.getInstance(clazz);    
    }

    public String toString(){
        StringBuffer sb = new StringBuffer(64);
        sb.append("Session: [sessionId: ").append(sessionId);
        if(env != null){
            sb.append(", Environment: ").append(env.toString());
        }else{
            sb.append(", Environment: null");
        }
        sb.append("]");

        return sb.toString();
    }
}
