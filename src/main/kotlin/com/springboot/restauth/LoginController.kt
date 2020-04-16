package com.springboot.restauth

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
class LoginController(
        private val authenticationManager: AuthenticationManager
) {
    @PostMapping("/api/login")
    fun login(
            @RequestBody loginCredentials: LoginCredentials,
            request: HttpServletRequest
    ): ResponseEntity<Authentication> {
        val auth: Authentication?
        try {
            auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                    loginCredentials.username,
                    loginCredentials.password
            ))
        } catch (e: AuthenticationException) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        setSessionAuth(request.session, auth)
        return ResponseEntity(auth, HttpStatus.OK)
    }

    @PostMapping("/api/logout")
    fun logout(
            session: HttpSession
    ) {
        session.invalidate()
    }

    private fun setSessionAuth(session: HttpSession, auth: Authentication?) {
        val securityContext = SecurityContextHolder.getContext()
        securityContext.authentication = auth
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext)
    }
}

data class LoginCredentials(val username: String, val password: String)
