package jmri.jmrit.beantable;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import jmri.InstanceManager;
import jmri.Memory;
import jmri.MemoryManager;
import jmri.NamedBean;
import jmri.UserPreferencesManager;
import jmri.util.JmriJFrame;
import jmri.util.swing.JmriJOptionPane;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

/**
 * Swing action to create and register a MemoryTable GUI.
 *
 * @author Bob Jacobsen Copyright (C) 2003
 */
public class MemoryTableAction extends AbstractTableAction<Memory> {

    /**
     * Create an action with a specific title.
     * <p>
     * Note that the argument is the Action title, not the title of the
     * resulting frame. Perhaps this should be changed?
     *
     * @param actionName title of the action
     */
    public MemoryTableAction(String actionName) {
        super(actionName);

        // disable ourself if there is no primary Memory manager available
        if (InstanceManager.getNullableDefault(MemoryManager.class) == null) {
            super.setEnabled(false);
        }

    }

    public MemoryTableAction() {
        this(Bundle.getMessage("TitleMemoryTable"));
    }

    /**
     * Create the JTable DataModel, along with the changes for the specific case
     * of Memory objects
     */
    @Override
    protected void createModel() {
        m = new MemoryTableDataModel(InstanceManager.getDefault(MemoryManager.class));
    }

    /** {@inheritDoc} */
    @Override
    protected void setTitle() {
        f.setTitle(Bundle.getMessage("TitleMemoryTable"));
    }

    /** {@inheritDoc} */
    @Override
    protected String helpTarget() {
        return "package.jmri.jmrit.beantable.MemoryTable";
    }

    JmriJFrame addFrame = null;
    JTextField sysNameField = new JTextField(20);
    JTextField userNameField = new JTextField(20);
    JLabel sysNameLabel = new JLabel(Bundle.getMessage("LabelSystemName"));
    JLabel userNameLabel = new JLabel(Bundle.getMessage("LabelUserName"));
    SpinnerNumberModel rangeSpinner = new SpinnerNumberModel(1, 1, 100, 1); // maximum 100 items
    JSpinner numberToAddSpinner = new JSpinner(rangeSpinner);
    JCheckBox rangeBox = new JCheckBox(Bundle.getMessage("AddRangeBox"));
    JCheckBox autoSystemNameBox = new JCheckBox(Bundle.getMessage("LabelAutoSysName"));
    JLabel statusBarLabel = new JLabel(Bundle.getMessage("AddBeanStatusEnter"), JLabel.LEADING);
    UserPreferencesManager p;

    /** {@inheritDoc} */
    @Override
    protected void addPressed(ActionEvent e) {
        p = InstanceManager.getDefault(UserPreferencesManager.class);
        if (addFrame == null) {
            addFrame = new JmriJFrame(Bundle.getMessage("TitleAddMemory"), false, true);
            addFrame.addHelpMenu("package.jmri.jmrit.beantable.MemoryAddEdit", true);
            addFrame.getContentPane().setLayout(new BoxLayout(addFrame.getContentPane(), BoxLayout.Y_AXIS));

            ActionListener okListener = (ActionEvent e1) -> {
                okPressed(e1);
            };
            ActionListener cancelListener = (ActionEvent e1) -> {
                cancelPressed(e1);
            };
            AddNewBeanPanel anbp = new AddNewBeanPanel(sysNameField, userNameField, numberToAddSpinner, rangeBox, autoSystemNameBox, "ButtonCreate", okListener, cancelListener, statusBarLabel);
            addFrame.add(anbp);
            addFrame.getRootPane().setDefaultButton(anbp.ok);
            addFrame.setEscapeKeyClosesWindow(true);
            sysNameField.setToolTipText(Bundle.getMessage("SysNameToolTip", "M")); // override tooltip with bean specific letter
        }
        sysNameField.setBackground(Color.white);
        // reset status bar text
        statusBarLabel.setText(Bundle.getMessage("AddBeanStatusEnter"));
        statusBarLabel.setForeground(Color.gray);
        if (p.getSimplePreferenceState(systemNameAuto)) {
            autoSystemNameBox.setSelected(true);
        }
        addFrame.pack();
        addFrame.setVisible(true);
    }

    String systemNameAuto = this.getClass().getName() + ".AutoSystemName";

    void cancelPressed(ActionEvent e) {
        addFrame.setVisible(false);
        addFrame.dispose();
        addFrame = null;
    }

