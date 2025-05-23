/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmri.web.servlet.operations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jmri.InstanceManager;
import jmri.jmrit.operations.locations.Track;
import jmri.jmrit.operations.rollingstock.RollingStock;
import jmri.jmrit.operations.rollingstock.cars.Car;
import jmri.jmrit.operations.rollingstock.engines.Engine;
import jmri.jmrit.operations.routes.RouteLocation;
import jmri.jmrit.operations.setup.Setup;
import jmri.jmrit.operations.trains.Train;
import jmri.jmrit.operations.trains.schedules.TrainScheduleManager;
import jmri.jmrit.operations.trains.trainbuilder.TrainCommon;

/**
 *
 * @author Randall Wood
 */
public class HtmlTrainCommon extends TrainCommon {

    protected final Properties strings = new Properties();
    protected final Locale locale;
    protected final Train train;
    protected String resourcePrefix;

    protected enum ShowLocation {

        location, track, both;
    }

    static private final Logger log = LoggerFactory.getLogger(HtmlTrainCommon.class);

    public HtmlTrainCommon(Locale locale, Train train) throws IOException {
        this.locale = locale;
        this.train = train;
        FileInputStream is = null;
        try {
            is = new FileInputStream(Bundle.getMessage(locale, "ManifestStrings.properties"));
            strings.load(is);
            is.close();
        }
        catch (IOException ex) {
            if (is != null) {
                is.close();
            }
            throw ex;
        }
    }

    public String pickupUtilityCars(List<Car> cars, Car car, boolean isManifest) {
        // list utility cars by type, track, length, and load
        String[] messageFormat;
        if (isManifest) {
            messageFormat = Setup.getPickupUtilityManifestMessageFormat();
        } else {
            messageFormat = Setup.getPickupUtilitySwitchListMessageFormat();
        }
        int count = countUtilityCars(messageFormat, cars, car, PICKUP);
        if (count == 0) {
            return ""; // already printed out this car type
        }
        return pickUpCar(car, count, messageFormat);
    }

    protected String setoutUtilityCars(List<Car> cars, Car car, boolean isManifest) {
        boolean isLocal = car.isLocalMove();
        if (Setup.isSwitchListFormatSameAsManifest()) {
            isManifest = true;
        }
        String[] messageFormat = Setup.getDropUtilityManifestMessageFormat();
        if (isLocal && isManifest) {
            messageFormat = Setup.getLocalUtilityManifestMessageFormat();
        } else if (isLocal && !isManifest) {
            messageFormat = Setup.getLocalUtilitySwitchListMessageFormat();
        } else if (!isLocal && !isManifest) {
            messageFormat = Setup.getDropUtilitySwitchListMessageFormat();
        }
        int count = countUtilityCars(messageFormat, cars, car, !PICKUP);
        if (count == 0) {
            return ""; // already printed out this car type
        }
        return dropCar(car, count, messageFormat, isLocal);
    }

    protected String pickUpCar(Car car, String[] format) {
        return pickUpCar(car, 0, format);
    }

    protected String pickUpCar(Car car, int count, String[] format) {
        StringBuilder builder = new StringBuilder();
        builder.append("<span style=\"color: " + Setup.getPickupTextColor() + ";\">");
        builder.append(Setup.getPickupCarPrefix()).append(" ");
        // count the number of utility cars
        if (count != 0) {
            builder.append(count);
        }
        for (String attribute : format) {
            builder.append(
                    String.format(locale, strings.getProperty("Attribute"), getCarAttribute(car, attribute, PICKUP,
                            !LOCAL), attribute.toLowerCase())).append(" "); // NOI18N
        }
        log.debug("Picking up car {}", builder);
        return String.format(locale, strings.getProperty(this.resourcePrefix + "PickUpCar"), builder.toString()); // NOI18N
    }

    protected String dropCar(Car car, String[] format, boolean isLocal) {
        return dropCar(car, 0, format, isLocal);
    }

