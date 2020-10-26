package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeRecurrence extends RuntimeProperty {
    Event targetEvent;
    int duration;
    HashMap<String, Integer> currentCount;

    public RuntimeRecurrence(Event event, Scope scope, int duration) {
        super(scope);
        this.targetEvent = event;
        this.name = event.getName() + " holds eventually";
        this.duration = duration;
        this.currentCount = new HashMap<>(0);
    }

    protected void evaluateState(Snapshot snapshot) {
        if (targetEvent instanceof SoSEvent){
            boolean isHolding = ((SoSEvent) targetEvent).checkHold(snapshot);
            if (isHolding)
                this.currentCount.put("main", 0);
            else if (this.currentCount.containsKey("main"))
                this.currentCount.put("main", this.currentCount.get("main") + 1);
            else
                this.currentCount.put("main", 1);

            if (this.currentCount.get("main") > this.duration){
                this.isHolding = false;
                this.beConfirmed(snapshot);
            }
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            for(String name: holdingResult.keySet()){
                if (isHolding)
                    this.currentCount.put(name, 0);
                else if (this.currentCount.containsKey(name))
                    this.currentCount.put(name, this.currentCount.get(name) + 1);
                else
                    this.currentCount.put(name, 1);
            }

            for (int count: currentCount.values()){
                if (count > duration) {
                    this.isHolding = false;
                    this.beConfirmed(snapshot);
                }
            }
        }
    }
}
