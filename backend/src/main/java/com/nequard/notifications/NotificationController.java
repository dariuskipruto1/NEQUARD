package com.nequard.notifications;
import org.springframework.web.bind.annotation.*;import java.time.Instant;import java.util.*;
@RestController @RequestMapping("/api/v1/notifications") public class NotificationController{@PostMapping("/test")public Map<String,Object> test(@RequestBody Map<String,Object> body){return Map.of("accepted",true,"queuedAt",Instant.now().toString(),"channel",body.getOrDefault("channel","EMAIL"),"provider","ABSTRACTED");}}