    protected String dropCar(Car car, int count, String[] format, boolean isLocal) {
        StringBuilder builder = new StringBuilder();
        if (!isLocal) {
            builder.append("<span style=\"color: " + Setup.getDropTextColor() + ";\">");
            builder.append(Setup.getDropCarPrefix()).append(" ");
        } else {
            builder.append("<span style=\"color: " + Setup.getLocalTextColor() + ";\">");
            builder.append(Setup.getLocalPrefix()).append(" ");
        }
        // count the number of utility cars
        if (count != 0) {
            builder.append(count);
        }
        for (String attribute : format) {
            builder.append(
                    String.format(locale, strings.getProperty("Attribute"), getCarAttribute(car, attribute, !PICKUP,
                            isLocal), attribute.toLowerCase())).append(" "); // NOI18N
        }
        log.debug("Dropping {}car {}", (isLocal) ? "local " : "", builder);
        if (!isLocal) {
            return String.format(locale, strings.getProperty(this.resourcePrefix + "DropCar"), builder.toString()); // NOI18N
        } else {
            return String.format(locale, strings.getProperty(this.resourcePrefix + "LocalCar"), builder.toString()); // NOI18N
        }
    }

    protected String engineChange(RouteLocation rl, int legOptions) {
        if ((legOptions & Train.HELPER_ENGINES) == Train.HELPER_ENGINES) {
            return String.format(strings.getProperty("AddHelpersAt"), rl.getSplitName()); // NOI18N
        } else if ((legOptions & Train.CHANGE_ENGINES) == Train.CHANGE_ENGINES
                && ((legOptions & Train.REMOVE_CABOOSE) == Train.REMOVE_CABOOSE || (legOptions & Train.ADD_CABOOSE) == Train.ADD_CABOOSE)) {
            return String.format(strings.getProperty("LocoAndCabooseChangeAt"), rl.getSplitName()); // NOI18N
        } else if ((legOptions & Train.CHANGE_ENGINES) == Train.CHANGE_ENGINES) {
            return String.format(strings.getProperty("LocoChangeAt"), rl.getSplitName()); // NOI18N
        } else if ((legOptions & Train.REMOVE_CABOOSE) == Train.REMOVE_CABOOSE
                || (legOptions & Train.ADD_CABOOSE) == Train.ADD_CABOOSE) {
            return String.format(strings.getProperty("CabooseChangeAt"), rl.getSplitName()); // NOI18N
        }
        return "";
    }

    protected String dropEngines(List<Engine> engines, RouteLocation location) {
        StringBuilder builder = new StringBuilder();
        for (Engine engine : engines) {
            if (engine.getRouteDestination().equals(location)) {
                builder.append(dropEngine(engine));
            }
        }
        return String.format(strings.getProperty("EnginesList"), builder.toString());
    }

    @Override
    public String dropEngine(Engine engine) {
        StringBuilder builder = new StringBuilder();
        builder.append("<span style=\"color: " + Setup.getDropEngineTextColor() + ";\">");
        builder.append(Setup.getDropEnginePrefix()).append(" ");
        for (String attribute : Setup.getDropEngineMessageFormat()) {
            builder.append(
                    String.format(locale, strings.getProperty("Attribute"),
                            getEngineAttribute(engine, attribute, false), attribute.toLowerCase())).append(" ");
        }
        log.debug("Drop engine: {}", builder);
        return String.format(locale, strings.getProperty(this.resourcePrefix + "DropEngine"), builder.toString());
    }

    protected String pickupEngines(List<Engine> engines, RouteLocation location) {
        StringBuilder builder = new StringBuilder();
        for (Engine engine : engines) {
            if (engine.getRouteLocation().equals(location) && !engine.getTrackName().isEmpty()) {
                builder.append(pickupEngine(engine));
            }
        }
        return String.format(locale, strings.getProperty("EnginesList"), builder.toString());
    }

    @Override
    public String pickupEngine(Engine engine) {
        StringBuilder builder = new StringBuilder();
        builder.append("<span style=\"color: " + Setup.getPickupEngineTextColor() + ";\">");
        builder.append(Setup.getPickupEnginePrefix()).append(" ");
        for (String attribute : Setup.getPickupEngineMessageFormat()) {
            builder.append(
                    String.format(locale, strings.getProperty("Attribute"),
                            getEngineAttribute(engine, attribute, true), attribute.toLowerCase())).append(" ");
        }
        log.debug("Picking up engine: {}", builder);
        return String.format(locale, strings.getProperty(this.resourcePrefix + "PickUpEngine"), builder.toString());
    }

