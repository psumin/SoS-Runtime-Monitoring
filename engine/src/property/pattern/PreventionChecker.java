package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;

public abstract class PreventionChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);

    @Override
    public boolean check(Log log, Property verificationProperty) {
        return evaluateState(log, verificationProperty);
    }
}
