package com.nequard.auth;

import com.nequard.organization.OrganizationRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserRepository users;
    private final OrganizationRepository organizations;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwt;
    private final boolean allowOrganizationId;

    public AuthController(UserRepository users, OrganizationRepository organizations,
                          BCryptPasswordEncoder encoder, JwtService jwt,
                          @Value("${nequard.registration.allow-organization-id:false}") boolean allowOrganizationId) {
        this.users = users;
        this.organizations = organizations;
        this.encoder = encoder;
        this.jwt = jwt;
        this.allowOrganizationId = allowOrganizationId;
    }

    record Register(@NotBlank String username, @Email @NotBlank String email,
                    @Size(min=8,max=128) String password, UUID organizationId) {}
    record Login(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Register r) {
        if (users.existsByUsername(r.username()) || users.existsByEmail(r.email()))
            return ResponseEntity.status(409).body(Map.of("error","USER_EXISTS"));

        if (r.organizationId() != null) {
            if (!allowOrganizationId)
                return ResponseEntity.badRequest().body(Map.of("error","ORGANIZATION_ASSIGNMENT_REQUIRES_INVITE"));
            if (!organizations.existsById(r.organizationId()))
                return ResponseEntity.badRequest().body(Map.of("error","ORGANIZATION_NOT_FOUND"));
        }

        User u = new User();
        u.setUsername(r.username());
        u.setEmail(r.email());
        u.setPasswordHash(encoder.encode(r.password()));
        u.setRole(Role.VIEWER);
        u.setOrganizationId(r.organizationId());
        users.save(u);

        return ResponseEntity.status(201).body(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "role", u.getRole(),
                "organizationId", u.getOrganizationId() == null ? "" : u.getOrganizationId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login r) {
        return users.findByUsername(r.username())
            .filter(u -> encoder.matches(r.password(), u.getPasswordHash()))
            .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of(
                "authenticated", true,
                "accessToken", jwt.generate(u),
                "tokenType", "Bearer",
                "userId", u.getId(),
                "username", u.getUsername(),
                "role", u.getRole(),
                "organizationId", u.getOrganizationId() == null ? "" : u.getOrganizationId()
            )))
            .orElseGet(() -> ResponseEntity.status(401).body(Map.of(
                "authenticated", false, "error", "INVALID_CREDENTIALS"
            )));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication a) {
        if (a == null || !a.isAuthenticated()) return ResponseEntity.status(401).build();
        return users.findByUsername(a.getName())
            .map(u -> ResponseEntity.ok(Map.of(
                "id",u.getId(),"username",u.getUsername(),"email",u.getEmail(),
                "role",u.getRole(),"organizationId",u.getOrganizationId() == null ? "" : u.getOrganizationId()
            )))
            .orElse(ResponseEntity.notFound().build());
    }
}
