package com.nequard.alerts;
import com.nequard.network.Device;import com.nequard.network.DeviceRepository;import org.springframework.stereotype.Service;import java.util.*;
@Service public class AlertEvaluationService{
private final AlertRuleRepository rules;private final AlertRepository alerts;private final DeviceRepository devices;
public AlertEvaluationService(AlertRuleRepository r,AlertRepository a,DeviceRepository d){rules=r;alerts=a;devices=d;}
public void evaluate(Device d,Map<String,Double> values){if(d.getOrganizationId()==null)return;for(AlertRule r:rules.findByOrganizationIdAndEnabledTrue(d.getOrganizationId())){Double v=values.get(r.getMetric());if(v==null||!matches(v,r.getOperator(),r.getThreshold()))continue;if(alerts.existsByDeviceIdAndStatus(d.getId(),"OPEN"))continue;Alert a=new Alert();a.setTitle(r.getName()+": "+d.getHostname());a.setSeverity(r.getSeverity());a.setDeviceId(d.getId());a.setOrganizationId(d.getOrganizationId());a.setMessage("Rule matched metric "+r.getMetric()+" with value "+v+" against "+r.getOperator()+" "+r.getThreshold());alerts.save(a);}}
private boolean matches(double v,String op,double t){return switch(op.toUpperCase()){case ">"->v>t;case ">="->v>=t;case "<"->v<t;case "<="->v<=t;case "=","=="->v==t;default->false;};}
}