package com.blogApp.spring_blog_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAthenticationFilter extends OncePerRequestFilter {

  //Inject required dependencies

  private JwtTokenProvider tokenProvider;
  private CustomerUserDetailsService customerUserDetailsService;
  @Autowired
  public JwtAthenticationFilter(JwtTokenProvider tokenProvider, CustomerUserDetailsService customerUserDetailsService) {
    this.tokenProvider = tokenProvider;
    this.customerUserDetailsService =customerUserDetailsService;
  }



  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    //get jwt from http request
    String token = getJwtFromRequest(request);
    //validate token
    if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
      //get the username from token
      String username = tokenProvider.getUserNameFromJwt(token);
      //Load user associated with token
      UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
              null,userDetails.getAuthorities());
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      //Set spring security
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

   filterChain.doFilter(request,response);


  }


  //Bearer <accessToken>
  private String getJwtFromRequest(HttpServletRequest request){
     String bearerToken = request.getHeader("Authorization");
     if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
       return bearerToken.substring(7);
     }
     return null;
  }
}
