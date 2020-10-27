package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.AgentEvent;

import java.util.HashMap;
import java.util.StringTokenizer;

public class PatientGetFirstAidAgainEvent extends AgentEvent {
    public PatientGetFirstAidAgainEvent() {
        super();
        this.name = "Event that patient is got first aid again";
    }

    public HashMap<String, Boolean> checkMultipleHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfPatient = 0;
        int currentTick = 0;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();

            if (target.equals("Frame:"))
                currentTick = Integer.parseInt(st.nextToken());

            if (target.equals("CurrentPat:"))
                numberOfPatient = Integer.parseInt(st.nextToken());

            if (target.equals("Pat:")) {
                for (int i = 0; i < numberOfPatient; i++) {
                    String[] targetToken = st.nextToken().split("/");
                    String name = targetToken[0];
                    int gotAidAt = Integer.parseInt(targetToken[2]);
                    int gotAidCount = Integer.parseInt(targetToken[3]);

                    if (gotAidAt == currentTick && gotAidCount >= 2){
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
