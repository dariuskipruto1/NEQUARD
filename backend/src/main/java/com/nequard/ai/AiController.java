package com.nequard.ai;

import com.nequard.auth.OrganizationAccessService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
    private final OrganizationAccessService access;
    public AiController(OrganizationAccessService access){this.access=access;}

    @GetMapping("/capabilities")
    public Map<String,Object> capabilities(){
        return Map.of("defaultMode","READ_ONLY","allowedModes",List.of("READ_ONLY","DIAGNOSTIC","ADMINISTRATIVE"),
                "destructiveCommands",false,"unrestrictedSql",false,"parameterizedActions",true);
    }

    @PostMapping("/query")
    public Map<String,Object> query(Authentication a,@RequestParam(defaultValue="READ_ONLY") AiMode mode,
                                    @RequestBody Map<String,String> body){
        String q=body.getOrDefault("query","").trim();
        if(q.isBlank()) throw new IllegalArgumentException("QUERY_REQUIRED");
        if(mode==AiMode.ADMINISTRATIVE && !access.isSuperAdmin(a))
            throw new SecurityException("ADMINISTRATIVE_MODE_REQUIRES_SUPER_ADMIN");
        if(mode==AiMode.DIAGNOSTIC && !(access.isSuperAdmin(a) ||
                a.getAuthorities().stream().anyMatch(x->x.getAuthority().matches("ROLE_(NETWORK_ADMIN|NETWORK_ENGINEER|NOC_OPERATOR|TECHNICIAN)"))))
            throw new SecurityException("DIAGNOSTIC_MODE_NOT_PERMITTED");
        return Map.of("mode",mode.name(),"query",q,"status","SAFE_QUERY_REQUIRED",
                "message","Query accepted only as a controlled request; execution must map to predefined parameterized actions.",
                "executed",false);
    }
}