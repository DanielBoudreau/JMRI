package jmri.jmrit.withrottle;

import java.beans.PropertyChangeEvent;

import jmri.*;
import jmri.jmrit.roster.RosterEntry;
import jmri.jmrit.throttle.ThrottlesPreferences;

/**
 * @author Brett Hoffman Copyright (C) 2011
 */
public class MultiThrottleController extends ThrottleController {
    
    protected boolean isStealAddress;

    public MultiThrottleController(char id, String key, ThrottleControllerListener tcl, ControllerInterface ci) {
        super(id, tcl, ci);
        log.debug("New MT controller");
        locoKey = key;
        isStealAddress = false;
    }

    /**
     * Builds a header to send to the wi-fi device for use in a message.
     * Includes a separator - {@literal <;>}
     *
     * @param chr the character indicating what action is performed
     * @return a pre-assembled header for this DccThrottle
     */
    public String buildPacketWithChar(char chr) {
        return ("M" + whichThrottle + chr + locoKey + "<;>");
    }


    /*
     * Send a message to the wi-fi device that a bound property of a DccThrottle
     * has changed.  Currently only handles function state.
     * Current Format:  Header + F(0 or 1) + function number
     *
     * Event may be from regular throttle or consist throttle, but is handled the same.
     *
     * Bound params: SpeedSteps, IsForward, SpeedSetting, F##, F##Momentary
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String eventName = event.getPropertyName();
        log.debug("property change: {}",eventName);
        if (eventName.startsWith("F")) {
            if (eventName.contains("Momentary")) {
                return;
            }
            StringBuilder message = new StringBuilder(buildPacketWithChar('A'));

            try {
                if ((Boolean) event.getNewValue()) {
                    message.append("F1");
                } else {
                    message.append("F0");
                }
                message.append(eventName.substring(1));
            } catch (ClassCastException cce) {
                log.debug("Invalid event value. {}", cce.getMessage());
            } catch (IndexOutOfBoundsException oob) {
                log.debug("Invalid event name. {}", oob.getMessage());
            }

            for (ControllerInterface listener : controllerListeners) {
                listener.sendPacketToDevice(message.toString());
            }
        }
        if (eventName.matches(Throttle.SPEEDSTEPS)) {
            StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
            message.append("s");
            message.append(encodeSpeedStepMode((SpeedStepMode)event.getNewValue()));
            for (ControllerInterface listener : controllerListeners) {
                listener.sendPacketToDevice(message.toString());
            }
        }
        if (eventName.matches(Throttle.ISFORWARD)) {
            StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
            message.append("R");
            message.append((Boolean) event.getNewValue() ? "1" : "0");
            for (ControllerInterface listener : controllerListeners) {
               listener.sendPacketToDevice(message.toString());
            }
        }
        if (eventName.matches(Throttle.SPEEDSETTING)) {
            float currentSpeed = ((Float) event.getNewValue()).floatValue();
            log.debug("Speed Setting: {} head of queue {}",currentSpeed, lastSentSpeed.peek());
            if(lastSentSpeed.isEmpty()) { 
               StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
               message.append("V");
               message.append(Math.round(currentSpeed / speedMultiplier));
               for (ControllerInterface listener : controllerListeners) {
                   listener.sendPacketToDevice(message.toString());
               }
            } else {
               if( Math.abs(lastSentSpeed.peek().floatValue()-currentSpeed)<0.0005 ) {
                  Float f = lastSentSpeed.poll(); // remove the value from the list.
                  log.debug("removed value {} from queue",f);
               }
            }
        }
    }

    /**
     * This replaces the previous method of sending a string of function labels.
     * 
     * Checks for labels across all possible functions of this roster entry.
     * 
     * Example:
     * {@code MTLL1234<;>]\[Light]\[Bell]\[Horn]\[]\[]\[]\[]\[]\[Mute]\[]\[]\[]\[} etc.
     */
    @Override
    public void sendFunctionLabels(RosterEntry re) {

        if (re != null) {
            StringBuilder functionString = new StringBuilder(buildPacketWithChar('L'));

            int i;
            for (i = 0; i < (re.getMaxFnNumAsInt()+1); i++) {
                functionString.append("]\\[");
                if ((re.getFunctionLabel(i) != null)) {
                    functionString.append(re.getFunctionLabel(i));
                }
            }
            for (ControllerInterface listener : controllerListeners) {
                listener.sendPacketToDevice(functionString.toString());
            }
        }
    }

