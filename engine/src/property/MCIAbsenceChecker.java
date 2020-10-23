package property;

import log.Log;
import log.Snapshot;
import property.pattern.AbsenceChecker;

import java.util.StringTokenizer;

public class MCIAbsenceChecker extends AbsenceChecker {
    
    public MCIAbsenceChecker() {
        super();
    }
    
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        double rescueRate = -1;
        double treatRate = -1;
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            String target = st.nextToken();
            if(target.equals("RescuedRate:")) {
                rescueRate = Double.parseDouble(st.nextToken());
            } else if (target.equals("TreatmentRate:")) {
                treatRate = Double.parseDouble(st.nextToken());
            }
        }
        System.out.println("rescuedrate :" + rescueRate);
        System.out.println("treatmentrate : " + treatRate);
        
        if(rescueRate - treatRate >= verificationProperty.getThresholdValue()){
            //rescuerate - treatrate >= 0; return false
            //rescuerate should be bigger than treatrate?
            return false;
        }
        else{
            return true;
        }
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, int until) {
        return false;
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int T) {
        return false;
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        return false;
    }
    
    @Override
    public boolean check(Log log, Property verificationProperty, int t, int T) {
        return false;
    }
    
    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {return false; }
}
