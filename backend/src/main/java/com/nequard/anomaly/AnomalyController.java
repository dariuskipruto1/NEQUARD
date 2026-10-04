package com.nequard.anomaly;
import jakarta.validation.constraints.NotNull;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/anomalies") public class AnomalyController{record Request(@NotNull Double value,@NotNull Double mean,@NotNull Double stddev){}@PostMapping("/z-score")public AnomalyDetector.Result detect(@RequestBody Request r){return AnomalyDetector.zScore(r.value(),r.mean(),r.stddev());}}
