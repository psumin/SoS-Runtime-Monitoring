package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class AllPatientArriveAtHospitalEvent extends SoSEvent {
    int xSize;
    int ySize;

    public AllPatientArriveAtHospitalEvent(int xSize, int ySize) {
        super("Event that at least one patient arrived at hospital");
        this.xSize = xSize;
        this.ySize = ySize;
    }

    protected boolean checkPosition(int xPos, int yPos) {
        if (xPos == 0 && yPos == 0)
            return true;

        if (xPos == 0 && yPos == this.ySize)
            return true;

        if (xPos == this.xSize && yPos == 0)
            return true;

        return xPos == this.xSize && yPos == this.ySize;
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfPatients = 0;

        for (String target = ""; st.hasMoreTokens(); ) {
            target = st.nextToken();

            if (target.equals("CurrentPat:")) {
                numberOfPatients = Integer.parseInt(st.nextToken());
            }

            if (target.equals("Pat:")) {
                for (int i = 0; i < numberOfPatients; i++) {
                    String[] position = st.nextToken().split("/")[1].split(",");
                    int xPos = Integer.parseInt(position[0]);
                    int yPos = Integer.parseInt(position[1]);

                    if (checkPosition(xPos, yPos))
                        return true;
                }
            }
        }

        return false;
    }
}
