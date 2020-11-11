package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimePrevention extends RuntimeProperty {
    Event causeEvent;
    Event preventedEvent;
    HashMap<String, Boolean> causeCheck;
    HashMap<String, Boolean> progressCheck;
    HashMap<String, Boolean> preventCheck;

    public RuntimePrevention(Event causeEvent, Event effectEvent, Scope scope) {
        super(scope);
        this.prefix = "Prevention";
        this.name = "If " + causeEvent.getName() + " has occurred, as in response " + effectEvent.getName() + " never holds";
        this.causeEvent = causeEvent;
        this.preventedEvent = effectEvent;
        this.causeCheck = new HashMap<>(0);
        this.progressCheck = new HashMap<>(0);
        this.preventCheck = new HashMap<>(0);
    }

    protected void evaluateState(Snapshot snapshot) {
        if (causeEvent instanceof SoSEvent) {
            if (this.causeCheck.containsKey("main"))
                this.causeCheck.put("main", this.causeCheck.get("main") || ((SoSEvent) causeEvent).checkHold(snapshot));
            else
                this.causeCheck.put("main", ((SoSEvent) causeEvent).checkHold(snapshot));

            this.preventCheck.put("main", ((SoSEvent) preventedEvent).checkHold(snapshot));

            if (this.causeCheck.get("main"))
                this.progressCheck.put("main", true);
            else if (this.progressCheck.containsKey("main") && this.progressCheck.get("main") && this.preventCheck.get("main")) {
                this.isHolding = false;
                this.beConfirmed(snapshot);
            }
        } else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) causeEvent).checkMultipleHold(snapshot);

            for (String name : holdingResult.keySet()) {
                if (this.causeCheck.containsKey(name))
                    this.causeCheck.put(name, this.causeCheck.get(name) || holdingResult.get(name));
                else
                    this.causeCheck.put(name, holdingResult.get(name));
            }

            holdingResult = ((AgentEvent) preventedEvent).checkMultipleHold(snapshot);

            for (String name : holdingResult.keySet()) {
                this.preventCheck.put(name, holdingResult.get(name));
            }

            for (String name : causeCheck.keySet()) {
                if (this.causeCheck.get(name)) {
                    this.progressCheck.put(name, true);
                } else if (this.progressCheck.containsKey(name) && this.progressCheck.get(name) && this.preventCheck.get(name)) {
                    this.isHolding = false;
                    this.beConfirmed(snapshot);
                }
            }
        }
    }
}
