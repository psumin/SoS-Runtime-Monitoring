import javafx.util.Pair;
import log.Log;
import property.*;
import simulation.SoSSimulationProgram;
import verifier.RuntimeVerification;
import verifier.SPRT;

import java.util.ArrayList;

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
//        ArrayList<SPRT> sprtVerifiers = new ArrayList<SPRT>(0);
        ArrayList<RuntimeVerification> runtimeVerifiers = new ArrayList<RuntimeVerification>(0);        // 여러개를 한번에 처리하기 위해 배열로 정의

        //Verification Properties
        ArrayList<MCIProperty> properties = new ArrayList<MCIProperty>(0);                      // 다양한 pattern 들을 한번에 처리하기 위해 배열로 정의

        // Existence
        // 설명: '시뮬레이션 종료 시점'까지, [누적된 발생 환자의 수가 '전체 기대 환자 수'와 같아지는 사건]이 언젠가 만족될 확률이 '기대 확률' 이상이다.
//        MCIProperty existenceProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
//        existenceProperty.setRescueRate(0.02);
//        MCIPropertyChecker mciPropertyChecker = new MCIPropertyChecker();
//        properties.add(existenceProperty);
//        properties.add(existenceProperty);
//        properties.add(existenceProperty);
//        properties.add(existenceProperty);
//        properties.add(existenceProperty);
//        properties.add(existenceProperty);


        // Absence
        // 설명: '시뮬레이션 종료 시점'까지, [Rescue rate보다 Treatment rate이 더 큰] 상태가 발생할 확률이 '기대 확률' 미만이다.
        MCIProperty absenceProperty = new MCIProperty("TreatmentRateRescueRateProperty", "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceProperty.setThresholdValue(0); // RescueRate - TreatmentRate can not be minus
        MCIAbsenceChecker mciPropertyChecker = new MCIAbsenceChecker();
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);

        // Universality
        // 설명: '시뮬레이션 종료 시점'까지, [Rescue rate이 0% 이상 100% 이하]가 항상 만족된다.
//        MCIProperty property = new MCIProperty("RescueRateProperty", "RescuedPatientRatioLowerThanValue", "MCIUniversality");
//        property.setThresholdValue(1.0); // RescueRate should be 100%?? condition is rescuerate <= threshold && rescuerate > 0
//        MCIUniversalityChecker mciPropertyChecker = new MCIUniversalityChecker();
//        properties.add(property);

        // TransientStateProbability
        // 설명: '시뮬레이션 종료 시점'까지, '지정 시간' 이후로 [구조된 환자의 수가 '문턱 환자 수'보다 큰] 상태가 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCITransientSP");
//        property.setStateProbabilityValues(0.6, 60, 81);
//        MCITransientSPChecker mciPropertyChecker = new MCITransientSPChecker();
//        properties.add(property);

        // SteadyStateProbability
        // 설명: '시뮬레이션 종료 시점'까지, 긴 시간동안 [구조된 환자의 수가 '문턱 환자 수'보다 큰] 상태가 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCISteadySP");
//        property.setStateProbabilityValues(0.15, 0, 81);
//        MCISteadySPChecker mciPropertyChecker = new MCISteadySPChecker();
//        properties.add(property);

        // MinimumDuration
        // 설명: '시뮬레이션 종료 시점'까지, [생성된 소방 요원들의 활동율이 문턱 활동율보다 큰] 상태가 최소 [일정 논리 시간] 이상 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIMinimumDuration");
//        property.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
//        property.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
//        MCIMinimumDurationChecker mciPropertyChecker = new MCIMinimumDurationChecker();
//        properties.add(property);

        // MaximumDuration
        // 설명: '시뮬레이션 종료 시점'까지, [남은 환자가 문턱 환자수 이하인] 상태가 최대 [일정 논리 시간]까지 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIMaximumDuration");
//        property.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
//        property.setDuration(60); // 최대 60 Frame 이하
//        MCIMaximumDurationChecker mciPropertyChecker = new MCIMaximumDurationChecker();
//        properties.add(property);

        // BoundedExistence
        // 설명: '입력 시점 t'까지, [모든 Ambulance가 활동 상태가 되는 사건]이 만족될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIBoundedExistence");
//        property.setDuration(20); // Bounded Frame 20
//        property.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
//        MCIBoundedExistenceChecker mciPropertyChecker = new MCIBoundedExistenceChecker();
//        properties.add(property);

        // Precedence --> Precedence checker always returns false in MCIPrecedenceChecker?
        // 설명: '시뮬레이션 종료 시점'까지, [소방 요원이 Rescue Activity를 진행하는] 상태가 [소방요원이 Treatment Activity를 진행하는] 상태보다 선행될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIPrecedence");
