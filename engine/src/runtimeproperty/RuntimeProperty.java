package runtimeproperty;

import log.Snapshot;
import runtimeproperty.scope.BeforeScope;
import runtimeproperty.scope.BetweenScope;
import runtimeproperty.scope.IntervalScope;

import java.util.StringTokenizer;

public abstract class RuntimeProperty {
    protected Scope scope;
    protected String name;
    protected String prefix;

    protected boolean isHolding;
    protected boolean isConfirmed;
    protected int confirmedAt;

    // Print Variables
    String pattern;
    String property;
    String confirmed;
    int maxConfirmedLength;

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
        this.setConfirmed();
    }

    public void check(Snapshot snapshot) {
        if (!isConfirmed) {
            if (scope.checkScope(snapshot))
                this.evaluateState(snapshot);
            else if (scope.isPassed && (scope instanceof BeforeScope || scope instanceof IntervalScope || scope instanceof BetweenScope))
                this.beConfirmed(snapshot);
        }
    }

    public void setPrintVariables(int maxPatternLength, int maxConfirmedLength, int maxPropertyLength) {
        this.pattern = this.prefix;
        int target = this.pattern.length();
        for (int i = 0; i < maxPatternLength - target; i++) {
            this.pattern += " ";
        }
        this.pattern = "| " + this.pattern + " | ";

        this.property = this.name + " in scope " + scope.getName();
        target = this.property.length();
        for (int i = 0; i < maxPropertyLength - target; i++) {
            this.property += " ";
        }
        this.property = "| " + this.property + " |";

        this.confirmed = "";
        for (int i = 0; i < maxConfirmedLength; i++) {
            this.confirmed += " ";
        }
        this.confirmed = " | " + this.confirmed + " ";
        this.maxConfirmedLength = maxConfirmedLength;
    }

    public void setConfirmed() {
        this.confirmed = String.valueOf(this.confirmedAt);
        int target = this.confirmed.length();
        for (int i = 0; i < this.maxConfirmedLength - target; i++) {
            this.confirmed = " " + this.confirmed;
        }
        this.confirmed = " | " + this.confirmed + " ";
    }

    public void printResult() {
        System.out.println(this.pattern + (this.isHolding ? "true " : "false") + this.confirmed + this.property);
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public Scope getScope() {
        return scope;
    }
}
