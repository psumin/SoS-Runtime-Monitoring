package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.Event;

import java.util.StringTokenizer;

public class RescuedRateEvent extends Event {
    double targetRate;

    public RescuedRateEvent(double targetRate) {
        this.targetRate = targetRate;
        this.name = "Event that rescue rate exceeds " + targetRate;
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        double currentRate = 0;

        for (String target = ""; st.hasMoreTokens(); target = st.nextToken()) {
            if (target.equals("RescuedRate:")) {
                currentRate = Double.parseDouble(st.nextToken());
            }
        }

        return currentRate >= targetRate;
    }
}
