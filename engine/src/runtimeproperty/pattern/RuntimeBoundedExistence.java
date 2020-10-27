package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.HashMap;

public class RuntimeBoundedExistence extends RuntimeProperty {
    Event targetEvent;

    boolean isAtMost;
    int targetCount;
    int currentCount;

    public RuntimeBoundedExistence(Event event, Scope scope, int targetCount, boolean isAtMost) {
        super(scope);
        this.targetEvent = event;
        this.targetCount = targetCount;
        this.currentCount = 0;
        this.isAtMost = isAtMost;
        this.prefix = "Bounded Existence";
        if (isAtMost)
            this.name = event.getName() + " holds at most " + targetCount + " times";
        else
            this.name = event.getName() + " holds at least " + targetCount + " times";
    }

    protected void evaluateState(Snapshot snapshot) {
        if (targetEvent instanceof SoSEvent){
            if(((SoSEvent) targetEvent).checkHold(snapshot))
                currentCount++;
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            for(Boolean result: holdingResult.values()){
                if (result)
                    currentCount++;
            }
        }

        if(isAtMost) {
            if (currentCount > targetCount) {
                this.isHolding = false;
                this.isConfirmed = true;
                this.beConfirmed(snapshot);
            }
            else {
                this.isHolding = true;
            }
        }
        else {
            if (currentCount >= targetCount) {
                this.isHolding = true;
                this.isConfirmed = true;
                this.beConfirmed(snapshot);
            }
            else {
                this.isHolding = false;
            }
        }

    }
}
