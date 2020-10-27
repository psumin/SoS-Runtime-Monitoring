package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Scope;
import runtimeproperty.SoSEvent;

public class AfterScope extends Scope {
    SoSEvent endEvent;
    boolean isHappened;

    public AfterScope(SoSEvent event) {
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
