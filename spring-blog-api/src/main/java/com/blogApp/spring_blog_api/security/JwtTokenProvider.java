package com.blogApp.spring_blog_api.security;

import com.blogApp.spring_blog_api.exception.BlogAppException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
  @Value("${app.jwt.secret}")
  private String jwtSecret;
  @Value("${app.jwt.expiration-milliseconds}")
  private int jwtExpirationInMs;





  //Create the Token

  public String generateToken(Authentication authentication){
    System.out.println("JWT Secret: " + jwtSecret);
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);
    String authorities = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.joining(","));
    return Jwts.builder()
            .setHeaderParam("type","JWT")
            .setSubject(username)
            .claim("roles",authorities)
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS512,jwtSecret)
            .compact();

  }

  //Extract username from token
  public String getUserNameFromJwt(String token){
    Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
    return claims.getSubject();

  }

  //Validate Jwt token
  public boolean validateToken(String token){
    try{
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    }catch(SignatureException ex){
         throw new BlogAppException(HttpStatus.BAD_REQUEST,"Invalid jwt signature");
    }catch (MalformedJwtException ex){
      throw new BlogAppException(HttpStatus.BAD_REQUEST,"Invalid jwt token");
    }catch (ExpiredJwtException ex){
      throw new BlogAppException(HttpStatus.BAD_REQUEST,"Expired jwt token");
    }catch(UnsupportedJwtException ex){
      throw new BlogAppException(HttpStatus.BAD_REQUEST,"Unsupported jwt token");
    }catch (IllegalArgumentException ex){
      throw new BlogAppException(HttpStatus.BAD_REQUEST,"Jwt claims string is empty");
    }

  }


}
