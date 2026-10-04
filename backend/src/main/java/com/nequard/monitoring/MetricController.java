package com.nequard.monitoring;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/metrics") public class MetricController{private final MetricSnapshotRepository repo;public MetricController(MetricSnapshotRepository r){repo=r;}@GetMapping("/device/{deviceId}")public List<MetricSnapshot> device(@PathVariable UUID deviceId){return repo.findTop100ByDeviceIdOrderByRecordedAtDesc(deviceId);}}
