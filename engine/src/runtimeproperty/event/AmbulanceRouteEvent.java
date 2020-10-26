package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class AmbulanceRouteEvent extends SoSEvent {
    int xSize;
    int ySize;

    public AmbulanceRouteEvent(int xSize, int ySize) {
        this.name = "Event that ambulance go out of the route";
        this.xSize = xSize;
        this.ySize = ySize;
    }

    protected boolean checkPosition(int xPos, int yPos) {
        if (xPos <= 5 && yPos <= 5)
            return true;

        if (xPos >= xSize - 5 && yPos >= ySize - 5)
            return true;

        if (xPos <= 5 && yPos >= ySize - 5)
            return true;

        if (xPos >= xSize - 5 && yPos <= 5)
            return true;

        if (xPos == 4)
            return true;

        if (yPos == 4)
            return true;

        if (xPos == xSize - 4)
            return true;

        if (yPos == ySize - 4)
            return true;

        System.out.println(xPos + "," + yPos);
        return false;
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
                    String[] position = st.nextToken().split("/")[1].split(",");
                    int xPos = Integer.parseInt(position[0]);
                    int yPos = Integer.parseInt(position[1]);

                    if (!checkPosition(xPos, yPos))
                        return true;
                }

            }
        }

        return false;
    }
}
