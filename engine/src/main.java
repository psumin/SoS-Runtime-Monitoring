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

        // Existence --> work!
//        MCIProperty property = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence", 0.02);
        // Absence --> implemented
//        property.setThresholdValue(0); // RescueRate - TreatmentRate can not be minus
        // Universality --> implemented
//        property.setThresholdValue(1.0); // RescueRate should be 100%?? condition is rescuerate <= threshold && rescuerate > 0
        // TransientStateProbability --> work!
//        property.setStateProbabilityValues(0.6, 60, 81);
        // SteadyStateProbability --> work!
//        property.setStateProbabilityValues(0.15, 0, 81);
        // MinimumDuration --> implemented
//        property.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
//        property.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        // MaximumDuration --> implemented
//        property.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
//        property.setDuration(60); // 최대 60 Frame 이하
        // Bounded Existence --> work!
//        property.setDuration(20); // Bounded Frame 20
//        property.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        // Precedence --> Implemented, precedence checker always returns false in MCIPrecedenceChecker?
        //property.setPrevState("MoveToPatient");
        //property.setState("FirstAid");
        // Response --> work!  ==> 근데 MCIResponseChecker 좀 바꿔야 할듯. 항상 true로 나오는 것 같음. 숫자 비교를 통해서 treatment의 값이 transfer 보다 작으면  true 인듯?
//        MCIProperty property = new MCIProperty("", "", "MCIResponse", 0);
//        property.setPrevState("FirstAid");
//        property.setState("TransferToBridgehead");
        // Recurrence --> work!  ==> 근데 MCIrecurrenceChecker 좀 바꿔야 할듯. 이것도 처음부터 true가 나옴.
//        property.setPrevState("MoveToPatient");
//        property.setThresholdValue(51);
        // Until--> work!  ==> 좀 바꿔줘야할듯?    -1 이 무슨 state 인 상태인걸까???
//        property.setPrevState("Free");
        MCIProperty property = new MCIProperty("", "", "MCIPrevention", 0);
        property.setPrevState("TransferToBridgehead");
        property.setThresholdValue(0);

//        MCIPropertyChecker existenceChecker = new MCIPropertyChecker();
//        MCIAbsenceChecker absenceChecker = new MCIAbsenceChecker();
//        MCIUniversalityChecker universalityChecker = new MCIUniversalityChecker();
//        MCITransientSPChecker transientSPChecker = new MCITransientSPChecker();
//        MCISteadySPChecker steadySPChecker = new MCISteadySPChecker();
//        MCIMinimumDurationChecker minimumDurationChecker = new MCIMinimumDurationChecker();
//        MCIMaximumDurationChecker maximumDurationChecker = new MCIMaximumDurationChecker();
//        MCIBoundedExistenceChecker boundedExistenceChecker = new MCIBoundedExistenceChecker();
//        MCIPrecedenceChecker precedenceChecker = new MCIPrecedenceChecker();
//        MCIResponseChecker responseChecker = new MCIResponseChecker();
//        MCIRecurrenceChecker recurrenceChecker = new MCIRecurrenceChecker();
//        MCIUntilChecker untilChecker = new MCIUntilChecker();
        MCIPreventionChecker preventionChecker = new MCIPreventionChecker();


//        verifier = new SPRT(existenceChecker);
//        verifier = new RuntimeVerification(existenceChecker);
//        verifier = new SPRT(absenceChecker);
//        verifier = new RuntimeVerification(absenceChecker);
//        verifier = new SPRT(universalityChecker);
//        verifier = new RuntimeVerification(universalityChecker);
//        verifier = new SPRT(transientSPChecker);
//        verifier = new RuntimeVerification(transientSPChecker);
//        verifier = new SPRT(steadySPChecker);
//        verifier = new RuntimeVerification(steadySPChecker);
//        verifier = new SPRT(minimumDurationChecker);
//        verifier = new RuntimeVerification(minimumDurationChecker);
//        verifier = new SPRT(maximumDurationChecker);
//        verifier = new RuntimeVerification(maximumDurationChecker);
//        verifier = new SPRT(boundedExistenceChecker);
//        verifier = new RuntimeVerification(boundedExistenceChecker);
//        verifier = new SPRT(precedenceChecker);
//        verifier = new RuntimeVerification(precedenceChecker);
//        verifier = new SPRT(responseChecker);
//        verifier = new RuntimeVerification(responseChecker);
//        verifier = new SPRT(recurrenceChecker);
//        verifier = new RuntimeVerification(recurrenceChecker);
//        verifier = new SPRT(untilChecker);
//        verifier = new RuntimeVerification(untilChecker);
        verifier = new RuntimeVerification(preventionChecker);
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
