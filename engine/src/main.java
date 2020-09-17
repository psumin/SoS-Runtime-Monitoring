import javafx.util.Pair;
import log.Log;
import property.*;
import simulation.SoSSimulationProgram;
import verifier.RuntimeVerification;

/* Runtime Verification
    RuntimeVerification verifier;

    verifier = new RuntimeVerification(existenceChecker);

    simulationEngine.runtimeVerificationRun();
 */

public class main {

    Log log = new Log();
    public static void main(String [] args){
        long programStartTime;                                          // 프로그램 시작 시간
        long programEndTime;                                            // 프로그램 종료 시간
        long thetaStartTime;                                            // 한 사이클 시작 시간
        long thetaEndTime;                                              // 한 사이클 종료 시간
        boolean ruuning = true;


        //verification method
//        SPRT verifier;
        RuntimeVerification verifier;


        //Verification Properties
        // Existence
        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        property.setRescueRate(0.02);
        MCIPropertyChecker mciPropertyChecker = new MCIPropertyChecker();

        // Absence
//        MCIProperty property = new MCIProperty("TreatmentRateRescueRateProperty", "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
//        property.setThresholdValue(0); // RescueRate - TreatmentRate can not be minus
//        MCIAbsenceChecker mciPropertyChecker = new MCIAbsenceChecker();

        // Universality
//        MCIProperty property = new MCIProperty("RescueRateProperty", "RescuedPatientRatioLowerThanValue", "MCIUniversality");
//        property.setThresholdValue(1.0); // RescueRate should be 100%?? condition is rescuerate <= threshold && rescuerate > 0
//        MCIUniversalityChecker mciPropertyChecker = new MCIUniversalityChecker();

        // TransientStateProbability
//        MCIProperty property = new MCIProperty("", "", "MCITransientSP");
//        property.setStateProbabilityValues(0.6, 60, 81);
//        MCITransientSPChecker mciPropertyChecker = new MCITransientSPChecker();

        // SteadyStateProbability
//        MCIProperty property = new MCIProperty("", "", "MCISteadySP");
//        property.setStateProbabilityValues(0.15, 0, 81);
//        MCISteadySPChecker mciPropertyChecker = new MCISteadySPChecker();

        // MinimumDuration
//        MCIProperty property = new MCIProperty("", "", "MCIMinimumDuration");
//        property.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
//        property.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
//        MCIMinimumDurationChecker mciPropertyChecker = new MCIMinimumDurationChecker();

        // MaximumDuration
//        MCIProperty property = new MCIProperty("", "", "MCIMaximumDuration");
//        property.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
//        property.setDuration(60); // 최대 60 Frame 이하
//        MCIMaximumDurationChecker mciPropertyChecker = new MCIMaximumDurationChecker();

        // Bounded Existence
//        MCIProperty property = new MCIProperty("", "", "MCIBoundedExistence");
//        property.setDuration(20); // Bounded Frame 20
//        property.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
//        MCIBoundedExistenceChecker mciPropertyChecker = new MCIBoundedExistenceChecker();

        // Precedence --> Precedence checker always returns false in MCIPrecedenceChecker?
//        MCIProperty property = new MCIProperty("", "", "MCIPrecedence");
//        property.setPrevState("MoveToPatient");
//        property.setState("FirstAid");
//        MCIPrecedenceChecker mciPropertyChecker = new MCIPrecedenceChecker();

        // Response --> 근데 MCIResponseChecker 좀 바꿔야 할듯. 항상 true로 나오는 것 같음. 숫자 비교를 통해서 treatment의 값이 transfer 보다 작으면  true 인듯?
//        MCIProperty property = new MCIProperty("", "", "MCIResponse");
//        property.setPrevState("FirstAid");
//        property.setState("TransferToBridgehead");
//        MCIResponseChecker mciPropertyChecker = new MCIResponseChecker();

        // Recurrence --> 근데 MCIRecurrenceChecker 좀 바꿔야 할듯. 이것도 처음부터 true가 나옴.
//        MCIProperty property = new MCIProperty("", "", "MCIRecurrence");
//        property.setPrevState("MoveToPatient");
//        property.setThresholdValue(51);
//        MCIRecurrenceChecker mciPropertyChecker = new MCIRecurrenceChecker();

        // Until --> 좀 바꿔줘야할듯? -1 이 무슨 state 인 상태인걸까???
//        MCIProperty property = new MCIProperty("", "", "MCIUntil");
//        property.setPrevState("Free");
//        MCIUntilChecker mciPropertyChecker = new MCIUntilChecker();




//        verifier = new SPRT(mciPropertyChecker);

        // Scopes
        // Globally
//        verifier = new RuntimeVerification(mciPropertyChecker);

        // Before
//        MCIProperty event = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
//        event.setRescueRate(0.02);
//        MCIPropertyChecker eventPropertyChecker = new MCIPropertyChecker();
//        verifier = new RuntimeVerification(mciPropertyChecker, "Before", event, eventPropertyChecker);

        // After
//        MCIProperty event = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
//        event.setRescueRate(0.02);
//        MCIPropertyChecker eventPropertyChecker = new MCIPropertyChecker();
//        verifier = new RuntimeVerification(mciPropertyChecker, "After", event, eventPropertyChecker);

        // Between
//        MCIProperty beforeEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
//        beforeEvent.setRescueRate(0.02);
//        MCIPropertyChecker beforeEventPropertyChecker = new MCIPropertyChecker();
//        MCIProperty afterEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
//        beforeEvent.setRescueRate(0.50);
//        MCIPropertyChecker afterEventPropertyChecker = new MCIPropertyChecker();
//        verifier = new RuntimeVerification(mciPropertyChecker, "Between", beforeEvent, beforeEventPropertyChecker, afterEvent, afterEventPropertyChecker);

        // Interval
//        verifier = new RuntimeVerification(mciPropertyChecker, "Interval", 100, 500);

        // Existence
        MCIProperty event = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        event.setRescueRate(0.02);
        MCIPropertyChecker eventPropertyChecker = new MCIPropertyChecker();
        verifier = new RuntimeVerification(mciPropertyChecker, "Existence", event, eventPropertyChecker);

        Pair<Pair<Integer, Boolean>, String> verificationResult;

        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        simulationEngine.setRunning();
//        System.out.println("Get Running: "+ simulationEngine.getRunning());
        programStartTime = System.nanoTime();           // 첫번째 시뮬레이션까지 포함할려면 여기에 정의
//        simulationEngine.run();                       // 통계적 검증을 위한 run


        simulationEngine.runtimeVerificationRun(verifier, property);                         // 런타임 검증을 위한 run
        simulationEngine.setSuper_counter();
        programEndTime = System.nanoTime();
        System.out.println("=== Total Program running time: " + (programEndTime - programStartTime) / (float)1000_000_000 + " sec");
//        double satisfactionProb = 0;
//        Boolean satisfaction = true;


//
//        for (int i = 1; i < 100; i++) {
////            programStartTime = System.nanoTime();           // 첫번째 시뮬레이션을 제외하려면 여기에 정의
//
////            System.out.println("inside for loop:" + simulationEngine.getRunning());
//            double theta = i * 0.01;
//
//            thetaStartTime = System.nanoTime();
//            verificationResult = verifier.verifyWithSimulationGUI(simulationEngine, property, 2000, 0.01);    //or T = 3
//            thetaEndTime = System.nanoTime();
//            System.out.println(i /(float)100 + " theta verification running time: " + (thetaEndTime - thetaStartTime) / (float)1000_000_000 + " sec");          // 한 theta 실행 시간
//
//            System.out.println(verificationResult.getValue());
//            if (satisfaction == true && !verificationResult.getKey().getValue()) {
//                satisfactionProb = theta;
//                satisfaction = false;
//            }
//
//        }
//        if (satisfaction == true) {
//            satisfactionProb = 1;
//        }
//        System.out.println("Verification property satisfaction probability: " + satisfactionProb);
//        programEndTime = System.nanoTime();
//        System.out.println("=== Total Program running time: " + (programEndTime - programStartTime) / (float)1000_000_000 + " sec");          // 전체 프로그램 실행 시간
//
////        new Thread(simulationEngine).start();
//

    }
}
