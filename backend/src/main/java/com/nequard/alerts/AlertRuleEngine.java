package com.nequard.alerts;

import com.nequard.monitoring.MetricSnapshot;
import com.nequard.monitoring.MetricSnapshotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AlertRuleEngine {
    private final MetricSnapshotRepository metrics; private final AlertRepository alerts;
    public AlertRuleEngine(MetricSnapshotRepository metrics,AlertRepository alerts){this.metrics=metrics;this.alerts=alerts;}
    @Scheduled(fixedDelayString="${nequard.monitoring.interval-ms:30000}")
    public void evaluate(){
        metrics.findAll().stream().filter(m->m.getDeviceId()!=null).collect(java.util.stream.Collectors.toMap(
            MetricSnapshot::getDeviceId,m->m,(a,b)->a.getRecordedAt().isAfter(b.getRecordedAt())?a:b)).values().forEach(this::evaluateMetric);
    }
    private void evaluateMetric(MetricSnapshot m){
        String severity=null,title=null,message=null;
        if(m.getCpuPercent()!=null&&m.getCpuPercent()>=90){severity="HIGH";title="High CPU utilization";message="CPU utilization reached or exceeded 90%.";}
        else if(m.getPacketLossPercent()!=null&&m.getPacketLossPercent()>=20){severity="HIGH";title="High packet loss";message="Observed packet loss reached or exceeded 20%.";}
        else if(m.getLatencyMs()!=null&&m.getLatencyMs()>=200){severity="WARNING";title="High latency";message="Observed latency reached or exceeded 200 ms.";}
        if(severity!=null&&!alerts.existsByDeviceIdAndStatus(m.getDeviceId(),"OPEN")){
            Alert a=new Alert();a.setDeviceId(m.getDeviceId());a.setSeverity(severity);a.setTitle(title);a.setMessage(message);alerts.save(a);
        }
    }
}