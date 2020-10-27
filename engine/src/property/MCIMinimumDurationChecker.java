package property;

import log.Log;
import log.Snapshot;
import property.pattern.MinimumDurationChecker;

import java.util.StringTokenizer;

public class MCIMinimumDurationChecker extends MinimumDurationChecker {
    public MCIMinimumDurationChecker() {
        super();
    }

    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while (st.hasMoreTokens()) {
            String target = st.nextToken();
            if (target.equals("CurrentFF:"))
                break;
        }

        int currentActivatedCSs = Integer.parseInt(st.nextToken());

        return currentActivatedCSs >= (int) verificationProperty.getThresholdValue();
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

    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {
        return false;
    }
}
