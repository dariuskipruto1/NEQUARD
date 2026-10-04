package com.nequard.location;
import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/locations")
public class LocationController{
 private final LocationRepository repo; public LocationController(LocationRepository r){repo=r;}
 record Request(@NotBlank String name,UUID organizationId,String address,Double latitude,Double longitude){}
 @GetMapping public List<Location> all(@RequestParam(required=false) UUID organizationId){return organizationId==null?repo.findAll():repo.findByOrganizationId(organizationId);}
 @GetMapping("/{id}") public ResponseEntity<Location> one(@PathVariable UUID id){return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping @PreAuthorize("hasAnyRole('SUPER_ADMIN','NETWORK_ADMIN')") public ResponseEntity<?> create(@RequestBody @Valid Request r){if(r.organizationId()==null)return ResponseEntity.badRequest().body(Map.of("error","ORGANIZATION_ID_REQUIRED"));Location l=new Location();l.setName(r.name());l.setOrganizationId(r.organizationId());l.setAddress(r.address());l.setLatitude(r.latitude());l.setLongitude(r.longitude());return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(l));}
 @DeleteMapping("/{id}") @PreAuthorize("hasAnyRole('SUPER_ADMIN','NETWORK_ADMIN')") public ResponseEntity<Void> delete(@PathVariable UUID id){if(!repo.existsById(id))return ResponseEntity.notFound().build();repo.deleteById(id);return ResponseEntity.noContent().build();}
}