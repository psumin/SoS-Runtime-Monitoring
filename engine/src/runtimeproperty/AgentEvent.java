package runtimeproperty;

import log.Snapshot;

import java.util.HashMap;

public abstract class AgentEvent extends Event {
    protected int currentTick;
    protected HashMap<String, Boolean> currentResult;

    public AgentEvent() {
        this.currentResult = new HashMap<>(0);
        this.currentTick = 0;
    }

    public abstract HashMap<String, Boolean> checkMultipleHold(Snapshot snapshot);
}
