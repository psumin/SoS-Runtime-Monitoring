package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.RuntimeProperty;
import runtimeproperty.Scope;

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
        if (isAtMost)
            this.name = event.getName() + " holds at most " + targetCount + " times";
        else
            this.name = event.getName() + " holds at least " + targetCount + " times";
    }

    protected void evaluateState(Snapshot snapshot) {
        if(targetEvent.checkHold(snapshot))
            currentCount++;

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
