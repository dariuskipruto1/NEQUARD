package com.nequard.auth;
import io.jsonwebtoken.*;import io.jsonwebtoken.security.Keys;import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;import javax.crypto.SecretKey;import java.nio.charset.StandardCharsets;import java.time.Instant;import java.util.*;
@Service public class JwtService{
 private final SecretKey key; private final long expiration;
 public JwtService(@Value("${nequard.security.jwt-secret}") String secret,@Value("${nequard.security.jwt-expiration-ms:86400000}") long expiration){
  if(secret.getBytes(StandardCharsets.UTF_8).length<32) throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
  key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expiration=expiration;
 }
 public String generate(User u){Instant now=Instant.now();return Jwts.builder().subject(u.getUsername()).claim("uid",u.getId().toString()).claim("role",u.getRole().name()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expiration))).signWith(key).compact();}
 public Claims parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}