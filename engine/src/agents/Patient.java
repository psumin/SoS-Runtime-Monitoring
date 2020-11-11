package agents;

import core.GlobalRandom;
import core.ImageObject;
import core.SoSObject;
import core.World;
import misc.Position;

/**
 * Project: NewSimulator
 * Created by IntelliJ IDEA
 * Author: Sumin Park <smpark@se.kaist.ac.kr>
 * Github: https://github.com/sumin0407/NewSimulator.git
 */

public class Patient extends CS {

    private final int seriousTreatmentTime = 40;                              // Hospital's treatment time for Serious patient
    private final int woundedTreatmentTime = 40;                               // Hospital's treatment time for Wounded patient
    public FireFighter assignedFireFighter = null;
    public boolean isSaved = false;
    public int gotFirstAidAt;
    public int gotFirstAidCount;
    SoSObject serious;
    SoSObject wounded;
    private Status status = Status.Wounded;
    private Hospital currentHospital = null;
    private boolean isTreatmenting = false;
    private int counter = 0;

    public Patient(World world, String name) {
        super(world, name);
        serious = new ImageObject("engine/resources/patient_serious1.png");              // Red dot
        wounded = new ImageObject("engine/resources/patient_wounded1.png");              // Blue dot
//        serious = new ImageObject("src/engine/resources/patient_serious1.png");              // Red dot
//        wounded = new ImageObject("src/engine/resources/patient_wounded1.png");              // Blue dot
        addChild(serious);
        addChild(wounded);
        this.gotFirstAidAt = -1;
        this.gotFirstAidCount = 0;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;

        switch (status) {
            case Wounded:                                       // Blue dot
                serious.visible(false);
                wounded.visible(true);
                break;
            case Serious:                                       // Red dot
                wounded.visible(false);
                serious.visible(true);
                break;
            case Dead:                                          // TODO: To be implemented
                break;
        }
    }

    public boolean isSerious() {
        return status == Status.Serious;
    }

    public boolean isWounded() {
        return status == Status.Wounded;
    }

    @Override
    public void remove() {
        super.remove();
    }

    public void treatmentStart(Hospital hospital) {
        currentHospital = hospital;
        isTreatmenting = true;
        if (status == Status.Serious) {
            counter = seriousTreatmentTime;
        } else if (status == Status.Wounded) {
            counter = woundedTreatmentTime;
        }
    }

    @Override
    public void onUpdate() {
        if (isTreatmenting) {
            counter--;
            if (counter <= 0) {
                // TODO: 치료 완료
                assert currentHospital != null : "이러면 안된다";
                currentHospital.leavePatient(this);                         // Leave patient after treatment
                currentHospital = null;
                world.removeChild(this);
                world.savedPatientCount++;                                  // Number of Saved patients
            }
        }
    }

    @Override
    public void setPosition(int x, int y) {
        worldMap.remove(this);
        super.setPosition(x, y);
        worldMap.add(this);
    }

    @Override
    public void setPosition(Position position) {
        worldMap.remove(this);
        super.setPosition(position);
        worldMap.add(this);
    }

    public enum Status {
        Wounded, Serious, Dead;

        public static Status random() {
            Status[] values = Status.values();
            int index = GlobalRandom.nextInt(1000);
            if (index >= 0)                                    // Ratio for Serious patient
                index = 0;
            else
                index = 1;
            return values[index];
        }
    }
}
