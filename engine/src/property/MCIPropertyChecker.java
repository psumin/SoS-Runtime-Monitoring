package property;

import log.Log;
import log.Snapshot;
import property.pattern.ExistenceChecker;

import java.util.StringTokenizer;

public class MCIPropertyChecker extends ExistenceChecker {

    public MCIPropertyChecker() {

        super();
    }

    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while (st.hasMoreTokens()) {
            String target = st.nextToken();
            if (target.equals("TreatmentRate:"))
                break;
        }

        double TreatmentRate = Double.parseDouble(st.nextToken());
//        System.out.println(TreatmentRate);

        return TreatmentRate >= verificationProperty.getValue();
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