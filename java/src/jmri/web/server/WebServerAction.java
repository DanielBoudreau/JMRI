package jmri.web.server;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import jmri.util.swing.JmriAbstractAction;
import jmri.util.swing.WindowInterface;

/**
 * Action to start a web server. Doesn't show a panel.
 *
 * @author Randall Wood Copyright (C) 2012
 */
public class WebServerAction extends JmriAbstractAction {

    private static ServerThread serverThread = null;
    private static final Object lock = new Object();

    public WebServerAction(String s, WindowInterface wi) {
        super(s, wi);
    }

    public WebServerAction(String s, Icon i, WindowInterface wi) {
        super(s, i, wi);
    }

    public WebServerAction() {
        super(Bundle.getMessage("MenuWebServerAction"));
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        synchronized (lock) {
            if (serverThread == null) {
                serverThread = new ServerThread();
                serverThread.setName("WebServerAction action");
                serverThread.start();
            } else {
                log.info("Web Server already running");
            }
        }
    }

    @Override
    public jmri.util.swing.JmriPanel makePanel() { return null; } // not used by this classes actionPerformed, as it doesn't show anything
    
    private static class ServerThread extends Thread {

        @Override
        public void run() {
            try {
                WebServer.getDefault().start();
            } catch (Exception ex) {
                log.error("Unable to start web server.", ex);
            }
        }
    }

    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebServerAction.class);
}
