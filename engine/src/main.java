import log.Log;
import runtimeproperty.Event;
import runtimeproperty.RuntimeProperty;
import runtimeproperty.event.RescuedRateEvent;
import runtimeproperty.pattern.RuntimeExistence;
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
    public static void main(String [] args) {
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

        ArrayList<RuntimeProperty> runtimeProperties = new ArrayList<RuntimeProperty>(0);

        // Scopes
        GloballyScope globallyScope = new GloballyScope();
        BeforeScope beforeScope = new BeforeScope(new RescuedRateEvent(0.1));
        AfterScope afterScope = new AfterScope(new RescuedRateEvent(0.1));
        BetweenScope betweenScope = new BetweenScope(new RescuedRateEvent(0.1), new RescuedRateEvent(0.5));
        IntervalScope intervalScope = new IntervalScope(50, 500);
        DuringScope duringScope = new DuringScope(new RescuedRateEvent(0.1));

        // Existence
        // 설명: '시뮬레이션 종료 시점'까지, [누적된 발생 환자의 수가 '전체 기대 환자 수'와 같아지는 사건]이 언젠가 만족될 확률이 '기대 확률' 이상이다.
        RescuedRateEvent rescuedRateEvent = new RescuedRateEvent(0.25);
        RuntimeExistence runtimeExistenceGlobally = new RuntimeExistence(rescuedRateEvent, globallyScope);
        runtimeProperties.add(runtimeExistenceGlobally);
        RuntimeExistence runtimeExistenceBefore = new RuntimeExistence(rescuedRateEvent, beforeScope);
        runtimeProperties.add(runtimeExistenceBefore);
        RuntimeExistence runtimeExistenceAfter = new RuntimeExistence(rescuedRateEvent, afterScope);
        runtimeProperties.add(runtimeExistenceAfter);
        RuntimeExistence runtimeExistenceBetween = new RuntimeExistence(rescuedRateEvent, betweenScope);
        runtimeProperties.add(runtimeExistenceBetween);
        RuntimeExistence runtimeExistenceInterval = new RuntimeExistence(rescuedRateEvent, intervalScope);
        runtimeProperties.add(runtimeExistenceInterval);
        RuntimeExistence runtimeExistenceDuring = new RuntimeExistence(rescuedRateEvent, duringScope);
        runtimeProperties.add(runtimeExistenceDuring);

        RuntimeVerifier runtimeVerifier = new RuntimeVerifier(runtimeProperties);

        simulationEngine.runtimeVerificationRun(runtimeVerifier);                         // 런타임 검증을 위한 run
//        simulationEngine.run();
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
