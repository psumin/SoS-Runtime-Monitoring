package runtimeproperty;

import log.Snapshot;
import property.Property;
import runtimeproperty.scope.AfterScope;
import runtimeproperty.scope.BeforeScope;

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
    protected abstract void endScope(Snapshot snapshot);
    public void check(Snapshot snapshot) {
        String base = this.name + " in scope " + scope.getName() + ": ";
        if (isConfirmed) {
            System.out.println(base + isHolding + " (Confirmed at " + this.confirmedAt + ")");
        }

        else if (scope.checkScope(snapshot)) {
            this.evaluateState(snapshot);
            if (isConfirmed)
                System.out.println(base + isHolding + " (Confirmed at " + this.confirmedAt + ")");
            else
                System.out.println(base + isHolding);
        }

        else if (scope instanceof BeforeScope || scope instanceof AfterScope) {
            if (scope.isPassed)
                this.endScope(snapshot);
        }

        else
            System.out.println(base + isHolding);
    }

    public void printFinalResult() {
        if (this.isConfirmed)
            System.out.println(this.name + " in scope " + scope.getName() + ": " + isHolding + " (Confirmed at " + this.confirmedAt + ")");
        else
            System.out.println(this.name + " in scope " + scope.getName() + ": " + isHolding + " (Confirmed at " + this.confirmedAt + ")");
    }
}
