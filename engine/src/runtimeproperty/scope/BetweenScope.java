package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.Scope;

public class BetweenScope extends Scope {
    Event startEvent;
    Event endEvent;
    boolean isHappened;

    public BetweenScope(Event startEvent, Event endEvent) {
        this.name = "Between " + startEvent.getName() + " and " + endEvent.getName();
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.isHappened = false;
    }

    public boolean checkScope(Snapshot snapshot) {
        if (!this.isHappened && this.startEvent.checkHold(snapshot)) {
            this.isHappened = true;
            return true;
        }

        if (this.isHappened && this.endEvent.checkHold(snapshot)) {
            this.isHappened = false;
            return false;
        }

        return this.isHappened;
    }
}
