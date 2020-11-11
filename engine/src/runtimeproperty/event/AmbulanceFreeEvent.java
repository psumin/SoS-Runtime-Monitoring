package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.AgentEvent;

import java.util.HashMap;
import java.util.StringTokenizer;

public class AmbulanceFreeEvent extends AgentEvent {
    public AmbulanceFreeEvent() {
        super("Event that ambulance is free");
    }

    public HashMap<String, Boolean> checkMultipleHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfAmbulance = 0;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();

            if (target.equals("Frame:")) {
                int newTick = Integer.parseInt(st.nextToken());
                if (newTick == this.currentTick){
                    return this.currentResult;
                }
                this.currentTick = newTick;
            }

            if (target.equals("CurrentAmb:")) {
                numberOfAmbulance = Integer.parseInt(st.nextToken());
            }

            if (target.equals("Amb:")) {
                for (int i = 0; i < numberOfAmbulance; i++) {
                    String[] targetToken = st.nextToken().split("/");
                    String name = targetToken[0];
                    String action = targetToken[2];

                    if (action.equals("Free")){
                        currentResult.put(name, true);
                    }
                    else {
                        currentResult.put(name, false);
                    }
                }
            }
        }

        return this.currentResult;
    }
}