    protected String getCarAttribute(Car car, String attribute, boolean isPickup, boolean isLocal) {
        if (attribute.equals(Setup.LOAD)) {
            return (car.isCaboose() || car.isPassenger()) ? ""
                    : StringEscapeUtils.escapeHtml4(car.getLoadName().split(TrainCommon.HYPHEN)[0]); // NOI18N
        } else if (attribute.equals(Setup.LOAD_TYPE)) {
            return car.getLoadType();
        } else if (attribute.equals(Setup.HAZARDOUS)) {
            return car.isHazardous() ? Setup.getHazardousMsg() : ""; // NOI18N
        } else if (attribute.equals(Setup.DROP_COMMENT)) {
            return car.getDropComment();
        } else if (attribute.equals(Setup.PICKUP_COMMENT)) {
            return car.getPickupComment();
        } else if (attribute.equals(Setup.KERNEL)) {
            return car.getKernelName();
        } else if (attribute.equals(Setup.KERNEL_SIZE)) {
            if (car.getKernel() != null) {
                return Integer.toString(car.getKernel().getSize());
            } else {
                return "";
            }
        } else if (attribute.equals(Setup.RWE)) {
            if (!car.getReturnWhenEmptyDestinationName().isEmpty()) {
                return String.format(locale, strings.getProperty("RWELocationAndTrack"), StringEscapeUtils
                        .escapeHtml4(car.getSplitReturnWhenEmptyDestinationName()), StringEscapeUtils
                        .escapeHtml4(car.getSplitReturnWhenEmptyDestinationTrackName()));
            }
            return ""; // NOI18N
        } else if (attribute.equals(Setup.FINAL_DEST)) {
            if (!car.getFinalDestinationName().isEmpty()) {
                return String.format(locale, strings.getProperty("FinalDestinationLocation"), StringEscapeUtils
                        .escapeHtml4(car.getSplitFinalDestinationName()));
            }
            return "";
        } else if (attribute.equals(Setup.FINAL_DEST_TRACK)) {
            if (!car.getFinalDestinationName().isEmpty()) {
                return String.format(locale, strings.getProperty("FinalDestinationLocationAndTrack"), StringEscapeUtils
                        .escapeHtml4(car.getSplitFinalDestinationName()), StringEscapeUtils
                                .escapeHtml4(car.getSplitFinalDestinationTrackName()));
            }
            return "";
        } else if (attribute.equals(Setup.DIVISION)) {
            return car.getDivisionName();
        } else if (attribute.equals(Setup.BLOCKING_ORDER)) {
            if (car.isPassenger()) {
                return Integer.toString(car.getBlocking());
            }
            return "";
        }
        return getRollingStockAttribute(car, attribute, isPickup, isLocal);
    }

    protected String getEngineAttribute(Engine engine, String attribute, boolean isPickup) {
        if (attribute.equals(Setup.MODEL)) {
            return engine.getModel();
        }
        if (attribute.equals(Setup.HP)) {
            return engine.getHp();
        }
        if (attribute.equals(Setup.CONSIST)) {
            return engine.getConsistName();
        }
        if (attribute.equals(Setup.DCC_ADDRESS)) {
            return engine.getDccAddress();
        }
        return getRollingStockAttribute(engine, attribute, isPickup, false);
    }

