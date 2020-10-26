package runtimeproperty;

import log.Snapshot;

public abstract class Scope {
    protected String name;
    protected boolean isPassed;

    public abstract boolean checkScope(Snapshot snapshot);

    public String getName() {
        return this.name;
    }
}
