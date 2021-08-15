package com.example.todo.config.security;

import com.example.todo.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  private final UserDetailsServiceImpl userDetailsService;


  private final JWTService tokenService;
  private final UserRepository userRepository;


  public SecurityConfig(UserDetailsServiceImpl userDetailsService, JWTService tokenService,
      UserRepository userRepository) {
    this.userDetailsService = userDetailsService;

    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  @Override
  @Bean
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/**html", "/v2/api-docs", "/webjars/**", "/configuration/**",
        "/swagger-resources/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults()).csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/auth").permitAll()
        .antMatchers(HttpMethod.POST, "/user").permitAll()

        .anyRequest().authenticated().and()
        .addFilterBefore(new JWTValidateFilter(tokenService, userRepository),
            UsernamePasswordAuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

}
