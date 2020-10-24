package property;

import log.Log;
import log.Snapshot;
import property.pattern.NumberofEventsChecker;

import java.util.StringTokenizer;

public class MCINumberofEventsChecker extends NumberofEventsChecker {
    public MCINumberofEventsChecker() { super(); }

    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("CurrentMsgCount:"))
                break;
        }

        //Simply checks for number of routed message not exceeding threshold value (<=)
        //Should probably implement something more complex like below message types
//        FF_TO_FF_SEND = 0;
//        FF_TO_FF_RECV = 0;
//        FF_TO_ORG_SEND = 0;
//        FF_TO_ORG_RECV = 0;
//        ORG_TO_FF_SEND = 0;
//        ORG_TO_FF_RECV = 0;
//        AMB_TO_ORG_SEND = 0;
//        AMB_TO_ORG_RECV = 0;
//        ORG_TO_AMB_SEND = 0;
//        ORG_TO_AMB_RECV = 0;
//        BRIDGE_TO_ORG_SEND = 0;
//        BRIDGE_TO_ORG_RECV = 0;
//        ORG_TO_BRIDGE_SEND = 0;
//        ORG_TO_BRIDGE_RECV = 0;

        int routeCount = Integer.parseInt(st.nextToken());
//        System.out.println("ROUTEMSGCOUNT: " + routeCount);

        if (routeCount <= (int) verificationProperty.getThresholdValue()) {
            return false;
        }
        else {
            return true;
        }

    }

    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, int until) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int T) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, int t, int T) {
        return false;
    }
}
