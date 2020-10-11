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
    public static void main(String [] args) {
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
        MCIProperty existenceProperty = new MCIProperty("[Existence Pattern] RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIExistence");
        existenceProperty.setRescueRate(0.02);
        MCIPropertyChecker existencePropertyChecker = new MCIPropertyChecker();
        properties.add(existenceProperty);
        properties.add(existenceProperty);
        properties.add(existenceProperty);
        properties.add(existenceProperty);
        properties.add(existenceProperty);
        properties.add(existenceProperty);


        // Absence
        // 설명: '시뮬레이션 종료 시점'까지, [Rescue rate보다 Treatment rate이 더 큰] 상태가 발생할 확률이 '기대 확률' 미만이다.
        MCIProperty absenceProperty = new MCIProperty("[Absence Pattern] TreatmentRateRescueRateProperty", "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceProperty.setThresholdValue(0); // RescueRate - TreatmentRate can not be minus
        MCIAbsenceChecker absencePropertyChecker = new MCIAbsenceChecker();
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);
        properties.add(absenceProperty);

        // Universality
        // 설명: '시뮬레이션 종료 시점'까지, [Rescue rate이 0% 이상 100% 이하]가 항상 만족된다.
        MCIProperty universalityproperty = new MCIProperty("[Universality Pattern] RescueRateProperty", "RescuedPatientRatioLowerThanValue", "MCIUniversality");
        universalityproperty.setThresholdValue(1.0); // RescueRate should be 100%?? condition is rescuerate <= threshold && rescuerate > 0
        MCIUniversalityChecker universalityPropertyChecker = new MCIUniversalityChecker();
        properties.add(universalityproperty);
        properties.add(universalityproperty);
        properties.add(universalityproperty);
        properties.add(universalityproperty);
        properties.add(universalityproperty);
        properties.add(universalityproperty);


        // TransientStateProbability
        // 설명: '시뮬레이션 종료 시점'까지, '지정 시간' 이후로 [구조된 환자의 수가 '문턱 환자 수'보다 큰] 상태가 지속될 확률이 '기대 확률' 이상이다.
        MCIProperty transientStateProbabilityproperty = new MCIProperty("[TransientStateProbability Pattern] ", "", "MCITransientSP");
        transientStateProbabilityproperty.setStateProbabilityValues(0.6, 60, 81);
        MCITransientSPChecker transientStateProbabilityPropertyChecker = new MCITransientSPChecker();
        properties.add(transientStateProbabilityproperty);
        properties.add(transientStateProbabilityproperty);
        properties.add(transientStateProbabilityproperty);
        properties.add(transientStateProbabilityproperty);
        properties.add(transientStateProbabilityproperty);
        properties.add(transientStateProbabilityproperty);

        // SteadyStateProbability
        // 설명: '시뮬레이션 종료 시점'까지, 긴 시간동안 [구조된 환자의 수가 '문턱 환자 수'보다 큰] 상태가 지속될 확률이 '기대 확률' 이상이다.
        MCIProperty steadyStateProbabilityproperty = new MCIProperty("[SteadyStateProbability Pattern] ", "", "MCISteadySP");
        steadyStateProbabilityproperty.setStateProbabilityValues(0.15, 0, 81);
        MCISteadySPChecker steadyStateProbabilityPropertyChecker = new MCISteadySPChecker();
        properties.add(steadyStateProbabilityproperty);
        properties.add(steadyStateProbabilityproperty);
        properties.add(steadyStateProbabilityproperty);
        properties.add(steadyStateProbabilityproperty);
        properties.add(steadyStateProbabilityproperty);
        properties.add(steadyStateProbabilityproperty);

        // MinimumDuration
        // 설명: '시뮬레이션 종료 시점'까지, [생성된 소방 요원들의 활동율이 문턱 활동율보다 큰] 상태가 최소 [일정 논리 시간] 이상 지속될 확률이 '기대 확률' 이상이다.
        MCIProperty minimumDurationProperty = new MCIProperty("[MinimumDuration Pattern] ", "", "MCIMinimumDuration");
        minimumDurationProperty.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
        minimumDurationProperty.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        MCIMinimumDurationChecker minimumDurationPropertyChecker = new MCIMinimumDurationChecker();
        properties.add(minimumDurationProperty);
        properties.add(minimumDurationProperty);
        properties.add(minimumDurationProperty);
        properties.add(minimumDurationProperty);
        properties.add(minimumDurationProperty);
        properties.add(minimumDurationProperty);

        // MaximumDuration
        // 설명: '시뮬레이션 종료 시점'까지, [남은 환자가 문턱 환자수 이하인] 상태가 최대 [일정 논리 시간]까지 지속될 확률이 '기대 확률' 이상이다.
        MCIProperty maximumDurationProperty = new MCIProperty("[MaximumDuration Pattern] ", "", "MCIMaximumDuration");
        maximumDurationProperty.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
        maximumDurationProperty.setDuration(60); // 최대 60 Frame 이하
        MCIMaximumDurationChecker maximumDurationPropertyChecker = new MCIMaximumDurationChecker();
        properties.add(maximumDurationProperty);
        properties.add(maximumDurationProperty);
        properties.add(maximumDurationProperty);
        properties.add(maximumDurationProperty);
        properties.add(maximumDurationProperty);
        properties.add(maximumDurationProperty);

        // BoundedExistence
        // 설명: '입력 시점 t'까지, [모든 Ambulance가 활동 상태가 되는 사건]이 만족될 확률이 '기대 확률' 이상이다.
        MCIProperty boundedExistenceProperty = new MCIProperty("[BoundedExistence Pattern] ", "", "MCIBoundedExistence");
        boundedExistenceProperty.setDuration(20); // Bounded Frame 20
        boundedExistenceProperty.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        MCIBoundedExistenceChecker boundedExistencePropertyChecker = new MCIBoundedExistenceChecker();
        properties.add(boundedExistenceProperty);
        properties.add(boundedExistenceProperty);
        properties.add(boundedExistenceProperty);
        properties.add(boundedExistenceProperty);
        properties.add(boundedExistenceProperty);
        properties.add(boundedExistenceProperty);

        // Precedence --> Precedence checker always returns false in MCIPrecedenceChecker?
        // 설명: '시뮬레이션 종료 시점'까지, [소방 요원이 Rescue Activity를 진행하는] 상태가 [소방요원이 Treatment Activity를 진행하는] 상태보다 선행될 확률이 '기대 확률' 이상이다.
        MCIProperty precedenceProperty = new MCIProperty("[Precedence Pattern] ", "", "MCIPrecedence");
        precedenceProperty.setPrevState("MoveToPatient");
        precedenceProperty.setState("FirstAid");
        MCIPrecedenceChecker precedencePropertyChecker = new MCIPrecedenceChecker();
        properties.add(precedenceProperty);

        // Response --> 근데 MCIResponseChecker 좀 바꿔야 할듯. 항상 true로 나오는 것 같음. 숫자 비교를 통해서 treatment의 값이 transfer 보다 작으면  true 인듯?
        // 설명: '시뮬레이션 종료 시점'까지, [소방요원이 Rescue Activity를 진행하는] 상태는 반드시 [소방요원이 Treatment Activity]상태 이후에 진행될 확률이 '기대 확률' 이상이다.
        MCIProperty responseProperty = new MCIProperty("[Response Pattern] ", "", "MCIResponse");
        responseProperty.setPrevState("FirstAid");
        responseProperty.setState("TransferToBridgehead");
        MCIResponseChecker responsePropertyChecker = new MCIResponseChecker();
        properties.add(responseProperty);
        properties.add(responseProperty);
        properties.add(responseProperty);
        properties.add(responseProperty);
        properties.add(responseProperty);
        properties.add(responseProperty);

        // Recurrence --> 근데 MCIRecurrenceChecker 좀 바꿔야 할듯. 이것도 처음부터 true가 나옴.
        // 설명: '시뮬레이션 종료 시점'까지, [모든 소방요원들이 일정 논리 시간 간격으로 환자 구조를 반복하는] 상태가 지속될 확률이 '기대 확률' 이상이다.
        MCIProperty recurrenceProperty = new MCIProperty("[Recurrence Pattern] ", "", "MCIRecurrence");
        recurrenceProperty.setPrevState("MoveToPatient");
        recurrenceProperty.setThresholdValue(51);
        MCIRecurrenceChecker recurrencePropertyChecker = new MCIRecurrenceChecker();
        properties.add(recurrenceProperty);
        properties.add(recurrenceProperty);
        properties.add(recurrenceProperty);
        properties.add(recurrenceProperty);
        properties.add(recurrenceProperty);
        properties.add(recurrenceProperty);

        // Until --> 좀 바꿔줘야할듯? -1 이 무슨 state 인 상태인걸까???
        // 설명: 'RescueRate = 1.00인 상태'까지, [모든 소방 요원들이 활동하는] 상태가 지속될 확률이 '기대 확률' 이상이다.
//        MCIProperty property = new MCIProperty("[Until Pattern] ", "", "MCIUntil");
//        property.setPrevState("Free");
//        MCIUntilChecker mciPropertyChecker = new MCIUntilChecker();
//        properties.add(property);

        // NumberOfEvents
//        MCIProperty property = new MCIProperty("[NumberOfEvents Pattern] RouteMsgCountProperty", "RouteMsgCountLTEQX", "MCINumberofEvents");
//        property.setThresholdValue(600);
//        MCINumberofEventsChecker mciPropertyChecker = new MCINumberofEventsChecker();
//        properties.add(property);

        // Prevention
//        MCIProperty property = new MCIProperty("[Prevention Pattern] ", "", "MCIPrevention");
//        property.setPrevState("TransferToBridgehead");
//        property.setThresholdValue(0);
//        MCIPreventionChecker mciPropertyChecker = new MCIPreventionChecker();
//        properties.add(property);


//        SPRT verifier;
//        verifier = new SPRT(mciPropertyChecker);
        RuntimeVerification existenceVerifier;
        RuntimeVerification absenceVerifier;
        RuntimeVerification universalityVerifier;
        RuntimeVerification transientStateProbabilityVerifier;
        RuntimeVerification steadyStateProbabilityVerifier;
        RuntimeVerification minimumDurationVerifier;
        RuntimeVerification maximumDurationVerifier;
        RuntimeVerification boundedExistenceVerifier;
        RuntimeVerification precedenceVerifier;
        RuntimeVerification responseVerifier;
        RuntimeVerification recurrenceVerifier;






/*
        // Existence Scopes Existence Scopes Existence Scopes Existence Scopes Existence Scopes Existence Scopes Existence Scopes Existence Scopes
        // Globally
        existenceVerifier = new RuntimeVerification(existencePropertyChecker);
        runtimeVerifiers.add(existenceVerifier);


        // Before
        MCIProperty existenceBeforeEvent = new MCIProperty("RescuePatientProperty",
                "RescuedPatientRatioUpperThanValue", "MCIExistence");
        existenceBeforeEvent.setRescueRate(0.02);
        MCIPropertyChecker existenceBeforePropertyChecker = new MCIPropertyChecker();
        existenceVerifier = new RuntimeVerification(existencePropertyChecker,
                "Before", existenceBeforeEvent, existenceBeforePropertyChecker);
        runtimeVerifiers.add(existenceVerifier);


        // After
        MCIProperty existenceAfterEvent = new MCIProperty("RescuePatientProperty",
                "RescuedPatientRatioUpperThanValue", "MCIExistence");
        existenceAfterEvent.setRescueRate(0.02);
        MCIPropertyChecker existenceAfterPropertyChecker = new MCIPropertyChecker();
        existenceVerifier = new RuntimeVerification(existencePropertyChecker,
                "After", existenceAfterEvent, existenceAfterPropertyChecker);
        runtimeVerifiers.add(existenceVerifier);

        // Between
        MCIProperty existenceBetweenEvent1 = new MCIProperty("[Previous event] RescuePatientProperty",
                "RescuedPatientRatioUpperThanValue", "MCIExistence");
        existenceBetweenEvent1.setRescueRate(0.02);
        MCIPropertyChecker existenceEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty exitenceBetweenEvent2 = new MCIProperty("[Next event] RescuePatientProperty",
                "RescuedPatientRatioUpperThanValue", "MCIExistence");
        exitenceBetweenEvent2.setRescueRate(0.50);
        MCIPropertyChecker existenceEvent2PropertyChecker = new MCIPropertyChecker();
        existenceVerifier = new RuntimeVerification(existencePropertyChecker, "Between", existenceBetweenEvent1,
                existenceEvent1PropertyChecker, exitenceBetweenEvent2, existenceEvent2PropertyChecker);
        runtimeVerifiers.add(existenceVerifier);

        // Interval
        existenceVerifier = new RuntimeVerification(existencePropertyChecker, "Interval", 100, 300);
        runtimeVerifiers.add(existenceVerifier);

        // Existence
        MCIProperty existenceExistenceEvent = new MCIProperty("RescuePatientProperty",
                "RescuedPatientRatioUpperThanValue", "MCIExistence");
        existenceExistenceEvent.setRescueRate(0.02);
        MCIPropertyChecker existenceExistencePropertyChecker = new MCIPropertyChecker();
        existenceVerifier = new RuntimeVerification(existencePropertyChecker, "Existence",
                existenceExistenceEvent, existenceExistencePropertyChecker);
        runtimeVerifiers.add(existenceVerifier);








        // Absence Scopes Absence Scopes Absence Scopes Absence Scopes Absence Scopes Absence Scopes Absence Scopes Absence Scopes Absence Scopes Absence Scopes
        // Globally
        absenceVerifier = new RuntimeVerification(absencePropertyChecker);
        runtimeVerifiers.add(absenceVerifier);


        // Before
        MCIProperty absenceBeforeEvent = new MCIProperty("[Absence Pattern] TreatmentRateRescueRateProperty",
                "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceBeforeEvent.setThresholdValue(0);
        MCIPropertyChecker absenceBeforePropertyChecker = new MCIPropertyChecker();
        absenceVerifier = new RuntimeVerification(absencePropertyChecker,
                "Before", absenceBeforeEvent, absenceBeforePropertyChecker);
        runtimeVerifiers.add(absenceVerifier);


        // After
        MCIProperty absenceAfterEvent = new MCIProperty("[Absence Pattern] TreatmentRateRescueRateProperty",
                "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceAfterEvent.setThresholdValue(0);
        MCIPropertyChecker absenceAfterPropertyChecker = new MCIPropertyChecker();
        absenceVerifier = new RuntimeVerification(absencePropertyChecker,
                "After", absenceAfterEvent, absenceAfterPropertyChecker);
        runtimeVerifiers.add(absenceVerifier);

        // Between
        MCIProperty absenceBetweenEvent1 = new MCIProperty("[Absence Pattern] TreatmentRateRescueRateProperty",
                "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceBetweenEvent1.setThresholdValue(0.02);
        MCIPropertyChecker absenceEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty absenceBetweenEvent2 = new MCIProperty("[Absence Pattern] TreatmentRateRescueRateProperty",
                "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceBetweenEvent2.setThresholdValue(0.05);
        MCIPropertyChecker absenceEvent2PropertyChecker = new MCIPropertyChecker();
        absenceVerifier = new RuntimeVerification(absencePropertyChecker, "Between", absenceBetweenEvent1,
                absenceEvent1PropertyChecker, absenceBetweenEvent2, absenceEvent2PropertyChecker);
        runtimeVerifiers.add(absenceVerifier);

        // Interval
        absenceVerifier = new RuntimeVerification(absencePropertyChecker, "Interval", 100, 300);
        runtimeVerifiers.add(absenceVerifier);

        // Existence
        MCIProperty absenceExistenceEvent = new MCIProperty("[Absence Pattern] TreatmentRateRescueRateProperty",
                "TreatmentRateMinusRescueRateUpperThanValue", "MCIAbsence");
        absenceExistenceEvent.setThresholdValue(0);
        MCIPropertyChecker absenceExistencePropertyChecker = new MCIPropertyChecker();
        absenceVerifier = new RuntimeVerification(absencePropertyChecker, "Existence", absenceExistenceEvent,
                absenceExistencePropertyChecker);
        runtimeVerifiers.add(absenceVerifier);









        // Universality Scopes Universality Scopes Universality Scopes Universality Scopes Universality Scopes Universality Scopes Universality Scopes
        // Globally
        universalityVerifier = new RuntimeVerification(universalityPropertyChecker);
        runtimeVerifiers.add(universalityVerifier);


        // Before
        MCIProperty universalityBeforeEvent = new MCIProperty("[Universality Pattern] RescueRateProperty",
                "RescuedPatientRatioLowerThanValue", "MCIUniversality");
        universalityBeforeEvent.setThresholdValue(1.0);
        MCIPropertyChecker universalityBeforePropertyChecker = new MCIPropertyChecker();
        universalityVerifier = new RuntimeVerification(universalityPropertyChecker,
                "Before", universalityBeforeEvent, universalityBeforePropertyChecker);
        runtimeVerifiers.add(universalityVerifier);


        // After
        MCIProperty universalityAfterEvent = new MCIProperty("[Universality Pattern] RescueRateProperty",
                "RescuedPatientRatioLowerThanValue", "MCIUniversality");
        universalityAfterEvent.setThresholdValue(1.0);
        MCIPropertyChecker universalityAfterPropertyChecker = new MCIPropertyChecker();
        universalityVerifier = new RuntimeVerification(universalityPropertyChecker,
                "After", universalityAfterEvent, universalityAfterPropertyChecker);
        runtimeVerifiers.add(universalityVerifier);

        // Between
        MCIProperty universalityBetweenEvent1 = new MCIProperty("[Universality Pattern] RescueRateProperty",
                "RescuedPatientRatioLowerThanValue", "MCIUniversality");
        universalityBetweenEvent1.setThresholdValue(0.02);
        MCIPropertyChecker universalityEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty universalityBetweenEvent2 = new MCIProperty("[Universality Pattern] RescueRateProperty",
                "RescuedPatientRatioLowerThanValue", "MCIUniversality");
        universalityBetweenEvent2.setThresholdValue(0.05);
        MCIPropertyChecker universalityEvent2PropertyChecker = new MCIPropertyChecker();
        universalityVerifier = new RuntimeVerification(universalityPropertyChecker,
                "Between", universalityBetweenEvent1, universalityEvent1PropertyChecker,
                universalityBetweenEvent2, universalityEvent2PropertyChecker);
        runtimeVerifiers.add(universalityVerifier);

        // Interval
        universalityVerifier = new RuntimeVerification(universalityPropertyChecker, "Interval", 100, 300);
        runtimeVerifiers.add(universalityVerifier);

        // Existence
        MCIProperty universalityExistenceEvent = new MCIProperty("[Universality Pattern] RescueRateProperty",
                "RescuedPatientRatioLowerThanValue", "MCIUniversality");
        universalityExistenceEvent.setThresholdValue(1.0);
        MCIPropertyChecker universalityExistencePropertyChecker = new MCIPropertyChecker();
        universalityVerifier = new RuntimeVerification(universalityPropertyChecker, "Existence",
                universalityExistenceEvent, universalityExistencePropertyChecker);
        runtimeVerifiers.add(universalityVerifier);








        // TransientStateProbability Scopes TransientStateProbability Scopes TransientStateProbability Scopes TransientStateProbability Scopes
        // Globally
        transientStateProbabilityVerifier = new RuntimeVerification(transientStateProbabilityPropertyChecker);
        runtimeVerifiers.add(transientStateProbabilityVerifier);


        // Before
        MCIProperty transientStateProbabilityBeforeEvent = new MCIProperty("[TransientStateProbability Pattern] ",
                "", "MCITransientSP");
        transientStateProbabilityBeforeEvent.setStateProbabilityValues(0.6, 60, 81);
        MCIPropertyChecker transientStateProbabilityBeforePropertyChecker = new MCIPropertyChecker();
        transientStateProbabilityVerifier = new RuntimeVerification(transientStateProbabilityPropertyChecker,
                "Before", transientStateProbabilityBeforeEvent, transientStateProbabilityBeforePropertyChecker);
        runtimeVerifiers.add(transientStateProbabilityVerifier);


        // After
        MCIProperty transientStateProbabilityAfterEvent = new MCIProperty("[TransientStateProbability Pattern] ",
                "", "MCITransientSP");
        transientStateProbabilityAfterEvent.setStateProbabilityValues(0.6, 60, 81);
        MCIPropertyChecker transientStateProbabilityAfterPropertyChecker = new MCIPropertyChecker();
        transientStateProbabilityVerifier = new RuntimeVerification(transientStateProbabilityPropertyChecker,
                "After", transientStateProbabilityAfterEvent, transientStateProbabilityAfterPropertyChecker);
        runtimeVerifiers.add(transientStateProbabilityVerifier);

        // Between
        MCIProperty transientStateProbabilityBetweenEvent1 = new MCIProperty("[TransientStateProbability Pattern] ",
                "", "MCITransientSP");
        transientStateProbabilityBetweenEvent1.setStateProbabilityValues(0.6, 60, 81);
        MCIPropertyChecker transientStateProbabilityEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty transientStateProbabilityBetweenEvent2 = new MCIProperty("[TransientStateProbability Pattern] ",
                "", "MCITransientSP");
        transientStateProbabilityBetweenEvent2.setStateProbabilityValues(0.6, 60, 81);
        MCIPropertyChecker transientStateProbabilityEvent2PropertyChecker = new MCIPropertyChecker();
        transientStateProbabilityVerifier = new RuntimeVerification(transientStateProbabilityPropertyChecker,
                "Between", transientStateProbabilityBetweenEvent1, transientStateProbabilityEvent1PropertyChecker,
                transientStateProbabilityBetweenEvent2, transientStateProbabilityEvent2PropertyChecker);
        runtimeVerifiers.add(transientStateProbabilityVerifier);

        // Interval
        transientStateProbabilityVerifier = new RuntimeVerification(transientStateProbabilityPropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(transientStateProbabilityVerifier);

        // Existence
        MCIProperty transientStateProbabilityExistenceEvent = new MCIProperty("[TransientStateProbability Pattern] ",
                "", "MCITransientSP");
        transientStateProbabilityExistenceEvent.setStateProbabilityValues(0.6, 60, 81);
        MCIPropertyChecker transientStateProbabilityExistencePropertyChecker = new MCIPropertyChecker();
        transientStateProbabilityVerifier = new RuntimeVerification(transientStateProbabilityPropertyChecker,
                "Existence", transientStateProbabilityExistenceEvent, transientStateProbabilityExistencePropertyChecker);
        runtimeVerifiers.add(transientStateProbabilityVerifier);







        // SteadyStateProbability Scopes SteadyStateProbability Scopes SteadyStateProbability Scopes SteadyStateProbability Scopes
        // Globally
        steadyStateProbabilityVerifier = new RuntimeVerification(steadyStateProbabilityPropertyChecker);
        runtimeVerifiers.add(steadyStateProbabilityVerifier);


        // Before
        MCIProperty steadyStateProbabilityBeforeEvent = new MCIProperty("[SteadyStateProbability Pattern] ",
                "", "MCISteadySP");
        steadyStateProbabilityBeforeEvent.setStateProbabilityValues(0.15, 0, 81);
        MCIPropertyChecker steadyStateProbabilityBeforePropertyChecker = new MCIPropertyChecker();
        steadyStateProbabilityVerifier = new RuntimeVerification(steadyStateProbabilityPropertyChecker,
                "Before", steadyStateProbabilityBeforeEvent, steadyStateProbabilityBeforePropertyChecker);
        runtimeVerifiers.add(steadyStateProbabilityVerifier);


        // After
        MCIProperty steadyStateProbabilityAfterEvent = new MCIProperty("[SteadyStateProbability Pattern] ",
                "", "MCISteadySP");
        steadyStateProbabilityAfterEvent.setStateProbabilityValues(0.15, 0, 81);
        MCIPropertyChecker steadyStateProbabilityAfterPropertyChecker = new MCIPropertyChecker();
        steadyStateProbabilityVerifier = new RuntimeVerification(steadyStateProbabilityPropertyChecker,
                "After", steadyStateProbabilityAfterEvent, steadyStateProbabilityAfterPropertyChecker);
        runtimeVerifiers.add(steadyStateProbabilityVerifier);

        // Between
        MCIProperty steadyStateProbabilityBetweenEvent1 = new MCIProperty("[SteadyStateProbability Pattern] ",
                "", "MCISteadySP");
        steadyStateProbabilityBetweenEvent1.setStateProbabilityValues(0.15, 0, 81);
        MCIPropertyChecker steadyStateProbabilityEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty steadyStateProbabilityBetweenEvent2 = new MCIProperty("[SteadyStateProbability Pattern] ",
                "", "MCISteadySP");
        steadyStateProbabilityBetweenEvent2.setStateProbabilityValues(0.15, 0, 81);
        MCIPropertyChecker steadyStateProbabilityEvent2PropertyChecker = new MCIPropertyChecker();
        steadyStateProbabilityVerifier = new RuntimeVerification(steadyStateProbabilityPropertyChecker,
                "Between", steadyStateProbabilityBetweenEvent1, steadyStateProbabilityEvent1PropertyChecker,
                steadyStateProbabilityBetweenEvent2, steadyStateProbabilityEvent2PropertyChecker);
        runtimeVerifiers.add(steadyStateProbabilityVerifier);

        // Interval
        steadyStateProbabilityVerifier = new RuntimeVerification(steadyStateProbabilityPropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(steadyStateProbabilityVerifier);

        // Existence
        MCIProperty steadyStateProbabilityExistenceEvent = new MCIProperty("[SteadyStateProbability Pattern] ",
                "", "MCISteadySP");
        steadyStateProbabilityExistenceEvent.setStateProbabilityValues(0.15, 0, 81);
        MCIPropertyChecker steadyStateProbabilityExistencePropertyChecker = new MCIPropertyChecker();
        steadyStateProbabilityVerifier = new RuntimeVerification(steadyStateProbabilityPropertyChecker,
                "Existence", steadyStateProbabilityExistenceEvent, steadyStateProbabilityExistencePropertyChecker);
        runtimeVerifiers.add(steadyStateProbabilityVerifier);







        // MinimumDuration Scopes MinimumDuration Scopes MinimumDuration Scopes MinimumDuration Scopes MinimumDuration Scopes
        // Globally
        minimumDurationVerifier = new RuntimeVerification(minimumDurationPropertyChecker);
        runtimeVerifiers.add(minimumDurationVerifier);


        // Before
        MCIProperty minimumDurationBeforeEvent = new MCIProperty("[MinimumDuration Pattern] ", "", "MCIMinimumDuration");
        minimumDurationBeforeEvent.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
        minimumDurationBeforeEvent.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        MCIPropertyChecker minimumDurationBeforePropertyChecker = new MCIPropertyChecker();
        minimumDurationVerifier = new RuntimeVerification(minimumDurationPropertyChecker,
                "Before", minimumDurationBeforeEvent, minimumDurationBeforePropertyChecker);
        runtimeVerifiers.add(minimumDurationVerifier);


        // After
        MCIProperty minimumDurationAfterEvent = new MCIProperty("[MinimumDuration Pattern] ", "", "MCIMinimumDuration");
        minimumDurationAfterEvent.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
        minimumDurationAfterEvent.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        MCIPropertyChecker minimumDurationAfterPropertyChecker = new MCIPropertyChecker();
        minimumDurationVerifier = new RuntimeVerification(minimumDurationPropertyChecker,
                "After", minimumDurationAfterEvent, minimumDurationAfterPropertyChecker);
        runtimeVerifiers.add(minimumDurationVerifier);

        // Between
        MCIProperty minimumDurationBetweenEvent1 = new MCIProperty("[MinimumDuration Pattern] ", "", "MCIMinimumDuration");
        minimumDurationBetweenEvent1.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
        minimumDurationBetweenEvent1.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        MCIPropertyChecker minimumDurationEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty minimumDurationBetweenEvent2 = new MCIProperty("[MinimumDuration Pattern] ", "", "MCIMinimumDuration");
        minimumDurationBetweenEvent2.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
        minimumDurationBetweenEvent2.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        MCIPropertyChecker minimumDurationEvent2PropertyChecker = new MCIPropertyChecker();
        minimumDurationVerifier = new RuntimeVerification(minimumDurationPropertyChecker,
                "Between", minimumDurationBetweenEvent1, minimumDurationEvent1PropertyChecker,
                minimumDurationBetweenEvent2, minimumDurationEvent2PropertyChecker);
        runtimeVerifiers.add(minimumDurationVerifier);

        // Interval
        minimumDurationVerifier = new RuntimeVerification(minimumDurationPropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(minimumDurationVerifier);

        // Existence
        MCIProperty minimumDurationExistenceEvent = new MCIProperty("[MinimumDuration Pattern] ", "", "MCIMinimumDuration");
        minimumDurationExistenceEvent.setThresholdValue(1); // FF가 10명 이상 활동하고 있어야 한다. More than 10 FF active -seems like value of 5 and above always results in false?
        minimumDurationExistenceEvent.setDuration(65); // 최소 65 Frame 이상   at least 65 frame
        MCIPropertyChecker minimumDurationExistenceEventChecker = new MCIPropertyChecker();
        minimumDurationVerifier = new RuntimeVerification(minimumDurationPropertyChecker,
                "Existence", minimumDurationExistenceEvent, minimumDurationExistenceEventChecker);
        runtimeVerifiers.add(minimumDurationVerifier);






        // MaximumDuration Scopes MaximumDuration Scopes MaximumDuration Scopes MaximumDuration Scopes MaximumDuration Scopes
        // Globally
        maximumDurationVerifier = new RuntimeVerification(maximumDurationPropertyChecker);
        runtimeVerifiers.add(maximumDurationVerifier);


        //before
        MCIProperty maximumDurationBeforeEvent = new MCIProperty("[MaximumDuration Pattern] ", "", "MCIMaximumDuration");
        maximumDurationProperty.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
        maximumDurationProperty.setDuration(60); // 최대 60 Frame 이하
        MCIPropertyChecker maximumDurationBeforePropertyChecker = new MCIPropertyChecker();
        maximumDurationVerifier = new RuntimeVerification(maximumDurationPropertyChecker,
                "Before", maximumDurationBeforeEvent, maximumDurationBeforePropertyChecker);
        runtimeVerifiers.add(maximumDurationVerifier);


        // After
        MCIProperty maximumDurationAfterEvent = new MCIProperty("[MaximumDuration Pattern] ", "", "MCIMaximumDuration");
        maximumDurationAfterEvent.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
        maximumDurationAfterEvent.setDuration(60); // 최대 60 Frame 이하
        MCIPropertyChecker maximumDurationAfterPropertyChecker = new MCIPropertyChecker();
        maximumDurationVerifier = new RuntimeVerification(maximumDurationPropertyChecker,
                "After", maximumDurationAfterEvent, maximumDurationAfterPropertyChecker);
        runtimeVerifiers.add(maximumDurationVerifier);

        // Between
        MCIProperty maximumDurationBetweenEvent1 = new MCIProperty("[MaximumDuration Pattern] ", "", "MCIMaximumDuration");
        maximumDurationBetweenEvent1.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
        maximumDurationBetweenEvent1.setDuration(60); // 최대 60 Frame 이하
        MCIPropertyChecker maximumDurationEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty maximumDurationBetweenEvent2 = new MCIProperty("[MaximumDuration Pattern] ", "", "MCIMaximumDuration");
        maximumDurationBetweenEvent2.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
        maximumDurationBetweenEvent2.setDuration(60); // 최대 60 Frame 이하
        MCIPropertyChecker maximumDurationEvent2PropertyChecker = new MCIPropertyChecker();
        maximumDurationVerifier = new RuntimeVerification(maximumDurationPropertyChecker,
                "Between", maximumDurationBetweenEvent1, maximumDurationEvent1PropertyChecker,
                maximumDurationBetweenEvent2, maximumDurationEvent2PropertyChecker);
        runtimeVerifiers.add(maximumDurationVerifier);

        // Interval
        maximumDurationVerifier = new RuntimeVerification(maximumDurationPropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(maximumDurationVerifier);

        // Existence
        MCIProperty maximumDurationExistenceEvent = new MCIProperty("[MaximumDuration Pattern] ", "", "MCIMaximumDuration");
        maximumDurationExistenceEvent.setThresholdValue(0); // rescueRate == verificationProperty.getThresholdValue() == 0? is this correct?
        maximumDurationExistenceEvent.setDuration(60); // 최대 60 Frame 이하
        MCIPropertyChecker maximumDurationExistenceEventChecker = new MCIPropertyChecker();
        maximumDurationVerifier = new RuntimeVerification(maximumDurationPropertyChecker,
                "Existence", maximumDurationExistenceEvent, maximumDurationExistenceEventChecker);
        runtimeVerifiers.add(maximumDurationVerifier);








        // BoundedExistence Scopes BoundedExistence Scopes BoundedExistence Scopes BoundedExistence Scopes BoundedExistence Scopes
        // Globally
        boundedExistenceVerifier = new RuntimeVerification(boundedExistencePropertyChecker);
        runtimeVerifiers.add(boundedExistenceVerifier);


        //before
        MCIProperty boundedExistenceBeforeEvent = new MCIProperty("[BoundedExistence Pattern] ", "", "MCIBoundedExistence");
        boundedExistenceBeforeEvent.setDuration(20); // Bounded Frame 20
        boundedExistenceBeforeEvent.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        MCIPropertyChecker boundedExistenceBeforePropertyChecker = new MCIPropertyChecker();
        boundedExistenceVerifier = new RuntimeVerification(boundedExistencePropertyChecker,
                "Before", boundedExistenceBeforeEvent, boundedExistenceBeforePropertyChecker);
        runtimeVerifiers.add(boundedExistenceVerifier);


        // After
        MCIProperty boundedExistenceAfterEvent = new MCIProperty("[BoundedExistence Pattern] ", "", "MCIBoundedExistence");
        boundedExistenceAfterEvent.setDuration(20); // Bounded Frame 20
        boundedExistenceAfterEvent.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        MCIPropertyChecker boundedExistenceAfterPropertyChecker = new MCIPropertyChecker();
        boundedExistenceVerifier = new RuntimeVerification(boundedExistencePropertyChecker,
                "After", boundedExistenceAfterEvent, boundedExistenceAfterPropertyChecker);
        runtimeVerifiers.add(boundedExistenceVerifier);

        // Between
        MCIProperty boundedExistenceBetweenEvent1 = new MCIProperty("[BoundedExistence Pattern] ", "", "MCIBoundedExistence");
        boundedExistenceBetweenEvent1.setDuration(20); // Bounded Frame 20
        boundedExistenceBetweenEvent1.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        MCIPropertyChecker boundedExistenceEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty boundedExistenceBetweenEvent2 = new MCIProperty("[BoundedExistence Pattern] ", "", "MCIBoundedExistence");
        boundedExistenceBetweenEvent2.setDuration(20); // Bounded Frame 20
        boundedExistenceBetweenEvent2.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        MCIPropertyChecker boundedExistenceEvent2PropertyChecker = new MCIPropertyChecker();
        boundedExistenceVerifier = new RuntimeVerification(boundedExistencePropertyChecker,
                "Between", boundedExistenceBetweenEvent1, boundedExistenceEvent1PropertyChecker,
                boundedExistenceBetweenEvent2, boundedExistenceEvent2PropertyChecker);
        runtimeVerifiers.add(boundedExistenceVerifier);

        // Interval
        boundedExistenceVerifier = new RuntimeVerification(boundedExistencePropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(boundedExistenceVerifier);

        // Existence
        MCIProperty boundedExistenceExistenceEvent = new MCIProperty("[BoundedExistence Pattern] ", "", "MCIBoundedExistence");
        boundedExistenceExistenceEvent.setDuration(20); // Bounded Frame 20
        boundedExistenceExistenceEvent.setState("Free"); // Ambulance's State가 Free인게 아님을 확인하기 위해
        MCIPropertyChecker boundedExistenceExistenceEventChecker = new MCIPropertyChecker();
        boundedExistenceVerifier = new RuntimeVerification(boundedExistencePropertyChecker,
                "Existence", boundedExistenceExistenceEvent, boundedExistenceExistenceEventChecker);
        runtimeVerifiers.add(boundedExistenceVerifier);






        // Precedence Scopes Precedence Scopes Precedence Scopes Precedence Scopes Precedence Scopes Precedence Scopes Precedence Scopes
        // Globally
        precedenceVerifier = new RuntimeVerification(precedencePropertyChecker);
        runtimeVerifiers.add(precedenceVerifier);


        //before
        MCIProperty precedenceBeforeEvent = new MCIProperty("[Precedence Pattern] ", "", "MCIPrecedence");
        precedenceBeforeEvent.setPrevState("MoveToPatient");
        precedenceBeforeEvent.setState("FirstAid");
        MCIPropertyChecker precedenceBeforePropertyChecker = new MCIPropertyChecker();
        precedenceVerifier = new RuntimeVerification(precedencePropertyChecker,
                "Before", precedenceBeforeEvent, precedenceBeforePropertyChecker);
        runtimeVerifiers.add(precedenceVerifier);


        // After
        MCIProperty precedenceAfterEvent = new MCIProperty("[Precedence Pattern] ", "", "MCIPrecedence");
        precedenceAfterEvent.setPrevState("MoveToPatient");
        precedenceAfterEvent.setState("FirstAid");
        MCIPropertyChecker precedenceAfterPropertyChecker = new MCIPropertyChecker();
        precedenceVerifier = new RuntimeVerification(precedencePropertyChecker,
                "After", precedenceAfterEvent, precedenceAfterPropertyChecker);
        runtimeVerifiers.add(precedenceVerifier);

        // Between
        MCIProperty precedenceBetweenEvent1 = new MCIProperty("[Precedence Pattern] ", "", "MCIPrecedence");
        precedenceBetweenEvent1.setPrevState("MoveToPatient");
        precedenceBetweenEvent1.setState("FirstAid");
        MCIPropertyChecker precedenceEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty precedenceBetweenEvent2 = new MCIProperty("[Precedence Pattern] ", "", "MCIPrecedence");
        precedenceBetweenEvent2.setPrevState("MoveToPatient");
        precedenceBetweenEvent2.setState("FirstAid");
        MCIPropertyChecker precedenceEvent2PropertyChecker = new MCIPropertyChecker();
        precedenceVerifier = new RuntimeVerification(precedencePropertyChecker,
                "Between", precedenceBetweenEvent1, precedenceEvent1PropertyChecker,
                precedenceBetweenEvent2, precedenceEvent2PropertyChecker);
        runtimeVerifiers.add(precedenceVerifier);

        // Interval
        precedenceVerifier = new RuntimeVerification(precedencePropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(precedenceVerifier);

        // Existence
        MCIProperty precedenceExistenceEvent = new MCIProperty("[Precedence Pattern] ", "", "MCIPrecedence");
        precedenceExistenceEvent.setPrevState("MoveToPatient");
        precedenceExistenceEvent.setState("FirstAid");
        MCIPropertyChecker precedenceExistenceEventChecker = new MCIPropertyChecker();
        precedenceVerifier = new RuntimeVerification(precedencePropertyChecker,
                "Existence", precedenceExistenceEvent, precedenceExistenceEventChecker);
        runtimeVerifiers.add(precedenceVerifier);




*/



        // Response Scopes Response Scopes Response Scopes Response Scopes Response Scopes Response Scopes Response Scopes
        // Globally
        responseVerifier = new RuntimeVerification(responsePropertyChecker);
        runtimeVerifiers.add(responseVerifier);


        //before
        MCIProperty responseBeforeEvent = new MCIProperty("[Response Pattern] ", "", "MCIResponse");
        responseBeforeEvent.setPrevState("FirstAid");
        responseBeforeEvent.setState("TransferToBridgehead");
        MCIPropertyChecker responseBeforePropertyChecker = new MCIPropertyChecker();
        responseVerifier = new RuntimeVerification(responsePropertyChecker,
                "Before", responseBeforeEvent, responseBeforePropertyChecker);
        runtimeVerifiers.add(responseVerifier);


        // After
        MCIProperty responseAfterEvent = new MCIProperty("[Response Pattern] ", "", "MCIResponse");
        responseAfterEvent.setPrevState("FirstAid");
        responseAfterEvent.setState("TransferToBridgehead");
        MCIPropertyChecker responseAfterPropertyChecker = new MCIPropertyChecker();
        responseVerifier = new RuntimeVerification(responsePropertyChecker,
                "After", responseAfterEvent, responseAfterPropertyChecker);
        runtimeVerifiers.add(responseVerifier);

        // Between
        MCIProperty responseBetweenEvent1 = new MCIProperty("[Response Pattern] ", "", "MCIResponse");
        responseBetweenEvent1.setPrevState("FirstAid");
        responseBetweenEvent1.setState("TransferToBridgehead");
        MCIPropertyChecker responseEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty responseBetweenEvent2 = new MCIProperty("[Response Pattern] ", "", "MCIResponse");
        responseBetweenEvent2.setPrevState("FirstAid");
        responseBetweenEvent2.setState("TransferToBridgehead");
        MCIPropertyChecker responseEvent2PropertyChecker = new MCIPropertyChecker();
        responseVerifier = new RuntimeVerification(responsePropertyChecker,
                "Between", responseBetweenEvent1, responseEvent1PropertyChecker,
                responseBetweenEvent2, responseEvent2PropertyChecker);
        runtimeVerifiers.add(responseVerifier);

        // Interval
        responseVerifier = new RuntimeVerification(responsePropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(responseVerifier);

        // Existence
        MCIProperty responseExistenceEvent = new MCIProperty("[Response Pattern] ", "", "MCIResponse");
        responseExistenceEvent.setPrevState("FirstAid");
        responseExistenceEvent.setState("TransferToBridgehead");
        MCIPropertyChecker responseExistenceEventChecker = new MCIPropertyChecker();
        responseVerifier = new RuntimeVerification(responsePropertyChecker,
                "Existence", responseExistenceEvent, responseExistenceEventChecker);
        runtimeVerifiers.add(responseVerifier);








        // Recurrence Scopes Recurrence Scopes Recurrence Scopes Recurrence Scopes Recurrence Scopes Recurrence Scopes Recurrence Scopes
        // Globally
        recurrenceVerifier = new RuntimeVerification(recurrencePropertyChecker);
        runtimeVerifiers.add(recurrenceVerifier);


        //before
        MCIProperty recurrenceBeforeEvent = new MCIProperty("[Recurrence Pattern] ", "", "MCIRecurrence");
        recurrenceBeforeEvent.setPrevState("MoveToPatient");
        recurrenceBeforeEvent.setThresholdValue(51);
        MCIPropertyChecker recurrenceBeforePropertyChecker = new MCIPropertyChecker();
        recurrenceVerifier = new RuntimeVerification(recurrencePropertyChecker,
                "Before", recurrenceBeforeEvent, recurrenceBeforePropertyChecker);
        runtimeVerifiers.add(recurrenceVerifier);


        // After
        MCIProperty recurrenceAfterEvent = new MCIProperty("[Recurrence Pattern] ", "", "MCIRecurrence");
        recurrenceAfterEvent.setPrevState("MoveToPatient");
        recurrenceAfterEvent.setThresholdValue(51);
        MCIPropertyChecker recurrenceAfterPropertyChecker = new MCIPropertyChecker();
        recurrenceVerifier = new RuntimeVerification(recurrencePropertyChecker,
                "After", recurrenceAfterEvent, recurrenceAfterPropertyChecker);
        runtimeVerifiers.add(recurrenceVerifier);

        // Between
        MCIProperty recurrenceBetweenEvent1 = new MCIProperty("[Recurrence Pattern] ", "", "MCIRecurrence");
        recurrenceBetweenEvent1.setPrevState("MoveToPatient");
        recurrenceBetweenEvent1.setThresholdValue(51);
        MCIPropertyChecker recurrenceEvent1PropertyChecker = new MCIPropertyChecker();
        MCIProperty recurrenceBetweenEvent2 = new MCIProperty("[Recurrence Pattern] ", "", "MCIRecurrence");
        recurrenceBetweenEvent2.setPrevState("MoveToPatient");
        recurrenceBetweenEvent2.setThresholdValue(51);
        MCIPropertyChecker recurrenceEvent2PropertyChecker = new MCIPropertyChecker();
        recurrenceVerifier = new RuntimeVerification(recurrencePropertyChecker,
                "Between", recurrenceBetweenEvent1, recurrenceEvent1PropertyChecker,
                recurrenceBetweenEvent2, recurrenceEvent2PropertyChecker);
        runtimeVerifiers.add(recurrenceVerifier);

        // Interval
        recurrenceVerifier = new RuntimeVerification(recurrencePropertyChecker,
                "Interval", 100, 300);
        runtimeVerifiers.add(recurrenceVerifier);

        // Existence
        MCIProperty recurrenceExistenceEvent = new MCIProperty("[Recurrence Pattern] ", "", "MCIRecurrence");
        recurrenceExistenceEvent.setPrevState("MoveToPatient");
        recurrenceExistenceEvent.setThresholdValue(51);
        MCIPropertyChecker recurrenceExistenceEventChecker = new MCIPropertyChecker();
        recurrenceVerifier = new RuntimeVerification(recurrencePropertyChecker,
                "Existence", recurrenceExistenceEvent, recurrenceExistenceEventChecker);
        runtimeVerifiers.add(recurrenceVerifier);



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
