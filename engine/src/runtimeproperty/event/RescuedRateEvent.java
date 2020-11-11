package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class RescuedRateEvent extends SoSEvent {
    double targetRate;

    public RescuedRateEvent(double targetRate) {
        super("Event that rescued rate exceeds " + targetRate);
        this.targetRate = targetRate;
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        double currentRate = 0;

        for (String target = ""; st.hasMoreTokens(); ) {
            target = st.nextToken();
            if (target.equals("RescuedRate:")) {
                currentRate = Double.parseDouble(st.nextToken());
            }
        }

        return currentRate >= targetRate;
    }
}
