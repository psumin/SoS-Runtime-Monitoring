package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.Event;

import java.util.StringTokenizer;

public class AmbulanceMoveEvent extends Event {
    public AmbulanceMoveEvent() {
        this.name = "Event that at least one ambulance moves a single tile";
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfAmbulance = 0;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();

            if (target.equals("CurrentAmb:")) {
                numberOfAmbulance = Integer.parseInt(st.nextToken());
            }

            if (target.equals("Amb:")) {
                for (int i = 0; i < numberOfAmbulance; i++) {
                    int movedTiles = Integer.parseInt(st.nextToken().split("/")[2]);

                    if (movedTiles == 1)
                        return true;
                }

            }
        }

        return false;
    }
}
