package com.nequard.network;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class InterfaceTelemetryEngine {
    private final NetworkInterfaceRepository interfaces;
    private final int threshold;
    public InterfaceTelemetryEngine(NetworkInterfaceRepository interfaces,
                                    org.springframework.beans.factory.annotation.Value("${nequard.monitoring.interface-utilization-warning:80}") int threshold){
        this.interfaces=interfaces;this.threshold=threshold;
    }
    @Scheduled(fixedDelayString="${nequard.monitoring.interval-ms:30000}")
    public void evaluate(){
        interfaces.findAll().forEach(i->{
            if(i.getUtilizationPercent()!=null){
                i.setStatus(i.getUtilizationPercent()>=threshold?"DEGRADED":"UP");
                i.setUpdatedAt(Instant.now());
                interfaces.save(i);
            }
        });
    }
}