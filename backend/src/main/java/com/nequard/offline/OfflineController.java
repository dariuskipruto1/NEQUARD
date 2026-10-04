package com.nequard.offline;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/offline") public class OfflineController{private final LocalServiceRepository repo;public OfflineController(LocalServiceRepository r){repo=r;}@GetMapping("/services")public List<LocalService> services(){return repo.findAll();}@PostMapping("/services")public LocalService create(@RequestBody LocalService s){return repo.save(s);}}
