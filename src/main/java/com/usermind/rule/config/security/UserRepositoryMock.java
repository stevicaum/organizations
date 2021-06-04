package com.usermind.rule.config.security;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserRepositoryMock {

  public static Optional<User> find(String username) {
    if (username.equals("stevica")) {
      return Optional.of(new User("stevica", "stevica",
          Stream.of(new SimpleGrantedAuthority("ROLE_CLIENT"),new SimpleGrantedAuthority("CREATE_ORGANIZATION")).collect(Collectors.toList())));
    } else if (username.equals("test")) {
      return Optional.of(new User("test", "test", new ArrayList<>()));
    }
    return Optional.empty();
  }
}