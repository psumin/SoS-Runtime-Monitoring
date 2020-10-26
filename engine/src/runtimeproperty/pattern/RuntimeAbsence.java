package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.RuntimeProperty;
import runtimeproperty.Scope;

public class RuntimeAbsence extends RuntimeProperty {
    Event targetEvent;

    public RuntimeAbsence(Event event, Scope scope) {
        super(scope);
        this.targetEvent = event;
        this.name = "It is never the case that " + event.getName() + " holds";
    }

    protected void evaluateState(Snapshot snapshot) {
        this.isHolding = !targetEvent.checkHold(snapshot);

        if (!isHolding) {
            this.beConfirmed(snapshot);
        }
    }
}
