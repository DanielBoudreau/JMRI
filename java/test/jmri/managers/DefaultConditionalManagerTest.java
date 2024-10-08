package jmri.managers;

import static org.junit.Assert.assertNotNull;

import jmri.Conditional;
import jmri.ConditionalManager;
import jmri.InstanceManager;
import jmri.Logix;
import jmri.jmrix.internal.InternalSystemConnectionMemo;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * Tests for the jmri.managers.DefaultConditionalManager class.
 *
 * @author Bob Jacobsen Copyright (C) 2015
 */
public class DefaultConditionalManagerTest extends AbstractManagerTestBase<jmri.ConditionalManager,jmri.Conditional> {

    @Test
    public void testCtor() {
        Assert.assertNotNull("exists",l);
    }

    @Test
    public void testCreate() {
        ConditionalManager m = l;

        Conditional c1 = m.createNewConditional("IX01C01", "");
        Conditional c2 = m.createNewConditional("IX01C02", "");

        Assert.assertFalse(c1 == c2);
        Assert.assertFalse(c1.equals(c2));
    }

    @Test
    public void testUserNameOverlap() {
        ConditionalManager m = l;

        Conditional c1 = m.createNewConditional("IX02C01", "Foo");
        Conditional c2 = m.createNewConditional("IX02C02", "Foo");

        Assert.assertTrue(c1.getUserName().equals("Foo"));
        Assert.assertTrue(c2.getUserName().equals("Foo"));
    }

    @Test
    @Override
    @Disabled("No manager-specific system name validation at present")
    public void testMakeSystemNameWithNoPrefixNotASystemName() {}

    @Test
    @Override
    @Disabled("No manager-specific system name validation at present")
    public void testMakeSystemNameWithPrefixNotASystemName() {}

    @BeforeEach
    public void setUp() throws Exception {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initIdTagManager();
        JUnitUtil.initLogixManager();
        JUnitUtil.initConditionalManager();

        Logix x1 = new jmri.implementation.DefaultLogix("IX01");
        assertNotNull("Logix x1 is null!", x1);
        InstanceManager.getDefault(jmri.LogixManager.class).register(x1);

        Logix x2 = new jmri.implementation.DefaultLogix("IX02");
        assertNotNull("Logix x2 is null!", x2);
        InstanceManager.getDefault(jmri.LogixManager.class).register(x2);
        l = new DefaultConditionalManager(InstanceManager.getDefault(InternalSystemConnectionMemo.class));
    }

    @AfterEach
    public void tearDown() throws Exception {
        l = null;
        JUnitUtil.deregisterBlockManagerShutdownTask();
        JUnitUtil.tearDown();
    }
}
