package com.nequard.security;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/security/fraud") public class FraudController{record Request(int failedLogins,int unusualSources,int trafficSpikes){}@PostMapping("/assess")public FraudAssessment assess(@RequestBody Request r){return FraudAssessment.assess(r.failedLogins(),r.unusualSources(),r.trafficSpikes());}}
