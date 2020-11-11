package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class TreatmentRescuedRateEvent extends SoSEvent {
    public TreatmentRescuedRateEvent() {
        super("Event that treatment rate is less or equal than rescued rate");
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        double rescuedRate = 0;
        double treatmentRate = 0;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();
            if (target.equals("RescuedRate:")) {
                rescuedRate = Double.parseDouble(st.nextToken());
            }

            if (target.equals("TreatmentRate:")) {
                treatmentRate = Double.parseDouble(st.nextToken());
            }
        }

        return treatmentRate <= rescuedRate;
    }
}
