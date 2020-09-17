package verifier;

import property.Property;
import property.PropertyChecker;
import log.Log;


public class RuntimeVerification extends Verifier {
    protected boolean isStarted;
    protected boolean isEnded;

    protected int start_tick;
    protected int end_tick;

    protected Property start_event;
    protected PropertyChecker start_event_checker;
    protected Property end_event;
    protected PropertyChecker end_event_checker;

    protected String scope;

    public RuntimeVerification(PropertyChecker checker) {
        super(checker);
        this.scope = null;
    }

    public RuntimeVerification(PropertyChecker checker, String scope, int start_tick, int end_tick) {
        super(checker);
        if (scope.equals("Interval")) {
            this.scope = "Interval";
            this.start_tick = start_tick;
            this.end_tick = end_tick;
            this.isStarted = false;
            this.isEnded = false;
        }
    }

    public RuntimeVerification(PropertyChecker checker, String scope, Property property, PropertyChecker propertyChecker) {
        super(checker);
        if (scope.equals("Before")) {
            this.scope = "Before";
            this.end_event = property;
            this.end_event_checker = propertyChecker;
            this.isStarted = true;
            this.isEnded = false;
        } else if (scope.equals("After")) {
            this.scope = "After";
            this.start_event = property;
            this.start_event_checker = propertyChecker;
            this.isStarted = false;
        } else if (scope.equals("Existence")) {
            this.scope = "Existence";
            this.start_event = property;
            this.start_event_checker = propertyChecker;
        }
    }

    public RuntimeVerification(PropertyChecker checker, String scope, Property property1, PropertyChecker propertyChecker1, Property property2, PropertyChecker propertyChecker2) {
        super(checker);
        if (scope.equals("Between")) {
            this.scope = "Between";
            this.start_event = property1;
            this.start_event_checker = propertyChecker1;
            this.end_event = property2;
            this.end_event_checker = propertyChecker2;
            this.isStarted = false;
            this.isEnded = false;
        }
    }

    public boolean checkScope(Log log) {
        if (this.scope == null) {
            return true;
        }

        else if (scope.equals("Before") && !isEnded) {
            isEnded = this.end_event_checker.check(log, end_event);
            return !isEnded;
        }

        else if (scope.equals("After")) {
            if (isStarted) {
                return true;
            }
            isStarted = this.start_event_checker.check(log, start_event);
            return isStarted;
        }

        else if (scope.equals("Between")) {
            if (isStarted && isEnded) {
                return false;
            }

            if (isStarted) {
                isEnded = this.end_event_checker.check(log, end_event);
                return !isEnded;
            }

            isStarted = this.start_event_checker.check(log, start_event);
            return isStarted;
        }

        else if (scope.equals("Interval")) {
            int tick = log.getSnapshotMap().size();

            return tick >= start_tick && tick <= end_tick;
        }

        else if (scope.equals("Existence")) {
            return this.start_event_checker.check(log, start_event);
        }

        return false;
    }

    public boolean RuntimeVerificationResult(Log log, Property verificationProperty) {
        boolean verificationResult;
        if (checkScope(log)){
            verificationResult = (this.propertychecker.check(log, verificationProperty));
            System.out.println("Verification result: " + verificationResult);
            return verificationResult;
        }

        System.out.println("Verification result: Not in the Scope");
        return true;
    }
}
