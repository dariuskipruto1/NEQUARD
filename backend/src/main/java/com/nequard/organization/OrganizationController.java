package com.nequard.organization;
import com.nequard.auth.OrganizationAccessService;import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/organizations") public class OrganizationController{
 private final OrganizationRepository repo;private final OrganizationAccessService access;public OrganizationController(OrganizationRepository r,OrganizationAccessService a){repo=r;access=a;}
 record Request(@NotBlank String name,@NotBlank String slug){}
 @GetMapping public List<Organization> all(Authentication a){if(access.isSuperAdmin(a))return repo.findAll();UUID id=access.currentOrganization(a);return repo.findById(id).map(List::of).orElse(List.of());}
 @GetMapping("/{id}") public ResponseEntity<Organization> one(Authentication a,@PathVariable UUID id){return repo.findById(id).filter(o->access.canAccess(a,o.getId())).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping @PreAuthorize("hasRole('SUPER_ADMIN')") public ResponseEntity<?> create(@RequestBody @Valid Request r){if(repo.existsBySlug(r.slug()))return ResponseEntity.status(409).body(Map.of("error","SLUG_EXISTS"));Organization o=new Organization();o.setName(r.name());o.setSlug(r.slug());return ResponseEntity.status(201).body(repo.save(o));}
}