package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeResponse extends RuntimeProperty {
    Event causeEvent;
    Event effectEvent;
    HashMap<String, Boolean> causeCheck;
    HashMap<String, Boolean> progressCheck;
    HashMap<String, Boolean> effectCheck;

    public RuntimeResponse(Event causeEvent, Event effectEvent, Scope scope) {
        super(scope);
        this.prefix = "Response";
        this.name = "If " + causeEvent.getName() + " has occurred, as in response " + effectEvent.getName() + " eventually holds";
        this.causeEvent = causeEvent;
        this.effectEvent = effectEvent;
        this.causeCheck = new HashMap<>(0);
        this.progressCheck = new HashMap<>(0);
        this.effectCheck = new HashMap<>(0);
    }

    protected void evaluateState(Snapshot snapshot) {
        if (causeEvent instanceof SoSEvent){
            if (this.causeCheck.containsKey("main"))
                this.causeCheck.put("main", this.causeCheck.get("main") || ((SoSEvent) causeEvent).checkHold(snapshot));
            else
                this.causeCheck.put("main", ((SoSEvent) causeEvent).checkHold(snapshot));

            this.effectCheck.put("main", ((SoSEvent) effectEvent).checkHold(snapshot));

            if (this.causeCheck.get("main")) {
                this.isHolding = false;
            }
            else if (!this.isHolding && this.effectCheck.get("main")) {
                this.isHolding = true;
            }
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) causeEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                if (this.causeCheck.containsKey(name))
                    this.causeCheck.put(name, this.causeCheck.get(name) || holdingResult.get(name));
                else {
                    this.causeCheck.put(name, holdingResult.get(name));
                    this.progressCheck.put(name, true);
                }
            }

            holdingResult = ((AgentEvent) effectEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                this.effectCheck.put(name, holdingResult.get(name));
            }

            for(String name: causeCheck.keySet()){
                if (this.causeCheck.get(name)) {
                    this.progressCheck.put(name, false);
                }
                else if (!this.progressCheck.get(name) && this.effectCheck.get(name)) {
                    this.progressCheck.put(name, true);
                }
            }

            this.isHolding = true;
            for (String name: progressCheck.keySet()) {
                this.isHolding = this.isHolding && progressCheck.get(name);
            }
        }
    }
}
