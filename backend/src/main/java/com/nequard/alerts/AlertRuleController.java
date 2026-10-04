package com.nequard.alerts;
import com.nequard.auth.OrganizationAccessService;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/alert-rules") public class AlertRuleController{
private final AlertRuleRepository repo;private final OrganizationAccessService access;public AlertRuleController(AlertRuleRepository r,OrganizationAccessService a){repo=r;access=a;}
@GetMapping public List<AlertRule> all(Authentication a){return access.isSuperAdmin(a)?repo.findAll():repo.findByOrganizationIdAndEnabledTrue(access.currentOrganization(a));}
@PostMapping @PreAuthorize("hasAnyRole('SUPER_ADMIN','NETWORK_ADMIN')") public AlertRule create(Authentication a,@RequestBody AlertRule r){r.setOrganizationId(access.requireOrganization(a,r.getOrganizationId()));return repo.save(r);}
@PutMapping("/{id}") @PreAuthorize("hasAnyRole('SUPER_ADMIN','NETWORK_ADMIN')") public AlertRule update(Authentication a,@PathVariable UUID id,@RequestBody AlertRule r){var old=repo.findById(id).orElseThrow(()->new NoSuchElementException("ALERT_RULE_NOT_FOUND"));if(!access.canAccess(a,old.getOrganizationId()))throw new SecurityException("TENANT_ACCESS_DENIED");r.setOrganizationId(old.getOrganizationId());return repo.save(r);}
}