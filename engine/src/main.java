import runtimeproperty.RuntimeProperty;
import runtimeproperty.Scope;
import runtimeproperty.event.*;
import runtimeproperty.pattern.*;
import runtimeproperty.scope.*;
import simulation.SoSSimulationProgram;
import verifier.RuntimeVerifier;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        int MAX_FRAME_COUNT = 1000;
        int max_round = 20;

        ArrayList<Long> timeWithVerification = new ArrayList<>(max_round);
        ArrayList<Long> timeWithoutVerification = new ArrayList<>(max_round);

        for (int i = 0; i < max_round; i++) {
            long programStartTime;
            long programEndTime;

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
            AllAmbulanceRouteEvent allAmbulanceRouteEvent = new AllAmbulanceRouteEvent(34, 34);
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeAbsence(allAmbulanceRouteEvent, scope));
            }

            // Universality
            // 설명: TreatmentRate 이 RescuedRate 이하인 사건이 항상 만족되어야 한다.
            TreatmentRescuedRateEvent treatmentRescuedRateEvent = new TreatmentRescuedRateEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeUniversality(treatmentRescuedRateEvent, scope));
            }

            // Existence
            // 설명: RescuedRate 이 0.25 이상이 되는 사건이 발생해야 한다.
            RescuedRateEvent rescuedRateEvent = new RescuedRateEvent(0.25);
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeExistence(rescuedRateEvent, scope));
            }

            // Bounded Existence
            // 설명: 최소한 하나의 Ambulance 가 단 한 칸만 움직이는 사건이 최대 500번 발생한다.
            AmbulanceMoveEvent ambulanceMoveEvent = new AmbulanceMoveEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeBoundedExistence(ambulanceMoveEvent, scope, 500, true));
            }

            // Minimum Duration
            // 설명: Firefighter 의 Action 이 First Aid 인 사건이 지속되는 최소 시간이 8이어야 한다.
            FirefighterFirstAidEvent firefighterFirstAidEvent = new FirefighterFirstAidEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeMinimumDuration(firefighterFirstAidEvent, scope, 8));
            }

            // Maximum Duration
            // 설명: Ambulance 의 Action 이 Free State 인 사건이 지속되는 최대 시간이 1 이어야 한다.
            AmbulanceFreeEvent ambulanceFreeEvent = new AmbulanceFreeEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeMaximumDuration(ambulanceFreeEvent, scope, 1));
            }

            // Recurrence
            // 설명: 환자가 병원에 도착하는 사건이 시간 10 간격 이내로 반복적으로 발생해야 한다.
            AllPatientArriveAtHospitalEvent allPatientArriveAtHospitalEvent = new AllPatientArriveAtHospitalEvent(34, 34);
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeRecurrence(allPatientArriveAtHospitalEvent, scope, 10));
            }

            // Precedence
            // 설명: 환자가 Bridgehead 에 도착하는 사건이 발생했다면, 그 이전에 해당 환자가 First Aid 를 받는 사건이 발생했어야 한다.
            PatientGetFirstAidEvent patientGetFirstAidEvent = new PatientGetFirstAidEvent();
            PatientArriveAtBridgeheadEvent patientArriveAtBridgeheadEvent = new PatientArriveAtBridgeheadEvent(34, 34);
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimePrecedence(patientGetFirstAidEvent, patientArriveAtBridgeheadEvent, scope));
            }

            // Response
            // 설명: Treatment Rate 가 100% 가 되는 사건이 발생한다면, Firefighter 의 Action 이 Halt 인 사건이 발생해야 한다.
            TreatmentRate100Event treatmentRate100Event = new TreatmentRate100Event();
            AllFirefighterHaltEvent allFirefighterHaltEvent = new AllFirefighterHaltEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeResponse(treatmentRate100Event, allFirefighterHaltEvent, scope));
            }

            // Until
            // 설명: Firefighter 의 Action 이 Halt 가 아닌 사건이 Unvisited Tile 이 없어지는 사건이 일어날 때까지 지속되어야 한다.
            AllFirefighterNotHaltEvent allFirefighterNotHaltEvent = new AllFirefighterNotHaltEvent();
            UnvisitedTileEvent unvisitedTileEvent = new UnvisitedTileEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimeUntil(allFirefighterNotHaltEvent, unvisitedTileEvent, scope));
            }

            // Prevention
            // 설명: 한 환자가 First Aid 를 받는 사건이 발생한다면, 그 이후에 해당 환자가 First Aid 를 받는 사건이 다시 발생하지 않아야 한다.
            PatientGetFirstAidAgainEvent patientGetFirstAidAgainEvent = new PatientGetFirstAidAgainEvent();
            for (Scope scope : scopes) {
                runtimeProperties.add(new RuntimePrevention(patientGetFirstAidEvent, patientGetFirstAidAgainEvent, scope));
            }

            RuntimeVerifier runtimeVerifier = new RuntimeVerifier(runtimeProperties, MAX_FRAME_COUNT);
            programStartTime = System.nanoTime();
            SoSSimulationProgram simulationEngine = new SoSSimulationProgram(MAX_FRAME_COUNT);


            simulationEngine.setRunning();
            simulationEngine.runtimeVerificationRun(runtimeVerifier);                         // 런타임 검증을 위한 run
            programEndTime = System.nanoTime();
            timeWithVerification.add(programEndTime - programStartTime);

            programStartTime = System.nanoTime();
            simulationEngine = new SoSSimulationProgram(MAX_FRAME_COUNT);


            simulationEngine.setRunning();
            simulationEngine.run();
            programEndTime = System.nanoTime();
            timeWithoutVerification.add(programEndTime - programStartTime);
        }


        // 실행 시간 출력을 위한 부분
        // 검증 포함 미포함 실행시간 출력
        System.out.println();
        System.out.println("=== OVERALL RESULT ===");

        long maxTimeWithVerification = 0;
        long maxTimeWithoutVerification = 0;
        long sumTimeWithVerification = 0;
        long sumTimeWithoutVerification = 0;

        for (int i = 0; i < max_round; i++) {
            maxTimeWithVerification = Math.max(maxTimeWithVerification, timeWithVerification.get(i));
            maxTimeWithoutVerification = Math.max(maxTimeWithoutVerification, timeWithoutVerification.get(i));
            sumTimeWithVerification += timeWithVerification.get(i);
            sumTimeWithoutVerification += timeWithoutVerification.get(i);
        }

        int indexNumber = String.valueOf(max_round).length();
        int lengthNumber = Math.max(10, String.valueOf((int) Math.max(maxTimeWithoutVerification / 1000_000_000, maxTimeWithVerification / 1000_000_000)).length() + 5);

        String title = "Simulation Running Time (s)";
        int target = title.length();
        for (int i = 0; i < indexNumber + lengthNumber * 2 + 6 - target; i++) {
            if (i % 2 == 0)
                title += " ";
            else
                title = " " + title;
        }
        System.out.println("| " + title + " |");

        String indexHeader = "#";
        target = indexHeader.length();
        for (int i = 0; i < indexNumber - target; i++) {
            if (i % 2 == 0)
                indexHeader += " ";
            else
                indexHeader = " " + indexHeader;
        }

        String RV = "With RV";
        target = RV.length();
        for (int i = 0; i < lengthNumber - target; i++) {
            if (i % 2 == 0)
                RV += " ";
            else
                RV = " " + RV;
        }

        String withoutRV = "Without RV";
        target = withoutRV.length();
        for (int i = 0; i < lengthNumber - target; i++) {
            if (i % 2 == 0)
                withoutRV += " ";
            else
                withoutRV = " " + withoutRV;
        }
        System.out.println("| " + indexHeader + " | " + RV + " | " + withoutRV + " |");

        for (int i = 0; i < max_round; i++) {
            System.out.print("| ");
            for (int j = 0; j < indexNumber - String.valueOf(i).length(); j++) {
                System.out.print(" ");
            }
            System.out.print(i + " | ");

            float value = timeWithVerification.get(i) / (float) 1000_000_000;
            for (int j = 0; j < lengthNumber - String.valueOf((int) value).length() - 5; j++) {
                System.out.print(" ");
            }
            System.out.printf("%,.4f", timeWithVerification.get(i) / (float) 1000_000_000);
            System.out.print(" | ");

            value = timeWithoutVerification.get(i) / (float) 1000_000_000;
            for (int j = 0; j < lengthNumber - String.valueOf((int) value).length() - 5; j++) {
                System.out.print(" ");
            }
            System.out.printf("%,.4f", timeWithoutVerification.get(i) / (float) 1000_000_000);
            System.out.print(" |\n");
        }

        System.out.print("=== Runtime Verification Overhead: ");
        System.out.printf("%,.4f", (sumTimeWithVerification - sumTimeWithoutVerification) / (float) sumTimeWithoutVerification * 100);
        System.out.println("% ===");

    }
}
