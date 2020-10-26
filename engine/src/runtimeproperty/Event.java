package runtimeproperty;

import log.Snapshot;

public abstract class Event {
    protected String name;

    public abstract boolean checkHold(Snapshot snapshot);

    public String getName() {
        return this.name;
    }
}
