package com.springboot.restauth

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service("userDetailsService")
class CustomUserDetailsService(
        private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    private val users = mutableListOf<UserDetails>()

    init {
        users.add(
                User.withUsername("hoge")
                        .passwordEncoder(::encodePassword)
                        .password("foobar")
                        .roles("USER")
                        .build()
        )
    }

    private fun encodePassword(password: String): String = passwordEncoder.encode(password)

    override fun loadUserByUsername(username: String): UserDetails {
        val user = users.find { userDetails -> userDetails.username == username }
        return user
                ?: throw UsernameNotFoundException("User '${username}' not found")
    }
}