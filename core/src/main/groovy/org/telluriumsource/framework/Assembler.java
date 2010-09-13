package org.telluriumsource.framework;

import org.telluriumsource.component.bundle.BundleProcessor;
import org.telluriumsource.component.connector.SeleniumConnector;
import org.telluriumsource.component.custom.Extension;
import org.telluriumsource.component.data.Accessor;
import org.telluriumsource.component.dispatch.Dispatcher;
import org.telluriumsource.component.event.EventHandler;
import org.telluriumsource.crosscut.i18n.IResourceBundle;
import org.telluriumsource.crosscut.i18n.ResourceBundle;
import org.telluriumsource.dsl.SeleniumWrapper;
import org.telluriumsource.dsl.TelluriumApi;
import org.telluriumsource.dsl.UiDslParser;
import org.telluriumsource.framework.config.TelluriumConfigurator;
import org.telluriumsource.ui.builder.UiObjectBuilderRegistry;
import org.telluriumsource.ui.locator.JQueryOptimizer;
import org.telluriumsource.ui.locator.LocatorProcessor;
import org.telluriumsource.ui.widget.WidgetConfigurator;

import java.util.Locale;

/**
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *         
 *         Date: Sep 11, 2010
 */
public class Assembler {
    
    private Lookup lookup;

    private RuntimeEnvironment env;

    private TelluriumConfigurator configurator;

    public Assembler(Lookup lookup, RuntimeEnvironment env, TelluriumConfigurator telluriumConfigurator) {
        this.lookup = lookup;
        this.env = env;
        this.configurator = telluriumConfigurator;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }

    public RuntimeEnvironment getEnv() {
        return env;
    }

    public void setEnv(RuntimeEnvironment env) {
        this.env = env;
    }

    public void assemble(){
        UiDslParser parser = new UiDslParser();
        lookup.register("uiParser", parser);
        EventHandler eventHandler = new EventHandler();
        lookup.register("eventHandler", eventHandler);
        Dispatcher dispatcher = new Dispatcher();
        lookup.register("dispatcher", dispatcher);
        Accessor accessor = new Accessor();
        lookup.register("accessor", accessor);
        Extension extension = new Extension();
        lookup.register("extension", extension);
        BundleProcessor bundleProcessor = new BundleProcessor();
        lookup.register("bundleProcessor", bundleProcessor);
        SeleniumConnector connector = new SeleniumConnector();
        lookup.register("connector", connector);
        SeleniumWrapper wrapper = new SeleniumWrapper();
        lookup.register("wrapper", wrapper);
        TelluriumApi api = new TelluriumApi();
        lookup.register("api", api);
        JQueryOptimizer optimizer = new JQueryOptimizer();
        lookup.register("optimizer", optimizer);
        LocatorProcessor locatorProcessor = new LocatorProcessor();
        lookup.register("locatorProcessor", locatorProcessor);
        IResourceBundle resourceBundle = new ResourceBundle();
        lookup.register("resourceBundle", resourceBundle);
        UiObjectBuilderRegistry uiObjectBuilderRegistry = new UiObjectBuilderRegistry();
        lookup.register("uiObjectBuilderRegistry", uiObjectBuilderRegistry);

        WidgetConfigurator widgetConfigurator = new WidgetConfigurator();
        lookup.register("widgetConfigurator", widgetConfigurator);
        IResourceBundle i18nBundle =  new org.telluriumsource.crosscut.i18n.ResourceBundle();

        String[] split = env.getLocale().split("_");
        Locale loc = new Locale(split[0], split[1]);
        i18nBundle.updateDefaultLocale(loc);

        env.setResourceBundle(i18nBundle);
        
        lookup.register("i18nBundle", i18nBundle);

        widgetConfigurator.setProperty("i18nBundle", i18nBundle);
        widgetConfigurator.setProperty("registry", uiObjectBuilderRegistry);

        wrapper.setProperty("optimizer", optimizer);
        wrapper.setProperty("locatorProcessor", locatorProcessor);
        wrapper.setProperty("env", env);
        wrapper.setProperty("eventHandler", eventHandler);
        wrapper.setProperty("accessor", accessor);
        wrapper.setProperty("extension", extension);
        wrapper.setProperty("uiParser", parser);
        wrapper.setProperty("i18nBundle", resourceBundle);
        
        api.setProperty("uiParser", parser);
        api.setProperty("i18nBundle", i18nBundle);

        //configure components from Tellurium configuration
        //configure custom UI ojects
        configurator.config(uiObjectBuilderRegistry);

        //configure widgets
        configurator.config(widgetConfigurator);

        //configure Event Handler
        configurator.config(eventHandler);

        //configure Data Accessor
        configurator.config(accessor);

        //configure Dispatcher
        configurator.config(dispatcher);

        //configure connector
        configurator.config(connector);

        //customize configuration with Environment variables
        connector.setProperty("seleniumServerHost", env.getServerHost());
        connector.setProperty("port", env.getServerPort());
        connector.setProperty("browser", env.getBrowser());
    }
}
