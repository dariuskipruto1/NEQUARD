package com.nequard.community;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/community") public class CommunityController{private final CommunityNodeRepository repo;public CommunityController(CommunityNodeRepository r){repo=r;}@GetMapping("/nodes")public List<CommunityNode> nodes(){return repo.findAll();}@PostMapping("/nodes")public CommunityNode create(@RequestBody CommunityNode n){return repo.save(n);}}