    protected String getRollingStockAttribute(RollingStock rs, String attribute, boolean isPickup, boolean isLocal) {
        if (attribute.equals(Setup.NUMBER)) {
            return splitString(rs.getNumber());
        } else if (attribute.equals(Setup.ROAD)) {
            return StringEscapeUtils.escapeHtml4(rs.getRoadName().split(TrainCommon.HYPHEN)[0]);
        } else if (attribute.equals(Setup.TYPE)) {
            return rs.getTypeName().split(TrainCommon.HYPHEN)[0];
        } else if (attribute.equals(Setup.LENGTH)) {
            return rs.getLength();
        } else if (attribute.equals(Setup.WEIGHT)) {
            return Integer.toString(rs.getAdjustedWeightTons());
        } else if (attribute.equals(Setup.COLOR)) {
            return rs.getColor();
        } else if (attribute.equals(Setup.LOCATION) && (isPickup || isLocal)
                || (attribute.equals(Setup.TRACK) && isPickup)) {
            if (rs.getTrack() != null) {
                return String.format(locale, strings.getProperty("FromTrack"), StringEscapeUtils.escapeHtml4(rs
                        .getSplitTrackName()));
            }
            return "";
        } else if (attribute.equals(Setup.LOCATION) && !isPickup && !isLocal) {
            return ""; // we don't have the car's origin, so nothing to return
            // Note that the JSON database does have the car's origin, so this could be fixed.
//            return String.format(locale, strings.getProperty("FromLocation"), StringEscapeUtils.escapeHtml4(rs
//                    .getLocationName()));
        } else if (attribute.equals(Setup.DESTINATION) && isPickup) {
            return String.format(locale, strings.getProperty("ToLocation"), StringEscapeUtils
                    .escapeHtml4(rs.getSplitDestinationName()));
        } else if ((attribute.equals(Setup.DESTINATION) || attribute.equals(Setup.TRACK)) && !isPickup) {
            return String.format(locale, strings.getProperty("ToTrack"), StringEscapeUtils.escapeHtml4(rs
                    .getSplitDestinationTrackName()));
        } else if (attribute.equals(Setup.DEST_TRACK)) {
            return String.format(locale, strings.getProperty("ToLocationAndTrack"), StringEscapeUtils
                    .escapeHtml4(rs.getSplitDestinationName()), StringEscapeUtils.escapeHtml4(rs
                    .getSplitDestinationTrackName()));
        } else if (attribute.equals(Setup.OWNER)) {
            return StringEscapeUtils.escapeHtml4(rs.getOwnerName());
        } else if (attribute.equals(Setup.COMMENT)) {
            return StringEscapeUtils.escapeHtml4(rs.getComment());
        } else if (attribute.equals(Setup.LAST_TRAIN)) {
            return String.format(locale, strings.getProperty("LastTrain"),
                    StringEscapeUtils.escapeHtml4(rs.getLastTrainName()));
        } else if (attribute.equals(Setup.BLANK) || attribute.equals(Setup.NO_NUMBER)
                || attribute.equals(Setup.NO_ROAD) || attribute.equals(Setup.NO_COLOR)
                || attribute.equals(Setup.NO_DESTINATION) || attribute.equals(Setup.NO_DEST_TRACK)
                || attribute.equals(Setup.NO_LOCATION) || attribute.equals(Setup.NO_TRACK)
                || attribute.equals(Setup.TAB) || attribute.equals(Setup.TAB2) || attribute.equals(Setup.TAB3)) {
            // attributes that don't print
            return "";
        }
        return String.format(Bundle.getMessage(locale, "ErrorPrintOptions"), attribute); // something is isn't right!
    }

    protected String getTrackComments(RouteLocation location, List<Car> cars) {
        StringBuilder builder = new StringBuilder();
        if (location.getLocation() != null) {
            List<Track> tracks = location.getLocation().getTracksByNameList(null);
            for (Track track : tracks) {
                // any pick ups or set outs to this track?
                boolean pickup = false;
                boolean setout = false;
                for (Car car : cars) {
                    if (car.getRouteLocation() == location && car.getTrack() != null && car.getTrack() == track) {
                        pickup = true;
                    }
                    if (car.getRouteDestination() == location && car.getDestinationTrack() != null
                            && car.getDestinationTrack() == track) {
                        setout = true;
                    }
                }
                // print the appropriate comment if there's one
                if (pickup && setout && !track.getCommentBoth().isEmpty()) {
                    builder.append(String.format(locale, strings.getProperty("TrackComments"), StringEscapeUtils
                            .escapeHtml4(track.getCommentBothWithColor())));
                } else if (pickup && !setout && !track.getCommentPickup().isEmpty()) {
                    builder.append(String.format(locale, strings.getProperty("TrackComments"), StringEscapeUtils
                            .escapeHtml4(track.getCommentPickupWithColor())));
                } else if (!pickup && setout && !track.getCommentSetout().isEmpty()) {
                    builder.append(String.format(locale, strings.getProperty("TrackComments"), StringEscapeUtils
                            .escapeHtml4(track.getCommentSetoutWithColor())));
                }
            }
        }
        return builder.toString();
    }

    public String getValidity() {
        if (Setup.isPrintTrainScheduleNameEnabled()) {
            return String.format(locale, strings.getProperty("ManifestValidityWithSchedule"), getDate(true),
                    InstanceManager.getDefault(TrainScheduleManager.class).getActiveSchedule().getName());
        } else {
            return String.format(locale, strings.getProperty("ManifestValidity"), getDate(true));
        }
    }

    /**
     * @param text Text with color tags needing conversion. See
     *             TrainCommon.formatColorString(String text, Color color) Also
     *             converts line feeds to HTLM
     * @return HTML text with style color option
     */
    public static String convertToHTMLColor(String text) {
        // convert line feeds
        text = text.replace("\n", "<br>");

        text = text.replace("&lt;FONT color=&quot;", "<p style=\"color: ");
        text = text.replace("&quot;&gt;", "\">");
        text = text.replace("&lt;/FONT&gt;", "");

        return text;
    }
}
