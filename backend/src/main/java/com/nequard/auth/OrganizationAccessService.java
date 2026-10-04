package com.nequard.auth;
import org.springframework.security.core.Authentication;import org.springframework.stereotype.Service;import java.util.*;
@Service public class OrganizationAccessService{
 public boolean canAccess(Authentication authentication,UUID organizationId){
  if(authentication==null||!authentication.isAuthenticated()||organizationId==null)return false;
  if(authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_SUPER_ADMIN")))return true;
  return false;
 }
}