package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.AgentEvent;

import java.util.HashMap;
import java.util.StringTokenizer;

public class AmbulanceMoveEvent extends AgentEvent {
    HashMap<String, Integer> ambulanceMovement;

    public AmbulanceMoveEvent() {
        super("Event that ambulance moves a single tile");
        this.ambulanceMovement = new HashMap<>(0);
    }

    public HashMap<String, Boolean> checkMultipleHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfAmbulance = 0;

        for (String target = ""; st.hasMoreTokens(); ) {
            target = st.nextToken();

            if (target.equals("Frame:")) {
                int newTick = Integer.parseInt(st.nextToken());
                if (newTick == this.currentTick) {
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
                    int totalTiles = Integer.parseInt(targetToken[3]);

                    if (this.ambulanceMovement.containsKey(name)) {
                        int movedTiles = totalTiles - this.ambulanceMovement.get(name);

                        if (movedTiles == 1)
                            this.currentResult.put(name, true);
                    } else if (totalTiles == 1) {
                        this.currentResult.put(name, true);
                    }

                    this.ambulanceMovement.put(name, totalTiles);
                }
            }
        }

        return this.currentResult;
    }
}
