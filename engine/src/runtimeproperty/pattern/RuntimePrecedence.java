package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimePrecedence extends RuntimeProperty {
    Event causeEvent;
    Event effectEvent;
    HashMap<String, Boolean> causeCheck;
    HashMap<String, Boolean> effectCheck;

    public RuntimePrecedence(Event causeEvent, Event effectEvent, Scope scope) {
        super(scope);
        this.prefix = "Precedence";
        this.name = "If " + effectEvent.getName() + " has occurred, then it must have been " + causeEvent.getName() + " has occurred before";
        this.causeEvent = causeEvent;
        this.effectEvent = effectEvent;
        this.causeCheck = new HashMap<>(0);
        this.effectCheck = new HashMap<>(0);
    }

    protected void evaluateState(Snapshot snapshot) {
        if (causeEvent instanceof SoSEvent){
            if (this.effectCheck.containsKey("main"))
                this.effectCheck.put("main", this.effectCheck.get("main") || ((SoSEvent) effectEvent).checkHold(snapshot));
            else
                this.effectCheck.put("main", ((SoSEvent) effectEvent).checkHold(snapshot));

            if (this.effectCheck.get("main")) {
                if (!this.causeCheck.containsKey("main") || !this.causeCheck.get("main")) {
                    this.isHolding = false;
                    this.beConfirmed(snapshot);
                }
            }

            if (this.causeCheck.containsKey("main"))
                this.causeCheck.put("main", this.causeCheck.get("main") || ((SoSEvent) causeEvent).checkHold(snapshot));
            else
                this.causeCheck.put("main", ((SoSEvent) causeEvent).checkHold(snapshot));
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) effectEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                if (this.effectCheck.containsKey(name))
                    this.effectCheck.put(name, this.effectCheck.get(name) || holdingResult.get(name));
                else
                    this.effectCheck.put(name, holdingResult.get(name));
            }

            if (this.causeCheck.size() == 0) {
                System.out.println("");
            }

            for(String name: effectCheck.keySet()){
                if (this.effectCheck.get(name)) {
                    if (!this.causeCheck.containsKey(name) || !this.causeCheck.get(name)) {
                        this.isHolding = false;
                        this.beConfirmed(snapshot);
                    }
                }
            }

            holdingResult = ((AgentEvent) causeEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                if (this.causeCheck.containsKey(name))
                    this.causeCheck.put(name, this.causeCheck.get(name) || holdingResult.get(name));
                else
                    this.causeCheck.put(name, holdingResult.get(name));
            }
        }
    }
}