    void okPressed(ActionEvent e) {

        int numberOfMemory = 1;

        if (rangeBox.isSelected()) {
            numberOfMemory = (Integer) numberToAddSpinner.getValue();
        }

        if (numberOfMemory >= 65) { // limited by JSpinnerModel to 100
            if (JmriJOptionPane.showConfirmDialog(addFrame,
                    Bundle.getMessage("WarnExcessBeans", Bundle.getMessage("Memories"), numberOfMemory),
                    Bundle.getMessage("WarningTitle"),
                    JmriJOptionPane.YES_NO_OPTION) != JmriJOptionPane.YES_OPTION) {
                return;
            }
        }

        String uName = NamedBean.normalizeUserName(userNameField.getText());
        if (uName == null || uName.isEmpty()) {
            uName = null;
        }
        String sName = sysNameField.getText();
        // initial check for empty entry
        if (sName.isEmpty() && !autoSystemNameBox.isSelected()) {
            statusBarLabel.setText(Bundle.getMessage("WarningSysNameEmpty"));
            statusBarLabel.setForeground(Color.red);
            sysNameField.setBackground(Color.red);
            return;
        } else {
            sysNameField.setBackground(Color.white);
        }

        // Add some entry pattern checking, before assembling sName and handing it to the memoryManager
        String statusMessage = Bundle.getMessage("ItemCreateFeedback", Bundle.getMessage("BeanNameMemory"));
        String errorMessage = null;
        for (int x = 0; x < numberOfMemory; x++) {
            if (uName != null && !uName.isEmpty()
                && InstanceManager.getDefault(MemoryManager.class).getByUserName(uName) != null
                && !p.getPreferenceState(getClassName(), "duplicateUserName")) {
                InstanceManager.getDefault(UserPreferencesManager.class).
                        showErrorMessage(Bundle.getMessage("ErrorTitle"), Bundle.getMessage("ErrorDuplicateUserName", uName), getClassName(), "duplicateUserName", false, true);
                // show in status bar
                errorMessage = Bundle.getMessage("ErrorDuplicateUserName", uName);
                statusBarLabel.setText(errorMessage);
                statusBarLabel.setForeground(Color.red);
                uName = null; // new Memory objects always receive a valid system name using the next free index, but uName names must not be in use so use none in that case
            }
            if (!sName.isEmpty()
                && InstanceManager.getDefault(MemoryManager.class).getBySystemName(sName) != null
                && !p.getPreferenceState(getClassName(), "duplicateSystemName")) {
                InstanceManager.getDefault(UserPreferencesManager.class).
                        showErrorMessage(Bundle.getMessage("ErrorTitle"), Bundle.getMessage("ErrorDuplicateSystemName", sName), getClassName(), "duplicateSystemName", false, true);
                // show in status bar
                errorMessage = Bundle.getMessage("ErrorDuplicateSystemName", sName);
                statusBarLabel.setText(errorMessage);
                statusBarLabel.setForeground(Color.red);
                return; // new Memory objects are always valid, but system names must not be in use so skip in that case
            }
            try {
                if (autoSystemNameBox.isSelected()) {
                    InstanceManager.getDefault(MemoryManager.class).newMemory(uName);
                } else {
                    InstanceManager.getDefault(MemoryManager.class).newMemory(sName, uName);
                }
            } catch (IllegalArgumentException ex) {
                // uName input no good
                handleCreateException(sName);
                errorMessage = Bundle.getMessage("ErrorAddFailedCheck");
                statusBarLabel.setText(errorMessage);
                statusBarLabel.setForeground(Color.red);
                return; // without creating
            }

            // add first and last names to statusMessage uName feedback string
            // only mention first and last of rangeBox added
            if (x == 0 || x == numberOfMemory - 1) {
                statusMessage = statusMessage + " " + sName + " (" + uName + ")";
            }
            if (x == numberOfMemory - 2) {
                statusMessage = statusMessage + " " + Bundle.getMessage("ItemCreateUpTo") + " ";
            }

            // bump system & uName names
            if (!autoSystemNameBox.isSelected()) {
                sName = nextName(sName);
            }
            if (uName != null) {
                uName = nextName(uName);
            }
        } // end of for loop creating rangeBox of Memories

        // provide feedback to uName
        if (errorMessage == null) {
            statusBarLabel.setText(statusMessage);
            statusBarLabel.setForeground(Color.gray);
        } else {
            statusBarLabel.setText(errorMessage);
            // statusBarLabel.setForeground(Color.red); // handled when errorMassage is set to differentiate urgency
        }

        p.setSimplePreferenceState(systemNameAuto, autoSystemNameBox.isSelected());
    }

    void handleCreateException(String sysName) {
        JmriJOptionPane.showMessageDialog(addFrame,
                Bundle.getMessage("ErrorMemoryAddFailed", sysName) + "\n" + Bundle.getMessage("ErrorAddFailedCheck"),
                Bundle.getMessage("ErrorTitle"),
                JmriJOptionPane.ERROR_MESSAGE);
    }

    /** {@inheritDoc} */
    @Override
    public String getClassDescription() {
        return Bundle.getMessage("TitleMemoryTable");
    }

    @Override
    public void setMessagePreferencesDetails() {
        InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                setPreferenceItemDetails(getClassName(), "duplicateUserName", Bundle.getMessage("HideDupUserError"));  // NOI18N
        InstanceManager.getDefault(jmri.UserPreferencesManager.class).
                setPreferenceItemDetails(getClassName(), "duplicateSystemName", Bundle.getMessage("HideDupSysError"));  // NOI18N
        super.setMessagePreferencesDetails();
    }

    /** {@inheritDoc} */
    @Override
    protected String getClassName() {
        return MemoryTableAction.class.getName();
    }

    // private final static Logger log = LoggerFactory.getLogger(MemoryTableAction.class);

}
