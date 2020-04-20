package com.springboot.restauth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomAuthenticationManager(
        private val passwordEncoder: PasswordEncoder,
        private val userDetailsService: UserDetailsService
) : AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

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
}
