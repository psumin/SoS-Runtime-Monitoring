package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.Scope;

public class AfterScope extends Scope {
    Event endEvent;
    boolean isHappened;

    public AfterScope(Event event) {
        this.name = "After " + event.getName();
        this.endEvent = event;
        this.isHappened = false;
    }

    public boolean checkScope(Snapshot snapshot) {
        if (this.isHappened)
            return true;

        if (endEvent.checkHold(snapshot)) {
            this.isHappened = true;
            this.isPassed = true;
            return true;
        }

        return false;
    }
}
