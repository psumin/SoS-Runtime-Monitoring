package verifier;

import log.Log;
import log.Snapshot;
import runtimeproperty.RuntimeProperty;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class RuntimeVerifier extends Verifier {
    protected ArrayList<RuntimeProperty> runtimeProperties;
    int currentTick;

    // print variables
    int maxPatternLength;
    int maxConfirmedLength;
    int maxPropertyLength;
    String divider;
    String header;

    public RuntimeVerifier(ArrayList<RuntimeProperty> runtimeProperties, int maxFrameCount) {
        this.runtimeProperties = runtimeProperties;
        this.maxPatternLength = 0;
        this.maxConfirmedLength = Math.max(12, String.valueOf(maxFrameCount).length());
        this.maxPropertyLength = 0;
        for (RuntimeProperty runtimeProperty : this.runtimeProperties) {
            this.maxPatternLength = Math.max(runtimeProperty.getPrefix().length(), this.maxPatternLength);
            this.maxPropertyLength = Math.max(runtimeProperty.getName().length() + runtimeProperty.getScope().getName().length() + 10, this.maxPropertyLength);
        }
        for (RuntimeProperty runtimeProperty : this.runtimeProperties) {
            runtimeProperty.setPrintVariables(this.maxPatternLength, this.maxConfirmedLength, this.maxPropertyLength);
        }

        this.divider = "";
        for (int i = 0; i < maxPatternLength + maxConfirmedLength + maxPropertyLength + 18; i++) {
            this.divider += "=";
        }

        String pattern = "PATTERN";
        int target = pattern.length();
        for (int i = 0; i < maxPatternLength - target; i++) {
            if (i % 2 == 0)
                pattern = pattern + " ";
            else
                pattern = " " + pattern;
        }
        pattern = "| " + pattern + " ";

        String trueFalse = "|  T/F  | ";
        String confirmed = "CONFIRMED AT";
        target = confirmed.length();
        for (int i = 0; i < maxConfirmedLength - target; i++) {
            if (i % 2 == 0)
                confirmed = confirmed + " ";
            else
                confirmed = " " + confirmed;
        }
        confirmed = trueFalse + confirmed + " ";

        String property = "PROPERTY";
        target = property.length();
        for (int i = 0; i < maxPropertyLength - target; i++) {
            if (i % 2 == 0)
                property = property + " ";
            else
                property = " " + property;
        }
        property = "| " + property + " |";
        this.header = pattern + confirmed + property;
    }

    public void runtimeVerificationResult(Log log) {
        Snapshot snapshot = log.getSnapshotMap().get(log.getSnapshotMap().size());
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");

        for (String target = ""; st.hasMoreTokens(); target = st.nextToken()) {
            if (target.equals("Frame:")) {
                this.currentTick = Integer.parseInt(st.nextToken());
            }
        }

        for (RuntimeProperty runtimeProperty : this.runtimeProperties) {
            runtimeProperty.check(snapshot);
        }
        this.printResult(this.currentTick, false);
    }

    public void printResult(int frameCount, boolean isFinal) {
        System.out.println(divider);

        String title;
        if (isFinal)
            title = "Final Result";
        else
            title = "Result at Tick " + frameCount;

        for (int i = 0; i < maxPatternLength + maxConfirmedLength + maxPropertyLength + 18; i++) {
            if (i % 2 == 0)
                title = title + " ";
            else
                title = " " + title;
        }

        System.out.println(title);
        System.out.println(divider);
        System.out.println(header);

        for (RuntimeProperty runtimeProperty : this.runtimeProperties) {
            runtimeProperty.printResult();
        }
        System.out.println();
    }
}
