package com.nequard.auth;
import org.springframework.security.core.Authentication;import org.springframework.stereotype.Service;import java.util.*;
@Service public class OrganizationAccessService{
 private final UserRepository users;
 public OrganizationAccessService(UserRepository users){this.users=users;}
 public User currentUser(Authentication a){if(a==null||!a.isAuthenticated())throw new SecurityException("AUTHENTICATION_REQUIRED");return users.findByUsername(a.getName()).orElseThrow(()->new SecurityException("USER_NOT_FOUND"));}
 public boolean isSuperAdmin(Authentication a){return currentUser(a).getRole()==Role.SUPER_ADMIN;}
 public UUID currentOrganization(Authentication a){User u=currentUser(a);if(u.getRole()==Role.SUPER_ADMIN)return null;if(u.getOrganizationId()==null)throw new SecurityException("ORGANIZATION_REQUIRED");return u.getOrganizationId();}
 public UUID requireOrganization(Authentication a,UUID requested){User u=currentUser(a);if(u.getRole()==Role.SUPER_ADMIN){if(requested==null)throw new IllegalArgumentException("ORGANIZATION_ID_REQUIRED");return requested;}if(u.getOrganizationId()==null)throw new SecurityException("ORGANIZATION_REQUIRED");if(requested!=null&&!u.getOrganizationId().equals(requested))throw new SecurityException("TENANT_ACCESS_DENIED");return u.getOrganizationId();}
 public boolean canAccess(Authentication a,UUID organizationId){if(organizationId==null)return false;return isSuperAdmin(a)||organizationId.equals(currentOrganization(a));}
}