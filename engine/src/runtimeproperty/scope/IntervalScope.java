package runtimeproperty.scope;

import log.Snapshot;
import runtimeproperty.Scope;

import java.util.StringTokenizer;

public class IntervalScope extends Scope {
    int startFrame;
    int endFrame;

    public IntervalScope(int startFrame, int endFrame) {
        this.name = "Interval from " + startFrame + " to " + endFrame;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public boolean checkScope(Snapshot snapshot) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int currentTick = -1;

        for (String target = ""; st.hasMoreTokens(); target = st.nextToken()) {
            if (target.equals("Frame:")) {
                currentTick = Integer.parseInt(st.nextToken());
            }
        }

        if (!this.isPassed && currentTick >= this.startFrame)
            this.isPassed = true;

        return currentTick >= this.startFrame && currentTick <= this.endFrame;
    }
}
