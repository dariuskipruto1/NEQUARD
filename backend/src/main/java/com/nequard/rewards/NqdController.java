package com.nequard.rewards;
import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/rewards") public class NqdController{private final NqdRepository repo;public NqdController(NqdRepository r){repo=r;}@GetMapping("/users/{userId}/ledger")public List<NqdLedgerEntry> ledger(@PathVariable UUID userId){return repo.findByUserIdOrderByCreatedAtDesc(userId);}@PostMapping("/ledger")public NqdLedgerEntry record(@RequestBody NqdLedgerEntry e){return repo.save(e);}}
