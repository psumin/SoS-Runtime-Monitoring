package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class TreatmentRate100Event extends SoSEvent {

    public TreatmentRate100Event() {
        super("Event that treatment rate reaches 100%");
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        double currentRate = 0;

        for (String target = ""; st.hasMoreTokens(); ) {
            target = st.nextToken();
            if (target.equals("TreatmentRate:")) {
                currentRate = Double.parseDouble(st.nextToken());
            }
        }

        return currentRate == 1;
    }
}
