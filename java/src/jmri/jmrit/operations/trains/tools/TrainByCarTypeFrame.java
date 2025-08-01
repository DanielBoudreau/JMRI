package jmri.jmrit.operations.trains.tools;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jmri.InstanceManager;
import jmri.jmrit.operations.OperationsFrame;
import jmri.jmrit.operations.OperationsPanel;
import jmri.jmrit.operations.locations.*;
import jmri.jmrit.operations.locations.schedules.Schedule;
import jmri.jmrit.operations.rollingstock.cars.*;
import jmri.jmrit.operations.routes.Route;
import jmri.jmrit.operations.routes.RouteLocation;
import jmri.jmrit.operations.setup.Control;
import jmri.jmrit.operations.trains.Train;
import jmri.jmrit.operations.trains.TrainManager;

/**
 * Frame to display by rolling stock, the locations serviced by this train
 *
 * @author Dan Boudreau Copyright (C) 2010, 2013, 2014
 */
public class TrainByCarTypeFrame extends OperationsFrame implements java.beans.PropertyChangeListener {

    protected static final String POINTER = "    -->";
    
    // train
    Train _train;

    LocationManager locationManager = InstanceManager.getDefault(LocationManager.class);

    // panels
    JPanel pRoute = new JPanel();

    // radio buttons
    // combo boxes
    JComboBox<Train> trainsComboBox = InstanceManager.getDefault(TrainManager.class).getTrainComboBox();
    JComboBox<String> typeComboBox = InstanceManager.getDefault(CarTypes.class).getComboBox();
    JComboBox<Car> carsComboBox = new JComboBox<>();

    // The car currently selected
    Car _car;

    /**
     * Show how cars for a train can be serviced
     * 
     * @param train the selected train
     */
    public TrainByCarTypeFrame(Train train) {
        super();
        _train = train;
        initComponents(null);
    }

    /**
     * Show how a car for a given train is serviced
     * 
     * @param car the car being checked
     */
    public TrainByCarTypeFrame(Car car) {
        super();
        if (car != null) {
            _train = car.getTrain();
        }
        initComponents(car);
    }

