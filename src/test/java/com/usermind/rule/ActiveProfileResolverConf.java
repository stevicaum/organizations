package com.usermind.rule;

import org.springframework.test.context.ActiveProfilesResolver;
import org.springframework.test.context.support.DefaultActiveProfilesResolver;

public class ActiveProfileResolverConf implements ActiveProfilesResolver {

  public static final String H_2 = "h2";
  public static final String POSTGRES = "postgres";
  private final DefaultActiveProfilesResolver defaultActiveProfilesResolver = new DefaultActiveProfilesResolver();

  @Override
  public String[] resolve(Class<?> testClass) {
    if (System.getProperties().containsKey("spring.profiles.active")) {
      final String profiles = System.getProperty("spring.profiles.active");
      return profiles.split("\\s*,\\s*");
    } else {
      return new String[] {H_2};
    }
  }
}