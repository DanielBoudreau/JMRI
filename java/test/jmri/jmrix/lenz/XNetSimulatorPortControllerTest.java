package jmri.jmrix.lenz;

import jmri.util.JUnitUtil;

import org.junit.jupiter.api.*;

/**
 * JUnit tests for the XNetSimulatorPortController class.
 *
 * @author      Paul Bender Copyright (C) 2016
 */
public class XNetSimulatorPortControllerTest extends jmri.jmrix.AbstractSerialPortControllerTestBase {

    @Override
    @BeforeEach
    public void setUp(){
       JUnitUtil.setUp();
       XNetInterfaceScaffold tc = new XNetInterfaceScaffold(new LenzCommandStation());
       new XNetSystemConnectionMemo(tc);
       apc = new XNetSimulatorPortController(){
            @Override
            public boolean status(){
              return true;
            }
            @Override
            public void configure(){
            }
            @Override
            public java.io.DataInputStream getInputStream(){
                return null;
            }
            @Override
            public java.io.DataOutputStream getOutputStream(){
                return null;
            }

            /**
             * Open a specified port. The appName argument is to be provided to the
             * underlying OS during startup so that it can show on status displays, etc
             */
            @Override
            public String openPort(String portName, String appName){
               return "";
            }

            @Override
            public void setOutputBufferEmpty(boolean s){
            }
            
            @edu.umd.cs.findbugs.annotations.SuppressFBWarnings( value = "OVERRIDING_METHODS_MUST_INVOKE_SUPER",
                justification = "always ok to send in test class")
            @Override
            public boolean okToSend(){
                  return true;
            }

       };
    }

    @Override
    @AfterEach
    public void tearDown(){
        JUnitUtil.clearShutDownManager(); // put in place because AbstractMRTrafficController implementing subclass was not terminated properly
        JUnitUtil.tearDown();
    }

}
