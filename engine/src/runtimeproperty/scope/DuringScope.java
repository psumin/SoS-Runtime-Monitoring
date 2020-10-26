package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.Scope;

public class DuringScope extends Scope {
    Event targetEvent;

    public DuringScope(Event event) {
        this.name = "During " + event.getName();
        this.targetEvent = event;
    }

    public boolean checkScope(Snapshot snapshot) {
        return this.targetEvent.checkHold(snapshot);
    }
}
