package property;

public class MCIProperty extends Property {
    double rescueRate;
    double thresholdValue;
    String prevState;
    String state;
    double prob;
    int t;
    int T;
    int duration;

    public MCIProperty(String name, String specification, String propertyType) {
        super(name, specification, propertyType);
    }

    public void setRescueRate(double rescueRate) {
        this.rescueRate = rescueRate;
    }

    public void setStateProbabilityValues(double prob, int t, int T) {
        this.prob = prob;
        this.t = t;
        this.T = T;
    }

    public double getProb() {
        return this.prob;
    }

    public int getT() {
        return this.t;
    }

    public int getTT() {
        return this.T;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getValue() {
        return this.rescueRate;
    }

    public double getThresholdValue() {
        return this.thresholdValue;
    }

    public void setThresholdValue(double thresholdV) {
        this.thresholdValue = thresholdV;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrevState() {
        return this.prevState;
    }

    public void setPrevState(String state) {
        this.prevState = state;
    }
}
