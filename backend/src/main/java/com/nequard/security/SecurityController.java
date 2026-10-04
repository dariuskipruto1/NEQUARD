package com.nequard.security;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/security") public class SecurityController{private final SecurityEventRepository repo;public SecurityController(SecurityEventRepository r){repo=r;}@GetMapping("/events")public List<SecurityEvent> events(){return repo.findTop100ByOrderByOccurredAtDesc();}@PostMapping("/events")public SecurityEvent record(@RequestBody SecurityEvent e){return repo.save(e);}}