    /**
     * This replaces the previous method of sending a string of function states,
     * and now sends them individually, the same as a property change would.
     *
     * @param t the throttle to send the states of.
     */
    @Override
    public void sendAllFunctionStates(DccThrottle t) {
        log.debug("Sending state of all functions");
        for (int cnt = 0; cnt < t.getFunctions().length; cnt++) {
            StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
            message.append( t.getFunction(cnt) ? "F1" : "F0" );
            message.append(cnt);
            controllerListeners.forEach(listener -> {
                listener.sendPacketToDevice(message.toString());
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized protected void sendCurrentSpeed(DccThrottle t) {
        float currentSpeed = t.getSpeedSetting();
        StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
        message.append("V");
        int outSpeed = Math.round(currentSpeed / speedMultiplier);
        if(currentSpeed < 0) {
            outSpeed = -126;        // ensure estop is not rounded to zero
        }
        if(currentSpeed > 0 && outSpeed == 0) {
            outSpeed = 1;           // ensure non-zero throttle speed is sent
                                    // as non-zero speed to wiThrottle
        }
        message.append(outSpeed);
        for (ControllerInterface listener : controllerListeners) {
            listener.sendPacketToDevice(message.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendCurrentDirection(DccThrottle t) {
        StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
        message.append("R");
        message.append(t.getIsForward() ? "1" : "0");
        for (ControllerInterface listener : controllerListeners) {
            listener.sendPacketToDevice(message.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendSpeedStepMode(DccThrottle t) {
        StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
        message.append("s");
        message.append(encodeSpeedStepMode(throttle.getSpeedStepMode()));
        for (ControllerInterface listener : controllerListeners) {
            listener.sendPacketToDevice(message.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void sendAllMomentaryStates(DccThrottle t) {
        log.debug("Sending momentary state of all functions");
        for (int cnt = 0; cnt < t.getFunctionsMomentary().length; cnt++) {
            StringBuilder message = new StringBuilder(buildPacketWithChar('A'));
            message.append( t.getFunctionMomentary(cnt) ? "m1" : "m0" );
            message.append(cnt);
            controllerListeners.forEach(listener -> {
                listener.sendPacketToDevice(message.toString());
            });
        }
    }

    /**
     * {@inheritDoc} A + indicates the address was acquired, - indicates
     * released
     */
    @Override
    public void sendAddress() {
        for (ControllerInterface listener : controllerListeners) {
            if (isAddressSet) {
                listener.sendPacketToDevice(buildPacketWithChar('+'));
            } else {
                listener.sendPacketToDevice(buildPacketWithChar('-'));
            }
        }
    }

    /**
     * Send a message to a device that steal is needed. This message can be sent 
     * back to JMRI verbatim to complete a steal.
     */
    public void sendStealAddress() {
        StringBuilder message = new StringBuilder(buildPacketWithChar('S'));
        message.append(locoKey);
        for (ControllerInterface listener : controllerListeners) {
            listener.sendPacketToDevice(message.toString());
        }
    }

    /**
     * A decision is required for Throttle creation to continue.
     * <p>
     * Steal / Cancel, Share / Cancel, or Steal / Share Cancel
     * <p>
     * Callback of a request for an address that is in use.
     * Will initiate a steal only if this MTC is flagged to do so.
     * Otherwise, it will remove the request for the address.
     *
     * {@inheritDoc}
     */
    @Override
    public void notifyDecisionRequired(LocoAddress address, DecisionType question) {
        log.debug("Hardware needs a {} decision for {}", question, address);
        if ( question == DecisionType.STEAL ){
            if (isStealAddress) {
                //  Address is now staged in ThrottleManager and has been requested as a steal
                //  Complete the process
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, DecisionType.STEAL);
                isStealAddress = false;
            } else {
                //  Address has not been requested as a steal yet
                sendStealAddress();
                notifyFailedThrottleRequest(address, "Steal Required");
            }
        }
        else if ( question == DecisionType.STEAL_OR_SHARE ){ 
            if ( InstanceManager.getDefault(ThrottlesPreferences.class).isSilentShare() ){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, DecisionType.SHARE );
                return;
            }
            if ( InstanceManager.getDefault(ThrottlesPreferences.class).isSilentSteal()){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, DecisionType.STEAL );
                return;
            }
            // using the same process as a Steal as we cannot ask a Share question
            if (isStealAddress) {
                //  Address is now staged in ThrottleManager and has been requested as a steal OR share.
                //  Complete the process
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, DecisionType.STEAL);
                isStealAddress = false;
            } else {
                //  Address has not been requested as a steal yet
                sendStealAddress();
                notifyFailedThrottleRequest(address, "Steal Required");
            }
        }
        else if ( question == DecisionType.SHARE ){
            if ( InstanceManager.getDefault(ThrottlesPreferences.class).isSilentShare() ){
                InstanceManager.throttleManagerInstance().responseThrottleDecision(address, this, DecisionType.SHARE );
                return;
            }
            log.info("Share? question not supported by WiThrottle.");
            log.info("Enable silent sharing in main Throttle preferences to auto-share sessions.");
        }

    }
    
    /**
     * Add option to not silently share ("steal") the requested address
     * 
     * {@inheritDoc}
     */
    @Override
    protected void setAddress(int number, boolean isLong) {
        if(isStealAddress
                || InstanceManager.throttleManagerInstance().getThrottleUsageCount(number, isLong) == 0 
                || ! InstanceManager.getDefault(WiThrottlePreferences.class).isExclusiveUseOfAddress()) {
            super.setAddress(number, isLong);
        }
        else {
            log.debug("Loco address {} already controlled by another JMRI throttle.", number);
            sendStealAddress();
            notifyFailedThrottleRequest(new DccLocoAddress(number, isLong), "Steal from other WiThrottle or JMRI throttle Required");
        }

    }

    // Encode a SpeedStepMode to a string.
    private static String encodeSpeedStepMode(SpeedStepMode mode) {
        switch(mode) {
            // NOTE: old speed step modes use the original numeric values
            // from when speed step modes were in DccThrottle. New speed step
            // modes use the mode name.
            case NMRA_DCC_128:
                return "1";
            case NMRA_DCC_28:
                 return "2";
            case NMRA_DCC_27:
                return "4";
            case NMRA_DCC_14:
                return "8";
            case MOTOROLA_28:
                return "16";
            default:
                return mode.name;
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MultiThrottleController.class);

}
