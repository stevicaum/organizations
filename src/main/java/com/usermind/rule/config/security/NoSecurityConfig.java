//package com.usermind.rule.config.security;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Profile("no-security")
//@Configuration
//public class NoSecurityConfig extends WebSecurityConfigurerAdapter {
//  private final String moduleName;
//  private final String apiVersion;
//
//  public NoSecurityConfig(
//      @Value("${module-name}") String moduleName,
//      @Value("${api-version}") String apiVersion) {
//    super();
//    this.moduleName = moduleName;
//    this.apiVersion = apiVersion;
//  }
//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/**");
//  }
//
//    @Bean
//  public OpenAPI springRuleOpenApi() {
//      final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
//    return new OpenAPI().info(new Info().title(apiTitle).version(apiVersion));
//  }
//}
