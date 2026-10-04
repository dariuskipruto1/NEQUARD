package com.nequard.auth;
import jakarta.validation.constraints.*;import org.springframework.http.*;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/v1/auth") public class AuthController {
 private final UserRepository users; private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
 public AuthController(UserRepository users){this.users=users;}
 record Register(@NotBlank String username,@Email @NotBlank String email,@Size(min=8,max=128) String password){}
 record Login(@NotBlank String username,@NotBlank String password){}
 @PostMapping("/register") public ResponseEntity<?> register(@RequestBody @jakarta.validation.Valid Register r){
  if(users.existsByUsername(r.username())||users.existsByEmail(r.email())) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","USER_EXISTS"));
  User u=new User();u.setUsername(r.username());u.setEmail(r.email());u.setPasswordHash(encoder.encode(r.password()));u.setRole(Role.VIEWER);users.save(u);
  return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id",u.getId(),"username",u.getUsername(),"role",u.getRole()));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody @jakarta.validation.Valid Login r){
  return users.findByUsername(r.username()).filter(u->encoder.matches(r.password(),u.getPasswordHash()))
   .<ResponseEntity<?>>map(u->ResponseEntity.ok(Map.of("authenticated",true,"userId",u.getId(),"username",u.getUsername(),"role",u.getRole())))
   .orElseGet(()->ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated",false,"error","INVALID_CREDENTIALS")));
 }
}
