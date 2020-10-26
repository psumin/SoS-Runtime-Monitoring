import log.Log;
import property.pattern.MinimumDurationChecker;
import runtimeproperty.RuntimeProperty;
import runtimeproperty.Scope;
import runtimeproperty.event.*;
import runtimeproperty.pattern.*;
import runtimeproperty.scope.*;
import simulation.SoSSimulationProgram;
import verifier.RuntimeVerifier;

import java.util.ArrayList;

/* Runtime Verification
    RuntimeVerification verifier;

    verifier = new RuntimeVerification(existenceChecker);

    simulationEngine.runtimeVerificationRun();
 */

public class main {

    Log log = new Log();

    public static void main(String[] args) {
        long programStartTime;                                          // 프로그램 시작 시간
        long programEndTime;                                            // 프로그램 종료 시간
        long thetaStartTime;                                            // 한 사이클 시작 시간
        long thetaEndTime;                                              // 한 사이클 종료 시간
        boolean ruuning = true;

        SoSSimulationProgram simulationEngine = new SoSSimulationProgram();
        simulationEngine.setRunning();
//        System.out.println("Get Running: "+ simulationEngine.getRunning());
        programStartTime = System.nanoTime();           // 첫번째 시뮬레이션까지 포함할려면 여기에 정의
//        simulationEngine.run();                       // 통계적 검증을 위한 run

        ArrayList<RuntimeProperty> runtimeProperties = new ArrayList<>(0);

        // Scopes
        ArrayList<Scope> scopes = new ArrayList<>(0);
        scopes.add(new GloballyScope());
        scopes.add(new BeforeScope(new RescuedRateEvent(0.5)));
        scopes.add(new AfterScope(new RescuedRateEvent(0.1)));
        scopes.add(new BetweenScope(new RescuedRateEvent(0.1), new RescuedRateEvent(0.5)));
        scopes.add(new IntervalScope(50, 500));
        scopes.add(new DuringScope(new RescuedRateEvent(0.25)));

        // Absence
        // 설명: Ambulance 가 특정 route 밖으로 나가는 사건이 절대로 일어나지 않아야 한다.
        AmbulanceRouteEvent ambulanceRouteEvent = new AmbulanceRouteEvent(34, 34);
        for(Scope scope: scopes) {
            runtimeProperties.add(new RuntimeAbsence(ambulanceRouteEvent, scope));
        }

        // Universality
        // 설명: TreatmentRate 이 RescuedRate 이하인 사건이 항상 만족되어야 한다.
        TreatmentRescuedRateEvent treatmentRescuedRateEvent = new TreatmentRescuedRateEvent();
        for(Scope scope: scopes) {
            runtimeProperties.add(new RuntimeUniversality(treatmentRescuedRateEvent, scope));
        }

        // Existence
        // 설명: RescuedRate 이 0.25 이상이 되는 사건이 발생해야 한다.
        RescuedRateEvent rescuedRateEvent = new RescuedRateEvent(0.25);
        for(Scope scope: scopes) {
            runtimeProperties.add(new RuntimeExistence(rescuedRateEvent, scope));
        }

        // Bounded Existence
        // 설명: 최소한 하나의 Ambulance 가 단 한 칸만 움직이는 사건이 최대 500번 발생한다.
        AmbulanceMoveEvent ambulanceMoveEvent = new AmbulanceMoveEvent();
        for(Scope scope: scopes) {
            runtimeProperties.add(new RuntimeBoundedExistence(ambulanceMoveEvent, scope, 500, true));
        }

        // Minimum Duration
        // Firefighter 의 Action 이 First Aid 인 사건이 지속되는 최소 시간이 8이어야 한다.
        FirefighterFirstAidEvent firefighterFirstAidEvent = new FirefighterFirstAidEvent();
        for(Scope scope: scopes) {
            runtimeProperties.add(new RuntimeMinimumDuration(firefighterFirstAidEvent, scope, 8));
        }

        // Maximum Duration
        // Ambulance 의 Action 이 Free State 인 사건이 지속되는 최대 시간이 1 이어야 한다.

        // Recurrence
        // 환자가 병원에 도착하는 사건이 시간 10 간격으로 반복적으로 발생해야 한다.

        // Precedence
        // 환자가 Bridgehead 에 도착하는 사건이 발생했다면, 그 이전에 해당 환자가 First Aid 를 받는 사건이 발생했어야 한다.

        // Response
        // Treatment Rate 가 100% 가 되는 사건이 발생한다면, Firefighter 의 Action 이 Halt 인 사건이 발생해야 한다.

        // Until
        // Firefighter 의 Action 이 Halt 가 아닌 사건이 Unvisited Tile 이 없어지는 사건이 일어날 때까지 지속되어야 한다.

        // Prevention
        // 한 환자가 First Aid 를 받는 사건이 발생한다면, 그 이후에 해당 환자가 First Aid 를 받는 사건이 다시 발생하지 않아야 한다.

        RuntimeVerifier runtimeVerifier = new RuntimeVerifier(runtimeProperties);

        simulationEngine.runtimeVerificationRun(runtimeVerifier);                         // 런타임 검증을 위한 run
//        simulationEngine.run();
        simulationEngine.setSuper_counter();
        programEndTime = System.nanoTime();
        System.out.println("=== Total Program running time: " + (programEndTime - programStartTime) / (float) 1000_000_000 + " sec");
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
