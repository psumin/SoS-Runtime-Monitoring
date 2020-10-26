package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.Event;
import runtimeproperty.RuntimeProperty;
import runtimeproperty.Scope;

import java.util.StringTokenizer;

public class RuntimeExistence extends RuntimeProperty {
    Event targetEvent;

    public RuntimeExistence(Event event, Scope scope) {
        super(scope);
        this.targetEvent = event;
        this.name = "Existence of the " + event.getName();
    }

    public void endScope(Snapshot snapshot) {
        this.isConfirmed = true;

        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int currentTick = -1;

        for (String target = ""; st.hasMoreTokens(); target = st.nextToken()){
            if (target.equals("Frame:")){
                currentTick = Integer.parseInt(st.nextToken());
            }
        }
        this.confirmedAt = currentTick;
    }

    protected void evaluateState(Snapshot snapshot) {
        this.isHolding = targetEvent.checkHold(snapshot);

        if (isHolding) {
            this.isConfirmed = true;

            StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
            int currentTick = -1;

            for (String target = ""; st.hasMoreTokens(); target = st.nextToken()){
                if (target.equals("Frame:")){
                    currentTick = Integer.parseInt(st.nextToken());
                }
            }
            this.confirmedAt = currentTick;
        }
    }
}
