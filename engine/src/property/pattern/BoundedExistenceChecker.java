package property.pattern;

import log.Log;
import log.Snapshot;
import property.Property;
import property.PropertyChecker;
import java.util.HashMap;

public abstract class BoundedExistenceChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot snapshot, Property verificationProperty);
    
    @Override
    public boolean check(Log log, Property verificationProperty) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
//        log.printSnapshot();

        // 문제가 하나 있는데, 한번만 체크함. 결과가 계속 true로 나오기 위해서는 다음과 같은 조건문을 사용해야함.
        // SPRT는 약간의 문제가 있음... 무조건 시뮬레이션을 한번 실행한다음에 진행하므로... 즉, 최소 샘플이 2개이므로. 로그 정보를 다 가지고 시작함...실시간 로그가 아니라
        if (snapshots.size() >= verificationProperty.getDuration()) {
            if (evaluateState(snapshots.get(verificationProperty.getDuration()), verificationProperty)) {
                return true;
            }
        }
        return false;
    }
}
