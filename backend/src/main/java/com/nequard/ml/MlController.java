package com.nequard.ml;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/ml") public class MlController{private final MlServiceClient ml;public MlController(MlServiceClient m){ml=m;}@PostMapping("/anomaly")public String anomaly(@RequestBody Map<String,Object>b){return ml.anomaly(b);}@PostMapping("/failure")public String failure(@RequestBody Map<String,Object>b){return ml.failure(b);}@PostMapping("/root-cause")public String rootCause(@RequestBody Map<String,Object>b){return ml.rootCause(b);}}
