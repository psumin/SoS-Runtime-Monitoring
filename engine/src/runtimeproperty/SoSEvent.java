package runtimeproperty;

import log.Snapshot;

public abstract class SoSEvent extends Event {
    public abstract boolean checkHold(Snapshot snapshot);
}
