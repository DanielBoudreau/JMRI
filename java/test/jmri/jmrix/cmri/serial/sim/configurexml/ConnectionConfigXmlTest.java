package jmri.jmrix.cmri.serial.sim.configurexml;

import jmri.jmrix.cmri.CMRISystemConnectionMemo;
import jmri.jmrix.cmri.serial.sim.ConnectionConfig;
import jmri.util.JUnitUtil;

import org.junit.jupiter.api.*;

/**
 * ConnectionConfigXmlTest.java
 *
 * Test for the ConnectionConfigXml class
 *
 * @author   Paul Bender  Copyright (C) 2016
 */
public class ConnectionConfigXmlTest extends jmri.jmrix.configurexml.AbstractSimulatorConnectionConfigXmlTestBase {

    @BeforeEach
    @Override
    public void setUp() {
        JUnitUtil.setUp();
        jmri.util.JUnitUtil.initDefaultUserMessagePreferences();
        xmlAdapter = new ConnectionConfigXml();
        cc = new ConnectionConfig();
    }

    @AfterEach
    @Override
    public void tearDown() {
        if (cc.getAdapter() != null) {
            ((CMRISystemConnectionMemo)cc.getAdapter().getSystemConnectionMemo()).getTrafficController().terminateThreads();
        }
        cc.dispose();
        xmlAdapter = null;
        cc = null;
        JUnitUtil.clearShutDownManager(); // put in place because AbstractMRTrafficController implementing subclass was not terminated properly
        JUnitUtil.tearDown();
    }
}

