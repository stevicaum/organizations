//package com.usermind.rule.config.security;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import javax.servlet.http.HttpServletResponse;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Profile("security")
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true //prePostEnabled = true enables @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
//)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//  private final String moduleName;
//  private final String apiVersion;
//
//  public SecurityConfig(
//      @Value("${module-name}") String moduleName,
//      @Value("${api-version}") String apiVersion) {
//    super();
//    this.moduleName = moduleName;
//    this.apiVersion = apiVersion;
//  }
//
//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    //here should check in repository does username exist and return User
//    auth.userDetailsService(username -> UserRepositoryMock.find(username).orElseThrow(() ->
//        new UsernameNotFoundException(String.format("User: %s, not found", username))));
//  }
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    // Enable CORS and disable CSRF
//    http = http.cors().and().csrf().disable();
//
//    // Set session management to stateless
//    http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
//
//    // Set unauthorized requests exception handler
//    http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
//      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
//    }).and();
//
//    // Set permissions on endpoints
//    http.authorizeRequests()
//        // Our public endpoints
//        .antMatchers("/swagger-ui.html").permitAll()
//        .antMatchers("/swagger-ui/**/**").permitAll()
//        .antMatchers("/api-docs*").permitAll()
//        .antMatchers("/api-docs/**").permitAll()
//        .antMatchers("/actuator/**").permitAll()
//        // Our private endpoints
//        .anyRequest().authenticated();
//
//    // Add JWT token filter
//    http.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }
//
//  // Used by spring security if CORS is enabled.
//  @Bean
//  public CorsFilter corsFilter() {
//    UrlBasedCorsConfigurationSource source =
//        new UrlBasedCorsConfigurationSource();
//    CorsConfiguration config = new CorsConfiguration();
//    config.setAllowCredentials(true);
//    config.addAllowedOrigin("*");
//    config.addAllowedHeader("*");
//    config.addAllowedMethod("*");
//    source.registerCorsConfiguration("/**", config);
//    return new CorsFilter(source);
//  }
//
//
//  @Bean
//  public OpenAPI springRuleOpenApi() {
//    final String securitySchemeName = "bearerAuth";
//    final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
//    return new OpenAPI()
//        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
//        .components(
//            new Components()
//                .addSecuritySchemes(securitySchemeName,
//                    new SecurityScheme()
//                        .name(securitySchemeName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                )
//        )
//        .info(new Info().title(apiTitle).version(apiVersion));
//  }
//}
