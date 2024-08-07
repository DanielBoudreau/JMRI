package jmri.jmrix.bachrus;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import jmri.InstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Swing action to create and register a SpeedoConsoleFrame object
 *
 * @author Andrew Crosland Copyright (C) 2010
 */
public class SpeedoConsoleAction extends AbstractAction {

    SpeedoSystemConnectionMemo _memo = null;

    public SpeedoConsoleAction(String s, SpeedoSystemConnectionMemo memo) {
        super(s);
        _memo = memo;
    }
    
     public SpeedoConsoleAction() {
        super(Bundle.getMessage("SpeedoConsole"));
        try {
            _memo = InstanceManager.getDefault(SpeedoSystemConnectionMemo.class);
        }
        catch (NullPointerException e) {
            _memo = new SpeedoSystemConnectionMemo();
            _memo.setSpeedoTrafficController(new SpeedoTrafficController(_memo));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SpeedoConsoleFrame f = new SpeedoConsoleFrame(_memo);
        try {
            f.initComponents();
        } catch (Exception ex) {
            log.error("Uncaught Exception in SpeedoConsoleFrame: ", ex);
        }
        f.setVisible(true);
    }
    private final static Logger log = LoggerFactory.getLogger(SpeedoConsoleAction.class);
}
