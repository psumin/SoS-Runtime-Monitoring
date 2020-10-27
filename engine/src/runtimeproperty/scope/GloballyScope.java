package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Scope;

public class GloballyScope extends Scope {
    public GloballyScope() {
        this.name = "Globally";
    }

    public boolean checkScope(Snapshot snapshot) {
        return true;
    }
}
