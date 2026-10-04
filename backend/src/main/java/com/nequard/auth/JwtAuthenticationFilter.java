package com.nequard.auth;
import jakarta.servlet.*;import jakarta.servlet.http.*;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;import org.springframework.security.core.authority.SimpleGrantedAuthority;import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;import java.io.IOException;import java.util.List;
@Component public class JwtAuthenticationFilter extends OncePerRequestFilter{
 private final JwtService jwt; public JwtAuthenticationFilter(JwtService jwt){this.jwt=jwt;}
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String h=req.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer ")){try{Claims c=jwt.parse(h.substring(7));String role=c.get("role",String.class);if(role!=null)SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(c.getSubject(),null,List.of(new SimpleGrantedAuthority("ROLE_"+role)));}catch(JwtException|IllegalArgumentException ignored){}}
  chain.doFilter(req,res);
 }
}