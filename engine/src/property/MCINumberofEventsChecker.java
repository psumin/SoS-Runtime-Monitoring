package property;

import log.Log;
import log.Snapshot;
import property.pattern.NumberofEventsChecker;

import java.util.StringTokenizer;

public class MCINumberofEventsChecker extends NumberofEventsChecker {
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        System.out.println("SNAP:" + snapshot);
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("CurrentAction:"))
                break;
        }

        System.out.println("MCINUMEVE: " + st);

        return false;
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
