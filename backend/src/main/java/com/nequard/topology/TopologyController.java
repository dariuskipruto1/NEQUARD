package com.nequard.topology;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/topology") public class TopologyController{private final TopologyLinkRepository repo;public TopologyController(TopologyLinkRepository r){repo=r;}@GetMapping("/links")public List<TopologyLink> links(){return repo.findAll();}@PostMapping("/links")public TopologyLink create(@RequestBody TopologyLink l){return repo.save(l);}@DeleteMapping("/links/{id}")public void delete(@PathVariable UUID id){repo.deleteById(id);}}
