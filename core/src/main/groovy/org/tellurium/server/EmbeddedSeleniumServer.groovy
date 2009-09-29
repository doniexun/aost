package org.tellurium.server

import org.tellurium.config.Configurable
import org.tellurium.server.SeleniumServerDaemon
import org.tellurium.util.Helper

/**
 * Embedded Selenium Server and will be running as a daemon thread
 * 
 * User: Jian Fang (John.Jian.Fang@gmail.com)
 */
class EmbeddedSeleniumServer implements Configurable{

    private SeleniumServerDaemon daemon;

    protected int port = 4444;

    protected String logFile = "selenium.log";

    protected boolean useMultiWindows = false;

    protected boolean trustAllSSLCertificates = false;

    protected int DEFAULT_DELAY_IN_SECONDS = 5;

    protected int serverDelayInSeconds = DEFAULT_DELAY_IN_SECONDS;

	protected boolean runSeleniumServerInternally = true;

    protected String profileLocation = null;

    protected String userExtension = null;

    public final boolean isUseMultiWindows() {
		return useMultiWindows;
	}

    public void setUpSeleniumServer(){

		try {
			daemon = new SeleniumServerDaemon (port, logFile, useMultiWindows,
                    this.trustAllSSLCertificates, this.profileLocation, this.userExtension);
			daemon.run();
            Helper.pause(serverDelayInSeconds*1000)
		} catch (Exception e) {

			e.printStackTrace();
		}
    }

    public void runSeleniumServer() {

		if(runSeleniumServerInternally)
			setUpSeleniumServer();
    }

    public final boolean isRunSeleniumServerInternally() {
		return runSeleniumServerInternally;
	}

    public void stopSeleniumServer(){
        if(runSeleniumServerInternally && daemon != null){
              daemon.stop()
        }
    }
}