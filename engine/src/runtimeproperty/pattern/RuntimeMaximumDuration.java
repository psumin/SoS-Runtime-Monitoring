package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeMaximumDuration extends RuntimeProperty {
    Event targetEvent;
    int targetCount;
    HashMap<String, Integer> currentCount;

    public RuntimeMaximumDuration(Event event, Scope scope, int targetCount) {
        super(scope);
        this.targetEvent = event;
        this.targetCount = targetCount;
        this.prefix = "Maximum Duration";
        this.name = event.getName() + " remains at most " + targetCount;
        this.currentCount = new HashMap<>(0);
    }

    protected void evaluateState(Snapshot snapshot) {
        if (targetEvent instanceof SoSEvent) {
            boolean isHolding = ((SoSEvent) targetEvent).checkHold(snapshot);
            if (isHolding) {
                if (this.currentCount.containsKey("main"))
                    this.currentCount.put("main", this.currentCount.get("main") + 1);
                else
                    this.currentCount.put("main", 1);

                int cur = this.currentCount.get("main");
                if (cur != 0 && cur > targetCount) {
                    this.isHolding = false;
                    this.beConfirmed(snapshot);
                }

            } else if (this.currentCount.containsKey("main"))
                this.currentCount.put("main", 0);
        } else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            for (String name : holdingResult.keySet()) {
                boolean result = holdingResult.get(name);
                if (result) {
                    if (this.currentCount.containsKey(name))
                        this.currentCount.put(name, this.currentCount.get(name) + 1);
                    else
                        this.currentCount.put(name, 1);

                    int cur = this.currentCount.get(name);
                    if (cur != 0 && cur > targetCount) {
                        this.isHolding = false;
                        this.beConfirmed(snapshot);
                    }
                } else if (this.currentCount.containsKey(name))
                    this.currentCount.put(name, 0);
            }
        }
    }
}
