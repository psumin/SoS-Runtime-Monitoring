package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeUntil extends RuntimeProperty {
    Event targetEvent;
    Event untilEvent;
    HashMap<String, Boolean> targetCheck;
    HashMap<String, Boolean> untilCheck;
    HashMap<String, Boolean> confirmedCheck;

    public RuntimeUntil(Event targetEvent, Event untilEvent, Scope scope) {
        super(scope);
        this.name = targetEvent.getName() + " holds without interruption until " + untilEvent.getName() + " holds";
        this.targetEvent = targetEvent;
        this.untilEvent = untilEvent;
        this.targetCheck = new HashMap<>(0);
        this.untilCheck = new HashMap<>(0);
        this.confirmedCheck = new HashMap<>(0);
    }

    @Override
    protected void evaluateState(Snapshot snapshot) {
        if (targetEvent instanceof SoSEvent){
            if (this.targetCheck.containsKey("main"))
                this.targetCheck.put("main", this.targetCheck.get("main") || ((SoSEvent) targetEvent).checkHold(snapshot));
            else
                this.targetCheck.put("main", ((SoSEvent) targetEvent).checkHold(snapshot));

            if (this.untilCheck.containsKey("main"))
                this.untilCheck.put("main", this.untilCheck.get("main") || ((SoSEvent) untilEvent).checkHold(snapshot));
            else
                this.untilCheck.put("main", ((SoSEvent) untilEvent).checkHold(snapshot));

            if (!this.targetCheck.get("main") && !this.untilCheck.get("main")) {
                this.isHolding = false;
                this.beConfirmed(snapshot);
            }
            else if (!this.targetCheck.get("main") && this.untilCheck.get("main")) {
                this.isHolding = true;
                this.beConfirmed(snapshot);
            }
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                if (this.targetCheck.containsKey(name))
                    this.targetCheck.put(name, this.targetCheck.get(name) || holdingResult.get(name));
                else
                    this.targetCheck.put(name, holdingResult.get(name));
            }

            holdingResult = ((AgentEvent) untilEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                if (this.untilCheck.containsKey(name))
                    this.untilCheck.put(name, this.untilCheck.get(name) || holdingResult.get(name));
                else
                    this.untilCheck.put(name, holdingResult.get(name));
            }

            for(String name: targetCheck.keySet()){
                if (!this.targetCheck.get(name) && !this.untilCheck.get(name)) {
                    if (!this.confirmedCheck.containsKey(name) || !this.confirmedCheck.get(name)) {
                        this.isHolding = false;
                        this.beConfirmed(snapshot);
                    }
                }
                else if (!this.targetCheck.get(name) && this.untilCheck.get(name)) {
                    this.confirmedCheck.put(name, true);
                }
            }
        }
    }
}
