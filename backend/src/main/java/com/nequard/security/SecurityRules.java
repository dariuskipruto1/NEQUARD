package com.nequard.security;
import org.springframework.stereotype.Component;import java.util.*;
@Component public class SecurityRules{public Map<String,Object> capabilities(){return Map.of("authentication","JWT","authorization","RBAC","tenantIsolation",true,"destructiveActions","disabled-by-default","audit","enabled");}}