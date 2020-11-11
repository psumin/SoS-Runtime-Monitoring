package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeExistence extends RuntimeProperty {
    Event targetEvent;

    public RuntimeExistence(Event event, Scope scope) {
        super(scope);
        this.targetEvent = event;
        this.prefix = "Existence";
        this.name = event.getName() + " holds eventually";
    }

    protected void evaluateState(Snapshot snapshot) {
        if (targetEvent instanceof SoSEvent) {
            this.isHolding = ((SoSEvent) targetEvent).checkHold(snapshot);

            if (isHolding) {
                this.beConfirmed(snapshot);
            }
        } else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            for (Boolean result : holdingResult.values()) {
                if (!result) {
                    this.isHolding = false;
                    this.beConfirmed(snapshot);
                }
            }

            this.isHolding = true;
        }
    }
}
