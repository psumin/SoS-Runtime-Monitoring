package runtimeproperty;

import log.Snapshot;
import runtimeproperty.scope.AfterScope;
import runtimeproperty.scope.BeforeScope;
import runtimeproperty.scope.BetweenScope;
import runtimeproperty.scope.IntervalScope;

import java.util.StringTokenizer;

public abstract class RuntimeProperty {
    protected Scope scope;
    protected String name;

    protected boolean isHolding;
    protected boolean isConfirmed;
    protected int confirmedAt;

    public RuntimeProperty(Scope scope) {
        this.scope = scope;
        this.isHolding = true;
    }

    protected abstract void evaluateState(Snapshot snapshot);

    protected void beConfirmed(Snapshot snapshot) {
        this.isConfirmed = true;

        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        int currentTick = -1;

        for (String target = ""; st.hasMoreTokens(); target = st.nextToken()) {
            if (target.equals("Frame:")) {
                currentTick = Integer.parseInt(st.nextToken());
            }
        }
        this.confirmedAt = currentTick;
    }

    public void check(Snapshot snapshot) {
        String base = this.name + " in scope " + scope.getName() + ": ";
        if (isConfirmed) {
            System.out.println(base + isHolding + " (Confirmed at " + this.confirmedAt + ")");
        } else if (scope.checkScope(snapshot)) {
            this.evaluateState(snapshot);
            if (isConfirmed)
                System.out.println(base + isHolding + " (Confirmed at " + this.confirmedAt + ")");
            else
                System.out.println(base + isHolding);
        } else if (scope instanceof BeforeScope || scope instanceof IntervalScope || scope instanceof BetweenScope) {
            if (scope.isPassed)
                this.beConfirmed(snapshot);
        } else
            System.out.println(base + isHolding);
    }

    public void printFinalResult() {
        if (this.isConfirmed)
            System.out.println(this.name + " in scope " + scope.getName() + ": " + isHolding + " (Confirmed at " + this.confirmedAt + ")");
        else
            System.out.println(this.name + " in scope " + scope.getName() + ": " + isHolding + " (Confirmed at simulation ends)");
    }
}
