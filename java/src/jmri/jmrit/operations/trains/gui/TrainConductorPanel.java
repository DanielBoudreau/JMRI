package jmri.jmrit.operations.trains.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jmri.jmrit.operations.CommonConductorYardmasterPanel;
import jmri.jmrit.operations.rollingstock.RollingStock;
import jmri.jmrit.operations.rollingstock.cars.Car;
import jmri.jmrit.operations.routes.RouteLocation;
import jmri.jmrit.operations.setup.Control;
import jmri.jmrit.operations.trains.*;
import jmri.jmrit.operations.trains.trainbuilder.TrainCommon;

/**
 * Conductor Panel. Shows work for a train one location at a time.
 *
 * @author Dan Boudreau Copyright (C) 2011, 2013
 * 
 */
public class TrainConductorPanel extends CommonConductorYardmasterPanel {

    // labels
    JLabel textTrainName = new JLabel();
    JLabel textTrainDepartureTime = new JLabel();
    JLabel textNextLocationName = new JLabel();

    // panels
    JPanel pTrainDepartureTime = new JPanel();

    /**
     * Default constructor required to use as JavaBean.
     */
    public TrainConductorPanel() {
        this(null);
    }

    public TrainConductorPanel(Train train) {
        super();

        _train = train;

        // row 2
        JPanel pRow2 = new JPanel();
        pRow2.setLayout(new BoxLayout(pRow2, BoxLayout.X_AXIS));

        // row 2a (train name)
        JPanel pTrainName = new JPanel();
        pTrainName.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("Train")));
        pTrainName.add(textTrainName);

        pRow2.add(pTrainName);
        pRow2.add(pTrainDescription);
        pRow2.add(pRailRoadName);

        JPanel pLocation = new JPanel();
        pLocation.setLayout(new BoxLayout(pLocation, BoxLayout.X_AXIS));

        // row 10b (train departure time)
        pTrainDepartureTime.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("DepartTime")));
        pTrainDepartureTime.add(textTrainDepartureTime);

        // row 10c (next location name)
        JPanel pNextLocationName = new JPanel();
        pNextLocationName.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("NextLocation")));
        pNextLocationName.add(textNextLocationName);

        pLocation.add(pLocationName); // location name
        pLocation.add(pTrainDepartureTime);
        pLocation.add(pNextLocationName);

        // row 14
        JPanel pRow14 = new JPanel();
        pRow14.setLayout(new BoxLayout(pRow14, BoxLayout.X_AXIS));
        pRow14.setMaximumSize(new Dimension(2000, 200));

        // row 14b
        JPanel pMoveButton = new JPanel();
        pMoveButton.setLayout(new GridBagLayout());
        pMoveButton.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("Train")));
        addItem(pMoveButton, moveButton, 1, 0);

        pRow14.add(pButtons);
        pRow14.add(pMoveButton);

        add(pRow2);
        add(pLocation);
        add(textTrainCommentPane);
        add(textTrainRouteCommentPane); // train route comment
        add(textTrainRouteLocationCommentPane); // train route location comment
        add(textLocationCommentPane);
        add(pTrackComments);
        add(textTrainStatusPane);
        add(locoPane);
        add(pWorkPanes);
        add(movePane);
        add(pStatus);
        add(pRow14);

        // setup buttons
        addButtonAction(moveButton);

        if (_train != null) {
            loadTrainDescription();
            loadTrainComment();
            loadRouteComment();
            loadRailroadName();
            // listen for train changes
            _train.addPropertyChangeListener(this);
        }
        trainManager.addPropertyChangeListener(this);
        
        update();
    }

    @Override
    public void buttonActionPerformed(java.awt.event.ActionEvent ae) {
        if (ae.getSource() == moveButton) {
            _train.move();
            return;
        }
        super.buttonActionPerformed(ae);
    }

    private boolean queued = false;

    @Override
    protected void update() {
        log.debug("queue update");
        if (queued) {
            return;
        }
        queued = true;
        // use invokeLater to prevent deadlock
        SwingUtilities.invokeLater(() -> {
            log.debug("run update, setMode: {}", isSetMode);
            queued = false;
            initialize();
            if (_train != null && _train.getRoute() != null) {
                textTrainName.setText(_train.getIconName());
                RouteLocation rl = _train.getCurrentRouteLocation();
                if (rl != null) {
                    loadRouteLocationComment(rl);

                    textLocationName.setText(trainManager.isShowLocationHyphenNameEnabled() ? rl.getLocation().getName()
                            : rl.getLocation().getSplitName());
                    pTrainDepartureTime.setVisible(_train.isShowArrivalAndDepartureTimesEnabled() &&
                            !rl.getDepartureTime().equals(RouteLocation.NONE));
                    textTrainDepartureTime.setText(rl.getFormatedDepartureTime());

                    loadLocationComment(rl.getLocation());

                    textNextLocationName
                            .setText(trainManager.isShowLocationHyphenNameEnabled() ? _train.getNextLocationName()
                                    : TrainCommon.splitString(_train.getNextLocationName()));
                    
                    updateTrackComments(rl, IS_MANIFEST);
                    
                    textTrainStatusPane.setText(TrainCommon.getTrainMessage(_train, rl));
                            
                    // check for locos
                    updateLocoPanes(rl);

                    // now update the car pick ups and set outs
                    blockCars(rl, IS_MANIFEST);
                }

                textStatus.setText(getStatus(rl, IS_MANIFEST));

                // adjust move button text
                if (rl == _train.getTrainTerminatesRouteLocation()) {
                    moveButton.setText(Bundle.getMessage("Terminate"));
                } else {
                    moveButton.setText(Bundle.getMessage("Move"));
                }
            }
            updateComplete();
        });
    }

    @Override
    public void dispose() {
        trainManager.removePropertyChangeListener(this);
        removePropertyChangeListerners();
        if (_train != null) {
            _train.removePropertyChangeListener(this);
        }
        super.dispose();
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (Control.SHOW_PROPERTY) {
            log.debug("Property change ({}) for: ({}) old: {}, new: {}", e.getPropertyName(), e.getSource().toString(),
                    e.getOldValue(), e.getNewValue());
        }
        if (e.getPropertyName().equals(Train.TRAIN_MOVE_COMPLETE_CHANGED_PROPERTY) ||
                e.getPropertyName().equals(Train.BUILT_CHANGED_PROPERTY)) {
            clearAndUpdate();
        }
        if ((e.getPropertyName().equals(RollingStock.ROUTE_LOCATION_CHANGED_PROPERTY) && e.getNewValue() == null) ||
                (e.getPropertyName().equals(RollingStock.ROUTE_DESTINATION_CHANGED_PROPERTY) &&
                        e.getNewValue() == null) ||
                e.getPropertyName().equals(RollingStock.TRAIN_CHANGED_PROPERTY) ||
                e.getPropertyName().equals(Train.TRAIN_MODIFIED_CHANGED_PROPERTY) ||
                e.getPropertyName().equals(TrainManager.TRAINS_SHOW_FULL_NAME_PROPERTY)) {
            // remove car from list so the text get's updated
            if (e.getSource().getClass().equals(Car.class)) {
                Car car = (Car) e.getSource();
                removeCarFromList(car);
            }
            update();
        }
    }

    private final static Logger log = LoggerFactory.getLogger(TrainConductorPanel.class);
}
