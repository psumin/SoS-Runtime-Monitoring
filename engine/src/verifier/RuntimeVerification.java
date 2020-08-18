package verifier;

import property.Property;
import property.PropertyChecker;
import log.Log;


public class RuntimeVerification extends Verifier {

    public RuntimeVerification(PropertyChecker checker) {
        super(checker);
    }

    public boolean RuntimeVerificationResult(Log log, Property verificationProperty) {
        Boolean verificationResult;
        verificationResult = (this.propertychecker.check(log, verificationProperty));
        System.out.println("Verification result: " + verificationResult);
        return verificationResult;
    }
}