//        property.setPrevState("MoveToPatient");
//        property.setState("FirstAid");
//        MCIPrecedenceChecker mciPropertyChecker = new MCIPrecedenceChecker();
//        properties.add(property);

        // Response --> 근데 MCIResponseChecker 좀 바꿔야 할듯. 항상 true로 나오는 것 같음. 숫자 비교를 통해서 treatment의 값이 transfer 보다 작으면  true 인듯?
        // 설명: '시뮬레이션 종료 시점'까지, [소방요원이 Rescue Activity를 진행하는] 상태는 반드시 [소방요원이 Treatment Activity]상태 이후에 진행될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIResponse");
//        property.setPrevState("FirstAid");
//        property.setState("TransferToBridgehead");
//        MCIResponseChecker mciPropertyChecker = new MCIResponseChecker();
//        properties.add(property);

        // Recurrence --> 근데 MCIRecurrenceChecker 좀 바꿔야 할듯. 이것도 처음부터 true가 나옴.
        // 설명: '시뮬레이션 종료 시점'까지, [모든 소방요원들이 일정 논리 시간 간격으로 환자 구조를 반복하는] 상태가 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIRecurrence");
//        property.setPrevState("MoveToPatient");
//        property.setThresholdValue(51);
//        MCIRecurrenceChecker mciPropertyChecker = new MCIRecurrenceChecker();
//        properties.add(property);

        // Until --> 좀 바꿔줘야할듯? -1 이 무슨 state 인 상태인걸까???
        // 설명: 'RescueRate = 1.00인 상태'까지, [모든 소방 요원들이 활동하는] 상태가 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("", "", "MCIUntil");
//        property.setPrevState("Free");
//        MCIUntilChecker mciPropertyChecker = new MCIUntilChecker();
//        properties.add(property);

        // NumberOfEvents
//        MCIProperty property = new MCIProperty("RouteMsgCountProperty", "RouteMsgCountLTEQX", "MCINumberofEvents");
//        property.setThresholdValue(600);
//        MCINumberofEventsChecker mciPropertyChecker = new MCINumberofEventsChecker();
//        properties.add(property);

        // Prevention
//        MCIProperty property = new MCIProperty("", "", "MCIPrevention");
//        property.setPrevState("TransferToBridgehead");
//        property.setThresholdValue(0);
//        MCIPreventionChecker mciPropertyChecker = new MCIPreventionChecker();
//        properties.add(property);


//        SPRT verifier;
//        verifier = new SPRT(mciPropertyChecker);
        RuntimeVerification verifier;

        // Scopes
        // Globally
        verifier = new RuntimeVerification(mciPropertyChecker);
        runtimeVerifiers.add(verifier);

        // Before
        MCIProperty beforeEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        beforeEvent.setRescueRate(0.02);
        MCIPropertyChecker beforeEventPropertyChecker = new MCIPropertyChecker();
        verifier = new RuntimeVerification(mciPropertyChecker, "Before", beforeEvent, beforeEventPropertyChecker);
        runtimeVerifiers.add(verifier);

        // After
        MCIProperty afterEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        afterEvent.setRescueRate(0.02);
        MCIPropertyChecker afterEventPropertyChecker = new MCIPropertyChecker();
        verifier = new RuntimeVerification(mciPropertyChecker, "After", afterEvent, afterEventPropertyChecker);
        runtimeVerifiers.add(verifier);

        // Between
        MCIProperty betweenBeforeEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        betweenBeforeEvent.setRescueRate(0.02);
        MCIPropertyChecker betweenBeforeEventPropertyChecker = new MCIPropertyChecker();
        MCIProperty betweenAfterEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        betweenAfterEvent.setRescueRate(0.50);
        MCIPropertyChecker betweenAfterEventPropertyChecker = new MCIPropertyChecker();
        verifier = new RuntimeVerification(mciPropertyChecker, "Between", betweenBeforeEvent, betweenBeforeEventPropertyChecker, betweenAfterEvent, betweenAfterEventPropertyChecker);
        runtimeVerifiers.add(verifier);

        // Interval
        verifier = new RuntimeVerification(mciPropertyChecker, "Interval", 100, 300);
        runtimeVerifiers.add(verifier);

        // Existence
        MCIProperty existenceEvent = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        existenceEvent.setRescueRate(0.02);
        MCIPropertyChecker existenceEventPropertyChecker = new MCIPropertyChecker();
        verifier = new RuntimeVerification(mciPropertyChecker, "Existence", existenceEvent, existenceEventPropertyChecker);
        runtimeVerifiers.add(verifier);

        Pair<Pair<Integer, Boolean>, String> verificationResult;

        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        simulationEngine.setRunning();
//        System.out.println("Get Running: "+ simulationEngine.getRunning());
        programStartTime = System.nanoTime();           // 첫번째 시뮬레이션까지 포함할려면 여기에 정의
//        simulationEngine.run();                       // 통계적 검증을 위한 run


        simulationEngine.runtimeVerificationRun(runtimeVerifiers, properties);                         // 런타임 검증을 위한 run
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
