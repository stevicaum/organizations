package com.usermind.rule.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JwtTokenUtil implements Serializable {

  public static void main(String[] args) {
    User test = new User("stevica", "stevica", Stream.of(new SimpleGrantedAuthority("CREATE_ORGANIZATION"),
        new SimpleGrantedAuthority("ROLE_CLIENT")).collect(Collectors.toList()));
    System.out.println(JwtTokenUtil.generateToken(test, 360000));
  }

  public static final String SIGNING_KEY = "usermindkey";


  public static String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public static Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private static Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(SIGNING_KEY)
        .parseClaimsJws(token)
        .getBody();
  }

  public static Boolean isTokenExpired(String token) {
    try {
      final Date expiration = getExpirationDateFromToken(token);
      return expiration.before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public static String generateToken(User user, long validitySeconds) {
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    claims.put("authorities", user.getAuthorities());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuer("http://usermind.com")
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + validitySeconds * 1000))
        .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
        .compact();
  }
}