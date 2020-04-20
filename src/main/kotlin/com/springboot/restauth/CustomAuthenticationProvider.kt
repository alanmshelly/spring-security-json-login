package com.springboot.restauth

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class CustomAuthenticationProvider(
        val userDetailsService: UserDetailsService,
        val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()
        print(authentication)

        val user = userDetailsService.loadUserByUsername(username)

        if (user == null || !passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("authentication failed")
        }

        return UsernamePasswordAuthenticationToken(
                username,
                null,
                user.authorities
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java
                .isAssignableFrom(authentication)
    }

}