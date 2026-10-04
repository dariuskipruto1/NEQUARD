package com.nequard.simulation;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/simulation") public class SimulationController{@PostMapping("/what-if")public Map<String,Object> whatIf(@RequestBody Map<String,Object> input){return Map.of("simulated",true,"status","SIMULATION_ONLY","inputs",input,"warning","Results are hypothetical and must not be treated as live network state.");}}
