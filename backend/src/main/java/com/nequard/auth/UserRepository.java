package com.nequard.auth;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;public interface UserRepository extends JpaRepository<User,UUID>{Optional<User> findByUsername(String username);boolean existsByUsername(String username);boolean existsByEmail(String email);}
