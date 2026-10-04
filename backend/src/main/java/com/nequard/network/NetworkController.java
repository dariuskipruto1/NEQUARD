package com.nequard.network;
import org.springframework.web.bind.annotation.*;import java.util.*;import java.time.Instant;
@RestController @RequestMapping("/api/v1/network") public class NetworkController{
@GetMapping("/health") public Map<String,Object> health(){return Map.of("status","UP","timestamp",Instant.now().toString(),"monitoring","READY","note","Live adapters are enabled only when configured.");}
@GetMapping("/capabilities") public Map<String,Object> capabilities(){return Map.of("protocols",List.of("ICMP","TCP","UDP","SNMPv2c","SNMPv3","HTTP","HTTPS","DNS","Syslog","LLDP","CDP"),"monitoring",List.of("availability","latency","packetLoss","cpu","memory","interfaces","services"));}
}