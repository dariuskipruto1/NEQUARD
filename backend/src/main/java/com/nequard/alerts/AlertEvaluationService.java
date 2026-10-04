package com.nequard.alerts;

import com.nequard.network.Device;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;

@Service
public class AlertEvaluationService {
 private final AlertRuleRepository rules; private final AlertRepository alerts; private final AlertRuleStateRepository states;
 public AlertEvaluationService(AlertRuleRepository r,AlertRepository a,AlertRuleStateRepository s){rules=r;alerts=a;states=s;}
 public void evaluate(Device d,Map<String,Double> values){
  if(d.getOrganizationId()==null)return;
  for(AlertRule r:rules.findByOrganizationIdAndEnabledTrue(d.getOrganizationId())){
   Double value=values.get(r.getMetric());
   AlertRuleState state=states.findByRuleIdAndDeviceId(r.getId(),d.getId()).orElseGet(()->{
    AlertRuleState s=new AlertRuleState();s.setRuleId(r.getId());s.setDeviceId(d.getId());return s;});
   boolean matched=value!=null&&matches(value,r.getOperator(),r.getThreshold());
   int count=matched?state.getConsecutiveMatches()+1:0;
   state.setConsecutiveMatches(count);state.setLastValue(value);state.setLastEvaluatedAt(Instant.now());states.save(state);
   int required=Math.max(1,r.getConsecutiveFailures()==null?1:r.getConsecutiveFailures());
   if(!matched||count<required||alerts.existsByDeviceIdAndStatus(d.getId(),"OPEN"))continue;
   Alert a=new Alert();a.setTitle(r.getName()+": "+d.getHostname());a.setSeverity(r.getSeverity());a.setDeviceId(d.getId());
   a.setOrganizationId(d.getOrganizationId());a.setMessage("Rule matched "+count+" consecutive evaluation(s): "+r.getMetric()+" value "+value+" "+r.getOperator()+" "+r.getThreshold());
   alerts.save(a);
  }
 }
 private boolean matches(double v,String op,double t){
  if(op==null)return false;
  return switch(op.trim().toUpperCase()){case ">"->v>t;case ">="->v>=t;case "<"->v<t;case "<="->v<=t;case "=","=="->v==t;default->false;};
 }
}