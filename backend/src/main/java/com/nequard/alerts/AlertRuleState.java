package com.nequard.alerts;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="alert_rule_states", uniqueConstraints=@UniqueConstraint(columnNames={"rule_id","device_id"}))
public class AlertRuleState {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @Column(nullable=false) private UUID ruleId;
 @Column(nullable=false) private UUID deviceId;
 @Column(nullable=false) private Integer consecutiveMatches=0;
 private Double lastValue;
 private Instant lastEvaluatedAt;
 public UUID getId(){return id;} public UUID getRuleId(){return ruleId;} public void setRuleId(UUID v){ruleId=v;}
 public UUID getDeviceId(){return deviceId;} public void setDeviceId(UUID v){deviceId=v;}
 public Integer getConsecutiveMatches(){return consecutiveMatches;} public void setConsecutiveMatches(Integer v){consecutiveMatches=v;}
 public Double getLastValue(){return lastValue;} public void setLastValue(Double v){lastValue=v;}
 public Instant getLastEvaluatedAt(){return lastEvaluatedAt;} public void setLastEvaluatedAt(Instant v){lastEvaluatedAt=v;}
}