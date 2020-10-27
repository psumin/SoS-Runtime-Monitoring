package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class UnvisitedTileEvent extends SoSEvent {

    public UnvisitedTileEvent() {
        this.name = "Event that there are no more unvisited tiles";
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int unvisitedTiles = 0;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();
            if (target.equals("UnvisitedTiles:")) {
                unvisitedTiles = Integer.parseInt(st.nextToken());
            }
        }

        return unvisitedTiles == 0;
    }
}