    private void initComponents(Car car) {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Set up the panels
        JPanel pTrain = new JPanel();
        pTrain.setLayout(new GridBagLayout());
        pTrain.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("Train")));
        pTrain.setMaximumSize(new Dimension(2000, 50));

        addItem(pTrain, trainsComboBox, 0, 0);
        trainsComboBox.setSelectedItem(_train);

        JPanel pCarType = new JPanel();
        pCarType.setLayout(new GridBagLayout());
        pCarType.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("Type")));
        pCarType.setMaximumSize(new Dimension(2000, 50));

        addItem(pCarType, typeComboBox, 0, 0);
        addItem(pCarType, carsComboBox, 1, 0);

        pRoute.setLayout(new GridBagLayout());
        JScrollPane locationPane = new JScrollPane(pRoute);
        locationPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        locationPane.setBorder(BorderFactory.createTitledBorder(Bundle.getMessage("Route")));
        updateCarsComboBox();
        updateRoute();

        // has to be after updateCarsComboBox()
        OperationsPanel.padComboBox(carsComboBox,
                InstanceManager.getDefault(CarRoads.class).getMaxNameLength() + Control.max_len_string_attibute);

        getContentPane().add(pTrain);
        getContentPane().add(pCarType);
        getContentPane().add(locationPane);

        // setup combo box
        addComboBoxAction(trainsComboBox);
        addComboBoxAction(typeComboBox);
        addComboBoxAction(carsComboBox);

        if (car != null) {
            typeComboBox.setSelectedItem(car.getTypeName());
            carsComboBox.setSelectedItem(car);
        }

        locationManager.addPropertyChangeListener(this);
        InstanceManager.getDefault(CarTypes.class).addPropertyChangeListener(this);
        // listen to all tracks and locations
        addLocationAndTrackPropertyChange();

        addHelpMenu("package.jmri.jmrit.operations.Operations_TrainShowCarTypesServiced", true); // NOI18N

        setPreferredSize(null);
        initMinimumSize();
    }

    @Override
    public void comboBoxActionPerformed(java.awt.event.ActionEvent ae) {
        log.debug("combo box action");
        if (ae.getSource().equals(typeComboBox)) {
            updateCarsComboBox();
        }
        updateRoute();
        if (ae.getSource().equals(trainsComboBox)) {
            pack();
        }
    }

    private void updateRoute() {
        if (_train != null) {
            _train.removePropertyChangeListener(this);
        }

        pRoute.removeAll();

        if (trainsComboBox.getSelectedItem() == null) {
            _train = null;
        } else {
            _train = (Train) trainsComboBox.getSelectedItem();
        }

        if (_train == null) {
            setTitle(Bundle.getMessage("MenuItemShowCarTypes"));
            repaint();
            return;
        }

        setTitle(Bundle.getMessage("MenuItemShowCarTypes") + " " + _train.getName());
        _train.addPropertyChangeListener(this);
        log.debug("update locations served by train ({})", _train.getName());

        int y = 0;
        String carType = (String) typeComboBox.getSelectedItem();
        if (_car != null) {
            _car.removePropertyChangeListener(this);
        }
        _car = null;
        if (carsComboBox.getSelectedItem() != null) {
            _car = (Car) carsComboBox.getSelectedItem();
            _car.addPropertyChangeListener(this);
        }
        Route route = _train.getRoute();
        if (route == null) {
            repaint();
            return;
        }
        List<RouteLocation> routeList = route.getLocationsBySequenceList();
        for (RouteLocation rl : routeList) {
            JLabel loc = new JLabel();
            String locationName = rl.getName();
            loc.setText(locationName);
            addItemLeft(pRoute, loc, 0, y++);
            Location location = locationManager.getLocationByName(locationName);
            if (location == null) {
                continue;
            }
            if (_car != null && _car.getTrack() != null && !_car.getTrack().isDestinationAccepted(location)) {
                JLabel locText = new JLabel();
                locText.setText(Bundle.getMessage("CarOnTrackDestinationRestriction",
                        _car.toString(), _car.getLocationName(), _car.getTrackName(), locationName));
                addItemWidth(pRoute, locText, 2, 1, y++);
                if (_car.getLocation() != location)
                    continue;
            }
            List<Track> tracks = location.getTracksByNameList(null);
            for (Track track : tracks) {
                // show the car's track if there's a track destination
                // restriction
                if (_car != null &&
                        _car.getTrack() != null &&
                        !_car.getTrack().isDestinationAccepted(location) &&
                        _car.getTrack() != track) {
                    continue;
                }
                JLabel trk = new JLabel();
                trk.setText(track.getName());
                addItemLeft(pRoute, trk, 1, y);
                // is the car at this location and track?
                if (_car != null && location.equals(_car.getLocation()) && track.equals(_car.getTrack())) {
                    JLabel here = new JLabel(POINTER); // NOI18N
                    addItemLeft(pRoute, here, 0, y);
                }
                JLabel op = new JLabel();
                addItemLeft(pRoute, op, 2, y++);
                if (!_train.isTypeNameAccepted(carType)) {
                    op.setText(Bundle.getMessage("X(TrainType)"));
                } else if (_car != null && !_car.isCaboose() && !_train.isCarRoadNameAccepted(_car.getRoadName())) {
                    op.setText(Bundle.getMessage("X(TrainRoad)"));
                } // TODO need to do the same tests for caboose changes in the
                  // train's route
                else if (_car != null &&
                        _car.isCaboose() &&
                        _train.isCabooseNeeded() &&
                        location.equals(_car.getLocation()) &&
                        track.equals(_car.getTrack()) &&
                        !_train.getCabooseRoad().equals(Train.NONE) &&
                        !_car.getRoadName().equals(_train.getCabooseRoad()) &&
                        location.getName().equals(_train.getTrainDepartsName())) {
                    op.setText(Bundle.getMessage("X(TrainRoad)"));
                } else if (_car != null &&
                        _car.hasFred() &&
                        _train.isFredNeeded() &&
                        location.equals(_car.getLocation()) &&
                        track.equals(_car.getTrack()) &&
                        !_train.getCabooseRoad().equals(Train.NONE) &&
                        !_car.getRoadName().equals(_train.getCabooseRoad()) &&
                        location.getName().equals(_train.getTrainDepartsName())) {
                    op.setText(Bundle.getMessage("X(TrainRoad)"));
                } else if (_car != null &&
                        !_car.isCaboose() &&
                        !_car.isPassenger() &&
                        !_train.isLoadNameAccepted(_car.getLoadName(), _car.getTypeName())) {
                    op.setText(Bundle.getMessage("X(TrainLoad)"));
                } else if (_car != null && !_train.isBuiltDateAccepted(_car.getBuilt())) {
                    op.setText(Bundle.getMessage("X(TrainBuilt)"));
                } else if (_car != null && !_train.isOwnerNameAccepted(_car.getOwnerName())) {
                    op.setText(Bundle.getMessage("X(TrainOwner)"));
                } else if (_train.isLocationSkipped(rl)) {
                    op.setText(Bundle.getMessage("X(TrainSkips)"));
                } else if (!rl.isDropAllowed() && !rl.isPickUpAllowed()) {
                    op.setText(Bundle.getMessage("X(Route)"));
                } else if (rl.getMaxCarMoves() <= 0) {
                    op.setText(Bundle.getMessage("X(RouteMoves)"));
                } else if (!location.acceptsTypeName(carType)) {
                    op.setText(Bundle.getMessage("X(LocationType)"));
                } // check route before checking train, check train calls check
                  // route
                else if (!track.isPickupRouteAccepted(route) && !track.isDropRouteAccepted(route)) {
                    op.setText(Bundle.getMessage("X(TrackRoute)"));
                } else if (!track.isPickupTrainAccepted(_train) && !track.isDropTrainAccepted(_train)) {
                    op.setText(Bundle.getMessage("X(TrackTrain)"));
                } else if (!track.isTypeNameAccepted(carType)) {
                    op.setText(Bundle.getMessage("X(TrackType)"));
                } else if (_car != null && !track.isRoadNameAccepted(_car.getRoadName())) {
                    op.setText(Bundle.getMessage("X(TrackRoad)"));
                } else if (_car != null &&
                        _car.getTrack() != track &&
                        !track.isLoadNameAndCarTypeAccepted(_car.getLoadName(), _car.getTypeName())) {
                    op.setText(Bundle.getMessage("X(TrackLoad)"));
                } else if (_car != null &&
                        !track.isDestinationAccepted(_car.getFinalDestination()) &&
                        _car.getDestination() == null) {
                    op.setText(Bundle.getMessage("X(TrackDestination)"));
                } else if ((rl.getTrainDirection() & location.getTrainDirections()) == 0) {
                    op.setText(Bundle.getMessage("X(DirLoc)"));
                } else if ((rl.getTrainDirection() & track.getTrainDirections()) == 0) {
                    op.setText(Bundle.getMessage("X(DirTrk)"));
                } else if (!track.checkScheduleAttribute(Track.TYPE, carType, null)) {
                    op.setText(Bundle.getMessage("X(ScheduleType)"));
                } else if (!track.checkScheduleAttribute(Track.LOAD, carType, _car)) {
                    op.setText(Bundle.getMessage("X(ScheduleLoad)"));
                } else if (!track.checkScheduleAttribute(Track.ROAD, carType, _car)) {
                    op.setText(Bundle.getMessage("X(ScheduleRoad)"));
                } else if (!track.checkScheduleAttribute(Track.TRAIN_SCHEDULE, carType, _car)) {
                    op.setText(Bundle.getMessage("X(ScheduleTrain)"));
                } else if (!track.checkScheduleAttribute(Track.ALL, carType, _car)) {
                    op.setText(Bundle.getMessage("X(Schedule)"));
                } else if (!track.isPickupTrainAccepted(_train)) {
                    // can the train drop off car?
                    if (rl.isDropAllowed() && track.isDropTrainAccepted(_train)) {
                        op.setText(Bundle.getMessage("DropOnly"));
                    } else {
                        op.setText(Bundle.getMessage("X(TrainPickup)"));
                    }
                } else if (!track.isDropTrainAccepted(_train)) {
                    // can the train pick up car?
                    if (rl.isPickUpAllowed() && track.isPickupTrainAccepted(_train)) {
                        op.setText(Bundle.getMessage("PickupOnly"));
                    } else {
                        op.setText(Bundle.getMessage("X(TrainDrop)"));
                    }
                    // determine if a local move with default loads
                } else if (!_train.isLocalSwitcher() &&
                        _car != null &&
                        _car.getTrack() != track &&
                        _car.getLocation() == track.getLocation() &&
                        _car.getFinalDestination() == null &&
                        _car.getDivision() == null &&
                        (_car.getLoadName().equals(InstanceManager.getDefault(CarLoads.class).getDefaultEmptyName()) ||
                                _car.getLoadName()
                                        .equals(InstanceManager.getDefault(CarLoads.class).getDefaultLoadName()))) {
                    op.setText(Bundle.getMessage("X(LocalMove)"));
                    // determine if local move with custom load or final
                    // destination is allowed
                } else if (!_train.isAllowLocalMovesEnabled() &&
                        _car != null &&
                        _car.getTrack() != track &&
                        _car.getLocation() == track.getLocation() &&
                        (_car.getDivision() != null ||
                                _car.getFinalDestination() != null ||
                                !_car.getLoadName()
                                        .equals(InstanceManager.getDefault(CarLoads.class).getDefaultEmptyName()) &&
                                        !_car.getLoadName().equals(
                                                InstanceManager.getDefault(CarLoads.class).getDefaultLoadName()))) {
                    op.setText(Bundle.getMessage("X(TrainLocalMove)"));
                    // determine if spur can accept car with custom load
                } else if (track.isSpur() &&
                        track.getSchedule() == null &&
                        _car != null &&
                        _car.getTrack() != track &&
                        !_car.getLoadName().equals(InstanceManager.getDefault(CarLoads.class).getDefaultEmptyName()) &&
                        !_car.getLoadName().equals(InstanceManager.getDefault(CarLoads.class).getDefaultLoadName())) {
                    op.setText(Bundle.getMessage("X(TrackCustomLoad)"));
                    // empty car with a home division
                } else if (_car != null &&
                        _car.getDivision() != null &&
                        _car.getLoadType().equals(CarLoad.LOAD_TYPE_EMPTY) &&
                        _car.getDivision() != track.getDivision() &&
                        !track.isInterchange()) {
                    op.setText(Bundle.getMessage("X(Division)"));
                    // loaded car with a home division
                } else if (_car != null &&
                        _car.getDivision() != null &&
                        _car.getLoadType().equals(CarLoad.LOAD_TYPE_LOAD) &&
                        _car.getDivision() != _car.getTrack().getDivision() &&
                        _car.getDivision() != track.getDivision() &&
                        !track.isInterchange()) {
                    op.setText(Bundle.getMessage("X(Division)"));
                } else if (rl.isDropAllowed() && rl.isPickUpAllowed()) {
                    op.setText(Bundle.getMessage("ButtonOK"));
                } else if (rl.isDropAllowed()) {
                    op.setText(Bundle.getMessage("DropOnly"));
                } else if (rl.isPickUpAllowed()) {
                    op.setText(Bundle.getMessage("PickupOnly"));
                } else {
                    op.setText("X"); // default shouldn't occur
                }
            }
        }
        pRoute.revalidate();
        repaint();
    }

    private void updateComboBox() {
        log.debug("update combobox");
        InstanceManager.getDefault(CarTypes.class).updateComboBox(typeComboBox);
    }

    private void updateCarsComboBox() {
        log.debug("update car combobox");
        carsComboBox.removeAllItems();
        String carType = (String) typeComboBox.getSelectedItem();
        // load car combobox
        carsComboBox.addItem(null);
        List<Car> cars = InstanceManager.getDefault(CarManager.class).getByTypeList(carType);
        for (Car car : cars) {
            carsComboBox.addItem(car);
        }
    }

    /**
     * Add property listeners for locations and tracks
     */
    private void addLocationAndTrackPropertyChange() {
        for (Location loc : locationManager.getList()) {
            loc.addPropertyChangeListener(this);
            for (Track track : loc.getTracksList()) {
                track.addPropertyChangeListener(this);
                Schedule schedule = track.getSchedule();
                if (schedule != null) {
                    schedule.addPropertyChangeListener(this);
                }
            }
        }
    }

    /**
     * Remove property listeners for locations and tracks
     */
    private void removeLocationAndTrackPropertyChange() {
        for (Location loc : locationManager.getList()) {
            loc.removePropertyChangeListener(this);
            for (Track track : loc.getTracksList()) {
                track.removePropertyChangeListener(this);
                Schedule schedule = track.getSchedule();
                if (schedule != null) {
                    schedule.removePropertyChangeListener(this);
                }
            }
        }
    }

    @Override
    public void dispose() {
        locationManager.removePropertyChangeListener(this);
        InstanceManager.getDefault(CarTypes.class).removePropertyChangeListener(this);
        removeLocationAndTrackPropertyChange();
        if (_train != null) {
            _train.removePropertyChangeListener(this);
        }
        if (_car != null) {
            _car.removePropertyChangeListener(this);
        }
        super.dispose();
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        log.debug("Property change ({}) old: ({}) new: ({})", e.getPropertyName(), e.getOldValue(), e.getNewValue()); // NOI18N
        if (e.getSource().equals(_car) ||
                e.getSource().equals(_train) ||
                e.getSource().getClass().equals(Track.class) ||
                e.getSource().getClass().equals(Location.class) ||
                e.getSource().getClass().equals(Schedule.class) ||
                e.getPropertyName().equals(LocationManager.LISTLENGTH_CHANGED_PROPERTY) ||
                e.getPropertyName().equals(Route.LISTCHANGE_CHANGED_PROPERTY)) {
            updateRoute();
        }
        if (e.getPropertyName().equals(Train.DISPOSE_CHANGED_PROPERTY)) {
            dispose();
        }
        if (e.getPropertyName().equals(CarTypes.CARTYPES_CHANGED_PROPERTY) ||
                e.getPropertyName().equals(CarTypes.CARTYPES_NAME_CHANGED_PROPERTY)) {
            updateComboBox();
        }
        if (e.getPropertyName().equals(Location.LENGTH_CHANGED_PROPERTY)) {
            // a track has been add or deleted update property listeners
            removeLocationAndTrackPropertyChange();
            addLocationAndTrackPropertyChange();
        }
    }

    private final static Logger log = LoggerFactory.getLogger(TrainByCarTypeFrame.class);
}
