package org.telluriumsource.framework.inject;

import org.telluriumsource.exception.BeanNotFoundException;
import org.telluriumsource.exception.InstanceCreationException;
import org.telluriumsource.framework.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 *         Date: Oct 8, 2010
 */
public class DefaultSessionAwareBeanFactory implements SessionAwareBeanFactory {
    private Map<String, Bean> map = new HashMap<String, Bean>();

    public void addBean(Session session, String name, Class clazz, Class concrete, Scope scope, boolean singleton, Object instance) {
        Bean bean;
        if (scope == Scope.Global) {
            bean = new GlobalBean();
        } else if (scope == Scope.Session) {
            bean = new SessionBean();
        } else {
            bean = new RequestBean();
        }
        bean.setName(name);
        bean.setClazz(clazz);
        bean.setConcrete(concrete);
        bean.setScope(scope);
        bean.setSingleton(singleton);
        if(instance != null){
            bean.setInstance(session, instance);
        }

        map.put(name, bean);

    }

    public Object getByName(Session session, String name) {
        Bean bean = map.get(name);
        if(bean == null){
            throw new BeanNotFoundException("Bean " + name + " is not found");
        }

        return getInstance(session, bean);
    }

    public <T> T getByClass(Session session, Class<T> clazz) {
        String name = clazz.getCanonicalName();

        return (T)getByName(session, name);
    }

    public List<Bean> getAllBeans() {
        List<Bean> list = new ArrayList<Bean>();
        list.addAll(map.values());
        return list;
    }

    public void destroy() {
        map = null;  
    }
        
    private synchronized Object getInstance(Session session, Bean bean){
        if(bean.isSingleton()){
            Object instance = bean.getInstance(session);
            if(instance == null){
                instance = createInstance(session, bean.getConcrete());
                bean.setInstance(session, instance);
            }

            return instance;
        }else{
            Object instance = createInstance(session, bean.getConcrete());
            bean.setInstance(session, instance);

            return instance;
        }
    }

    private Object createInstance(Session session, Class clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new InstanceCreationException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InstanceCreationException(e.getMessage());
        }
    }
}
