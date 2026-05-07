package com.RamonVale.api_getaway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
    http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(auth -> auth
//        .pathMatchers("/actuator/health").permitAll()
        .anyExchange().authenticated()
      )
      .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtConverter() {
    var conv = new JwtGrantedAuthoritiesConverter();
    conv.setAuthoritiesClaimName("realm_access.roles");
    conv.setAuthorityPrefix("ROLE_");

    var authConv = new JwtAuthenticationConverter();
    authConv.setJwtGrantedAuthoritiesConverter(conv);
    return authConv;
  }

}

