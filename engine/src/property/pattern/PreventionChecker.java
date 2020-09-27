package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;

public class PreventionChecker extends PropertyChecker {
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        return false;
    }

    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty) {
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
