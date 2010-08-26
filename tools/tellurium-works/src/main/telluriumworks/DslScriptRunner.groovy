package telluriumworks

/**
 * 
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 * 
 * Date: Aug 25, 2010
 * 
 */
class DslScriptRunner {
  
  TelluriumConfig config = new TelluriumConfig()

  public void updateConfig(TelluriumConfig config){
    this.config.updateFrom(config)
  }

  public void run(String dsl) {
    def script =
    """
                import telluriumworks.DslScriptEngine
                import org.telluriumsource.framework.Environment
                import org.telluriumsource.crosscut.i18n.IResourceBundle
                import org.telluriumsource.framework.config.CustomConfig
                
                class DslTest extends DslScriptEngine{
                    def test(){
                        this.aost.customConfig = new CustomConfig(false, ${config.serverPort}, '${config.browser}', false, null,  '${config.serverHost}', null)
                        init()
                        this.aost.useTrace(${config.useTrace})
                        this.aost.setMaxMacroCmd(${config.macroCmd})
                        ${dsl}
                        shutDown()
                        IResourceBundle i18nBundle = Environment.instance.myResourceBundle()
                        println i18nBundle.getMessage("DslScriptExecutor.DslTestDone")
                    }
                }

                DslTest instance = new DslTest()
                instance.test()
     """
     
//    println "Script: \n" + script

    new GroovyShell().evaluate(script)
  }
}