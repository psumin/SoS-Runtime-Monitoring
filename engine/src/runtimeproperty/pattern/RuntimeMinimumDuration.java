package runtimeproperty.pattern;

import log.Snapshot;
import runtimeproperty.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RuntimeMinimumDuration extends RuntimeProperty {
    Event targetEvent;
    int targetCount;
    HashMap<String, Integer> currentCount;

    boolean firstTick;
    boolean needsException;
    ArrayList<String> exception;

    public RuntimeMinimumDuration(Event event, Scope scope, int targetCount) {
        super(scope);
        this.targetEvent = event;
        this.targetCount = targetCount;
        this.name = event.getName() + " remains at least " + targetCount;
        this.currentCount = new HashMap<>(0);
        this.exception = new ArrayList<>(0);
        this.needsException = false;
        this.firstTick = true;
    }

    private void firstWork(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();

            if (target.equals("Frame:")) {
                int currentTick = Integer.parseInt(st.nextToken());
                if (currentTick != 0) {
                    this.needsException = true;
                }
            }
        }
    }

    protected void evaluateState(Snapshot snapshot) {
        if (this.firstTick) {
            this.firstWork(snapshot);
            this.firstTick = false;
        }

        if (targetEvent instanceof SoSEvent){
            boolean isHolding = ((SoSEvent) targetEvent).checkHold(snapshot);
            if (isHolding) {
                if (this.currentCount.containsKey("main"))
                    this.currentCount.put("main", this.currentCount.get("main") + 1);
                else
                    this.currentCount.put("main", 1);
            }
            else {
                if (this.currentCount.containsKey("main")) {
                    int cur = this.currentCount.get(name);

                    if (this.needsException && cur != 0 && cur < targetCount) {
                        this.needsException = false;
                    }

                    else if (cur != 0 && cur < targetCount){
                        this.isHolding = false;
                        this.beConfirmed(snapshot);
                    }

                    this.currentCount.put("main", 0);
                }
            }
        }
        else {
            HashMap<String, Boolean> holdingResult = ((AgentEvent) targetEvent).checkMultipleHold(snapshot);

            if (this.firstTick) {
                this.firstWork(snapshot);
                this.firstTick = false;
            }

            for(String name: holdingResult.keySet()){
                boolean result = holdingResult.get(name);
                if (needsException) {
                    this.exception.add(name);
                }

                if (result) {
                    if (this.currentCount.containsKey(name))
                        this.currentCount.put(name, this.currentCount.get(name) + 1);
                    else
                        this.currentCount.put(name, 1);
                }
                else {
                    if (this.currentCount.containsKey(name)) {
                        int cur = this.currentCount.get(name);

                        if (this.needsException && this.exception.contains(name)) {
                            this.exception.remove(name);
                            if (this.exception.size() == 0)
                                this.needsException = false;
                        }
                        else if (cur != 0 && cur < targetCount){
                            this.isHolding = false;
                            this.beConfirmed(snapshot);
                        }
                        this.currentCount.put(name, 0);
                    }
                }
            }
        }
    }
}
