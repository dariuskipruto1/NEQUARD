package com.nequard.auth;
import jakarta.persistence.*;import java.util.UUID;
@Entity @Table(name="users") public class User {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @Column(nullable=false,unique=true) private String username;
 @Column(nullable=false,unique=true) private String email;
 @Column(name="password_hash",nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role=Role.VIEWER;
 @Column(name="organization_id") private UUID organizationId;
 public UUID getId(){return id;} public String getUsername(){return username;} public void setUsername(String v){username=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;} public UUID getOrganizationId(){return organizationId;} public void setOrganizationId(UUID v){organizationId=v;}
}