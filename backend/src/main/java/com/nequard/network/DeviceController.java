package com.nequard.network;
import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/devices") public class DeviceController {
 private final DeviceRepository repo; public DeviceController(DeviceRepository r){repo=r;}
 record Request(@NotBlank String hostname,String managementIp,DeviceType deviceType,String vendor,String model){}
 @GetMapping public List<Device> all(){return repo.findAll();}
 @GetMapping("/{id}") public ResponseEntity<Device> one(@PathVariable UUID id){return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping @PreAuthorize("hasAnyRole('SUPER_ADMIN','NETWORK_ADMIN','NETWORK_ENGINEER')") public ResponseEntity<Device> create(@RequestBody @Valid Request r){Device d=new Device();d.setHostname(r.hostname());d.setManagementIp(r.managementIp());d.setDeviceType(r.deviceType()==null?DeviceType.OTHER:r.deviceType());d.setVendor(r.vendor());d.setModel(r.model());return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(d));}
 @DeleteMapping("/{id}") @PreAuthorize("hasAnyRole('SUPER_ADMIN','NETWORK_ADMIN')") public ResponseEntity<Void> delete(@PathVariable UUID id){if(!repo.existsById(id))return ResponseEntity.notFound().build();repo.deleteById(id);return ResponseEntity.noContent().build();}
}