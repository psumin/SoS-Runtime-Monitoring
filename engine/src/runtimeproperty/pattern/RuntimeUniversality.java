package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeUniversality extends RuntimeProperty {
    Event targetEvent;

    public RuntimeUniversality(Event event, Scope scope) {
        super(scope);
        this.targetEvent = event;
        this.prefix = "Universality";
        this.name = "It is always the case that " + event.getName() + " holds";
    }

    protected void evaluateState(Snapshot snapshot) {
        if (targetEvent instanceof SoSEvent){
            this.isHolding = ((SoSEvent) targetEvent).checkHold(snapshot);

            if (!isHolding) {
                this.beConfirmed(snapshot);
            }
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            for(Boolean result: holdingResult.values()){
                if (!result){
                    this.isHolding = false;
                    this.beConfirmed(snapshot);
                }
            }

            this.isHolding = true;
        }
    }
}
