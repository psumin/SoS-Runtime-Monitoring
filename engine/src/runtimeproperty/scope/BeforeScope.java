package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Scope;
import runtimeproperty.SoSEvent;

public class BeforeScope extends Scope {
    SoSEvent startEvent;
    boolean isHappened;

    public BeforeScope(SoSEvent event) {
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
