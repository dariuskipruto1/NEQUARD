package com.nequard.alerts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AlertRuleStateRepository extends JpaRepository<AlertRuleState,UUID>{
 Optional<AlertRuleState> findByRuleIdAndDeviceId(UUID ruleId,UUID deviceId);
}