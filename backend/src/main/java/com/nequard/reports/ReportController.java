package com.nequard.reports;
import org.springframework.web.bind.annotation.*;import java.time.Instant;import java.util.*;
@RestController @RequestMapping("/api/v1/reports") public class ReportController{@GetMapping("/summary")public Map<String,Object> summary(){return Map.of("generatedAt",Instant.now().toString(),"format","JSON","status","READY","exportFormats",List.of("CSV","PDF","XLSX"));}}
