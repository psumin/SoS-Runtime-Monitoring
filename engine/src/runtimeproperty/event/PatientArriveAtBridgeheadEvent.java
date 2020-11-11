package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.AgentEvent;

import java.util.HashMap;
import java.util.StringTokenizer;

public class PatientArriveAtBridgeheadEvent extends AgentEvent {
    int xSize;
    int ySize;

    public PatientArriveAtBridgeheadEvent(int xSize, int ySize) {
        super("Event that patient is arrived at bridgehead");
        this.xSize = xSize;
        this.ySize = ySize;
    }

    protected boolean checkPosition(int xPos, int yPos) {
        if (xPos == 4 && yPos == 4)
            return true;

        if (xPos == this.xSize - 4 && yPos == 4)
            return true;

        if (xPos == 4 && yPos == this.ySize - 4)
            return true;

        return xPos == this.xSize - 4 && yPos == this.ySize - 4;
    }

    public HashMap<String, Boolean> checkMultipleHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfPatient = 0;
        int currentTick = 0;

        for (String target = ""; st.hasMoreTokens(); ) {
            target = st.nextToken();

            if (target.equals("Frame:"))
                currentTick = Integer.parseInt(st.nextToken());

            if (target.equals("CurrentPat:"))
                numberOfPatient = Integer.parseInt(st.nextToken());

            if (target.equals("Pat:")) {
                for (int i = 0; i < numberOfPatient; i++) {
                    String[] targetToken = st.nextToken().split("/");
                    String name = targetToken[0];

                    String[] position = targetToken[1].split(",");
                    int xPos = Integer.parseInt(position[0]);
                    int yPos = Integer.parseInt(position[1]);

                    if (checkPosition(xPos, yPos)) {
                        currentResult.put(name, true);
                    } else {
                        currentResult.put(name, false);
                    }
                }
            }
        }

        return this.currentResult;
    }
}
