package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.Scope;

public class BeforeScope extends Scope {
    Event startEvent;
    boolean isHappened;

    public BeforeScope(Event event) {
        this.name = "Before " + event.getName();
        this.startEvent = event;
        this.isHappened = false;
        this.isPassed = true;
    }

    public boolean checkScope(Snapshot snapshot) {
        if (this.isHappened)
            return false;

        if (startEvent.checkHold(snapshot)) {
            this.isHappened = true;
            return false;
        }

        return true;
    }
}
