package jmri.jmrit.operations.automation.gui;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.jupiter.api.Test;

import jmri.jmrit.operations.OperationsTestCase;
import jmri.util.JUnitUtil;
import jmri.util.JmriJFrame;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class AutomationStartupActionTest extends OperationsTestCase {

    @Test
    public void testCTor() {
        AutomationStartupAction t = new AutomationStartupAction();
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testAction() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        AutomationStartupAction a = new AutomationStartupAction();
        Assert.assertNotNull("exists", a);
        
        a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        
        JmriJFrame f = JmriJFrame.getFrame(Bundle.getMessage("MenuStartupAutomation"));
        Assert.assertNotNull("exists", f);
        JUnitUtil.dispose(f);
    }
}
