package com.dokhabackend.dokha.config.security

import com.dokhabackend.dokha.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

/**
 * @author v.butuzov
 * Утилиты для jwt
 */
@Component
//Спринг пьян и не инжектить сюда черезе @ConfigProps/@Value
class JwtTokenUtil(val jwtProperties: JwtProperties) {

    fun getUsernameFromToken(token: String): String = getClaimFromToken(token, Function { it.subject })

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.expiration })
    }

    private fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.signingKey)
                    .parseClaimsJws(token)
                    .body
        } catch (e: Exception) {
            throw IllegalArgumentException("Не верный токен")
        }

    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(user: User): String {
        return doGenerateToken(user.login, user.roles.toString())
    }

    private fun doGenerateToken(subject: String, role: String): String {
        val simpleGrantedAuthorities = ArrayList<SimpleGrantedAuthority>()
        simpleGrantedAuthorities.add(SimpleGrantedAuthority(role))
        val claims = Jwts.claims().setSubject(subject)
        claims["role"] = simpleGrantedAuthorities

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("dokha")
                .signWith(SignatureAlgorithm.HS256, jwtProperties.signingKey)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessTokenValidityMillisecond * 1000))
                .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean? {
        return getUsernameFromToken(token) == userDetails.username && !isTokenExpired(token)
    }
}