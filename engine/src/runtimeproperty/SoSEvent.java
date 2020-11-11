package runtimeproperty;

import log.Snapshot;

public abstract class SoSEvent extends Event {
    public SoSEvent(String name) {
        super(name);
    }

    public abstract boolean checkHold(Snapshot snapshot);
}
