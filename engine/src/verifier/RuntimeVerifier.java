package verifier;

import log.Log;
import log.Snapshot;
import runtimeproperty.RuntimeProperty;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class RuntimeVerifier extends Verifier {
    protected ArrayList<RuntimeProperty> runtimeProperties;

    public RuntimeVerifier(ArrayList<RuntimeProperty> runtimeProperties) {
        this.runtimeProperties = runtimeProperties;
    }

    public void runtimeVerificationResult(Log log) {
        Snapshot snapshot = log.getSnapshotMap().get(log.getSnapshotMap().size());
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int currentTick = -1;

        for (String target = ""; st.hasMoreTokens(); target = st.nextToken()){
            if (target.equals("Frame:")){
                currentTick = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println("Tick: " + currentTick);

        for (RuntimeProperty runtimeProperty : this.runtimeProperties) {
            runtimeProperty.check(snapshot);
        }

        System.out.println("");
    }

    public void printFinalResult() {
        System.out.println("==========================");
        System.out.println("       Final Result       ");
        System.out.println("==========================");

        for (RuntimeProperty runtimeProperty : this.runtimeProperties) {
            runtimeProperty.printFinalResult();
        }
        System.out.println("");
    }
}
