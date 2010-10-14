package org.telluriumsource.component.connector

import org.telluriumsource.component.client.SeleniumClient
import org.telluriumsource.framework.config.Configurable

//import com.thoughtworks.selenium.CommandProcessor
import org.telluriumsource.component.bundle.BundleProcessor
import org.telluriumsource.framework.SessionManager
import org.telluriumsource.annotation.Inject
import org.telluriumsource.annotation.Provider

/**
 * The connector that ties the Selenium server and Selenium Client together
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 * 
 */
@Provider
class SeleniumConnector implements Configurable {

  @Inject(name="tellurium.connector.port")
  protected int port = 4444

  protected final String HTTPS_BASE_URL = "https://localhost:8443"

  protected final String HTTP_BASE_URL = "http://localhost:8080"

  @Inject(name="customSelenium")
  protected CustomSelenium sel

//  protected CustomSelenium customSelenium

  protected CommandProcessor commandProcessor

  @Inject(name="tellurium.connector.baseUrl")
  protected String baseURL = HTTP_BASE_URL

  @Inject(name="tellurium.connector.browser")
  protected String browser = "*chrome"

  @Inject(name="tellurium.connector.serverHost")
  protected seleniumServerHost = "localhost"

  @Inject(name="tellurium.embeddedserver.userExtension")
  protected String userExtension = null

  @Inject(name="tellurium.connector.customClass")
  protected def customClass = null

  @Inject(name="tellurium.connector.options")
  protected String options = null

  @Inject
  private BundleProcessor processor

  public CustomSelenium getSelenium() {
    return sel;
  }

  public void connect(String url) {
    sel.open(baseURL + url);
//        sel.cleanCache();
    //connect up cache
    processor.cleanAllCache();
  }

  public void connectUrl(String url) {
    sel.open(url);
//        sel.cleanCache();
    //connect up cache
    processor.cleanAllCache();
  }

  public void configBrowser(String serverHost, int serverPort, String baseUrl, String browser, String browserOptions) {
    this.seleniumServerHost = serverHost;
    this.port = serverPort;

    if (baseUrl != null)
      this.baseURL = baseUrl;

    this.browser = browser;

    if (browserOptions != null)
      this.options = browserOptions;
  }
  
   public void connectSeleniumServer() {

//        sel = new CustomSelenium(seleniumServerHost, port, browser, baseURL)
        sel.init(seleniumServerHost, port, browser, baseURL);

        sel.start()

        SeleniumClient sc = new SeleniumClient()
        sc.client = sel

    }

	public void disconnectSeleniumServer() {

		if(sel != null)
			sel.stop();
	}

 /* public synchronized void connectSeleniumServer() {

    //The selenium server startup logic is moved to EmbeddedSeleniumServer so that we can
    //decouple the selenium client and the selenium server.
//		if(runSeleniumServerInternally)
//			setUpSeleniumServer();

    //Works for https and http
//        sel = new DefaultSelenium(seleniumServerHost, port, browser, baseURL);
//        sel = new CustomSelenium(seleniumServerHost, port, browser, baseURL)
    // CustomSelenium with the new argument CommandProcess
    // This is done to make sure that implementing the Selenium Grid does
    // not break the inheritance model for CustomSelenium.

    customSelenium = new CustomSelenium(commandProcessor)
    customSelenium.setUserExt(this.userExtension)
    customSelenium.customClass = this.customClass
    if (this.options != null && this.options.trim().length() > 0) {
      customSelenium.startSeleniumSession(seleniumServerHost, port, browser, baseURL, this.options)
    } else {
      customSelenium.startSeleniumSession(seleniumServerHost, port, browser, baseURL)
    }
    sel = customSelenium.getActiveSeleniumSession()

    SeleniumClient sc = new SeleniumClient()
    sc.client = customSelenium
  }

  public synchronized void disconnectSeleniumServer() {
    if (customSelenium != null) {
      CustomSelenium aseles = customSelenium.getActiveSeleniumSession();
      if (aseles != null) {
        //flush out remaining commands in the command bundle before disconnection
        BundleProcessor processor = BundleProcessor.instance
        processor.flush()
        //clean up cache before close the connection
        aseles.cleanCache();
        customSelenium.closeSeleniumSession();
      }
    }
  }

  protected void initSeleniumClient() {
    SeleniumClient sc = new SeleniumClient()
    sc.client = customSelenium;
  }
*/
}