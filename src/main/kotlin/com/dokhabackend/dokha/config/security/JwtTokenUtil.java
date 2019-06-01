package com.dokhabackend.dokha.config.security;

import com.dokhabackend.dokha.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

/**
 * @author v.butuzov
 * Утилиты для jwt
 */
@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.accessTokenValidityMillisecond}")
    private long ACCESS_TOKEN_VALIDITY_SECONDS;

    @Value("${jwt.signingKey}")
    private String SIGNING_KEY;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Не верный токен");
        }

    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        return doGenerateToken(user.getLogin(), user.getRoles().toString());
    }

    private String doGenerateToken(String subject, String role) {
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("role", simpleGrantedAuthorities);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("dokha")
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return (getUsernameFromToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}