package property;

import log.Log;
import log.Snapshot;
import property.pattern.PreventionChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MCIPreventionChecker extends PreventionChecker {

    public MCIPreventionChecker() {

        super();
    }

    @Override
    protected boolean evaluateState(Log log, Property verificationProperty) {
        String prev = verificationProperty.getPrevState();
        String latter = verificationProperty.getState();
        String temp = "";
        int numFF = 0;
        int counter = 0;
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10
        ArrayList<Integer> prevList = new ArrayList<>(Collections.nCopies(50,-1));
        //ArrayList<Integer> latterList = new ArrayList<>(Collections.nCopies(50,-1));
        ArrayList<Integer> indexCounter = new ArrayList<>(Collections.nCopies(15,0));
        boolean occurrence = false;
        double treatRate = -1;

        for(int i = 1; i < logSize; i++) {
            temp = snapshots.get(i).getSnapshotString();

            StringTokenizer st = new StringTokenizer(temp, " ");
            counter = 0;
            while(st.hasMoreTokens()) {
                String tokens = st.nextToken();
                if(occurrence){
                    if(tokens.equals("TreatmentRate:")) {
                        treatRate = Double.parseDouble(st.nextToken());
                        System.out.println(temp);
                        if(treatRate <= verificationProperty.getThresholdValue()){
                            return false;
                        }
                        break;
                    }
                }
                if(tokens.equals("Amb:")) break;
                if(tokens.equals("CurrentFF:")) {
                    int tmpFF = Integer.parseInt(st.nextToken());
                    if(tmpFF > numFF) numFF = tmpFF;
                }
                if(tokens.equals("FF:")) {
                    while(counter < numFF) {
                        String fflog = st.nextToken();
                        if(fflog.contains(prev)) { // 마지막으로 prev state가 관찰될때
                            System.out.println(temp);
                            occurrence = true;
                            break;
                        }
                        counter++;
                    }
                }
            }
        }
        //System.out.println("Tick list of Treatment Activity: " + prevList);
        //System.out.println("Tick list of Transfer Activity: " + latterList);
        return true;
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
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {return false; }
}
