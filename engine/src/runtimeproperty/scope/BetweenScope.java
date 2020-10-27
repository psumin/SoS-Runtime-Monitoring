package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Scope;
import runtimeproperty.SoSEvent;

public class BetweenScope extends Scope {
    SoSEvent startEvent;
    SoSEvent endEvent;
    boolean isHappened;

    public BetweenScope(SoSEvent startEvent, SoSEvent endEvent) {
        this.name = "Between " + startEvent.getName() + " and " + endEvent.getName();
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.isHappened = false;
    }

    public boolean checkScope(Snapshot snapshot) {
        if (!this.isHappened && !this.isPassed && this.startEvent.checkHold(snapshot)) {
            this.isHappened = true;
            this.isPassed = true;
            return true;
        }

        if (this.isHappened && this.endEvent.checkHold(snapshot)) {
            this.isHappened = false;
            return false;
        }

        return this.isHappened;
    }
}
