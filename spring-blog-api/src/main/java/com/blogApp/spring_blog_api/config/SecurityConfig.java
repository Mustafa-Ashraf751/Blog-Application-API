package com.blogApp.spring_blog_api.config;

import com.blogApp.spring_blog_api.security.CustomerUserDetailsService;
import com.blogApp.spring_blog_api.security.JwtAthenticationFilter;
import com.blogApp.spring_blog_api.security.JwtAuthentationEntryPoint;
import com.blogApp.spring_blog_api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(enforceUniqueMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig   {


  private CustomerUserDetailsService userDetailsService;

  private  JwtAuthentationEntryPoint authentationEntryPoint;
  private JwtTokenProvider tokenProvider;
  @Autowired
  public SecurityConfig( CustomerUserDetailsService userDetailsService,
                        JwtAuthentationEntryPoint authentationEntryPoint,
                         JwtTokenProvider tokenProvider) {
    this.userDetailsService = userDetailsService;
    this.authentationEntryPoint = authentationEntryPoint;
    this.tokenProvider =tokenProvider;
  }

  @Bean
  public JwtAthenticationFilter jwtAthenticationFilter(){
    return  new JwtAthenticationFilter(tokenProvider,userDetailsService);
  }

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }


  @Bean
protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
  http
          .csrf().disable()
          .exceptionHandling()
          .authenticationEntryPoint(authentationEntryPoint)
          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .authorizeHttpRequests()
          .requestMatchers(HttpMethod.GET,"/api/v1/**")
          .permitAll()
          .requestMatchers("api/v1/auth/**").permitAll()
          .requestMatchers("/v3/api-docs").permitAll()
          .requestMatchers("/v3/api-docs/**").permitAll()
          .requestMatchers("/swagger-ui/**").permitAll()
          .requestMatchers("/swagger-resources/**").permitAll()
          .requestMatchers("/swagger-ui.html").permitAll()
          .requestMatchers("/webjars/**").permitAll()
          .anyRequest()
          .authenticated();

http.addFilterBefore(jwtAthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
return http.build();
}
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
    return authenticationConfiguration.getAuthenticationManager();
}


  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
  }



}
