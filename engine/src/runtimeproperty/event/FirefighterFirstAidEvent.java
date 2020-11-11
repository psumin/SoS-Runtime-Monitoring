package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.AgentEvent;

import java.util.HashMap;
import java.util.StringTokenizer;

public class FirefighterFirstAidEvent extends AgentEvent {
    public FirefighterFirstAidEvent() {
        super("Event that firefighter is doing first aid");
    }

    public HashMap<String, Boolean> checkMultipleHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfFirefighter = 0;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();

            if (target.equals("Frame:")) {
                int newTick = Integer.parseInt(st.nextToken());
                if (newTick == this.currentTick){
                    return this.currentResult;
                }
                this.currentTick = newTick;
            }

            if (target.equals("CurrentFF:")) {
                numberOfFirefighter = Integer.parseInt(st.nextToken());
            }

            if (target.equals("FF:")) {
                for (int i = 0; i < numberOfFirefighter; i++) {
                    String[] targetToken = st.nextToken().split("/");
                    String name = targetToken[0];
                    String action = targetToken[2];

                    if (action.equals("FirstAid")){
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
