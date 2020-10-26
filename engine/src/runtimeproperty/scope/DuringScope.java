package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Scope;
import runtimeproperty.SoSEvent;

public class DuringScope extends Scope {
    SoSEvent targetEvent;

    public DuringScope(SoSEvent event) {
        this.name = "During " + event.getName();
        this.targetEvent = event;
    }

    public boolean checkScope(Snapshot snapshot) {
        return this.targetEvent.checkHold(snapshot);
    }
}
