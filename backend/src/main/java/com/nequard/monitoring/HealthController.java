package com.nequard.monitoring;
import com.nequard.network.Device;
import com.nequard.network.DeviceRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController("monitoringHealthController")
@RequestMapping("/api/v1/monitoring")
public class HealthController {
 private final DeviceRepository repo;
 public HealthController(DeviceRepository r){repo=r;}
 @GetMapping("/health")
 public Map<String,Object> health(){
  List<Device>d=repo.findAll();
  long online=d.stream().filter(x->"ONLINE".equalsIgnoreCase(x.getOperationalStatus())).count();
  double score=d.isEmpty()?0:(online*100.0/d.size());
  return Map.of("deviceCount",d.size(),"onlineDevices",online,"healthScore",Math.round(score*100)/100.0,"status",score>=90?"HEALTHY":score>=75?"WARNING":score>=50?"DEGRADED":score>0?"CRITICAL":"OFFLINE");
 }
}
