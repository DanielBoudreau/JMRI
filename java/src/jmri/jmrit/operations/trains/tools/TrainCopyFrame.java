package jmri.jmrit.operations.trains.tools;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.*;

import jmri.InstanceManager;
import jmri.jmrit.operations.OperationsFrame;
import jmri.jmrit.operations.setup.Control;
import jmri.jmrit.operations.trains.*;
import jmri.jmrit.operations.trains.gui.TrainEditFrame;
import jmri.util.swing.JmriJOptionPane;

/**
 * Frame for making a new copy of a train.
 *
 * @author Bob Jacobsen Copyright (C) 2001
 * @author Daniel Boudreau Copyright (C) 2011, 2013
 */
public class TrainCopyFrame extends OperationsFrame {

    TrainManager trainManager = InstanceManager.getDefault(TrainManager.class);

    // labels
    // text field
    javax.swing.JTextField trainNameTextField = new javax.swing.JTextField(Control.max_len_string_train_name);

    // major buttons
    javax.swing.JButton copyButton = new javax.swing.JButton(Bundle.getMessage("ButtonCopy"));

    // combo boxes
    JComboBox<Train> trainBox = InstanceManager.getDefault(TrainManager.class).getTrainComboBox();

    public TrainCopyFrame(Train train) {
        // general GUI config

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Set up the panels
        // Layout the panel by rows
        // row 1
        JPanel pName = new JPanel();
        pName.setLayout(new GridBagLayout());
        pName.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("Name")));
        addItem(pName, trainNameTextField, 0, 0);

        // row 2
        JPanel pCopy = new JPanel();
        pCopy.setLayout(new GridBagLayout());
        pCopy.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("SelectTrain")));
        addItem(pCopy, trainBox, 0, 0);

        trainBox.setSelectedItem(train);

        // row 4
        JPanel pButton = new JPanel();
        pButton.add(copyButton);

        getContentPane().add(pName);
        getContentPane().add(pCopy);
        getContentPane().add(pButton);

        // add help menu to window
        addHelpMenu("package.jmri.jmrit.operations.Operations_CopyTrain", true); // NOI18N

        pack();
        setMinimumSize(new Dimension(Control.panelWidth400, Control.panelHeight200));

        setTitle(Bundle.getMessage("TitleTrainCopy"));

        // setup buttons
        addButtonAction(copyButton);
    }

    @Override
    public void buttonActionPerformed(java.awt.event.ActionEvent ae) {
        if (ae.getSource() == copyButton) {
            log.debug("copy train button activated");
            if (!checkName()) {
                return;
            }

            Train newTrain = trainManager.getTrainByName(trainNameTextField.getText());
            if (newTrain != null) {
                reportTrainExists();
                return;
            }
            if (trainBox.getSelectedItem() == null) {
                reportTrainDoesNotExist();
                return;
            }
            Train oldTrain = (Train) trainBox.getSelectedItem();
            if (oldTrain == null) {
                reportTrainDoesNotExist();
                return;
            }

            // now copy
            newTrain = trainManager.copyTrain(oldTrain, trainNameTextField.getText());
            new TrainEditFrame(newTrain);
        }
    }

    private void reportTrainExists() {
        JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("TrainNameExists"), Bundle
                .getMessage("CanNotTrain", Bundle.getMessage("copy")), JmriJOptionPane.ERROR_MESSAGE);
    }

    private void reportTrainDoesNotExist() {
        JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("SelectTrain"), Bundle
                .getMessage("CanNotTrain", Bundle.getMessage("copy")), JmriJOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @return true if name isn't too long
     */
    private boolean checkName() {
        if (trainNameTextField.getText().trim().isEmpty()) {
            JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("EnterTrainName"), Bundle
                    .getMessage("CanNotTrain", Bundle.getMessage("copy")), JmriJOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (trainNameTextField.getText().length() > Control.max_len_string_train_name) {
            JmriJOptionPane.showMessageDialog(this, Bundle.getMessage("TrainNameLess",
                    Control.max_len_string_train_name + 1), Bundle
                            .getMessage("CanNot", Bundle.getMessage("copy")), JmriJOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TrainCopyFrame.class);
}
