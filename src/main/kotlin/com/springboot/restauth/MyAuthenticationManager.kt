package com.springboot.restauth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

data class UserDetails(
        val username: String,
        val password: String
)

@Service
class MyAuthenticationManager(
        private val passwordEncoder: PasswordEncoder
) : AuthenticationManager {
    private val users: Map<String, UserDetails> = mapOf(Pair("hoge",
            UserDetails(
                    "hoge",
                    passwordEncoder.encode("foobar")
            )
    ))

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val user = users.get(username)
        if (user == null || !passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("authentication failed")
        }

        return UsernamePasswordAuthenticationToken(username, null, listOf(
                GrantedAuthority { "ROLE_USER" }
        ))
    }
}