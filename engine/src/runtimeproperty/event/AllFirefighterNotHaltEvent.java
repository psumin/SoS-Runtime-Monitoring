package runtimeproperty.event;

import log.Snapshot;
import runtimeproperty.SoSEvent;

import java.util.StringTokenizer;

public class AllFirefighterNotHaltEvent extends SoSEvent {
    public AllFirefighterNotHaltEvent() {
        super("Event that all firefighters do actions except halt");
    }

    public boolean checkHold(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int numberOfFirefighter = 0;
        boolean result = true;

        for (String target = ""; st.hasMoreTokens();) {
            target = st.nextToken();

            if (target.equals("CurrentFF:")) {
                numberOfFirefighter = Integer.parseInt(st.nextToken());
            }

            if (target.equals("FF:")) {
                for (int i = 0; i < numberOfFirefighter; i++) {
                    String[] targetToken = st.nextToken().split("/");
                    String action = targetToken[2];

                    if (action.equals("Halt"))
                        result = false;
                }
            }
        }

        return result;
    }
}
